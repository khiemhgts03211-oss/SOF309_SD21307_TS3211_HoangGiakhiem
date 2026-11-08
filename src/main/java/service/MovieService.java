package service;

import java.util.List;

import model.Movie;

public interface MovieService extends GenericService<Movie, Integer> {

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
    
    List<Movie> filter(String genre,String status);
    
    List<Movie> sort(String sortBy, String order);
    
    List<Movie> searchActiveMovies(String keyword);
    
    List<Movie> searchMovies(
            String keyword,
            String genre);

    List<String> getAllGenres();
    
    
}