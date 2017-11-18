package com.wifi.ethereumtracker.ui.activities.splash.mvp;

import com.wifi.ethereumtracker.model.Profile;
import com.wifi.ethereumtracker.services.apiCalls.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashModel {

    private ApiService apiService;

    public SplashModel(ApiService apiService){
        this.apiService = apiService;
    }


    public void test(){
        Call<List<Profile>> sources = apiService.getSources();
        sources.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                System.out.println(response.code());
            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {

            }
        });
    }

}
