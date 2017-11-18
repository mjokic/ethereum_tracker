package com.wifi.ethereumtracker.app.network;


import com.wifi.ethereumtracker.model.Profile;
import com.wifi.ethereumtracker.model.pojo.ResponsePojo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/info")
    Observable<List<Profile>> getSources();

    @GET("{source}/{currency}")
    Observable<ResponsePojo> getPrice(@Path("source") String source, @Path("currency") String currency);

}
