package service.impl;

import java.util.List;

import dao.ServiceCategoryDAO;
import dao.ServiceDAO;
import dao.impl.ServiceCategoryDAOImpl;
import dao.impl.ServiceDAOImpl;
import model.ServiceCategory;
import service.ServiceCategoryService;

public class ServiceCategoryServiceImpl implements ServiceCategoryService {

    private final ServiceCategoryDAO categoryDAO;
    private final ServiceDAO serviceDAO =
            new ServiceDAOImpl();
    
    public ServiceCategoryServiceImpl() {
        this.categoryDAO = new ServiceCategoryDAOImpl();
    }

    @Override
    public List<ServiceCategory> getAllCategories() {
        return categoryDAO.findAll();
    }

    @Override
    public ServiceCategory getCategoryById(Integer id) {
        return categoryDAO.findById(id);
    }

    @Override
    public boolean createCategory(ServiceCategory category) {

        // Business rule: không cho trùng tên category
        if (categoryDAO.existsCategory(category.getCategoryName())) {
            return false;
        }

        // Default status
        if (category.getStatus() == null) {
            category.setStatus("ACTIVE");
        }

        return categoryDAO.insert(category);
    }

    @Override
    public boolean updateCategory(ServiceCategory category) {

        return categoryDAO.update(category);

    }
    
    @Override
    public boolean deleteCategory(Integer id) {

        if (serviceDAO.existsByCategory(id)) {

            return false;

        }

        return categoryDAO.delete(id);

    }

    @Override
    public List<ServiceCategory> searchCategories(String keyword) {
        return categoryDAO.search(keyword);
    }

    @Override
    public List<ServiceCategory> getCategoriesByPage(int page, int pageSize) {
        return categoryDAO.findByPage(page, pageSize);
    }

    @Override
    public int countCategories() {
        return categoryDAO.count();
    }

    @Override
    public List<ServiceCategory> getCategoriesByStatus(String status) {
        return categoryDAO.findByStatus(status);
    }

    @Override
    public List<ServiceCategory> getAvailableCategories() {
        return categoryDAO.findAvailableCategories();
    }

    @Override
    public boolean checkCategoryExists(String categoryName) {
        return categoryDAO.existsCategory(categoryName);
    }
    
    @Override
    public List<ServiceCategory> sortCategories(String sortBy) {

        return categoryDAO.sort(sortBy);

    }
    
}