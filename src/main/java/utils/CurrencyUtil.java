package utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtil {

    private CurrencyUtil() {
    }

    public static String formatVND(BigDecimal amount) {
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }
}