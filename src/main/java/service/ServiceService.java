package service;

import java.util.List;

import model.Service;

public interface ServiceService {

    List<Service> getAllServices();

    Service getServiceById(Integer id);

    boolean createService(Service service);

    boolean updateService(Service service);

    boolean deleteService(Integer id);

    List<Service> searchServices(String keyword);

    List<Service> getServicesByPage(int page, int pageSize);

    int countServices();

    List<Service> getServicesByCategory(Integer categoryId);

    List<Service> getAvailableServices();

    List<Service> getServicesByStatus(String status);

    boolean checkServiceExists(String serviceName);
    
    List<Service> filterServices(Integer categoryId,
            String status);
    
    List<Service> sort(String sortBy,
            String order);
    
    List<Service> getAllActiveServices();
    
    List<Service> getActiveServices();
    
}