package org.eclipse.pass.policy.rules;

import java.net.URI;
import java.util.ArrayList;
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
    private Boolean shifted;

    public Variable(String fullName) {
        this.fullName = fullName;
        this.shifted = false;
    }

    @Override
    public List<URI> resolve(URI varString) throws Exception {
        List<URI> resolvedVar = new ArrayList<URI>();
        resolvedVar.add(varString);
        return resolvedVar;
    }

    @Override
    public VariablePinner pin(Object variable, Object value) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * isVariable()
     * Determines if a string is a variable (e.g. of the form '${foo.bar.baz}').
     *
     * @param source - the string to be checked
     * @return Boolean - the text either is or isn't a variable
     */
    public static Boolean isVariable(URI source) {
        String string = source.toString();
        Boolean isVariable = string.startsWith("${") && string.endsWith("}");
        return isVariable;
    }

    /**
     * toVariable()
     * Converts a valid URI to a variable for interpolation.
     *
     * @param source - the string to be checked
     * @return Variable - the converted source for interpolation
     */
    public static Variable toVariable(URI source) {
        // ensure that text is a proper variable
        if (!isVariable(source)) {
            return null;
        }
        // need to trim string based on ${} chars
        Variable variable = new Variable(source.toString());
        return variable;
    }

    /**
     * shift()
     * shift() is used for producing a segment of a variable, e.g. shift() of
     * ${foo.bar.baz} is ${foo}. shift() of that ${foo} is ${foo.bar}, and shift()
     * of that ${foo.bar} is ${foo.bar.baz}.
     *
     * @return Variable - the newly shifted variable
     */
    public Variable shift() {
        // find trim left and trim prefix methods
        String remaining;
        return null;
    }

    /**
     * getSegment()
     *
     * @return String
     */
    public String getSegment() {
        return this.segment;
    }

    /**
     * setSegment()
     *
     * @param segment
     */
    public void setSegment(String segment) {
        this.segment = segment;
    }

    /**
     * getSegmentName()
     *
     * @return String
     */
    public String getSegmentName() {
        return this.segmentName;
    }

    /**
     * setSegmentName()
     *
     * @param segmentName
     */
    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }

    /**
     * getFullName()
     *
     * @return String
     */
    public String getFullName() {
        return this.fullName;
    }

    /**
     * setFullName()
     *
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * isShifted()
     * Check to see if a variable's full name has been shifted fully
     *
     * @return Boolean
     */
    public Boolean isShifted() {
        return this.shifted;
    }

    /**
     * setShifted()
     *
     * @param shifted
     */
    public void setShifted(Boolean shifted) {
        this.shifted = shifted;
    }
}
