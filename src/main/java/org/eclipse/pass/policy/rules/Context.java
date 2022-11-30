package org.eclipse.pass.policy.rules;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dataconservancy.pass.client.PassClient;
import org.eclipse.pass.policy.components.ResolvedObject;
import org.eclipse.pass.policy.components.VariablePinner;

/**
 * Represents the Context object
 * Context establishes a rule evaluation/resolution context
 *
 * @author David McIntyre
 */
public class Context extends VariablePinner {

    private String submissionURI;
    private Map<String, String> headers;
    private PassClient passClient;
    private Map<String, Object> values;

    public Context(String submissionURI, Map<String, String> headers, PassClient passClient) {
        this.submissionURI = submissionURI;
        this.headers = headers;
        this.passClient = passClient;
        this.values = new HashMap<String, Object>();
    }

    public Context(String submissionURI, Map<String, String> headers, PassClient passClient,
            Map<String, Object> values) {
        this.submissionURI = submissionURI;
        this.headers = headers;
        this.passClient = passClient;
        this.values = values;
    }

    public void init(URI source) throws Exception {

        // if the values && headers map are already initialised, we're done
        if (this.values.size() == 0) {
            if (this.submissionURI == null) {
                throw new Exception("Context requires a submission URI");
            }

            this.values.put("submission", this.submissionURI);
        }

        if (this.headers.size() == 0) {
            throw new Exception("Context requires a map of request headers");
        }

        this.values.put("header", new ResolvedObject(source, this.headers));
    }

    @Override
    public List<URI> resolve(URI source) throws Exception {
        // resolve a variable returning a list of strings
        this.init(source);
        List<URI> resolved = new ArrayList<URI>();

        // check for proper variable conversion
        Variable variable = Variable.toVariable(source);
        if (variable == null) {
            resolved.add(source);
            return resolved;
        }

        Variable segment = new Variable("");
        // Resolve each part of the variable (e.g. a, a.b, a.b.c, a.b.c.d)
        for (segment = variable.shift(); variable.isShifted(); segment = segment.shift()) {
            try {
                this.resolveSegment(segment);
            } catch (Exception e) {
                throw new Exception("Could not resolve variable segment " + segment.getSegmentName(), e);
            }
        }

        return null;
    }

    /**
     * pin()
     *
     * Pins a given value to a given variable.
     * For pinning of context values, Objects must be of type URI.
     *
     * @param variable - the URI to pin to
     * @param value    - the value to be pinned
     * @return VariablePinner - the pinned object to be returned
     * @throws Exception - incorrect object types supplied
     */
    @Override
    public VariablePinner pin(Object variable, Object value) throws Exception {
        if (variable instanceof URI && value instanceof URI) {
            Variable parsed = Variable.toVariable((URI) variable);

            if (parsed == null) {
                return this;
            }

            Map<String, Object> pinnedValues = new HashMap<String, Object>();
            Iterator<Entry<String, String>> it = headers.entrySet().iterator();

            while (it.hasNext()) {
                Entry<String, String> header = (Entry<String, String>) it.next();
                pinnedValues.put((String) header.getKey(), header.getValue());
            }

            List<String> segments = Arrays.asList(parsed.getFullName().split("."));

            pinnedValues.put(parsed.getFullName(), value);
            pinnedValues.put(segments.get(segments.size() - 1), value);

            this.values = pinnedValues;

            return this;

        } else {
            throw new Exception("Must supply two URIs: A variable, and a value to pin");
        }
    }

    /**
     * resolveSegment()
     * Resolves a variable segment (e.g. ${x.y} out of ${x.y.z})
     *
     * @param segment - the segment to be resolved
     * @return Boolean - a segment will either resolve or fail
     */
    public void resolveSegment(Variable segment) throws Exception {

        // If we already have a value, no need to re-resolve it. Likewise, no point in
        // resolving ${} from ${x}
        Boolean hasKey = this.values.containsKey(segment.getSegmentName());
        Boolean hasValue = this.values.get(segment.getSegmentName()) != null;
        if ((hasKey && hasValue) || segment.prev().getSegmentName() == "") {
            return;
        }

        // We need to resolve the previous segment into a map, (or list of maps) if it
        // isn't already, and extract its value
        Object prevValue = this.values.get(segment.prev().getSegmentName());

        // ${foo} is a JSON object, or list of JSON objects, or a JSON blob that had
        // previously been resolved from URIs. So just look up key 'bar' and save those
        // values to ${foo.bar}
        if (prevValue instanceof ResolvedObject && !(prevValue instanceof List<?>)) {
            // Case ResolvedObject
            this.extractValue(segment, (ResolvedObject) prevValue);
        } else if (prevValue instanceof ResolvedObject && prevValue instanceof List<?>) {
            // Case List<ResolvedObject>
            this.extractValues(segment, (List<ResolvedObject>) prevValue);
        } else if (prevValue instanceof String && !(prevValue instanceof List<?>)) {
            // Case String
            // ${foo} is a String, or list of Strings. In order to find ${foo.bar}, see if
            // each foo is a stringified JSON blob, or an HTTP URI.
            // If it's a blob, parse to a JSON object and save it as a ResolvedObject to
            // ${foo.bar}.
            // If it's a URI, dereference it and, if its a JSON blob, parse it to a JSON
            // object and save a ResolvedObject containing both the URI and the resulting
            // blob to ${foo.bar}

            this.resolveSegment(segment);
        } else if (prevValue instanceof String && prevValue instanceof List<?>) {
            // Case List<String>

            this.resolveSegment(segment);
        } else if (prevValue instanceof Object && prevValue instanceof List<?>) {
            // Case Object
            // ${foo} is a list of some sort, but we don't know the type of the items. If
            // they are URIs, dereference the URIs.
            // If the Result is a JSON blob, parse it and look up the value of the key 'bar'
            // in each blob. Save to ${foo.bar}
            // error checking

            this.resolveSegment(segment);
        } else if (prevValue == null) {
            // Case Null
            // ${bar} has no value, so of course ${foo.bar} has no value either
            this.values.put(segment.getSegmentName(), new ArrayList<String>());
        } else {
            // Case Default
            // ${bar} is some unexpected type.
            // do something
        }

        return;
    }

    /**
     * extractValue()
     * Set ${foo.bar} to foo[bar]
     *
     * @param v        - the variable to extract
     * @param resolved - the resolved Object
     * @throws Exception
     */
    public void extractValue(Variable v, ResolvedObject resolved) throws Exception {
        Object val = resolved.getObject().get(v.getSegment());

        if (val == null) {
            this.values.put(v.getSegmentName(), new ArrayList<String>());
            this.values.put(v.getSegment(), new ArrayList<String>());
        }

        if (!(val instanceof String) || val instanceof List<?>) {
            throw new Exception(v.getSegmentName() + " is " + val.getClass().getName() + ", or a list, not a String");
        }

        List<String> valList = new ArrayList<String>();
        valList.add((String) val);

        this.values.put(v.getSegmentName(), valList);
        this.values.put(v.getSegment(), valList); // this is the shortcut ${properties}, instead of ${x.y.properties}
    }

    /**
     * extractValues()
     * Append foo[bar] to ${foo.bar} for each foo
     *
     * @param v        - the variable to extract
     * @param resolved - the resolved Object
     * @throws Exception - Object does not contain a list of Strings
     */
    public void extractValues(Variable v, List<ResolvedObject> resolvedList) throws Exception {
        List<String> vals = new ArrayList<String>();

        for (ResolvedObject resolved : resolvedList) {
            if (resolved.getObject().containsKey(v.getSegment())
                    && resolved.getObject().get(v.getSegment()) != null) {

                Object typedVal = resolved.getObject().get(v.getSegment());

                if (typedVal instanceof String && !(typedVal instanceof List<?>)) {
                    // Case String
                    vals.add((String) typedVal);
                } else if (typedVal instanceof Object && typedVal instanceof List<?>) {
                    // Case List<Object>
                    for (Object item : (List<Object>) typedVal) {
                        if (item instanceof String) {
                            String strVal = (String) item;
                            vals.add(strVal);
                        } else {
                            throw new Exception(
                                    v.getSegmentName() + " is a list of " + item.getClass().getName()
                                            + ", not Strings");
                        }
                    }
                } else {
                    // Case default
                    throw new Exception(
                            v.getSegmentName() + " is a " + typedVal.getClass().getName() + ", not a String");
                }
            }
        }

        this.values.put(v.getSegmentName(), vals);
        this.values.put(v.getSegment(), vals); // this is the shortcut ${properties}, instead of ${x.y.properties}
    }

}
