package org.eclipse.pass.policy.rules;

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
    private Map values;

    public Context(String submissionURI, Map headers, PassClient passClient) {
        this.submissionURI = submissionURI;
        this.headers = headers;
        this.passClient = passClient;
    }

    @Override
    public List<String> resolve(String varString) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public VariablePinner pin(Variable variable, String value) {
        // TODO Auto-generated method stub
        return null;
    }
}
