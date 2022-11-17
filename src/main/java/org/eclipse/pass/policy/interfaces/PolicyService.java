package org.eclipse.pass.policy.interfaces;

import java.util.List;
import java.util.Map;

import org.dataconservancy.pass.model.Policy;

/**
 * Represents PolicyService interface.
 * Provides blueprint for business logic.
 *
 * @author David McIntyre
 */
public interface PolicyService {
    public List<Policy> findPolicies(String submissionURI, Map headers);
}
