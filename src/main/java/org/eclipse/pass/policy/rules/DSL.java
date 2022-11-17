package org.eclipse.pass.policy.rules;

import org.dataconservancy.pass.model.Policy;
import org.eclipse.pass.policy.interfaces.PolicyResolver;
import org.eclipse.pass.policy.interfaces.VariablePinner;

/**
 * Represents the DSL object
 * DSL encapsulates to a policy rules document
 *
 * @author David McIntyre
 */
public class DSL implements PolicyResolver {

    private String schema; // json:"$schema"
    private Policy[] policies; // json:"policy-rules"

    @Override
    public Policy[] resolve(VariablePinner variables) {
        Policy[] policies;

        return null;
    }

}
