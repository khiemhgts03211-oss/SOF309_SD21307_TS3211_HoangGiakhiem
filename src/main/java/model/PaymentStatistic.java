package model;

import java.math.BigDecimal;
import java.sql.Date;

public class PaymentStatistic {

    /*
     * Dùng cho thống kê doanh thu theo ngày.
     */
    private Date statisticDate;
    private int transactionCount;
    private BigDecimal totalRevenue;

    /*
     * Dùng cho thống kê theo phương thức.
     */
    private String paymentMethod;

    public PaymentStatistic() {
        this.totalRevenue = BigDecimal.ZERO;
    }

    public Date getStatisticDate() {
        return statisticDate;
    }

    public void setStatisticDate(Date statisticDate) {
        this.statisticDate = statisticDate;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}