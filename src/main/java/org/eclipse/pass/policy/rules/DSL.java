package org.eclipse.pass.policy.rules;

import java.util.ArrayList;
import java.util.List;

import org.dataconservancy.pass.model.Policy;
import org.eclipse.pass.policy.components.VariablePinner;
import org.eclipse.pass.policy.interfaces.PolicyResolver;

/**
 * Represents the DSL object
 * DSL encapsulates to a policy rules document
 *
 * @author David McIntyre
 */
public class DSL implements PolicyResolver {

    private PolicyRules resolver;

    private String schema; // json:"$schema"
    private List<Policy> policies; // json:"policy-rules"

    /**
     * DSL.resolve()
     *
     * @param variables - the ruleset to be resolved against
     * @return List<Policy> - the List of resolved policies
     * @throws RuntimeException - could not resolve policy rule
     */
    @Override
    public List<Policy> resolve(VariablePinner variables) throws RuntimeException {
        List<Policy> resolvedPolicies = new ArrayList<Policy>();

        for (Policy policy : policies) {
            try {
                if (resolver.resolve(policy, variables) != null) {
                    resolvedPolicies.add(policy);
                }
            } catch (Exception e) {
                throw new RuntimeException("Could not resolve policy rule");
            }
        }
        return null;
    }
}
