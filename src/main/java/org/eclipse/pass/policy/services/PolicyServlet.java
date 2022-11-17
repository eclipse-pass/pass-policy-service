package org.eclipse.pass.policy.services;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dataconservancy.pass.model.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Policy Servlet.
 * Handles requests & responses relating to policies.
 *
 * @author David McIntyre
 */
@WebServlet("/policies")
public class PolicyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(PolicyServlet.class);

    private PolicyServiceImpl policyService = new PolicyServiceImpl();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PolicyServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     *      Handles incoming GET requests to the /policies endpoint
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        LOG.info("Servicing new request......");
        LOG.debug("Context path: " + request.getContextPath() + "; query string " + request.getQueryString());

        // retrieve submission URI from request
        String submission = request.getParameter("submission");

        // handle empty request submission error
        if (submission == null)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No submission query param provided");

        // retieve map of headers and values from request
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headers = new HashMap<String, String>();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                headers.put(key, value);
            }
        }

        // call to policy service
        try {
            Policy policies[] = policyService.findPolicies(submission, headers);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }

        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     *      Handles incoming POST requests to the /policies endpoint
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // handle wrong request content-type
        if (request.getHeader("Content-Type") != "application/x-www-form-urlencoded")
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Expected media type: application/x-www-form-urlencoded but got "
                            + request.getHeader("Content-Type"));

        response.getWriter().append("Served at: ").append(request.getContextPath());
    }
}