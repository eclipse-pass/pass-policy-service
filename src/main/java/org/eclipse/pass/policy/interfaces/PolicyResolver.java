package org.eclipse.pass.policy.interfaces;

import java.util.List;

import org.dataconservancy.pass.model.Policy;
import org.eclipse.pass.policy.components.VariablePinner;

/**
 * Represents PolicyResolver interface
 * Interpolates any variables in a policy
 * If policy ID resolves to a list, the list of resolved policies is returned
 *
 * @author David McIntyre
 */
public interface PolicyResolver {

    public List<Policy> resolve(VariablePinner variables) throws Exception;
}