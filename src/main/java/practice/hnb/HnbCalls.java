package practice.hnb;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface HnbCalls {

    @GET("tecajn/v2?valuta=EUR")
    Call<List<ExchangeRate>> exchangeRateEuro();
}
