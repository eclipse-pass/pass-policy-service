package org.eclipse.pass.policy.services;

import java.util.List;
import java.util.Map;

import org.dataconservancy.pass.client.PassClient;
import org.dataconservancy.pass.client.PassClientFactory;
import org.dataconservancy.pass.model.Policy;
import org.dataconservancy.pass.model.Repository;
import org.eclipse.pass.policy.interfaces.PolicyService;
import org.eclipse.pass.policy.rules.Context;
import org.eclipse.pass.policy.rules.DSL;

/**
 * Represents PolicyService object.
 * Handles business logic needed to complete requests and provide responses to
 * Servlets.
 *
 * @author David McIntyre
 */
public class PolicyServiceImpl implements PolicyService {

    PassClient passClient = PassClientFactory.getPassClient();

    /**
     * PolicyService.findPolicies()
     *
     * @param submissionURI - PASS submissionURI
     * @param headers       - request headers
     * @return List<Policy> - List of policies relevant to submissionURI
     * @throws Exception
     */
    @Override
    public List<Policy> findPolicies(String submissionURI, Map<String, String> headers) throws Exception {
        Context context = new Context(submissionURI, headers, passClient);
        DSL dsl = new DSL();
        try {
            return dsl.resolve(context);
        } catch (Exception e) {
            throw new Exception("Could not resolve policy rule", e);
        }
    }

    // public void sendPolicies() {

    // }

    // Repositories endpoint functions
    @Override
    public List<Repository> findRepositories(String submissionURI, Map<String, String> headers) {
        return null;
    }
    // public void reconcileRepositories() {

    // }

    // Policy Service Functions
    // public void requestPolicies() {
    // }

    // public void requestRepositories() {
    // }

    // public void doRequest(){
    // }
}