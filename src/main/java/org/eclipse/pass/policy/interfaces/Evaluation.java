package org.eclipse.pass.policy.interfaces;

/**
 * Represents Evaluation interface
 * Provides structure for evaluation function (ie. String.equals() or
 * String.startsWith() to be defined using lambda function when evaluation check
 * is needed.
 *
 * @author David McIntyre
 */
public interface Evaluation {
    public Boolean eval(String a, String b);
}
