package controller.admin.services;

import java.io.IOException;
import java.math.BigDecimal;
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

@WebServlet("/admin/services/edit")
public class EditServiceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ServiceService serviceService =
            new ServiceServiceImpl();

    private final ServiceCategoryService categoryService =
            new ServiceCategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            Integer id = Integer.parseInt(
                    request.getParameter("id"));

            Service service =
                    serviceService.getServiceById(id);

            if (service == null) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/services");

                return;
            }

            List<ServiceCategory> categories =
                    categoryService.getAllCategories();

            request.setAttribute("service", service);
            request.setAttribute("categories", categories);

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/services/edit-service.jsp")
                    .forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/services");

        }

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {

            int serviceId =
                    Integer.parseInt(
                            request.getParameter("serviceId"));

            int categoryId =
                    Integer.parseInt(
                            request.getParameter("categoryId"));

            String serviceName =
                    request.getParameter("serviceName");

            BigDecimal price =
                    new BigDecimal(
                            request.getParameter("price"));

            String status =
                    request.getParameter("status");

            Service service = new Service();

            service.setServiceId(serviceId);
            service.setCategoryId(categoryId);
            service.setServiceName(serviceName);
            service.setPrice(price);
            service.setStatus(status);

            boolean success =
                    serviceService.updateService(service);

            if (success) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/services");

                return;

            }

            request.setAttribute(
                    "error",
                    "Cập nhật dịch vụ thất bại.");

            request.setAttribute(
                    "service",
                    service);

            request.setAttribute(
                    "categories",
                    categoryService.getAllCategories());

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/services/edit-service.jsp")
                    .forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Dữ liệu không hợp lệ.");

            request.setAttribute(
                    "categories",
                    categoryService.getAllCategories());

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/services/edit-service.jsp")
                    .forward(request, response);

        }

    }

}