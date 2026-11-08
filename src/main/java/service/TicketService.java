package service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import model.Ticket;

public interface TicketService {

    List<Ticket> getAllTickets();

    Ticket getTicketById(Integer id);

    boolean createTicket(Ticket ticket);

    boolean updateTicket(Ticket ticket);

    boolean deleteTicket(Integer id);

    List<Ticket> searchTickets(String keyword);

    List<Ticket> getTicketsByPage(int page, int pageSize);

    int countTickets();

    List<Ticket> getTicketsByUser(Integer userId);

    List<Ticket> getTicketsByShowtime(Integer showtimeId);

    List<Ticket> getTicketsByStatus(String status);

    Ticket getTicketByBookingCode(String bookingCode);

    List<Ticket> getTicketsByDate(Date bookingDate);

    List<Ticket> getTicketsByDateRange(
            Timestamp fromDate,
            Timestamp toDate);

    List<Ticket> getTicketsByUserAndStatus(
            Integer userId,
            String status);

    int countTicketsByUser(Integer userId);

    double getTotalRevenue();

    boolean checkBookingCodeExists(String bookingCode);

    List<Ticket> filter(String status);

    List<Ticket> sort(String sort, String order);

    void updateStatus(Integer ticketId, String status);

    int countBookedTickets();

    int countPaidTickets();

    /**
     * Đếm vé đã check-in
     */
    int countCheckedInTickets();

    /**
     * Đếm vé đã hủy
     */
    int countCancelledTickets();

    /**
     * Đếm vé được tạo trong hôm nay
     */
    int countTodayTickets();

    boolean checkInTicket(Integer ticketId);

    boolean updateTotalPrice(
            Integer ticketId,
            BigDecimal totalPrice);

    boolean increaseTotalPrice(
            Integer ticketId,
            BigDecimal amount);
}