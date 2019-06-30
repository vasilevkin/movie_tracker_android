package com.vasilevkin.movie_tracker_android.network;

import com.vasilevkin.movie_tracker_android.utils.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HTTPClient {
    private static final OkHttpClient httpClient;
    private static ApiThemoviedb apiServiceInstance;
    private static final Object serviceLock = new Object();

    static {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(new AuthInterceptor())
                .build();
    }

    public static ApiThemoviedb getApiServiceInstance() {
        synchronized (serviceLock) {
            if (apiServiceInstance == null) {
                apiServiceInstance = getRetrofitInstance().create(ApiThemoviedb.class);
            }
            return apiServiceInstance;
        }
    }

    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
    }
}
