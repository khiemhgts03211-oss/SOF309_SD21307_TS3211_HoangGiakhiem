package service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import dao.TicketDAO;
import dao.impl.TicketDAOImpl;
import model.Ticket;
import service.TicketService;

public class TicketServiceImpl implements TicketService {

    private final TicketDAO ticketDAO;

    public TicketServiceImpl() {
        this.ticketDAO = new TicketDAOImpl();
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketDAO.findAll();
    }

    @Override
    public Ticket getTicketById(Integer id) {
        return ticketDAO.findById(id);
    }

    @Override
    public boolean createTicket(Ticket ticket) {

        // Business rule 1: auto-generate booking code nếu chưa có
        if (ticket.getBookingCode() == null || ticket.getBookingCode().isEmpty()) {
            ticket.setBookingCode(generateBookingCode());
        }

        // Business rule 2: tránh trùng booking code
        if (ticketDAO.existsBookingCode(ticket.getBookingCode())) {
            return false;
        }

        // Business rule 3: mặc định status nếu chưa set
        if (ticket.getStatus() == null) {
            ticket.setStatus("BOOKED");
        }

        return ticketDAO.insert(ticket);
    }

    @Override
    public boolean updateTicket(Ticket ticket) {
        return ticketDAO.update(ticket);
    }

    @Override
    public boolean deleteTicket(Integer id) {
        return ticketDAO.delete(id);
    }

    @Override
    public List<Ticket> searchTickets(String keyword) {
        return ticketDAO.search(keyword);
    }

    @Override
    public List<Ticket> getTicketsByPage(int page, int pageSize) {
        return ticketDAO.findByPage(page, pageSize);
    }

    @Override
    public int countTickets() {
        return ticketDAO.count();
    }

    @Override
    public List<Ticket> getTicketsByUser(Integer userId) {
        return ticketDAO.findByUser(userId);
    }

    @Override
    public List<Ticket> getTicketsByShowtime(Integer showtimeId) {
        return ticketDAO.findByShowtime(showtimeId);
    }

    @Override
    public List<Ticket> getTicketsByStatus(String status) {
        return ticketDAO.findByStatus(status);
    }

    @Override
    public Ticket getTicketByBookingCode(String bookingCode) {
        return ticketDAO.findByBookingCode(bookingCode);
    }

    @Override
    public List<Ticket> getTicketsByDate(Date bookingDate) {
        return ticketDAO.findByDate(bookingDate);
    }

    @Override
    public List<Ticket> getTicketsByDateRange(Timestamp fromDate, Timestamp toDate) {
        return ticketDAO.findByDateRange(fromDate, toDate);
    }

    @Override
    public List<Ticket> getTicketsByUserAndStatus(Integer userId, String status) {
        return ticketDAO.findByUserAndStatus(userId, status);
    }

    @Override
    public int countTicketsByUser(Integer userId) {
        return ticketDAO.countByUser(userId);
    }

    @Override
    public double getTotalRevenue() {
        return ticketDAO.getTotalRevenue();
    }

    @Override
    public boolean checkBookingCodeExists(String bookingCode) {
        return ticketDAO.existsBookingCode(bookingCode);
    }

    /**
     * ==========================
     * BUSINESS LOGIC SUPPORT
     * ==========================
     */

    private String generateBookingCode() {
        return "TCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    @Override
    public List<Ticket> filter(String status) {

        return ticketDAO.filter(status);

    }
    
    @Override
    public List<Ticket> sort(String sort,
            String order) {

        return ticketDAO.sort(sort, order);

    }
    
    @Override
    public void updateStatus(Integer ticketId,
            String status) {

        ticketDAO.updateStatus(ticketId, status);

    }
    
    @Override
    public int countBookedTickets() {

        return ticketDAO.countBookedTickets();

    }

    @Override
    public int countPaidTickets() {

        return ticketDAO.countPaidTickets();

    }
 
    @Override
    public boolean checkInTicket(Integer ticketId) {
        return ticketDAO.checkIn(ticketId);
    }

    @Override
    public boolean updateTotalPrice(
            Integer ticketId,
            BigDecimal totalPrice) {

        if (ticketId == null || ticketId <= 0
                || totalPrice == null
                || totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }

        return ticketDAO.updateTotalPrice(ticketId, totalPrice);
    }

    @Override
    public boolean increaseTotalPrice(
            Integer ticketId,
            BigDecimal amount) {

        if (ticketId == null || ticketId <= 0
                || amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        return ticketDAO.increaseTotalPrice(ticketId, amount);
    }

    @Override
    public int countCheckedInTickets() {
        return ticketDAO.countCheckedInTickets();
    }

    @Override
    public int countCancelledTickets() {
        return ticketDAO.countCancelledTickets();
    }

    @Override
    public int countTodayTickets() {
        return ticketDAO.countTodayTickets();
    }
    
}