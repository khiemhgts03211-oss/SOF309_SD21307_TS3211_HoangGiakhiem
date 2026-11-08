package utils;

import java.util.regex.Pattern;

public class ValidationUtil {

    private ValidationUtil() {
    }

    // Username: 4-20 ký tự
    public static boolean isValidUsername(String username) {

        return Pattern.matches(
                "^[a-zA-Z0-9_]{4,20}$",
                username);

    }

    // Email
    public static boolean isValidEmail(String email) {

        return Pattern.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
                email);

    }

    // Phone Việt Nam
    public static boolean isValidPhone(String phone) {

        return phone == null
                || phone.isBlank()
                || Pattern.matches(
                        "^(0|84)(3|5|7|8|9)[0-9]{8}$",
                        phone);

    }

    // Password tối thiểu 8 ký tự
    public static boolean isStrongPassword(String password) {

        return Pattern.matches(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
                password);

    }

}