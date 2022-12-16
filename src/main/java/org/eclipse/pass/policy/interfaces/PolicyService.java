package org.eclipse.pass.policy.interfaces;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.dataconservancy.pass.model.Policy;
import org.dataconservancy.pass.model.Repository;

/**
 * Represents PolicyService interface.
 * Provides blueprint for business logic.
 *
 * @author David McIntyre
 */
public interface PolicyService {
    /**
     * findPolicies()
     * Receives a submission URI and a map of headers. Resolves a list of relevant
     * Policies based on the context created by a submission against a set of DSL
     * rules.
     *
     * @param submission - PASS Submission URI
     * @param headers    - map of submission headers
     * @return List<Policy> - the list of applicable policies
     * @throws RuntimeException - submission could not be resolved to a list of
     *                          policies
     */
    public List<Policy> findPolicies(String submission, Map<String, String> headers) throws RuntimeException;

    /**
     * findRepositories
     * Receives a submission URI and a map of headers. Resolves a list of relevant
     * Repositories based on the context created by a submission against a set of
     * DSL rules.
     *
     * @param submissionURI - PASS submission URI
     * @param headers       - map of submission headers
     * @return List<Repository> - the list of applicable repositories
     * @throws RuntimeException - the submission could not be resolved to a list of
     *                          repositories
     */
    public List<Repository> findRepositories(URI submissionURI, Map<String, Object> headers) throws RuntimeException;
}
