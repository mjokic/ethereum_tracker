package com.wifi.ethereumtracker.services.apiCalls;


import com.wifi.ethereumtracker.model.pojo.ResponsePojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiService {

    @GET("{source}/{currency}")
    Call<ResponsePojo> getPrice(@Path("source") String source, @Path("currency") String currency);

}
