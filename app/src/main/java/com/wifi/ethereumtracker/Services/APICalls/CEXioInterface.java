package com.wifi.ethereumtracker.Services.APICalls;

import com.wifi.ethereumtracker.Pojo.CEXPojo;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CEXioInterface {

    @GET("/api/last_price/ETH/USD")
    Call<CEXPojo> getLastPriceUSD();

}
