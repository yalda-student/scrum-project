package ir.scrumproject.api;

import ir.scrumproject.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofit {
    private static Retrofit INSTANCE;

    private MyRetrofit() {
    }

    private static Retrofit getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return INSTANCE;
    }

    public static ApiService getApi() {
        return MyRetrofit.getInstance().create(ApiService.class);
    }
}
