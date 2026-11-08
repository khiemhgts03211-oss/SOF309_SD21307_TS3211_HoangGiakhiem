package controller.admin.movies;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import service.MovieService;
import service.impl.MovieServiceImpl;

@WebServlet("/admin/movies/delete")
public class DeleteMovieServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final MovieService movieService =
            new MovieServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");

        if (id != null && !id.isBlank()) {

            movieService.delete(Integer.parseInt(id));

        }

        boolean success =
                movieService.delete(Integer.parseInt(id));

        if (!success) {

            request.getSession().setAttribute(
                    "error",
                    "Không thể xóa phim vì đã có suất chiếu.");

        }
        
        response.sendRedirect(
                request.getContextPath()
                + "/admin/movies");

    }

}