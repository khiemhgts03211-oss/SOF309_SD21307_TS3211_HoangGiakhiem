package dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import model.Ticket;

public interface TicketDAO extends GenericDAO<Ticket, Integer> {

    /*
     * ==========================
     * BASIC QUERY
     * ==========================
     */

    List<Ticket> findByUser(Integer userId);

    List<Ticket> findByShowtime(Integer showtimeId);

    List<Ticket> findByStatus(String status);

    Ticket findByBookingCode(String bookingCode);

    List<Ticket> findByDate(Date bookingDate);

    List<Ticket> findByDateRange(Timestamp fromDate,
                                 Timestamp toDate);

    List<Ticket> findByUserAndStatus(Integer userId,
                                     String status);

    int countByUser(Integer userId);

    double getTotalRevenue();

    List<Ticket> search(String keyword);

    List<Ticket> findByPage(int page,
                            int pageSize);

    boolean existsBookingCode(String bookingCode);

    /*
     * ==========================
     * FILTER / SORT
     * ==========================
     */

    List<Ticket> filter(String status);

    List<Ticket> sort(String sort,
                      String order);

    /*
     * ==========================
     * UPDATE
     * ==========================
     */

    void updateStatus(Integer ticketId,
                      String status);

    boolean checkIn(Integer ticketId);

    /*
     * ==========================
     * DASHBOARD
     * ==========================
     */

    int countBookedTickets();

    int countPaidTickets();

    /**
     * Tổng số vé đã check-in
     */
    int countCheckedInTickets();

    /**
     * Tổng số vé đã hủy
     */
    int countCancelledTickets();

    /**
     * Tổng số vé hôm nay
     */
    int countTodayTickets();

    /*
     * ==========================
     * SELL SERVICE
     * ==========================
     */

    /**
     * Ghi đè tổng tiền của vé
     */
    boolean updateTotalPrice(Integer ticketId,
                             BigDecimal totalPrice);

    /**
     * Cộng thêm tiền vào tổng tiền hiện tại
     */
    boolean increaseTotalPrice(Integer ticketId,
                               BigDecimal amount);

    /*
     * ==========================
     * STAFF REVENUE
     * ==========================
     */

    /**
     * Doanh thu hôm nay
     */
    double getTodayRevenue();

    /**
     * Doanh thu tháng
     */
    double getMonthlyRevenue(int month,
                             int year);

    /**
     * Doanh thu năm
     */
    double getYearlyRevenue(int year);

}