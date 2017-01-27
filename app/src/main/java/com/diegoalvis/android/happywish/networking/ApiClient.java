package com.diegoalvis.android.happywish.networking;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by diegoalvis on 1/23/17.
 *
 * Singleton of Retrofit class
 * for connection to server online
 * and parse in JSON file
 */

public class ApiClient {

    //URL service list of applications
    public static final String BASE_URL = "https://itunes.apple.com";

    // Config Timeout for connection
    public final static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build();

    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
