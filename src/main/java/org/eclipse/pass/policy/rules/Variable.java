package org.eclipse.pass.policy.rules;

import java.net.URI;
import java.util.List;

import org.eclipse.pass.policy.components.VariablePinner;

/**
 * Represents the Variable object
 * Encodes a variable for interpolation, eg. ${foo.bar.baz}, or a segment of one
 * eg. ${foo.bar} of ${foo.bar.baz}
 *
 * @author David McIntyre
 */
public class Variable extends VariablePinner {

    private String segment;
    private String segmentName;
    private String fullName;

    @Override
    public List<String> resolve(String varString) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public VariablePinner pin(Variable variable, String value) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * isVariable()
     * Determines if a string is a variable (e.g. of the form '${foo.bar.baz}').
     *
     * @param text - the string to be checked
     * @return Boolean - the text either is or isn't a variable
     */
    public static Boolean isVariable(URI text) {
        String string = text.toString();
        return string.startsWith("${") && string.endsWith("}");
    }

}
