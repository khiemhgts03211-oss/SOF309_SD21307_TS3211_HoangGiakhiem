package dao;

import java.util.List;

import model.ServiceCategory;

public interface ServiceCategoryDAO extends GenericDAO<ServiceCategory, Integer> {

    List<ServiceCategory> findByStatus(String status);

    List<ServiceCategory> findAvailableCategories();

    boolean existsCategory(String categoryName);

    List<ServiceCategory> sort(String sortBy);
    
}