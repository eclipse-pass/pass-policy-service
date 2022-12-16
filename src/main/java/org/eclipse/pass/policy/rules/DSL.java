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

    private PolicyRules policyRules;

    private String schema; // json:"$schema"
    private List<Policy> policies; // json:"policy-rules"

    /**
     * DSL.resolve()
     * Resolves a list of applicable Policies using a provided ruleset against a
     * database of policies that are instantiated at runtime.
     *
     * @param variables - the ruleset to be resolved against
     * @return List<Policy> - the List of resolved policies
     * @throws RuntimeException - Policy rule could not be resolved
     */
    @Override
    public List<Policy> resolve(VariablePinner variables) throws RuntimeException {
        List<Policy> resolvedPolicies = new ArrayList<Policy>();

        for (Policy policy : policies) {
            try {
                List<Policy> resolved = policyRules.resolve(policy, variables);

                // If a resolved policy or policies exist, append to final list
                if (resolved.size() > 0) {
                    resolvedPolicies.addAll(resolved);
                }

            } catch (RuntimeException e) {
                throw new RuntimeException("Could not resolve policy rule", e);
            }
        }

        return policyRules.uniquePolicies(resolvedPolicies);
    }
}
