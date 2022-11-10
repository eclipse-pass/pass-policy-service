package org.eclipse.pass.policy.rules;

/**
 * Represents the Variable object
 * Encodes a variable for interpolation, eg. ${foo.bar.baz}, or a segment of one
 * eg. ${foo.bar} of ${foo.bar.baz}
 * 
 * @author David McIntyre
 */
public class Variable {

    private String segment;
    private String segmentName;
    private String fullName;

}
