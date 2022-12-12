package org.eclipse.pass.policy.services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dataconservancy.pass.model.Policy;
import org.eclipse.pass.policy.interfaces.PolicyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the PolicyServlet
 *
 * @author David McIntyre
 */
@DisplayName("PolicyServlet Tests")
public class PolicyServletTest {

    // test doGet()
    @Test
    @DisplayName("Test: Test of doGet() method. Should return a response to a given request")
    void TestDoGet() throws Exception {
        // mock HttpServletRequest & HttpServletResponse & PolicyService
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        PolicyService mockPolicyService = mock(PolicyServiceImpl.class);

        // mock the returned values of
        Map<String, String> headers = new HashMap();
        headers.put("Context-Path", "test.submission.com");
        headers.put("Query-String", "test.query");
        Enumeration<String> headerNames = Collections.enumeration(headers.keySet());

        when(mockRequest.getParameter("submission")).thenReturn("valid submission");
        when(mockRequest.getHeaderNames()).thenReturn(headerNames);
        when(mockRequest.getHeader("Context-Path")).thenReturn("test.submission.com");
        when(mockRequest.getHeader("Query-String")).thenReturn("test.query");

        // mock the returned value of policyService.findPolicies()
        String submission = "valid submission";
        List<Policy> policies = new ArrayList<Policy>();
        policies.add(new Policy());
        when(mockPolicyService.findPolicies(submission, headers)).thenReturn(policies);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);

        new PolicyServlet(mockPolicyService).doGet(mockRequest, mockResponse);

        verify(mockRequest, atLeast(1)).getParameter("submission");
        verify(mockRequest, atLeast(1)).getHeaderNames();
        verify(mockPolicyService, atLeast(1)).findPolicies(submission, headers);
        writer.flush();
        assertTrue(stringWriter.toString().contains("Served at:"));
    }

    // test doPost()
    @Test
    @DisplayName("Test: Test of doPost() method. Should return a response to a given request")
    void TestDoPost() throws Exception {
        // mock HttpServletRequest & HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // mock the returned value of request.getParameter
        when(request.getHeader("Content-Type")).thenReturn("application/x-www-form-urlencoded");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new PolicyServlet().doPost(request, response);
        verify(request, atLeast(1)).getHeader("Content-Type");
        assertTrue(stringWriter.toString().contains("Served at:"));
    }

}
