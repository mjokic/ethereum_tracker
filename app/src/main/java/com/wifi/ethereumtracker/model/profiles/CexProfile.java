package com.wifi.ethereumtracker.model.profiles;

import com.wifi.ethereumtracker.pojo.CEXPojo;
import com.wifi.ethereumtracker.services.apiCalls.CEXioInterface;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CexProfile {

    private CEXioInterface ceXioInterface;
    private String baseUrl = "https://cex.io";

    public CexProfile(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        this.ceXioInterface = retrofit.create(CEXioInterface.class);

    }


    public Call initialize(String option){

        Call<CEXPojo> call;

        switch (option){
            case "EUR":
                call = this.ceXioInterface.getLastPriceEUR();
                break;
            case "GBP":
                call = this.ceXioInterface.getLastPriceGBP();
                break;
            default:
                call = this.ceXioInterface.getLastPriceUSD();
        }

        return call;

    }


}
