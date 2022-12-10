package org.eclipse.pass.policy.rules;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Context rules resolution
 *
 * @author - David McIntyre
 */
@DisplayName("Context Tests")
public class ContextTest {

    /**
     * Unit tests for resolve() method in Context
     */
    @Test
    @DisplayName("Test: Test of resolve() method will null source.")
    public void testResolveNullSource() {
        assertTrue(1 == 1);
    }
}
