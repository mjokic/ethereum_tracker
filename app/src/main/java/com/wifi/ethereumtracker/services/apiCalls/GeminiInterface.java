package com.wifi.ethereumtracker.services.apiCalls;


import com.wifi.ethereumtracker.pojo.GeminiPojo;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GeminiInterface {

    @GET("v1/pubticker/ethusd")
    Call<GeminiPojo> getLastPriceUSD();

}
