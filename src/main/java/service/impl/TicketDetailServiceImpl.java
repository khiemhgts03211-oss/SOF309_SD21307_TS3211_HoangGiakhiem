package service.impl;

import java.util.List;

import dao.TicketDetailDAO;
import dao.impl.TicketDetailDAOImpl;
import model.TicketDetail;
import service.TicketDetailService;

public class TicketDetailServiceImpl implements TicketDetailService {

    private final TicketDetailDAO ticketDetailDAO;

    public TicketDetailServiceImpl() {
        this.ticketDetailDAO = new TicketDetailDAOImpl();
    }

    @Override
    public List<TicketDetail> getAllTicketDetails() {
        return ticketDetailDAO.findAll();
    }

    @Override
    public TicketDetail getTicketDetailById(Integer id) {
        return ticketDetailDAO.findById(id);
    }

    @Override
    public boolean createTicketDetail(TicketDetail ticketDetail) {

        // Business rule: nếu đã tồn tại (ticket + service) thì không tạo mới
        boolean exists = ticketDetailDAO.existsDetail(
                ticketDetail.getTicketId(),
                ticketDetail.getServiceId()
        );

        if (exists) {
            return false;
        }

        return ticketDetailDAO.insert(ticketDetail);
    }

    @Override
    public boolean updateTicketDetail(TicketDetail ticketDetail) {
        return ticketDetailDAO.update(ticketDetail);
    }

    @Override
    public boolean deleteTicketDetail(Integer id) {
        return ticketDetailDAO.delete(id);
    }

    @Override
    public List<TicketDetail> getTicketDetailsByTicket(Integer ticketId) {
        return ticketDetailDAO.findByTicket(ticketId);
    }

    @Override
    public List<TicketDetail> getTicketDetailsByService(Integer serviceId) {
        return ticketDetailDAO.findByService(serviceId);
    }

    @Override
    public boolean deleteByTicket(Integer ticketId) {
        return ticketDetailDAO.deleteByTicket(ticketId);
    }

    @Override
    public int getTotalQuantityByTicket(Integer ticketId) {
        return ticketDetailDAO.getTotalQuantity(ticketId);
    }

    @Override
    public TicketDetail getByTicketAndService(Integer ticketId, Integer serviceId) {
        return ticketDetailDAO.findByTicketAndService(ticketId, serviceId);
    }

    @Override
    public boolean checkExists(Integer ticketId, Integer serviceId) {
        return ticketDetailDAO.existsDetail(ticketId, serviceId);
    }

    @Override
    public List<TicketDetail> searchTicketDetails(String keyword) {
        return ticketDetailDAO.search(keyword);
    }

    @Override
    public List<TicketDetail> getTicketDetailsByPage(int page, int pageSize) {
        return ticketDetailDAO.findByPage(page, pageSize);
    }

    @Override
    public int countTicketDetails() {
        return ticketDetailDAO.count();
    }
    
    @Override
    public double getServiceTotal(Integer ticketId) {

        if (ticketId == null || ticketId <= 0) {
            return 0;
        }

        return ticketDetailDAO.getServiceTotal(ticketId);
    }

    @Override
    public int getTodayServiceQuantity() {
        return ticketDetailDAO.getTodayServiceQuantity();
    }

    @Override
    public double getTodayServiceRevenue() {
        return ticketDetailDAO.getTodayServiceRevenue();
    }
    
}