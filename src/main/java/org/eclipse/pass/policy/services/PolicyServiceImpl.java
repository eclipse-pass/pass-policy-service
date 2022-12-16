package org.eclipse.pass.policy.services;

import java.net.URI;
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

    PassClient passClient;

    public PolicyServiceImpl() {
        this.passClient = PassClientFactory.getPassClient();
    }

    public PolicyServiceImpl(PassClient client) {
        this.passClient = client;
    }

    @Override
    public List<Policy> findPolicies(String submission, Map<String, String> headers) throws RuntimeException {
        Context context = new Context(submission, headers, passClient);
        DSL dsl = new DSL();
        try {
            return dsl.resolve(context);
        } catch (RuntimeException e) {
            throw new RuntimeException("Could not resolve policy rule", e);
        }
    }

    // public void sendPolicies() {

    // }

    @Override
    public List<Repository> findRepositories(URI submissionURI, Map<String, Object> headers) throws RuntimeException {
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