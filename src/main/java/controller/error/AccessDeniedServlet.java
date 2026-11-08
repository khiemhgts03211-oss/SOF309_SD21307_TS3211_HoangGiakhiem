package controller.error;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/access-denied")
public class AccessDeniedServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public AccessDeniedServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

    	System.out.println("[AccessDeniedServlet]");
    	
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        request.getRequestDispatcher(
                "/WEB-INF/views/error/access-denied.jsp")
                .forward(request, response);
    }

}