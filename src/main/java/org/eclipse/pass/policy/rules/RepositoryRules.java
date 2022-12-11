package org.eclipse.pass.policy.rules;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.dataconservancy.pass.client.PassClient;
import org.dataconservancy.pass.client.PassClientFactory;
import org.dataconservancy.pass.model.Repository;
import org.eclipse.pass.policy.interfaces.VariableResolver;

/**
 * Represents the RepositoryRules object
 * RepositoryRules resolves rulesets to return relevant policies or repositories
 * in
 * a repository.
 *
 * @author David McIntyre
 */
public class RepositoryRules {

    PassClient passClient = PassClientFactory.getPassClient();

    /**
     * Resolve interpolates any variables in a repository and resolves against a
     * ruleset
     * to a list of applicable Repositories.
     *
     * @param variables - the ruleset to be resolved against
     * @return List<Repository> - the List of resolved Repositories
     * @throws Exception - Repository could not be resolved
     */
    public List<Repository> resolve(URI repo, VariableResolver variables) throws Exception {
        List<Repository> resolvedRepos = new ArrayList<Repository>();
        Repository repository = passClient.readResource(repo, Repository.class);

        if (Variable.isVariable(repository.getId().toString())) {

            // resolve repository ID/s
            List<String> resolvedIDs = new ArrayList<String>();

            try {
                resolvedIDs.addAll(variables.resolve(repository.getId().toString()));

                for (String id : resolvedIDs) {
                    Repository resolved = new Repository();
                    URI uriID = new URI(id);
                    resolved.setId(uriID);

                    resolvedRepos.add(resolved);
                }
            } catch (Exception e) {
                throw new Exception("Could not resolve property ID " + repository.getId().toString(), e);
            }
        } else {
            resolvedRepos.add(repository);
        }

        return resolvedRepos;
    }

}
