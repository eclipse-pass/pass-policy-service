package org.eclipse.pass.policy.interfaces;

import org.eclipse.pass.policy.rules.Variable;

/**
 * Represents VariablePinner Interface
 * VariablePinner is a VariableResolver that can pin variables to specific values
 * @author David McIntyre
 */
public interface VariablePinner {

    // implements VariableResolver??
    public VariablePinner Pin(Variable variable, String string);
}
