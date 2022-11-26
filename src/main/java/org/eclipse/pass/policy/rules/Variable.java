package org.eclipse.pass.policy.rules;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
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

    public Variable(String fullName) {
        this.fullName = fullName;
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
     * @param text - the string to be checked
     * @return Boolean - the text either is or isn't a variable
     */
    public static Boolean isVariable(URI text) {
        String string = text.toString();
        Boolean isVariable = string.startsWith("${") && string.endsWith("}");
        return isVariable;
    }

    public static Variable toVariable(URI text) {
        // ensure that text is a proper variable
        if (!isVariable(text)) {
            return null;
        }
        // need to trim string based on ${} chars
        Variable variable = new Variable(text.toString());
        return variable;
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

}
