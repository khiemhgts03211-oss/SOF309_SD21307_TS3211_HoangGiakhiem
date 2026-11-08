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

@WebServlet("/admin/service-categories/add")
public class AddServiceCategoryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ServiceCategoryService service =
            new ServiceCategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/service-categories/add.jsp")
                .forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ServiceCategory category = new ServiceCategory();

        category.setCategoryName(
                request.getParameter("categoryName"));

        category.setStatus(
                request.getParameter("status"));

        boolean success =
                service.createCategory(category);

        if (!success) {

            request.setAttribute(
                    "error",
                    "Category already exists.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/service-categories/add.jsp")
                    .forward(request, response);

            return;
        }

        response.sendRedirect(
                request.getContextPath()
                + "/admin/service-categories");

    }

}