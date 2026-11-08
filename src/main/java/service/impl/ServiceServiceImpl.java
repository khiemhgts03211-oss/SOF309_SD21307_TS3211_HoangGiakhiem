package service.impl;

import java.util.Collections;
import java.util.List;

import dao.ServiceDAO;
import dao.impl.ServiceDAOImpl;
import model.Service;
import service.ServiceService;

public class ServiceServiceImpl implements ServiceService {

    private final ServiceDAO serviceDAO;

    public ServiceServiceImpl() {
        this.serviceDAO = new ServiceDAOImpl();
    }

    @Override
    public List<Service> getAllServices() {
        return serviceDAO.findAll();
    }

    @Override
    public Service getServiceById(Integer id) {
        return serviceDAO.findById(id);
    }

    @Override
    public boolean createService(Service service) {

        // Business rule: không cho trùng tên dịch vụ
        if (serviceDAO.existsService(service.getServiceName())) {
            return false;
        }

        // Default status nếu chưa set
        if (service.getStatus() == null) {
            service.setStatus("ACTIVE");
        }

        return serviceDAO.insert(service);
    }

    @Override
    public boolean updateService(Service service) {
        return serviceDAO.update(service);
    }

    @Override
    public boolean deleteService(Integer id) {
        return serviceDAO.delete(id);
    }

    @Override
    public List<Service> searchServices(String keyword) {
        return serviceDAO.search(keyword);
    }

    @Override
    public List<Service> getServicesByPage(int page, int pageSize) {
        return serviceDAO.findByPage(page, pageSize);
    }

    @Override
    public int countServices() {
        return serviceDAO.count();
    }

    @Override
    public List<Service> getServicesByCategory(Integer categoryId) {
        return serviceDAO.findByCategory(categoryId);
    }

    @Override
    public List<Service> getAvailableServices() {
        return serviceDAO.findAvailableServices();
    }

    @Override
    public List<Service> getServicesByStatus(String status) {
        return serviceDAO.findByStatus(status);
    }

    @Override
    public boolean checkServiceExists(String serviceName) {
        return serviceDAO.existsService(serviceName);
    }
    
    @Override
    public List<Service> filterServices(Integer categoryId,
                                        String status) {

        return serviceDAO.filter(categoryId, status);

    }
    
    @Override
    public List<Service> sort(String sortBy,
                              String order) {

        return serviceDAO.sort(sortBy, order);

    }
    
    @Override
    public List<Service> getAllActiveServices() {

        return serviceDAO.findAllActive();

    }
    
    @Override
    public List<Service> getActiveServices() {

        List<Service> services =
                serviceDAO.findByStatus("ACTIVE");

        return services == null
                ? Collections.emptyList()
                : services;
    }
    
}