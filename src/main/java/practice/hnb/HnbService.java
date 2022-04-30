package practice.hnb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

@Slf4j
@Service
public class HnbService {

    public static final String HNB_API_URL = "https://api.hnb.hr/";

    public void call() throws IOException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HNB_API_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        HnbCalls service = retrofit.create(HnbCalls.class);

        var exchangeRateEuroList = service.exchangeRateEuro().execute().body();
        var exchangeRateEuro = exchangeRateEuroList.get(0); // Disregard NPE

        log.info("exchangeRateEuro= {}", exchangeRateEuro);
    }
}
