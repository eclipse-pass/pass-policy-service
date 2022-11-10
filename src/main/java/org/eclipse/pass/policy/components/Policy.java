package org.eclipse.pass.policy.components;

import org.eclipse.pass.policy.rules.Condition;

/**
 * Represents the Policy object
 * Contains policy details & associated repositories
 *
 * @author David McIntyre
 */
public class Policy {

    private String id; // json:"policy-id"
    private String description; // json:"description"
    private String type; // json:"type"
    private Repository[] respositories; // json:"repositories"
    private Condition[] conditions; // json:"conditions"

}
