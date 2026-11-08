package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Payment {

    private int paymentId;
    private int ticketId;
    private String transactionCode;
    private String paymentMethod;
    private BigDecimal amount;
    private String status;
    private Timestamp paidAt;
    private Timestamp createdAt;

    private Ticket ticket;
    private String customerName;
    private String paymentStatus;
    private String bookingCode;
    private String movieTitle;
    private String roomName;
    private Timestamp showtime;
    private String customerEmail;
    private Integer customerId;
    private String customerPhone;
    private java.sql.Date showDate;
    private java.sql.Time startTime;
    private java.sql.Time endTime;
    private BigDecimal ticketTotalPrice;
    private int peopleCount;
    private String ticketStatus;
    
    public Payment() {
    }

    public Payment(int paymentId, int ticketId, String transactionCode,
                   String paymentMethod, BigDecimal amount, String status,
                   Timestamp paidAt, Timestamp createdAt) {
        this.paymentId = paymentId;
        this.ticketId = ticketId;
        this.transactionCode = transactionCode;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.status = status;
        this.paidAt = paidAt;
        this.createdAt = createdAt;
    }

    public int getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getTicketId() {
        return ticketId;
    }
    
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getTransactionCode() {
        return transactionCode;
    }
    
    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getPaidAt() {
        return paidAt;
    }
    
    public void setPaidAt(Timestamp paidAt) {
        this.paidAt = paidAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Ticket getTicket() {
        return ticket;
    }
    
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
    
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
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
    
    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public java.sql.Date getShowDate() {
        return showDate;
    }

    public void setShowDate(java.sql.Date showDate) {
        this.showDate = showDate;
    }

    public java.sql.Time getStartTime() {
        return startTime;
    }

    public void setStartTime(java.sql.Time startTime) {
        this.startTime = startTime;
    }

    public java.sql.Time getEndTime() {
        return endTime;
    }

    public void setEndTime(java.sql.Time endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getTicketTotalPrice() {
        return ticketTotalPrice;
    }

    public void setTicketTotalPrice(
            BigDecimal ticketTotalPrice) {

        this.ticketTotalPrice = ticketTotalPrice;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }
    
}