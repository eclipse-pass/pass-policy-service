package org.eclipse.pass.policy.rules;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private RepositoryRules repositoryRules;

    private Policy policy;
    private List<Repository> repositories;
    private List<Condition> conditions;

    /**
     * Resolve interpolates any variables in a policy and resolves against a ruleset
     * to a list of applicable Policies.
     *
     * @param variables - the ruleset to be resolved against
     * @return List<Policy> - the List of resolved Policies
     * @throws Exception -
     */
    public List<Policy> resolve(Policy policy, VariablePinner variables) throws Exception {
        List<Policy> resolvedPolicies = new ArrayList<Policy>();
        List<Repository> resolvedRepos = new ArrayList<Repository>();

        // If the policy ID is a variable, we need to resolve/expand it. If the result
        // is a list of IDs, we return a list of policies, each one with an ID from the
        // list.
        if (Variable.isVariable(policy.getId())) {

            // resolve policy ID/s
            List<URI> resolvedIDs = new ArrayList<URI>();
            try {
                resolvedIDs.addAll(variables.resolve(policy.getId()));

                URI curID = resolvedIDs.get(0); // for exception handling
                try {

                    for (URI id : resolvedIDs) {

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
                        resolved.setId(id);
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

            } catch (Exception e) {
                throw new Exception("Could not resolve repositories in policy " + policy.getId().toString(), e);
            }
        }
        return null;
    }

    public List<Repository> resolveRepositories(Policy policy, VariablePinner variables) throws Exception {
        List<Repository> resolvedRepos = new ArrayList<Repository>();

        try {
            for (URI repo : policy.getRepositories()) {
                List<Repository> repos = repositoryRules.resolve(repo, variables);

                resolvedRepos.addAll(repos);
            }
        } catch (Exception e) {
            throw new Exception("Could not resolve repositories for " + policy.getId().toString(), e);
        }
        return null;
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
