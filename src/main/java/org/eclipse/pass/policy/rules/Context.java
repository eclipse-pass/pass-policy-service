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
        try {
            // Resolve each part of the variable (e.g. a, a.b, a.b.c, a.b.c.d)
            for (segment = variable.shift(); variable.isShifted(); segment = segment.shift()) {
                Boolean error = this.resolveSegment(segment);
            }

        } catch (Exception e) {
            throw new Exception("Could not resolve variable segment" + segment.getSegmentName());
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
    public Boolean resolveSegment(Variable segment) {

        // if we already have a value, no need to re-resolve it. Likewise, no point in
        // resolving ${} from ${x}
        Boolean hasKey = this.values.containsKey(segment.getSegmentName());
        Boolean hasValue = this.values.get(segment.getSegmentName()) != null;
        if ((hasKey && hasValue) || segment.prev().getSegmentName() == "") {
            return null;
        }

        return null;
    }

}
