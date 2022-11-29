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
}