package org.eclipse.pass.policy.rules;

import java.util.List;

import org.eclipse.pass.policy.interfaces.VariableResolver;

/**
 * Represents the Condition object
 * Condition determines whether a policy applies or not
 *
 * @author David McIntyre
 */
public class Condition {

    private List<String> conditions;

    public Condition(List<String> conditions) {
        this.conditions = conditions;
    }

    public Boolean apply(VariableResolver variables) throws Exception {
        for (String cond : conditions) {
            switch (cond) {
                case "endsWith":
                    endsWith(cond, variables);
                    break;
                case "equals":
                    equals(cond, variables);
                    break;
                case "anyOf":
                    anyOf(cond, variables);
                    break;
                case "noneOf":
                    noneOf(cond, variables);
                    break;
                case "contains":
                    contains(cond, variables);
                    break;
                default:
                    throw new Exception("Unknown condition " + cond);
            }
        }
        return null;
    }

    public Boolean endsWith(Object fromCondition, VariableResolver variables) {
        return null;
    }

    public Boolean equals(Object fromCondition, VariableResolver variables) {
        return null;
    }

    public Boolean anyOf(Object arg, VariableResolver variables) {
        return null;
    }

    public Boolean noneOf(Object arg, VariableResolver variables) {
        return null;
    }

    public Boolean contains(Object fromCondition, VariableResolver variables) {
        return null;
    }

}
