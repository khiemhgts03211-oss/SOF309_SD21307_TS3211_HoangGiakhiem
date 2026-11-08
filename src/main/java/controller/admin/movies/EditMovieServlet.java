package controller.admin.movies;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Movie;
import service.MovieService;
import service.impl.MovieServiceImpl;

@WebServlet("/admin/movies/edit")
@MultipartConfig
public class EditMovieServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final MovieService movieService =
            new MovieServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        Movie movie = movieService.findById(id);

        request.setAttribute("movie", movie);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/movies/edit.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

    	Movie oldMovie = movieService.findById(
    	        Integer.parseInt(request.getParameter("movieId")));
    	
        Movie movie = new Movie();

        movie.setMovieId(
                Integer.parseInt(request.getParameter("movieId")));

        movie.setTitle(request.getParameter("title"));
        movie.setGenre(request.getParameter("genre"));
        movie.setDuration(
                Integer.parseInt(request.getParameter("duration")));
        movie.setDescription(request.getParameter("description"));
        movie.setReleaseDate(
                Date.valueOf(request.getParameter("releaseDate")));
        movie.setStatus(request.getParameter("status"));

        Part filePart = request.getPart("poster");

        String poster = oldMovie.getPoster();

        if (filePart != null && filePart.getSize() > 0) {

            String fileName = Paths.get(filePart.getSubmittedFileName())
                                   .getFileName()
                                   .toString();

            String uploadPath = getServletContext()
                    .getRealPath("/uploads");

            File dir = new File(uploadPath);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            filePart.write(uploadPath + File.separator + fileName);

            poster = "uploads/" + fileName;
        }

        movie.setPoster(poster);

        boolean success = movieService.update(movie);

        System.out.println("Update = " + success);
        System.out.println("Poster = " + poster);
        
        movieService.update(movie);

        response.sendRedirect(
                request.getContextPath()
                        + "/admin/movies");

    }

}