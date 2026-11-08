package controller.admin.movies;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.Movie;
import service.MovieService;
import service.impl.MovieServiceImpl;
import jakarta.servlet.annotation.MultipartConfig;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 5,
    maxRequestSize = 1024 * 1024 * 10
)

@WebServlet("/admin/movies/add")
public class AddMovieServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final MovieService movieService =
            new MovieServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/movies/add.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Movie movie = new Movie();

        movie.setTitle(request.getParameter("title"));
        movie.setGenre(request.getParameter("genre"));

        movie.setDuration(
                Integer.parseInt(
                        request.getParameter("duration")));

        movie.setDescription(
                request.getParameter("description"));

        movie.setReleaseDate(
                Date.valueOf(
                        request.getParameter("releaseDate")));

        movie.setStatus(
                request.getParameter("status"));

        Part filePart = request.getPart("poster");

        String poster = "";

        if (filePart != null && filePart.getSize() > 0) {

            String fileName = Paths.get(filePart.getSubmittedFileName())
                    .getFileName()
                    .toString();

            String uploadPath =
                    getServletContext().getRealPath("/uploads");

            File dir = new File(uploadPath);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            filePart.write(uploadPath + File.separator + fileName);

            poster = "uploads/" + fileName;
        }

        movie.setPoster(poster);

        boolean success = movieService.save(movie);

        if (success) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/movies");

        } else {

            request.setAttribute(
                    "error",
                    "Thêm phim thất bại");

            request.setAttribute(
                    "movie",
                    movie);
            		
            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/movies/add.jsp")
                    .forward(request, response);
        }
    }

}