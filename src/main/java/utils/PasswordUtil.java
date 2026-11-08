package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    /**
     * Hash mật khẩu bằng BCrypt
     *
     * @param plainPassword mật khẩu gốc
     * @return mật khẩu đã hash
     */
    public static String hash(String plainPassword) {

        return BCrypt.hashpw(
                plainPassword,
                BCrypt.gensalt());

    }

    /**
     * Kiểm tra mật khẩu
     *
     * @param plainPassword mật khẩu người dùng nhập
     * @param hashedPassword mật khẩu đã lưu trong database
     * @return true nếu đúng
     */
    public static boolean verify(String plainPassword,
                                 String hashedPassword) {

        if (plainPassword == null
                || hashedPassword == null) {

            return false;
        }

        return BCrypt.checkpw(
                plainPassword,
                hashedPassword);

    }

    private PasswordUtil() {

        // Prevent instantiate

    }

}