package controller.admin.servicecategorys;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ServiceCategory;
import service.ServiceCategoryService;
import service.impl.ServiceCategoryServiceImpl;

@WebServlet("/admin/service-categories/detail")
public class ServiceCategoryDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ServiceCategoryService service =
            new ServiceCategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");

        if (id == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/service-categories");

            return;

        }

        ServiceCategory category =
                service.getCategoryById(Integer.parseInt(id));

        if (category == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/service-categories");

            return;

        }

        request.setAttribute("category", category);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/service-categories/detail.jsp")
                .forward(request, response);

    }

}