package practice.hnb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class HnbService {

    @Autowired
    private HnbRetrofit hnbRetrofit;

    public ExchangeRate exchangeRateEuro() throws IOException {

        var exchangeRateEuroList = hnbRetrofit.getService().exchangeRateEuro().execute().body();
        var exchangeRateEuro = exchangeRateEuroList.get(0); // Disregard NPE

        return exchangeRateEuro;
    }
}
