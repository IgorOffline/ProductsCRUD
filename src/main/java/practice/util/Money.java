package practice.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money {

    public static BigDecimal priceKunaToEuro(BigDecimal kn, BigDecimal exchangeRate) {
        return kn.divide(exchangeRate, 4, RoundingMode.HALF_UP);
    }
}
