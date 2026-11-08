package controller.admin.showtimes;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Showtime;
import service.ShowtimeService;
import service.impl.ShowtimeServiceImpl;

@WebServlet("/admin/showtimes/detail")
public class ShowtimeDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ShowtimeService showtimeService =
            new ShowtimeServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/showtimes");
            return;
        }

        Integer id = Integer.parseInt(idParam);

        Showtime showtime =
                showtimeService.getShowtimeById(id);

        if (showtime == null) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/showtimes");
            return;
        }

        request.setAttribute("showtime", showtime);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/showtimes/detail.jsp")
                .forward(request, response);
    }

}