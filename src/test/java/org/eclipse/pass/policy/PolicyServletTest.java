package org.eclipse.pass.policy;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.pass.policy.services.PolicyServlet;
import org.junit.jupiter.api.*;

/**
 * Unit tests for the PolicyServlet
 *
 * @author David McIntyre
 */
@DisplayName("PolicyServlet Tests")
public class PolicyServletTest {

    // test doGet()
    @Test
    @DisplayName("Should return a response to a given request")
    void TestDoGet() throws Exception {
        // mock HttpServletRequest & HttpServletResponse
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // mock the returned value of request.getParameter
        when(request.getParameter("submission")).thenReturn("testSubmission");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new PolicyServlet().doGet(request, response);

        verify(request, atLeast(1)).getParameter("submission");
        writer.flush();
        assertTrue(stringWriter.toString().contains("Served at:"));
    }

    // test doPost()
    @Test
    @DisplayName("Should return a response to a given request")
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
