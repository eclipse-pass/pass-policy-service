package org.eclipse.pass.policy.rules;

import org.eclipse.pass.policy.components.Policy;

/**
 * Represents the DSL object
 * DSL encapsulates to a policy rules document
 * @author David McIntyre
 */
public class DSL {

    private String schema; //json:"$schema"
    private Policy[] policies; //json:"policy-rules"
}
