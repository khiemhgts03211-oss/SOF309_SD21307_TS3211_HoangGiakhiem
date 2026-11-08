package controller.admin.services;

import java.io.IOException;
import java.math.BigDecimal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Service;
import service.ServiceCategoryService;
import service.ServiceService;
import service.impl.ServiceCategoryServiceImpl;
import service.impl.ServiceServiceImpl;

@WebServlet("/admin/services/add")
public class AddServiceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ServiceService serviceService = new ServiceServiceImpl();
    private final ServiceCategoryService categoryService = new ServiceCategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("categories",
                categoryService.getAllCategories());

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/services/add-service.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String serviceName = request.getParameter("serviceName");
        String priceStr = request.getParameter("price");
        String categoryIdStr = request.getParameter("categoryId");
        String status = request.getParameter("status");

        try {

            Service service = new Service();

            service.setServiceName(serviceName);
            service.setPrice(new BigDecimal(priceStr));
            service.setCategoryId(Integer.parseInt(categoryIdStr));
            service.setStatus(status);

            boolean success = serviceService.createService(service);

            if (success) {

                response.sendRedirect(request.getContextPath() + "/admin/services");

            } else {

                request.setAttribute("error", "Tên dịch vụ đã tồn tại.");

                request.setAttribute("categories", categoryService.getAllCategories());

                request.getRequestDispatcher("/WEB-INF/views/admin/services/add-service.jsp")
                        .forward(request, response);
            }

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute("error", "Dữ liệu không hợp lệ.");

            request.setAttribute("categories", categoryService.getAllCategories());

            request.getRequestDispatcher("/WEB-INF/views/admin/services/add-service.jsp")
                    .forward(request, response);
        }
    }
}