package config;

public class EmailConfig {

    // Gmail gửi OTP
    public static final String EMAIL =
            System.getenv().getOrDefault("MOVIE_SMTP_EMAIL", "");

    // App Password của Gmail
    public static final String PASSWORD =
            System.getenv().getOrDefault("MOVIE_SMTP_PASSWORD", "");

    private EmailConfig() {
    }
}