package controller.admin.user;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import service.UserService;
import service.impl.UserServiceImpl;

@WebServlet("/admin/users/delete")
public class DeleteUserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");

        if (id != null && !id.isBlank()) {

            userService.delete(Integer.parseInt(id));

        }

        response.sendRedirect(
                request.getContextPath()
                + "/admin/users");
    }
}