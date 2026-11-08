package controller.admin.services;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Service;
import service.ServiceService;
import service.impl.ServiceServiceImpl;

@WebServlet("/admin/services/detail")
public class DetailServiceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ServiceService service =
            new ServiceServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Integer id =
                Integer.parseInt(request.getParameter("id"));

        Service s = service.getServiceById(id);

        request.setAttribute("service", s);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/services/detail-service.jsp")
                .forward(request, response);

    }

}