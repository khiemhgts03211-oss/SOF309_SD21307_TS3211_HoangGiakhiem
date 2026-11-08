package controller.admin.user;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.User;
import service.UserService;
import service.impl.UserServiceImpl;

@WebServlet("/admin/users/edit")
public class EditUserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserService userService =
            new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(
                request.getParameter("id"));

        User user = userService.findById(id);

        request.setAttribute("user", user);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/users/edit.jsp")
                .forward(request, response);

    }

}