package com.wifi.ethereumtracker.app.network;


import com.wifi.ethereumtracker.app.model.Price;
import com.wifi.ethereumtracker.app.model.Source;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/info")
    Observable<List<Source>> getSources();

    @GET("/{source}/{currency}")
    Observable<Price> getPrice(@Path("source") String source, @Path("currency") String currency);

    @GET("/graph/{source}/{currency}/{days}")
    Observable<List<Price>> getPrices(@Path("source") String source,
                                      @Path("currency") String currency,
                                      @Path("days") int days);
}
