package org.eclipse.pass.policy.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;

import org.dataconservancy.pass.client.PassClient;
import org.dataconservancy.pass.model.Policy;
import org.eclipse.pass.policy.components.ResolvedObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for Context rules resolution
 *
 * @author - David McIntyre
 */
@DisplayName("Context Tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContextTest {

    @Mock
    private PassClient mockPassClient;

    @Mock
    private Policy mockPolicy;

    private Context context;

    @BeforeAll
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void setup() {
        context = new Context();
    }

    /**
     * Unit tests for init() method in Context
     */
    @Test
    @DisplayName("Test: Test init() with valid submission & headers")
    public void testInitValid() {
        context.setSubmission("${example.submission}");
        context.setHeaders(new HashMap<String, String>() {
            {
                put("Content-Type", "application/json");
                put("Accept", "application/json");
            }
        });

        try {
            context.init("source");
            assertEquals(2, context.getValues().size());
            assertEquals(2, context.getHeaders().size());
            assertEquals("${example.submission}", context.getValues().get("submission"));
            assertTrue(context.getValues().get("header") instanceof ResolvedObject);
        } catch (Exception e) {
            fail("Unexpected Exception thrown for valid input");
        }
    }

    @Test
    @DisplayName("Test: Test init() with only valid submission")
    public void testInitSubmissionOnly() {
        Context context = new Context();
        context.setSubmission("${example.submission}");

        try {
            context.init("source");
            fail("Expected exception to be thrown for no request headers");
        } catch (Exception e) {
            assertEquals("Context requires a map of request headers", e.getMessage());
        }
    }

    @Test
    @DisplayName("Test: Test init() with only valid headers")
    public void testInitHeadersOnly() {
        Context context = new Context();
        context.setHeaders(new HashMap<String, String>() {
            {
                put("Content-Type", "application/json");
                put("Accept", "application/json");
            }
        });

        try {
            context.init("source");
            fail("Expected exception to be thrown for no submission URI");
        } catch (Exception e) {
            assertEquals("Context requires a submission URI", e.getMessage());
        }
    }

    /**
     * Unit tests for resolve() method in Context
     */

    /**
     * Unit tests for pin() method in Context
     */

    /**
     * Unit tests for resolveSegment() method in Context
     */

    /**
     * Unit tests for extractValue() method in Context
     */

    /**
     * Unit tests for extractValues() method in Context
     */

    /**
     * Unit tests for resolveToObject() method in Context
     */

    /**
     * Unit tests for resolveToObjects() method in Context
     *
     * @throws URISyntaxException
     */
    // @Test
    // @DisplayName("Test: Test resolveToObjects() with a list of valid submission
    // URIs")
    // public void testResolveToObjectsValid() throws URISyntaxException {
    // List<String> uris = Arrays.asList(
    // "http://example.com/policies/1",
    // "http://example.com/policies/2");
    // Variable v = new Variable("foo.bar.baz.policy");
    // v.setSegment("policySegmentName");
    // v.setSegmentName("policySegment");
    // List<ResolvedObject> expected = Arrays.asList(
    // new ResolvedObject("http://example.com/policies/1", mockPolicy),
    // new ResolvedObject("http://example.com/policies/2", mockPolicy));
    // when(mockPassClient.readResource(new URI("http://example.com/policies/1"),
    // Policy.class))
    // .thenReturn(mockPolicy);
    // when(mockPassClient.readResource(new URI("http://example.com/policies/2"),
    // Policy.class))
    // .thenReturn(mockPolicy);
    // try {
    // context.setPassClient(mockPassClient);
    // context.resolveToObjects(v, uris);
    // } catch (Exception e) {
    // fail("Unexpected Exception thrown for valid submission", e);
    // }

    // List<Object> result = Arrays.asList(
    // context.getValues().get("policySegmentName"),
    // context.getValues().get("policySegment"));
    // assertTrue(expected.equals(result.get(0)));
    // assertTrue(expected.equals(result.get(1)));
    // }

    // @Test
    // public void testResolveToObjects_policyUri() throws Exception {
    // List<String> vals = Arrays.asList("http://example.com/policies/123");
    // Variable v = new Variable("someName", "someSegment");

    // MyClass testClass = new MyClass();
    // testClass.resolveToObjects(v, vals);

    // // Verify that the values map contains the expected keys and values
    // assertTrue(testClass.values.containsKey("someName"));
    // assertTrue(testClass.values.containsKey("someSegment"));
    // assertEquals(1, testClass.values.get("someName").size());
    // assertEquals(1, testClass.values.get("someSegment").size());

    // // Verify that the resolved object is of the expected type and has the
    // expected
    // // properties
    // ResolvedObject resolved = testClass.values.get("someName").get(0);
    // assertTrue(resolved.getObject() instanceof Policy);
    // assertEquals("http://example.com/policies/123", resolved.getSource());
    // assertEquals("123", resolved.getObject().getId());
    // assertEquals("Policy 123", resolved.getObject().getName());
    // // (and so on for any other relevant properties of the Policy object)
    // }

}
