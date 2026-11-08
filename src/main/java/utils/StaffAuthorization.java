package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.User;

public final class StaffAuthorization {

    private StaffAuthorization() {
    }

    public static User getLoggedInUser(HttpServletRequest request) {

        if (request == null) {
            return null;
        }

        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object value = session.getAttribute("user");

        return value instanceof User
                ? (User) value
                : null;
    }

    public static boolean isLoggedIn(HttpServletRequest request) {
        return getLoggedInUser(request) != null;
    }

    public static boolean isActive(User user) {

        if (user == null || user.getStatus() == null) {
            return false;
        }

        return "ACTIVE".equalsIgnoreCase(
                user.getStatus().trim());
    }

    public static boolean canManagePayments(
            HttpServletRequest request) {

        User user = getLoggedInUser(request);

        if (user == null
                || !isActive(user)
                || user.getRole() == null) {

            return false;
        }

        String role = user.getRole().trim();

        return "STAFF".equalsIgnoreCase(role)
                || "ADMIN".equalsIgnoreCase(role);
    }

    public static boolean canManageCustomers(
            HttpServletRequest request) {

        return canManagePayments(request);
    }

    public static boolean isCustomer(User user) {

        return user != null
                && user.getRole() != null
                && "CUSTOMER".equalsIgnoreCase(
                        user.getRole().trim());
    }
}
