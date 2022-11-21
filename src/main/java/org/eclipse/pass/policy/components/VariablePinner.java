package org.eclipse.pass.policy.components;

import org.eclipse.pass.policy.interfaces.VariableResolver;
import org.eclipse.pass.policy.rules.Variable;

public abstract class VariablePinner implements VariableResolver {

    public abstract VariablePinner pin(Variable variable, String value);
}
