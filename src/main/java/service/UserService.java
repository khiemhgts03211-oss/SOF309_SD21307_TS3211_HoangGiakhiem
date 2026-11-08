package service;

import java.util.List;

import model.User;

public interface UserService extends GenericService<User, Integer> {

    /**
     * Đăng nhập
     */
    User login(String username, String passwordHash);

    /**
     * Tìm theo username
     */
    User findByUsername(String username);

    /**
     * Tìm theo email
     */
    User findByEmail(String email);

    /**
     * Tìm theo số điện thoại
     */
    User findByPhone(String phone);

    /**
     * Danh sách theo Role
     */
    List<User> findByRole(String role);

    /**
     * Kiểm tra username tồn tại
     */
    boolean existsUsername(String username);

    /**
     * Kiểm tra email tồn tại
     */
    boolean existsEmail(String email);

    /**
     * Kiểm tra số điện thoại tồn tại
     */
    boolean existsPhone(String phone);

    boolean updatePassword(String email, String passwordHash);
    
    List<User> filter(String role, String status);  
    
    List<User> sort(String sortBy, String order);
    
    List<User> getCustomers();
    
    List<User> searchCustomers(String keyword);

    List<User> searchCustomers(
            String keyword,
            String status);

    List<User> getCustomersByStatus(String status);
    
    boolean updateCustomer(
            Integer userId,
            String fullName,
            String email,
            String phone);

    boolean checkEmailExistsExceptUser(
            String email,
            Integer userId);
    
    boolean updateCustomerStatus(
            Integer userId,
            String status);
    
}