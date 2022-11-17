package org.eclipse.pass.policy.services;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Repositories Servlet.
 * Handles requests & responses relating to repositories.
 *
 * @author David McIntyre
 */
@WebServlet("/repositories")
public class RepositoriesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(PolicyServlet.class);

    private PolicyServiceImpl policyService = new PolicyServiceImpl();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RepositoriesServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     *      Handles incoming GET requests to /repositories endpoint
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

        // call to policy service
        try {

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }

        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     *      Handles incoming POST requests to /repositories endpoint
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // handle wrong request content-type
        if (request.getHeader("Content-Type") != "application/x-www-form-urlencoded")
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Expected media type: application/x-www-form-urlencoded but got "
                            + request.getHeader("Content-Type"));

        response.getWriter().append("Served at: ").append(request.getContextPath());
    }
}
