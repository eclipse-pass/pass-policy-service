package org.eclipse.pass.policy.components;

import org.eclipse.pass.policy.interfaces.VariableResolver;

public abstract class VariablePinner implements VariableResolver {

    public abstract VariablePinner pin(Object variable, Object value) throws Exception;
}
