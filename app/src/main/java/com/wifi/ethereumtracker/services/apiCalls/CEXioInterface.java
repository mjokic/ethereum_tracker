package com.wifi.ethereumtracker.services.apiCalls;

import com.wifi.ethereumtracker.pojo.CEXPojo;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CEXioInterface {

    @GET("/api/last_price/ETH/USD")
    Call<CEXPojo> getLastPriceUSD();


    @GET("/api/last_price/ETH/EUR")
    Call<CEXPojo> getLastPriceEUR();


    @GET("/api/last_price/ETH/GBP")
    Call<CEXPojo> getLastPriceGBP();

}
