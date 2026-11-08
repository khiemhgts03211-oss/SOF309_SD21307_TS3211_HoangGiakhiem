package service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import model.Showtime;

public interface ShowtimeService {

    List<Showtime> getAllShowtimes();

    Showtime getShowtimeById(Integer id);

    boolean createShowtime(Showtime showtime);

    boolean updateShowtime(Showtime showtime);

    boolean deleteShowtime(Integer id);

    List<Showtime> searchShowtimes(String keyword);

    List<Showtime> getShowtimesByPage(int page, int pageSize);

    int countShowtimes();

    List<Showtime> getShowtimesByMovie(Integer movieId);

    List<Showtime> getShowtimesByRoom(Integer roomId);

    List<Showtime> getShowtimesByDate(Date date);

    List<Showtime> getShowtimesByMovieAndDate(Integer movieId, Date date);

    List<Showtime> getShowtimesByStatus(String status);

    List<Showtime> getAvailableShowtimes();

    boolean checkShowtimeConflict(Integer roomId, Date date, Time startTime);
    
    List<Showtime> sortShowtimes(String sortBy);
    
    List<Showtime> getTodayShowtimes();
    
    List<Showtime> getAvailableShowtimesByMovie(
            Integer movieId);
    
}