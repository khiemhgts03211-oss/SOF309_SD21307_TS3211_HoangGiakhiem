package service.impl;

import java.sql.Date;
import java.sql.Time;
import java.util.Collections;
import java.util.List;

import dao.ShowtimeDAO;
import dao.impl.ShowtimeDAOImpl;
import model.Showtime;
import service.ShowtimeService;

public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeDAO showtimeDAO;

    public ShowtimeServiceImpl() {
        this.showtimeDAO = new ShowtimeDAOImpl();
    }

    @Override
    public List<Showtime> getAllShowtimes() {
        return showtimeDAO.findAll();
    }

    @Override
    public Showtime getShowtimeById(Integer id) {
        return showtimeDAO.findById(id);
    }

    @Override
    public boolean createShowtime(Showtime showtime) {

        // Business rule: check trùng lịch chiếu
        boolean conflict = showtimeDAO.existsShowtime(
                showtime.getRoomId(),
                showtime.getShowDate(),
                showtime.getStartTime()
        );

        if (conflict) {
            return false;
        }

        return showtimeDAO.insert(showtime);
    }

    @Override
    public boolean updateShowtime(Showtime showtime) {
        return showtimeDAO.update(showtime);
    }

    @Override
    public boolean deleteShowtime(Integer id) {
        return showtimeDAO.delete(id);
    }

    @Override
    public List<Showtime> searchShowtimes(String keyword) {
        return showtimeDAO.search(keyword);
    }

    @Override
    public List<Showtime> getShowtimesByPage(int page,
                                             int pageSize) {

        if (page <= 0 || pageSize <= 0) {
            return showtimeDAO.findAll();
        }

        List<Showtime> list =
                showtimeDAO.findByPage(page, pageSize);

        if (list == null || list.isEmpty()) {

            // Nếu phân trang không lấy được
            // thì trả toàn bộ dữ liệu
            return showtimeDAO.findAll();
        }

        return list;
    }

    @Override
    public int countShowtimes() {
        return showtimeDAO.count();
    }

    @Override
    public List<Showtime> getShowtimesByMovie(Integer movieId) {
        return showtimeDAO.findByMovie(movieId);
    }

    @Override
    public List<Showtime> getShowtimesByRoom(Integer roomId) {
        return showtimeDAO.findByRoom(roomId);
    }

    @Override
    public List<Showtime> getShowtimesByDate(Date date) {
        return showtimeDAO.findByDate(date);
    }

    @Override
    public List<Showtime> getShowtimesByMovieAndDate(Integer movieId, Date date) {
        return showtimeDAO.findByMovieAndDate(movieId, date);
    }

    @Override
    public List<Showtime> getShowtimesByStatus(String status) {
        return showtimeDAO.findByStatus(status);
    }

    @Override
    public List<Showtime> getAvailableShowtimes() {
        return showtimeDAO.findAvailableShowtimes();
    }

    @Override
    public boolean checkShowtimeConflict(Integer roomId, Date date, Time startTime) {
        return showtimeDAO.existsShowtime(roomId, date, startTime);
    }
    
    @Override
    public List<Showtime> sortShowtimes(String sortBy) {

        return showtimeDAO.sort(sortBy);

    }
    
    @Override
    public List<Showtime> getTodayShowtimes() {

        return showtimeDAO.findTodayShowtimes();

    }
    
    @Override
    public List<Showtime> getAvailableShowtimesByMovie(
            Integer movieId) {

        if (movieId == null || movieId <= 0) {
            return Collections.emptyList();
        }

        List<Showtime> showtimes =
                showtimeDAO.findAvailableByMovie(movieId);

        return showtimes == null
                ? Collections.emptyList()
                : showtimes;
    }
    
}