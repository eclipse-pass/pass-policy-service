package org.eclipse.pass.policy.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.dataconservancy.pass.client.PassClient;
import org.dataconservancy.pass.model.Policy;
import org.eclipse.pass.policy.components.ResolvedObject;
import org.json.JSONException;
import org.json.JSONObject;
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
        } catch (IOException e) {
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
    @Test
    @DisplayName("Test: Test resolveToObject() with a valid submission URI")
    public void testResolveToObjectValidURI() throws URISyntaxException {
        String uri = "http://example.com/policies/1";
        Variable v = new Variable("foo.bar.baz.policy");
        v.setSegment("policySegmentName");
        v.setSegmentName("policySegment");
        ResolvedObject expected = null;
        try {
            expected = new ResolvedObject("http://example.com/policies/1", mockPolicy);
        } catch (IOException e) {
            fail("Unexpected exception thrown for valid submission URI", e);
        }

        when(mockPassClient.readResource(new URI("http://example.com/policies/1"),
                Policy.class))
                .thenReturn(mockPolicy);
        try {
            context.setPassClient(mockPassClient);
            context.resolveToObject(v, uri);
        } catch (RuntimeException e) {
            fail("Unexpected Exception thrown for valid submission", e);
        }

        assertTrue(expected.equals(context.getValues().get("policySegmentName")));
        assertTrue(expected.equals(context.getValues().get("policySegmentName")));
    }

    @Test
    @DisplayName("Test: Test resolveToObject() with an invalid submission URI")
    public void testResolveToObjectInvalidURI() throws URISyntaxException {
        String uri = "http://example.com/1";
        Variable v = new Variable("foo.bar.baz.policy");
        v.setSegment("policySegmentName");
        v.setSegmentName("policySegment");

        try {
            context.setPassClient(mockPassClient);
            context.resolveToObject(v, uri);
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof IOException);
            return;
        }

        fail("resolveToObject() should throw a RuntimeException when invalid submissions are given");
    }

    @Test
    @DisplayName("Test: Test resolveToObject() with an invalid submission")
    public void testResolveToObjectInvalid() throws URISyntaxException {
        String uri = "example.com/policies/1";
        Variable v = new Variable("foo.bar.baz.policy");
        v.setSegment("policySegmentName");
        v.setSegmentName("policySegment");
        try {
            context.setPassClient(mockPassClient);
            context.resolveToObject(v, uri);
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof JSONException);
            return;
        }

        fail("resolveToObject() should throw a RuntimeException when invalid submissions are given");
    }

    @Test
    @DisplayName("Test: Test resolveToObject() with a valid JSON string")
    public void testResolveToObjectValidJSON() {
        String json = "{"
                + "\"policy-id\": \"1\","
                + "\"description\": \"test\","
                + "}";
        Variable v = new Variable("foo.bar.baz.policy");
        v.setSegment("policySegmentName");
        v.setSegmentName("policySegment");
        ResolvedObject expected = new ResolvedObject(json, new JSONObject(json));

        try {
            context.setPassClient(mockPassClient);
            context.resolveToObject(v, json);
        } catch (RuntimeException e) {
            fail("Unexpected Exception thrown for valid submission", e);
        }

        assertTrue(expected.equals(context.getValues().get("policySegmentName")));
        assertTrue(expected.equals(context.getValues().get("policySegmentName")));
    }

    @Test
    @DisplayName("Test: Test resolveToObject() with an invalid JSON string")
    public void testResolveToObjectInvalidJSON() {
        String json = "{"
                + "\"policy-id\"1\","
                + "\"description\": \"test\","
                + "}";
        Variable v = new Variable("foo.bar.baz.policy");
        v.setSegment("policySegmentName");
        v.setSegmentName("policySegment");

        try {
            context.setPassClient(mockPassClient);
            context.resolveToObject(v, json);
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof JSONException);
            return;
        }

        fail("resolveToObject() should throw a RuntimeException when invalid submissions are given");
    }

    /**
     * Unit tests for resolveToObjects() method in Context
     */
    @Test
    @DisplayName("Test: Test resolveToObjects() with a list of valid submission URIs")
    public void testResolveToObjectsValidURI() {
        List<String> uris = Arrays.asList(
                "http://example.com/policies/1",
                "http://example.com/policies/2");
        Variable v = new Variable("foo.bar.baz.policy");
        v.setSegment("policySegmentName");
        v.setSegmentName("policySegment");
        List<ResolvedObject> expected;
        try {
            expected = Arrays.asList(
                    new ResolvedObject("http://example.com/policies/1", mockPolicy),
                    new ResolvedObject("http://example.com/policies/2", mockPolicy));

            when(mockPassClient.readResource(new URI("http://example.com/policies/1"),
                    Policy.class))
                    .thenReturn(mockPolicy);
            when(mockPassClient.readResource(new URI("http://example.com/policies/2"),
                    Policy.class))
                    .thenReturn(mockPolicy);
            try {
                context.setPassClient(mockPassClient);
                context.resolveToObjects(v, uris);
            } catch (RuntimeException e) {
                fail("Unexpected Exception thrown for valid submission", e);
            }

            assertTrue(expected.equals(context.getValues().get("policySegmentName")));
            assertTrue(expected.equals(context.getValues().get("policySegmentName")));
        } catch (URISyntaxException | IOException e) {
            fail("Unexpected Exception thrown for valid submission", e);
        }
    }

    @Test
    @DisplayName("Test: Test resolveToObjects() with a list of invalid submission URIs")
    public void testResolveToObjectsInvalidURI() throws URISyntaxException {
        List<String> uris = Arrays.asList(
                "http://example.com/1",
                "http://example.com/2");
        Variable v = new Variable("foo.bar.baz.policy");
        v.setSegment("policySegmentName");
        v.setSegmentName("policySegment");
        try {
            context.setPassClient(mockPassClient);
            context.resolveToObjects(v, uris);
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof IOException);
            return;
        }

        fail("resolveToObjects() should throw a RuntimeException when invalid submissions are given");
    }

    @Test
    @DisplayName("Test: Test resolveToObjects() with a list of invalid submissions")
    public void testResolveToObjectsInvalid() throws URISyntaxException {
        List<String> uris = Arrays.asList(
                "example.com/policies/1",
                "example.com/policies/2");
        Variable v = new Variable("foo.bar.baz.policy");
        v.setSegment("policySegmentName");
        v.setSegmentName("policySegment");
        try {
            context.setPassClient(mockPassClient);
            context.resolveToObjects(v, uris);
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof JSONException);
            return;
        }

        fail("resolveToObjects() should throw a RuntimeException when invalid submissions are given");
    }

    @Test
    @DisplayName("Test: Test resolveToObjects() with a list of valid JSON strings")
    public void testResolveToObjectsValidJSON() throws URISyntaxException {
        List<String> json = Arrays.asList(
                "{"
                        + "\"policy-id\": \"1\","
                        + "\"description\": \"test\","
                        + "}",
                "{"
                        + "\"policy-id\": \"2\","
                        + "\"description\": \"test\","
                        + "}");
        Variable v = new Variable("foo.bar.baz.policy");
        v.setSegment("policySegmentName");
        v.setSegmentName("policySegment");
        List<ResolvedObject> expected = Arrays.asList(
                new ResolvedObject(json.get(0), new JSONObject(json.get(0))),
                new ResolvedObject(json.get(1), new JSONObject(json.get(1))));

        try {
            context.setPassClient(mockPassClient);
            context.resolveToObjects(v, json);
        } catch (RuntimeException e) {
            fail("Unexpected Exception thrown for valid submission", e);
        }

        assertTrue(expected.equals(context.getValues().get("policySegmentName")));
        assertTrue(expected.equals(context.getValues().get("policySegmentName")));
    }

    @Test
    @DisplayName("Test: Test resolveToObjects() with a list of invalid JSON strings")
    public void testResolveToObjectsInvalidJSON() throws URISyntaxException {
        List<String> json = Arrays.asList(
                "{"
                        + "\"policy-id\"1\","
                        + "\"description\": \"test\","
                        + "}",
                "{"
                        + "\"policy-id,"
                        + "\"description\": \"test\","
                        + "}");
        Variable v = new Variable("foo.bar.baz.policy");
        v.setSegment("policySegmentName");
        v.setSegmentName("policySegment");

        try {
            context.setPassClient(mockPassClient);
            context.resolveToObjects(v, json);
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof JSONException);
            return;
        }

        fail("resolveToObjects() should throw a RuntimeException when invalid submissions are given");
    }
}
