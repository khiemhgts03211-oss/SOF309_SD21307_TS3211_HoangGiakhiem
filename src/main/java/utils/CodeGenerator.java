package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class CodeGenerator {

    private static final Random RANDOM = new Random();

    private CodeGenerator() {
    }

    public static String generateBookingCode() {
        return "BK" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + RANDOM.nextInt(1000);
    }

    public static String generateTransactionCode() {
        return "TRX" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + RANDOM.nextInt(1000);
    }
}