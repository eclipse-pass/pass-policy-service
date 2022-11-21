package org.eclipse.pass.policy.rules;

import java.util.ArrayList;
import java.util.List;

import org.dataconservancy.pass.model.Policy;
import org.dataconservancy.pass.model.Repository;
import org.eclipse.pass.policy.components.VariablePinner;
import org.eclipse.pass.policy.interfaces.VariableResolver;

/**
 * Represents the PolicyRules object
 * PolicyRules resolves rulesets to return relevant policies or repositories in
 * a repository.
 *
 * @author David McIntyre
 */
public class PolicyRules {

    private Policy policy;
    private List<Repository> repositories;
    private List<Condition> conditions;

    /**
     * Resolve interpolates any variables in a policy and resolves against a ruleset
     * to a list of applicable Policies.
     *
     * @param variables - the ruleset to be resolved against
     * @return List<Policy> - the List of resolved Policies
     */
    public List<Policy> resolve(Policy policy, VariablePinner variables) {
        List<Policy> resolvedPolicies = new ArrayList<Policy>();
        List<Repository> resolvedRepos = new ArrayList<Repository>();

        // If the policy ID is a variable, we need to resolve/expand it. If the result
        // is a list of IDs, we return a list of policies, each one with an ID from the
        // list.
        if (Variable.isVariable(policy.getId())) {

            // resolve policy ID/s
            List<Policy> resolvedIDs = new ArrayList<Policy>();
        } else {

            // Individual policy. Resolve the repositories section, and filter by condition
            // to see if it is applicable
            try {
                // need to figure out java implementation of interfaces implementing interfaces
                // in golang
                resolvedRepos.addAll(resolveRepositories(policy, variables));

            } catch (Exception e) {
                throw new RuntimeException("Could not resolve repositories in policy " + policy.getId());
            }
        }
        return null;
    }

    public List<Repository> resolveRepositories(Policy policy, VariableResolver variables) {
        return null;
    }
}
