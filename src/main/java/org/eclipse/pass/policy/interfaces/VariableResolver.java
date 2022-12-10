package org.eclipse.pass.policy.interfaces;

import java.net.URI;
import java.util.List;

/**
 * Represents VariableResolver interface
 * Resolves a variable (i.e. a string of form "${foo.bar.bax}") and returns a
 * list of values
 *
 * @author David McIntyre
 */
public interface VariableResolver {

    /**
     * Resolves the given variable by checking for proper variable conversion, then
     * resolves each segment of the variable.
     *
     * @param varString - the variable to resolve
     * @return List<String> - a list of resolved variables
     * @throws Exception - if the variable could not be resolved
     */
    public List<String> resolve(String varString) throws Exception;

}
