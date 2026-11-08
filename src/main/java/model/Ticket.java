package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Ticket {

    private int ticketId;
    private String bookingCode;
    private int userId;
    private int showtimeId;
    private int roomId;
    private int peopleCount;
    private BigDecimal totalPrice;
    private String status;
    private Timestamp createdAt;

    private User user;


    private Room room;
    private String customerName;
    private String movieTitle;
    private String roomName;
    private Timestamp showtime;
    private Timestamp startTime;
    private String customerEmail;
    private String customerPhone;
    public Ticket() {
    }

    public Ticket(int ticketId, String bookingCode, int userId, int showtimeId,
                  int roomId, int peopleCount, BigDecimal totalPrice,
                  String status, Timestamp createdAt) {
        this.ticketId = ticketId;
        this.bookingCode = bookingCode;
        this.userId = userId;
        this.showtimeId = showtimeId;
        this.roomId = roomId;
        this.peopleCount = peopleCount;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getTicketId() {
        return ticketId;
    }
    
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getBookingCode() {
        return bookingCode;
    }
    
    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getShowtimeId() {
        return showtimeId;
    }
    
    public void setShowtimeId(int showtimeId) {
        this.showtimeId = showtimeId;
    }

    public int getRoomId() {
        return roomId;
    }
    
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getPeopleCount() {
        return peopleCount;
    }
    
    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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

    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }
    
    public void setRoom(Room room) {
        this.room = room;
    }
    
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public Timestamp getShowtime() {
        return showtime;
    }

    public void setShowtime(Timestamp showtime) {
        this.showtime = showtime;
    }
    
    public Timestamp getStartTime() {
        return startTime;
    }
    
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    
}