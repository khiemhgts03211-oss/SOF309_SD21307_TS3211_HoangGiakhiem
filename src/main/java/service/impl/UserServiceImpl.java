package service.impl;

import java.util.List;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import model.User;
import service.UserService;
import utils.PasswordUtil;

public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    /*
     * ==========================
     * GenericService
     * ==========================
     */

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User findById(Integer id) {
        return userDAO.findById(id);
    }

    @Override
    public boolean save(User user) {
        return userDAO.insert(user);
    }

    @Override
    public boolean update(User user) {
        return userDAO.update(user);
    }

    @Override
    public boolean delete(Integer id) {
        return userDAO.delete(id);
    }

    @Override
    public int count() {
        return userDAO.count();
    }

    @Override
    public List<User> search(String keyword) {
        return userDAO.search(keyword);
    }

    @Override
    public List<User> findByPage(int page, int pageSize) {
        return userDAO.findByPage(page, pageSize);
    }

    /*
     * ==========================
     * Business Methods
     * ==========================
     */

    @Override
    public User login(String username, String password) {

        User user = userDAO.findByUsername(username);

        if (user == null) {
            return null;
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            return null;
        }

        if (!PasswordUtil.verify(password, user.getPasswordHash())) {
            return null;
        }

        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    @Override
    public User findByPhone(String phone) {
        return userDAO.findByPhone(phone);
    }

    @Override
    public List<User> findByRole(String role) {
        return userDAO.findByRole(role);
    }

    @Override
    public boolean existsUsername(String username) {
        return userDAO.existsUsername(username);
    }

    @Override
    public boolean existsEmail(String email) {
        return userDAO.existsEmail(email);
    }

    @Override
    public boolean existsPhone(String phone) {
        return userDAO.existsPhone(phone);
    }

    @Override
    public boolean updatePassword(String email, String passwordHash) {

        return userDAO.updatePassword(email, passwordHash);

    }
   
    @Override
    public List<User> filter(String role, String status) {
        return userDAO.filter(role, status);
    }
    
    @Override
    public List<User> sort(String sortBy, String order) {
        return userDAO.sort(sortBy, order);
    }
    
    @Override
    public List<User> getCustomers() {

        List<User> customers = userDAO.findCustomers();

        return customers != null
                ? customers
                : java.util.Collections.emptyList();
    }
    
    @Override
    public List<User> searchCustomers(
            String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return userDAO.findCustomers();
        }

        return userDAO.searchCustomers(
                keyword.trim());
    }

    @Override
    public List<User> searchCustomers(
            String keyword,
            String status) {

        String normalizedKeyword =
                keyword == null
                        ? ""
                        : keyword.trim();

        String normalizedStatus =
                status == null
                        ? ""
                        : status.trim();

        return userDAO.searchCustomers(
                normalizedKeyword,
                normalizedStatus);
    }

    @Override
    public List<User> getCustomersByStatus(
            String status) {

        if (status == null || status.isBlank()) {
            return userDAO.findCustomers();
        }

        return userDAO.findCustomersByStatus(
                status.trim());
    }
    
    @Override
    public boolean updateCustomer(
            Integer userId,
            String fullName,
            String email,
            String phone) {

        if (userId == null || userId <= 0) {
            return false;
        }

        if (fullName == null || fullName.isBlank()) {
            return false;
        }

        if (email == null || email.isBlank()) {
            return false;
        }

        return userDAO.updateCustomer(
                userId,
                fullName.trim(),
                email.trim().toLowerCase(),
                phone == null ? "" : phone.trim());
    }

    @Override
    public boolean checkEmailExistsExceptUser(
            String email,
            Integer userId) {

        if (email == null
                || email.isBlank()
                || userId == null) {

            return false;
        }

        return userDAO.existsEmailExceptUser(
                email.trim(),
                userId);
    }
    
    @Override
    public boolean updateCustomerStatus(
            Integer userId,
            String status) {

        if (userId == null
                || userId <= 0
                || status == null
                || status.isBlank()) {

            return false;
        }

        String normalizedStatus =
                status.trim().toUpperCase();

        if (!"ACTIVE".equals(normalizedStatus)
                && !"LOCKED".equals(normalizedStatus)) {

            return false;
        }

        User customer =
                userDAO.findById(userId);

        if (customer == null
                || customer.getRole() == null
                || !"CUSTOMER".equalsIgnoreCase(
                        String.valueOf(
                                customer.getRole()).trim())) {

            return false;
        }

        return userDAO.updateCustomerStatus(
                userId,
                normalizedStatus);
    }
    
}