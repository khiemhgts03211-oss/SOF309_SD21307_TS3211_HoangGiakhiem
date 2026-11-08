package dao;

import java.util.List;

import model.Service;

public interface ServiceDAO extends GenericDAO<Service, Integer> {

    /**
     * Lấy dịch vụ theo danh mục
     */
    List<Service> findByCategory(Integer categoryId);

    /**
     * Lấy dịch vụ đang hoạt động
     */
    List<Service> findAvailableServices();

    /**
     * Tìm theo trạng thái
     */
    List<Service> findByStatus(String status);

    /**
     * Kiểm tra tên dịch vụ tồn tại
     */
    boolean existsService(String serviceName);
    
    boolean existsByCategory(Integer categoryId);
    
    List<Service> filter(Integer categoryId, String status);
    
    List<Service> sort(String sortBy, String order);
    
    List<Service> findAllActive();
    
}