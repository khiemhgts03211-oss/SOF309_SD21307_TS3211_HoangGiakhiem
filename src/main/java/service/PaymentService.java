package service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import model.Payment;
import model.PaymentStatistic;

public interface PaymentService {

    List<Payment> getAllPayments();

    Payment getPaymentById(Integer id);

    boolean createPayment(Payment payment);

    boolean updatePayment(Payment payment);

    boolean deletePayment(Integer id);

    List<Payment> searchPayments(String keyword);

    List<Payment> getPaymentsByPage(int page, int pageSize);

    int countPayments();

    List<Payment> getPaymentsByTicket(Integer ticketId);

    List<Payment> getPaymentsByStatus(String status);

    Payment getPaymentByTransactionCode(String transactionCode);

    List<Payment> getPaymentsByMethod(String paymentMethod);

    List<Payment> getPaymentsByDate(Date paymentDate);

    double getRevenueByDate(Date paymentDate);

    double getRevenueByMonth(int month, int year);

    double getRevenueByYear(int year);

    boolean checkTransactionCodeExists(String transactionCode);
    
    List<Payment> filterPayments(String status, String method);
    
    List<Payment> sortPayments(String sortBy, String order);
    
    boolean updatePaymentStatus(Integer paymentId, String status);
    
    long countPaidPayments();

    long countPendingPayments();

    double getTotalRevenue();
    
    List<Payment> getTodayPayments();
    
    double getTodayRevenue();

    List<Payment> getPaidPayments();
    
    List<Payment> searchStaffPayments(String keyword);
    
    List<Payment> searchAndFilterStaffPayments(
            String keyword,
            String status,
            String method);
    
    Payment getStaffPaymentDetail(Integer paymentId);
    
    List<Payment> getStaffPaymentsByDateRange(
            java.sql.Date fromDate,
            java.sql.Date toDate);

    BigDecimal getPaidRevenue(
            java.sql.Date fromDate,
            java.sql.Date toDate);

    int countPaymentsByStatusAndDateRange(
            String status,
            java.sql.Date fromDate,
            java.sql.Date toDate);

    List<PaymentStatistic> getDailyPaymentStatistics(
            java.sql.Date fromDate,
            java.sql.Date toDate);

    List<PaymentStatistic> getPaymentMethodStatistics(
            java.sql.Date fromDate,
            java.sql.Date toDate);
    
}