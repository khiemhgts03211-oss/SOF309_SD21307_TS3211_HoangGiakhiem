package controller.auth;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public LogoutServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

    	System.out.println("[LogoutServlet]");
    	
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        HttpSession newSession = request.getSession(true);

        newSession.setAttribute("success",
                "Đăng xuất thành công.");

        response.sendRedirect(request.getContextPath() + "/login");

    }

}