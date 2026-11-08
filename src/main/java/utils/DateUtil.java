package utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private DateUtil() {
    }

    public static String formatDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }

        LocalDateTime dateTime = timestamp.toLocalDateTime();
        return dateTime.format(DATE_TIME_FORMATTER);
    }
}