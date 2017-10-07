package com.wifi.ethereumtracker.services.apiCalls;


import com.wifi.ethereumtracker.model.Profile;
import com.wifi.ethereumtracker.model.pojo.ResponsePojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/info")
    Call<List<Profile>> getSources();

    @GET("{source}/{currency}")
    Call<ResponsePojo> getPrice(@Path("source") String source, @Path("currency") String currency);

}
