package org.eclipse.pass.policy.rules;

import org.eclipse.pass.policy.components.Repository;

/**
 * Represents the Requirements object
 * Requirements encapsulates deposit requirements by sorting repositories into:
 * "required", "oneOf", and "optional" buckets
 * 
 * @author David McIntyre
 */
public class Requirements {

    private Repository[] required; // json:"required"
    private Repository[][] oneOf; // json:"one-of"
    private Repository[] optional; // json:"optional"

}
