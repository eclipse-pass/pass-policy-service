package interfaces;

import rules.Variable;

public interface VariablePinner {

    // implements VariableResolver??
    public VariablePinner Pin(Variable variable, String string);
}
