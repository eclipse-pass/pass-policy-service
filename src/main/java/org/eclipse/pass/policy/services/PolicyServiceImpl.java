package org.eclipse.pass.policy.services;

import java.util.Map;

import org.dataconservancy.pass.client.PassClient;
import org.dataconservancy.pass.client.PassClientFactory;
import org.dataconservancy.pass.model.Policy;
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

    // Policy endpoint functions
    // find applicable policies based on request context
    @Override
    public Policy[] findPolicies(String submissionURI, Map headers) {
        Context context = new Context(submissionURI, headers, passClient);
        DSL dsl = new DSL();
        return dsl.resolve(context);
    }

    // public void sendPolicies() {

    // }

    // Repositories endpoint functions
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