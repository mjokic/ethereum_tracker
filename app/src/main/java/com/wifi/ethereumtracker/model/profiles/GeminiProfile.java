package com.wifi.ethereumtracker.model.profiles;

import com.wifi.ethereumtracker.pojo.CEXPojo;
import com.wifi.ethereumtracker.pojo.GeminiPojo;
import com.wifi.ethereumtracker.services.apiCalls.CEXioInterface;
import com.wifi.ethereumtracker.services.apiCalls.GeminiInterface;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeminiProfile {

    private GeminiInterface geminiInterface;
    private String baseUrl = "https://api.gemini.com/";

    public GeminiProfile(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        this.geminiInterface = retrofit.create(GeminiInterface.class);
    }

    public Call initialize(String option){

        Call<GeminiPojo> call;

        switch (option){
            default:
                call = this.geminiInterface.getLastPriceUSD();
        }

        return call;

    }


}
