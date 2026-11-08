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

@WebServlet("/admin/service-categories/edit")
public class EditServiceCategoryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ServiceCategoryService service =
            new ServiceCategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int id =
                Integer.parseInt(request.getParameter("id"));

        ServiceCategory category =
                service.getCategoryById(id);

        if (category == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/service-categories");

            return;
        }

        request.setAttribute("category", category);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/service-categories/edit.jsp")
                .forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ServiceCategory category =
                new ServiceCategory();

        category.setCategoryId(
                Integer.parseInt(
                        request.getParameter("categoryId")));

        category.setCategoryName(
                request.getParameter("categoryName"));

        category.setStatus(
                request.getParameter("status"));

        service.updateCategory(category);

        response.sendRedirect(
                request.getContextPath()
                + "/admin/service-categories");

    }

}