package controller.admin.movies;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Movie;
import service.MovieService;
import service.impl.MovieServiceImpl;

@WebServlet("/admin/movies/detail")
public class DetailMovieServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final MovieService movieService =
            new MovieServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");

        if (id == null || id.isBlank()) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/movies");
            return;
        }

        Movie movie =
                movieService.findById(Integer.parseInt(id));

        if (movie == null) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/movies");
            return;
        }

        request.setAttribute("movie", movie);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/movies/detail.jsp")
                .forward(request, response);
    }

}