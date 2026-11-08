package controller.movie;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * /movies không còn là trang riêng — toàn bộ danh sách phim,
 * tìm kiếm và lọc thể loại cho khách hàng đã được xây đầy đủ
 * ngay tại trang chủ (xem HomeServlet + home.jsp).
 *
 * Servlet này chỉ giữ lại để không phá vỡ các link cũ trỏ
 * tới /movies — nó chuyển tiếp (redirect) về /home, giữ
 * nguyên tham số keyword/genre nếu có.
 */
@WebServlet("/movies")
public class MovieServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String genre = request.getParameter("genre");

        StringBuilder redirectUrl =
                new StringBuilder(
                        request.getContextPath() + "/home");

        StringBuilder query = new StringBuilder();

        if (keyword != null && !keyword.isBlank()) {

            query.append("keyword=")
                    .append(URLEncoder.encode(
                            keyword, StandardCharsets.UTF_8));
        }

        if (genre != null && !genre.isBlank()) {

            if (query.length() > 0) {
                query.append("&");
            }

            query.append("genre=")
                    .append(URLEncoder.encode(
                            genre, StandardCharsets.UTF_8));
        }

        if (query.length() > 0) {
            redirectUrl.append("?").append(query);
        } else {
            redirectUrl.append("#now-showing");
        }

        response.sendRedirect(redirectUrl.toString());
    }

}