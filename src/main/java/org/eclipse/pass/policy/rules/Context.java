package org.eclipse.pass.policy.rules;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dataconservancy.pass.client.PassClient;
import org.eclipse.pass.policy.components.VariablePinner;

/**
 * Represents the Context object
 * Context establishes a rule evaluation/resolution context
 *
 * @author David McIntyre
 */
public class Context extends VariablePinner {

    private String submissionURI;
    private Map headers;
    private PassClient passClient;
    private Map<String, Object> values;

    public Context(String submissionURI, Map headers, PassClient passClient) {
        this.submissionURI = submissionURI;
        this.headers = headers;
        this.passClient = passClient;
        this.values = new HashMap<String, Object>();
    }

    public Context(String submissionURI, Map headers, PassClient passClient, Map<String, Object> values) {
        this.submissionURI = submissionURI;
        this.headers = headers;
        this.passClient = passClient;
        this.values = values;
    }

    @Override
    public List<URI> resolve(URI varString) throws Exception {
        // resolve a variable returning a list of strings
        // need to init context first
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
            Iterator<Map<String, List<String>>> it = headers.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<String, List<String>> header = (Map.Entry<String, List<String>>) it.next();
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
}
