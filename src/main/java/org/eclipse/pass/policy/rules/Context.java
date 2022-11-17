package org.eclipse.pass.policy.rules;

import java.util.Map;

import org.dataconservancy.pass.client.PassClient;
import org.eclipse.pass.policy.interfaces.VariablePinner;

/**
 * Represents the Context object
 * Context establishes a rule evaluation/resolution context
 *
 * @author David McIntyre
 */
public class Context implements VariablePinner {

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
    public VariablePinner Pin(Variable variable, String string) {
        // TODO Auto-generated method stub
        return null;
    }
}
