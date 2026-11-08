package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Showtime {

	private int showtimeId;
	private int movieId;
	private int roomId;

	private Date showDate;

	private Time startTime;

	private Time endTime;

	private BigDecimal ticketPrice;

	private String status;

	private Timestamp createdAt;

	private Movie movie;

	private Room room;
	private String movieTitle;
	private String roomName;
	
    public Showtime() {
    }

	public Showtime(int showtimeId, int movieId, int roomId, Date showDate, Time startTime, Time endTime,
			BigDecimal ticketPrice, String status, Timestamp createdAt, Movie movie, Room room) {
		super();
		this.showtimeId = showtimeId;
		this.movieId = movieId;
		this.roomId = roomId;
		this.showDate = showDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.ticketPrice = ticketPrice;
		this.status = status;
		this.createdAt = createdAt;
		this.movie = movie;
		this.room = room;
	}

	public int getShowtimeId() {
		return showtimeId;
	}

	public void setShowtimeId(int showtimeId) {
		this.showtimeId = showtimeId;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public Date getShowDate() {
		return showDate;
	}

	public void setShowDate(Date showDate) {
		this.showDate = showDate;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public BigDecimal getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(BigDecimal ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getMovieTitle() {
	    return movieTitle;
	}

	public void setMovieTitle(String movieTitle) {
	    this.movieTitle = movieTitle;
	}

	public String getRoomName() {
	    return roomName;
	}

	public void setRoomName(String roomName) {
	    this.roomName = roomName;
	}
	
}