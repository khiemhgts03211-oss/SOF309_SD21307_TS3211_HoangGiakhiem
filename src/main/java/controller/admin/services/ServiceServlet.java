package controller.admin.services;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Service;
import model.ServiceCategory;
import service.ServiceCategoryService;
import service.ServiceService;
import service.impl.ServiceCategoryServiceImpl;
import service.impl.ServiceServiceImpl;

@WebServlet("/admin/services")
public class ServiceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ServiceService service =
            new ServiceServiceImpl();

    private final ServiceCategoryService categoryService =
            new ServiceCategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String categoryParam = request.getParameter("category");
        String status = request.getParameter("status");
        String sort = request.getParameter("sort");

        List<Service> services;

        if (keyword != null && !keyword.isBlank()) {

            services = service.searchServices(keyword);

        } else if (categoryParam != null && !categoryParam.isBlank()) {

            services = service.getServicesByCategory(
                    Integer.parseInt(categoryParam));

        } else if (status != null && !status.isBlank()) {

            services = service.getServicesByStatus(status);

        } else {

            services = service.getAllServices();

        }

        // Sort chỉ áp dụng khi không đang search/filter,
        // để tránh mất kết quả filter đang có
        if (sort != null && !sort.isBlank()
                && (keyword == null || keyword.isBlank())
                && (categoryParam == null || categoryParam.isBlank())
                && (status == null || status.isBlank())) {

            if ("name".equals(sort)) {

                services.sort((a, b) ->
                        a.getServiceName().compareToIgnoreCase(
                                b.getServiceName()));

            } else if ("price".equals(sort)) {

                services.sort((a, b) ->
                        a.getPrice().compareTo(b.getPrice()));

            }

        }

        List<ServiceCategory> categories =
                categoryService.getAllCategories();

        request.setAttribute("services", services);
        request.setAttribute("categories", categories);
        request.setAttribute("keyword", keyword);
        request.setAttribute("category", categoryParam);
        request.setAttribute("status", status);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/services/list.jsp")
                .forward(request, response);
    }
}
