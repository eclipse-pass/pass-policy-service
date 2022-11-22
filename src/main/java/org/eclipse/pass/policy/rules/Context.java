package org.eclipse.pass.policy.rules;

import java.net.URI;
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

    @Override
    public List<URI> resolve(URI varString) throws Exception {
        // resolve a variable returning a list of strings
        // need to init context first
        return null;
    }

    @Override
    public VariablePinner pin(Object variable, Object value) throws Exception {
        if (variable instanceof URI && value instanceof URI) {
            Variable parsed = Variable.toVariable((URI) variable);

            if (parsed == null) {
                return this;
            }

            Map<String, Object> pinnedValues = new HashMap<String, Object>();
            Iterator it = headers.entrySet().iterator();

            while(it.hasNext()) {
                Map.Entry header = (Map.Entry)it.next();
                headers = header.getValue();
            }


            }
            return null;
        } else {
            throw new Exception("Must supply two URIs: A variable, and a value to pin");
        }
    }}
