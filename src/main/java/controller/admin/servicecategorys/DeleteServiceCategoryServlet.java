package controller.admin.servicecategorys;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import service.ServiceCategoryService;
import service.impl.ServiceCategoryServiceImpl;

@WebServlet("/admin/service-categories/delete")
public class DeleteServiceCategoryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ServiceCategoryService service =
            new ServiceCategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam != null) {

            int id = Integer.parseInt(idParam);
            
            boolean success = service.deleteCategory(id);

            if (!success) {

                request.getSession().setAttribute(
                        "error",
                        "Cannot delete category because it is being used.");

            }

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/service-categories");
            
        }
        
    }

}