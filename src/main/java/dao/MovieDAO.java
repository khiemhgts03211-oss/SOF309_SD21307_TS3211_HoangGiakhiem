package dao;

import java.util.List;

import model.Movie;

public interface MovieDAO extends GenericDAO<Movie, Integer> {

    /**
     * Phim đang chiếu
     */
    List<Movie> findNowShowing();

    /**
     * Phim sắp chiếu
     */
    List<Movie> findComingSoon();

    /**
     * Tìm theo thể loại
     */
    List<Movie> findByGenre(String genre);

    /**
     * Tìm theo trạng thái
     */
    List<Movie> findByStatus(String status);

    /**
     * Kiểm tra tên phim tồn tại
     */
    boolean existsTitle(String title);
    
    List<Movie> findAll();
    
    List<Movie> search(String keyword);
    
    List<Movie> filter(String genre, String status);
    
    boolean insert(Movie movie);
    
    List<Movie> sort(String sortBy, String order);
    
    /**
     * Tìm phim đang hoạt động theo tên phim,
     * thể loại, đạo diễn hoặc diễn viên.
     */
    List<Movie> searchActiveMovies(String keyword);
    
    List<Movie> searchMovies(
            String keyword,
            String genre);

    List<String> getAllGenres();
}