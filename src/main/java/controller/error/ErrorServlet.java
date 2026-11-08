package controller.error;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/error")
public class ErrorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Integer status =
            (Integer) request.getAttribute(
                    "jakarta.servlet.error.status_code");

        if(status == null)
            status = 500;

        request.setAttribute("statusCode", status);

        switch(status){

        case 403:

            request.setAttribute("title",
                    "403 - Forbidden");

            request.setAttribute("message",
                    "Bạn không có quyền truy cập trang này.");

            break;

        case 404:

            request.setAttribute("title",
                    "404 - Not Found");

            request.setAttribute("message",
                    "Trang bạn yêu cầu không tồn tại.");

            break;

        case 500:

            request.setAttribute("title",
                    "500 - Internal Server Error");

            request.setAttribute("message",
                    "Có lỗi xảy ra trong hệ thống.");

            break;

        default:

            request.setAttribute("title",
                    "Đã xảy ra lỗi");

            request.setAttribute("message",
                    "Có lỗi không xác định.");

        }

        request.getRequestDispatcher(
                "/WEB-INF/views/error/error.jsp")
                .forward(request, response);

    }

}