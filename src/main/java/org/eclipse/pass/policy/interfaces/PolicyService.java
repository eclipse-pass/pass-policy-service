package org.eclipse.pass.policy.interfaces;

import java.util.Map;

import org.dataconservancy.pass.model.Policy;

/**
 * Represents PolicyService interface.
 * Provides blueprint for business logic.
 *
 * @author David McIntyre
 */
public interface PolicyService {
    public Policy[] findPolicies(String submissionURI, Map headers);
}
