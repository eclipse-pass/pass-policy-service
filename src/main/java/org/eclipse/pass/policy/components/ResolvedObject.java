package org.eclipse.pass.policy.components;

import java.net.URI;
import java.util.Map;

/**
 * Represents the ResolvedObject object
 * Contains a parsed JSON object, as well as the source URI where it came from
 *
 * @author David McIntyre
 */
public class ResolvedObject {

    private URI source;
    private Map<String, String> object;

    public ResolvedObject(URI source, Map<String, String> object) {
        this.source = source;
        this.object = object;
    }

    /**
     * getSource()
     *
     * @return URI
     */
    public URI getSource() {
        return this.source;
    }

    /**
     * setSource()
     *
     * @param source
     */
    public void setSource(URI source) {
        this.source = source;
    }

    /**
     * getObject()
     *
     * @return Map<String, String>
     */
    public Map<String, String> getObject() {
        if (this.object.size() > 0) {
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
    public void setObject(Map<String, String> object) {
        this.object = object;
    }

}