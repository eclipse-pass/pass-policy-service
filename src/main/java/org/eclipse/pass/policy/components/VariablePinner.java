package org.eclipse.pass.policy.components;

import org.eclipse.pass.policy.interfaces.VariableResolver;

/**
 * Represents VariablePinner interface
 * Pins a given value to a given variable
 *
 * @author David McIntyre
 */
public abstract class VariablePinner implements VariableResolver {

    /**
     * Pins a given value to a given variable.
     * For pinning of context values, Objects must be of type URI.
     *
     * @param variable - the URI to pin to
     * @param value    - the value to be pinned
     * @return VariablePinner - the pinned object to be returned
     * @throws Exception - incorrect object types supplied
     */
    public abstract VariablePinner pin(Object variable, Object value) throws Exception;
}
