package controller.admin.services;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ServiceService;
import service.impl.ServiceServiceImpl;

@WebServlet("/admin/services/delete")
public class DeleteServiceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ServiceService serviceService =
            new ServiceServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            Integer id =
                    Integer.parseInt(request.getParameter("id"));

            serviceService.deleteService(id);

        } catch (Exception e) {

            e.printStackTrace();

        }

        response.sendRedirect(
                request.getContextPath()
                + "/admin/services");

    }

}