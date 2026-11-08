package dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import model.Payment;
import model.PaymentStatistic;

public interface PaymentDAO extends GenericDAO<Payment, Integer> {

	    List<Payment> findByTicket(Integer ticketId);

	    List<Payment> findByStatus(String status);

	    Payment findByTransactionCode(String transactionCode);

	    List<Payment> findByMethod(String paymentMethod);

	    List<Payment> findByDate(Date paymentDate);

	    double getRevenueByDate(Date paymentDate);

	    double getRevenueByMonth(int month, int year);

	    double getRevenueByYear(int year);

	    boolean existsTransactionCode(String transactionCode);
	    
	    List<Payment> filter(String status, String method);
	    
	    List<Payment> sort(String sortBy, String order);
	    
	    boolean updateStatus(Integer paymentId, String status);
	    
	    long countPaid();

	    long countPending();

	    double getTotalRevenue();
	    
	    List<Payment> findTodayPayments();
	    
	    List<Payment> searchStaffPayments(String keyword);
	    
	    List<Payment> searchAndFilterStaffPayments(
	            String keyword,
	            String status,
	            String method);
	    
	    Payment findStaffPaymentDetail(Integer paymentId);
	    
	    /**
	     * Lấy danh sách thanh toán trong khoảng ngày.
	     */
	    List<Payment> findStaffPaymentsByDateRange(
	            Date fromDate,
	            Date toDate);

	    /**
	     * Doanh thu PAID trong khoảng ngày.
	     */
	    BigDecimal calculatePaidRevenue(
	            Date fromDate,
	            Date toDate);

	    /**
	     * Đếm giao dịch theo trạng thái trong khoảng ngày.
	     */
	    int countPaymentsByStatusAndDateRange(
	            String status,
	            Date fromDate,
	            Date toDate);

	    /**
	     * Doanh thu theo từng ngày.
	     */
	    List<PaymentStatistic> getDailyPaymentStatistics(
	            Date fromDate,
	            Date toDate);

	    /**
	     * Thống kê theo phương thức thanh toán.
	     */
	    List<PaymentStatistic> getPaymentMethodStatistics(
	            Date fromDate,
	            Date toDate);
}