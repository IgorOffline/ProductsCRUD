package practice.hnb;

import lombok.Getter;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Service
public class HnbRetrofit {

    private Retrofit retrofit;

    @Getter
    private HnbCalls service;

    public HnbRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.hnb.hr/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        service = retrofit.create(HnbCalls.class);
    }
}
