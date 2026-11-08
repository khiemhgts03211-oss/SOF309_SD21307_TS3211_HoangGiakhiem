package service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

import dao.PaymentDAO;
import dao.TicketDAO;
import dao.impl.PaymentDAOImpl;
import dao.impl.TicketDAOImpl;
import model.Payment;
import model.PaymentStatistic;
import service.PaymentService;

public class PaymentServiceImpl implements PaymentService {

    private final PaymentDAO paymentDAO;

    public PaymentServiceImpl() {
        this.paymentDAO = new PaymentDAOImpl();
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentDAO.findAll();
    }

    @Override
    public Payment getPaymentById(Integer id) {
        return paymentDAO.findById(id);
    }

    @Override
    public boolean createPayment(Payment payment) {

        // Business rule 1: auto-generate transaction code nếu chưa có
        if (payment.getTransactionCode() == null || payment.getTransactionCode().isEmpty()) {
            payment.setTransactionCode(generateTransactionCode());
        }

        // Business rule 2: tránh trùng transaction code
        if (paymentDAO.existsTransactionCode(payment.getTransactionCode())) {
            return false;
        }

        // Business rule 3: default status
        if (payment.getStatus() == null) {
            payment.setStatus("PENDING");
        }

        return paymentDAO.insert(payment);
    }

    @Override
    public boolean updatePayment(Payment payment) {
        return paymentDAO.update(payment);
    }

    @Override
    public boolean deletePayment(Integer id) {
        return paymentDAO.delete(id);
    }

    @Override
    public List<Payment> searchPayments(String keyword) {
        return paymentDAO.search(keyword);
    }

    @Override
    public List<Payment> getPaymentsByPage(int page, int pageSize) {
        return paymentDAO.findByPage(page, pageSize);
    }

    @Override
    public int countPayments() {
        return paymentDAO.count();
    }

    @Override
    public List<Payment> getPaymentsByTicket(Integer ticketId) {
        return paymentDAO.findByTicket(ticketId);
    }

    @Override
    public List<Payment> getPaymentsByStatus(String status) {
        return paymentDAO.findByStatus(status);
    }

    @Override
    public Payment getPaymentByTransactionCode(String transactionCode) {
        return paymentDAO.findByTransactionCode(transactionCode);
    }

    @Override
    public List<Payment> getPaymentsByMethod(String paymentMethod) {
        return paymentDAO.findByMethod(paymentMethod);
    }

    @Override
    public List<Payment> getPaymentsByDate(Date paymentDate) {
        return paymentDAO.findByDate(paymentDate);
    }

    @Override
    public double getRevenueByDate(Date paymentDate) {
        return paymentDAO.getRevenueByDate(paymentDate);
    }

    @Override
    public double getRevenueByMonth(int month, int year) {
        return paymentDAO.getRevenueByMonth(month, year);
    }

    @Override
    public double getRevenueByYear(int year) {
        return paymentDAO.getRevenueByYear(year);
    }

    @Override
    public boolean checkTransactionCodeExists(String transactionCode) {
        return paymentDAO.existsTransactionCode(transactionCode);
    }
    
    @Override
    public List<Payment> filterPayments(String status, String method) {
        return paymentDAO.filter(status, method);
    }

    @Override
    public List<Payment> sortPayments(String sortBy, String order) {

        return paymentDAO.sort(sortBy, order);

    }
    
    @Override
    public boolean updatePaymentStatus(
            Integer paymentId,
            String status) {

        if (paymentId == null
                || paymentId <= 0
                || status == null
                || status.isBlank()) {

            return false;
        }

        String normalizedStatus =
                status.trim().toUpperCase();

        if (!"PAID".equals(normalizedStatus)
                && !"CANCELLED".equals(normalizedStatus)
                && !"FAILED".equals(normalizedStatus)) {

            return false;
        }

        Payment payment =
                paymentDAO.findById(paymentId);

        if (payment == null
                || payment.getStatus() == null) {

            return false;
        }

        String currentStatus =
                payment.getStatus()
                        .trim()
                        .toUpperCase();

        if (!"PENDING".equals(currentStatus)) {
            return false;
        }

        return paymentDAO.updateStatus(
                paymentId,
                normalizedStatus);
    }
    
    @Override
    public long countPaidPayments() {

        return paymentDAO.countPaid();

    }

    @Override
    public long countPendingPayments() {

        return paymentDAO.countPending();

    }

    @Override
    public double getTotalRevenue() {

        return paymentDAO.getTotalRevenue();

    }
    
    @Override
    public List<Payment> getTodayPayments() {
        return paymentDAO.findTodayPayments();
    }
    
    @Override
    public double getTodayRevenue() {
        return paymentDAO.getRevenueByDate(
                new java.sql.Date(System.currentTimeMillis()));
    }

    @Override
    public List<Payment> getPaidPayments() {
        return paymentDAO.findByStatus("PAID");
    }
    
    @Override
    public List<Payment> searchStaffPayments(
            String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return paymentDAO.findAll();
        }

        return paymentDAO.searchStaffPayments(
                keyword.trim());
    }
    
    @Override
    public List<Payment> searchAndFilterStaffPayments(
            String keyword,
            String status,
            String method) {

        String normalizedKeyword =
                keyword == null
                        ? ""
                        : keyword.trim();

        String normalizedStatus =
                status == null
                        ? ""
                        : status.trim().toUpperCase();

        String normalizedMethod =
                method == null
                        ? ""
                        : method.trim().toUpperCase();

        /*
         * Không chấp nhận trạng thái lạ.
         */
        if (!normalizedStatus.isBlank()
                && !"PENDING".equals(normalizedStatus)
                && !"PAID".equals(normalizedStatus)
                && !"FAILED".equals(normalizedStatus)
                && !"CANCELLED".equals(normalizedStatus)) {

            return java.util.Collections.emptyList();
        }

        /*
         * Phương thức đúng theo dữ liệu project.
         */
        if (!normalizedMethod.isBlank()
                && !"CASH".equals(normalizedMethod)
                && !"BANKING".equals(normalizedMethod)
                && !"MOMO".equals(normalizedMethod)
                && !"VNPAY".equals(normalizedMethod)) {

            return java.util.Collections.emptyList();
        }

        return paymentDAO.searchAndFilterStaffPayments(
                normalizedKeyword,
                normalizedStatus,
                normalizedMethod);
    }
    
    @Override
    public Payment getStaffPaymentDetail(
            Integer paymentId) {

        if (paymentId == null || paymentId <= 0) {
            return null;
        }

        return paymentDAO.findStaffPaymentDetail(
                paymentId);
    }
    
    @Override
    public List<Payment> getStaffPaymentsByDateRange(
            java.sql.Date fromDate,
            java.sql.Date toDate) {

        if (fromDate == null || toDate == null) {
            return java.util.Collections.emptyList();
        }

        if (fromDate.after(toDate)) {
            return java.util.Collections.emptyList();
        }

        return paymentDAO.findStaffPaymentsByDateRange(
                fromDate,
                toDate);
    }

    @Override
    public BigDecimal getPaidRevenue(
            java.sql.Date fromDate,
            java.sql.Date toDate) {

        if (fromDate == null
                || toDate == null
                || fromDate.after(toDate)) {

            return BigDecimal.ZERO;
        }

        return paymentDAO.calculatePaidRevenue(
                fromDate,
                toDate);
    }

    @Override
    public int countPaymentsByStatusAndDateRange(
            String status,
            java.sql.Date fromDate,
            java.sql.Date toDate) {

        if (status == null
                || status.isBlank()
                || fromDate == null
                || toDate == null
                || fromDate.after(toDate)) {

            return 0;
        }

        return paymentDAO
                .countPaymentsByStatusAndDateRange(
                        status.trim().toUpperCase(),
                        fromDate,
                        toDate);
    }

    @Override
    public List<PaymentStatistic> getDailyPaymentStatistics(
            java.sql.Date fromDate,
            java.sql.Date toDate) {

        if (fromDate == null
                || toDate == null
                || fromDate.after(toDate)) {

            return java.util.Collections.emptyList();
        }

        return paymentDAO.getDailyPaymentStatistics(
                fromDate,
                toDate);
    }

    @Override
    public List<PaymentStatistic> getPaymentMethodStatistics(
            java.sql.Date fromDate,
            java.sql.Date toDate) {

        if (fromDate == null
                || toDate == null
                || fromDate.after(toDate)) {

            return java.util.Collections.emptyList();
        }

        return paymentDAO.getPaymentMethodStatistics(
                fromDate,
                toDate);
    }
    
    /**
     * ==========================
     * BUSINESS LOGIC SUPPORT
     * ==========================
     */
    private String generateTransactionCode() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}