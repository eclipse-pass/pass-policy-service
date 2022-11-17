package org.eclipse.pass.policy.rules;

import java.util.List;

import org.dataconservancy.pass.model.Policy;
import org.eclipse.pass.policy.interfaces.PolicyResolver;
import org.eclipse.pass.policy.interfaces.VariablePinner;

/**
 * Represents the DSL object
 * DSL encapsulates to a policy rules document
 *
 * @author David McIntyre
 */
public class DSL implements PolicyResolver {

    private String schema; // json:"$schema"
    private List<Policy> policies; // json:"policy-rules"

    /**
     * DSL.resolve()
     *
     * @param variables - the ruleset to be resolved
     * @return List<Policy> - the ist of resolved policies
     * @throws RuntimeException - could not resolve policy rule
     */
    @Override
    public List<Policy> resolve(VariablePinner variables) throws RuntimeException {
        List<Policy> resolvedPolicies;

        for (Policy policy : policies) {
            try {
                // requires PolicyRules class to perform Resolve() function specific to
                // policy.go
                // Policy resolved = policy.resolve(variables);
                // resolvedPolicies.add(resolved);
            } catch (Exception e) {
                throw new RuntimeException("Could not resolve policy rule");
            }
        }
        return null;
    }

}
