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
    public List<Policy> findPolicies(URI submissionURI, Map<String, Object> headers) throws Exception;

    public List<Repository> findRepositories(URI submissionURI, Map<String, Object> headers) throws Exception;
}
