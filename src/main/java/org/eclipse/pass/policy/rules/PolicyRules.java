package org.eclipse.pass.policy.rules;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.dataconservancy.pass.model.Policy;
import org.dataconservancy.pass.model.Repository;
import org.eclipse.pass.policy.components.VariablePinner;

/**
 * Represents the PolicyRules object
 * PolicyRules resolves rulesets to return relevant policies or repositories in
 * a repository.
 *
 * @author David McIntyre
 */
public class PolicyRules {

    private RepositoryRules repositoryRules;

    private List<Repository> repositories;
    private List<Condition> conditions;

    /**
     * Resolve interpolates any variables in a policy and resolves against a ruleset
     * to a list of applicable Policies.
     *
     * @param variables - the ruleset to be resolved against
     * @return List<Policy> - the List of resolved Policies
     * @throws Exception - Policy could not be resolved
     */
    public List<Policy> resolve(Policy policy, VariablePinner variables) throws Exception {
        List<Policy> resolvedPolicies = new ArrayList<Policy>();
        List<Repository> resolvedRepos = new ArrayList<Repository>();

        // If the policy ID is a variable, we need to resolve/expand it. If the result
        // is a list of IDs, we return a list of policies, each one with an ID from the
        // list.
        if (Variable.isVariable(policy.getId().toString())) {

            // resolve policy ID/s
            List<String> resolvedIDs = new ArrayList<String>();
            try {
                resolvedIDs.addAll(variables.resolve(policy.getId().toString()));

                String curID = resolvedIDs.get(0); // for exception handling
                try {

                    for (String id : resolvedIDs) {

                        // Now that we have a concrete ID, resolve any other variables elsewhere in the
                        // policy. Some of them may depend on knowing the ID we just found.
                        //
                        // We take a shortcut by pinning only the ID variable, meaning ${foo.bar.baz.id}
                        // is pinned, but ${foo.bar} is not.
                        curID = id; // for exception handling
                        Policy resolved = new Policy();
                        resolved.setTitle(policy.getTitle());
                        resolved.setDescription(policy.getDescription());
                        resolved.setPolicyUrl(policy.getPolicyUrl());
                        resolved.setRepositories(policy.getRepositories());
                        resolved.setInstitution(policy.getInstitution());
                        URI uriID = new URI(id);
                        resolved.setId(uriID);
                        resolve(resolved, variables.pin(policy.getId(), id));

                        resolvedPolicies.add(resolved);
                    }
                } catch (Exception e) {
                    throw new Exception("Could not resolve policy rule for " + curID.toString());
                }
            } catch (Exception e) {
                throw new Exception("Could not resolve property ID " + policy.getId().toString(), e);
            }

        } else {

            // Individual policy. Resolve the repositories section, and filter by condition
            // to see if it is applicable
            try {
                resolvedRepos.addAll(resolveRepositories(policy, variables));
                // policy.setRepositories(resolvedRepos);

                try {
                    Boolean valid = applyConditions(variables);

                    if (valid) {
                        resolvedPolicies.add(policy);
                    }
                } catch (Exception e) {
                    throw new Exception("Error applying conditions to policy " + policy.getId().toString());
                }
            } catch (Exception e) {
                throw new Exception("Could not resolve repositories in policy " + policy.getId().toString(), e);
            }
        }

        return uniquePolicies(resolvedPolicies);
    }

    /**
     * Receives a policy and a set of variables to resolve against. Returns a list
     * of applicable policies to the given policy.
     *
     * @param policy    - the parent Policy for repositories
     * @param variables - the variables to resolved against
     * @return List<Repository> - the list of resolved repositories
     * @throws Exception - repositories could not be resolved
     */
    public List<Repository> resolveRepositories(Policy policy, VariablePinner variables) throws Exception {
        List<Repository> resolvedRepos = new ArrayList<Repository>();

        try {
            for (URI repo : policy.getRepositories()) {
                List<Repository> repos = this.repositoryRules.resolve(repo, variables);

                resolvedRepos.addAll(repos);
            }
        } catch (Exception e) {
            throw new Exception("Could not resolve repositories for " + policy.getId().toString(), e);
        }

        return resolvedRepos;
    }

    /**
     * Applies conditions present if any for the current policy. Evaluates
     * conditions on supplied variables. Returns true if all conditions are met,
     * otherwise returns false.
     *
     * @param variables - the variables to check conditions against
     * @return Boolean - true if variables meet all conditions, false otherwise
     * @throws Exception - Condition could not be resolved
     */
    private Boolean applyConditions(VariablePinner variables) throws Exception {
        Boolean valid;
        try {
            for (Condition cond : this.conditions) {
                valid = cond.apply(variables);

                if (!valid) {
                    return false;
                }
            }
        } catch (Exception e) {
            throw new Exception("Invalid condition", e);
        }

        return true;
    }

    /**
     * uniquePolicies()
     * Removes duplicates from a given list of Policies and returns the unique List.
     *
     * @param policies - the list of policies with potential duplicates
     * @return List<Policy> - the list of unique policies
     */
    public List<Policy> uniquePolicies(List<Policy> policies) {

        if (policies.size() < 2) {
            return policies;
        }

        List<Policy> uniquePolicies = policies.stream().distinct().collect(Collectors.toList());

        return uniquePolicies;
    }
}
