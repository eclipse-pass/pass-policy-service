package org.eclipse.pass.policy.interfaces;

import org.dataconservancy.pass.model.Policy;

/**
 * Represents PolicyResolver interface
 * Interpolates any variables in a policy
 * If policy ID resolves to a list, the list of resolved policies is returned
 *
 * @author David McIntyre
 */
public interface PolicyResolver {

    // public ([]Policy, error) Resolve(VariablePinner variables);
    public Policy[] resolve(VariablePinner variables);
}