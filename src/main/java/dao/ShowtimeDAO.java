package dao;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import model.Showtime;

public interface ShowtimeDAO extends GenericDAO<Showtime, Integer> {

    /**
     * Lấy danh sách suất chiếu theo phim
     */
    List<Showtime> findByMovie(Integer movieId);

    /**
     * Lấy danh sách suất chiếu theo phòng
     */
    List<Showtime> findByRoom(Integer roomId);

    /**
     * Lấy danh sách suất chiếu theo ngày
     */
    List<Showtime> findByDate(Date showDate);

    /**
     * Lấy suất chiếu theo phim và ngày
     */
    List<Showtime> findByMovieAndDate(Integer movieId, Date showDate);

    /**
     * Lấy suất chiếu theo trạng thái
     */
    List<Showtime> findByStatus(String status);

    /**
     * Lấy các suất chiếu đang hoạt động
     */
    List<Showtime> findAvailableShowtimes();

    /**
     * Tìm kiếm suất chiếu
     */
    List<Showtime> search(String keyword);

    /**
     * Phân trang
     */
    List<Showtime> findByPage(int page, int pageSize);

    /**
     * Kiểm tra trùng lịch chiếu
     */
    boolean existsShowtime(Integer roomId, Date showDate, Time startTime);

    List<Showtime> sort(String sortBy);
    
    List<Showtime> findTodayShowtimes();
    
    /**
     * Lấy các suất chiếu còn khả dụng của một phim.
     */
    List<Showtime> findAvailableByMovie(Integer movieId);
    
}