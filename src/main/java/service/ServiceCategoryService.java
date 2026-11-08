package service;

import java.util.List;

import model.ServiceCategory;

public interface ServiceCategoryService {

    List<ServiceCategory> getAllCategories();

    ServiceCategory getCategoryById(Integer id);

    boolean createCategory(ServiceCategory category);

    boolean updateCategory(ServiceCategory category);

    boolean deleteCategory(Integer id);

    List<ServiceCategory> searchCategories(String keyword);

    List<ServiceCategory> getCategoriesByPage(int page, int pageSize);

    int countCategories();

    List<ServiceCategory> getCategoriesByStatus(String status);

    List<ServiceCategory> getAvailableCategories();

    boolean checkCategoryExists(String categoryName);
    
    List<ServiceCategory> sortCategories(String sortBy);
    
}