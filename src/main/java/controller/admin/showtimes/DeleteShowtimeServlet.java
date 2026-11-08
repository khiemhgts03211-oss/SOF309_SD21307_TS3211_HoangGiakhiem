package controller.admin.showtimes;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import service.ShowtimeService;
import service.impl.ShowtimeServiceImpl;

@WebServlet("/admin/showtimes/delete")
public class DeleteShowtimeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ShowtimeService showtimeService;

    @Override
    public void init() throws ServletException {

        try {

            showtimeService = new ShowtimeServiceImpl();

        } catch (Exception exception) {

            throw new ServletException(
                    "Không thể khởi tạo ShowtimeService.",
                    exception);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        Integer showtimeId =
                parsePositiveInteger(
                        request.getParameter("id"));

        if (showtimeId == null) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/showtimes?error=invalid_id");

            return;
        }

        try {

            /*
             * Đổi tên method nếu ShowtimeService của project
             * đang dùng delete(), removeShowtime() hoặc deleteById().
             */
            boolean deleted =
                    showtimeService.deleteShowtime(
                            showtimeId);

            if (deleted) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/showtimes?success=deleted");

            } else {

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/showtimes?error=delete_failed");
            }

        } catch (Exception exception) {

            exception.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/showtimes?error=delete_failed");
        }
    }

    /*
     * Không cho xóa bằng GET.
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/admin/showtimes");
    }

    private Integer parsePositiveInteger(
            String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        try {

            int parsedValue =
                    Integer.parseInt(
                            value.trim());

            return parsedValue > 0
                    ? parsedValue
                    : null;

        } catch (NumberFormatException exception) {

            return null;
        }
    }
}