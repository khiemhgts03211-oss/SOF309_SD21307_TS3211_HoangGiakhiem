package dao;

import java.util.List;

import model.TicketDetail;

public interface TicketDetailDAO
        extends GenericDAO<TicketDetail, Integer> {

    /**
     * Lấy danh sách chi tiết dịch vụ theo vé
     */
    List<TicketDetail> findByTicket(Integer ticketId);

    /**
     * Lấy danh sách chi tiết theo dịch vụ
     */
    List<TicketDetail> findByService(Integer serviceId);

    /**
     * Tìm chi tiết theo vé và dịch vụ
     */
    TicketDetail findByTicketAndService(
            Integer ticketId,
            Integer serviceId);

    /**
     * Kiểm tra một dịch vụ đã có trong vé hay chưa
     */
    boolean existsDetail(
            Integer ticketId,
            Integer serviceId);

    /**
     * Xóa toàn bộ chi tiết dịch vụ của một vé
     */
    boolean deleteByTicket(Integer ticketId);

    /**
     * Tổng số lượng dịch vụ của một vé
     */
    int getTotalQuantity(Integer ticketId);

    /**
     * Tổng tiền dịch vụ của một vé
     */
    double getServiceTotal(Integer ticketId);

    /**
     * Tổng số lượng dịch vụ bán trong hôm nay
     */
    int getTodayServiceQuantity();

    /**
     * Tổng doanh thu dịch vụ trong hôm nay
     */
    double getTodayServiceRevenue();
}