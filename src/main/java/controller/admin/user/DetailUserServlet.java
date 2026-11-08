package controller.admin.user;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.User;
import service.UserService;
import service.impl.UserServiceImpl;

@WebServlet("/admin/users/detail")
public class DetailUserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserService userService =
            new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");

        if (id == null) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/users");
            return;
        }

        User user = userService.findById(
                Integer.parseInt(id));

        if (user == null) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/users");
            return;
        }

        request.setAttribute("user", user);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/users/detail.jsp")
                .forward(request, response);
    }
}