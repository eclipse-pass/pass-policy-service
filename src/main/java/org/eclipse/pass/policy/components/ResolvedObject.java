package org.eclipse.pass.policy.components;

import org.json.JSONObject;

/**
 * Represents the ResolvedObject object
 * Contains a parsed JSON object, as well as the source URI where it came from
 *
 * @author David McIntyre
 */
public class ResolvedObject {

    private String source;
    private JSONObject object;

    /**
     * Constructs a new ResolvedObject with an empty source and JSONObject
     */
    public ResolvedObject() {
        this.source = new String();
        this.object = new JSONObject();
    }

    /**
     * Constructs a new ResolvedObject with specified source and JSONObject
     *
     * @param source - the source for the object
     * @param object - the JSONObject belonging to the source
     */
    public ResolvedObject(String source, JSONObject object) {
        this.source = source;
        this.object = object;
    }

    /**
     * getSource()
     *
     * @return URI
     */
    public String getSource() {
        return this.source;
    }

    /**
     * setSource()
     *
     * @param source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * getObject()
     *
     * @return Map<String, String>
     */
    public JSONObject getObject() {
        if (this.object.length() > 0) {
            return this.object;
        } else {
            return null;
        }
    }

    /**
     * setObject()
     *
     * @param object
     */
    public void setObject(JSONObject object) {
        this.object = object;
    }

}