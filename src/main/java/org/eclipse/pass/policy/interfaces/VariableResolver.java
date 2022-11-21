package org.eclipse.pass.policy.interfaces;

import java.util.List;

/**
 * Represents VariableResolver interface
 * Resolves a variable (i.e. a string of form "${foo.bar.bax}") and returns a
 * list of values
 *
 * @author David McIntyre
 */
public interface VariableResolver {

    // public (String[], error) Resolve(String varString);
    public List<String> resolve(String varString) throws Exception;

}
