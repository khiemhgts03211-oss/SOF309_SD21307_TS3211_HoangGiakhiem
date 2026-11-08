package service;

import java.util.List;

import model.TicketDetail;

public interface TicketDetailService {

    List<TicketDetail> getAllTicketDetails();

    TicketDetail getTicketDetailById(Integer id);

    boolean createTicketDetail(TicketDetail ticketDetail);

    boolean updateTicketDetail(TicketDetail ticketDetail);

    boolean deleteTicketDetail(Integer id);

    List<TicketDetail> getTicketDetailsByTicket(Integer ticketId);

    List<TicketDetail> getTicketDetailsByService(Integer serviceId);

    boolean deleteByTicket(Integer ticketId);

    int getTotalQuantityByTicket(Integer ticketId);

    TicketDetail getByTicketAndService(Integer ticketId, Integer serviceId);

    boolean checkExists(Integer ticketId, Integer serviceId);

    List<TicketDetail> searchTicketDetails(String keyword);

    List<TicketDetail> getTicketDetailsByPage(int page, int pageSize);

    int countTicketDetails();
    
    double getServiceTotal(Integer ticketId);

    int getTodayServiceQuantity();

    double getTodayServiceRevenue();
    
}