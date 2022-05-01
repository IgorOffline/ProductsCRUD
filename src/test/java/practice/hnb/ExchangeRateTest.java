package practice.hnb;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import practice.util.Money;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class ExchangeRateTest {

    @Test
    void bigDecimal() {
        var sellingRate = "7,585804";
        sellingRate = sellingRate.replace(",", ".");
        var kn23 = BigDecimal.valueOf(23.0);
        var sellingRateEuro = new BigDecimal(sellingRate);
        var euro = Money.priceKunaToEuro(kn23, sellingRateEuro);
        var euroEquals = BigDecimal.valueOf(3.032);
        assertEquals(0, euroEquals.compareTo(euro));
    }
}
