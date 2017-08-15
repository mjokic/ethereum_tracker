package com.wifi.ethereumtracker.model.profiles;


import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.wifi.ethereumtracker.model.pojo.CEXPojo;
import com.wifi.ethereumtracker.model.pojo.GeminiPojo;
import com.wifi.ethereumtracker.services.apiCalls.CEXioInterface;
import com.wifi.ethereumtracker.services.apiCalls.GeminiInterface;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeminiProfile extends Profile {

    public static String baseUrl = "https://api.gemini.com/";

    public static CharSequence[] currencies = new CharSequence[] {
            "USD"
    };

    public GeminiProfile(){
        super(baseUrl);
    }


    @Override
    public Call initialize(String option) {
        GeminiInterface geminiInterface = super.retrofit.create(GeminiInterface.class);

        Call call;

        switch (option){
            default:
                call = geminiInterface.getLastPriceUSD();
        }

        return call;
    }

    @Override
    public void runInFront(Call call, final double myValue, final TextView etherValue, final ImageView refreshImageView, Context context) {

        super.startRefreshAnimation(refreshImageView, context);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                GeminiPojo geminiPojo = (GeminiPojo) response.body();

                etherValue.setText(String.format("%.2f", geminiPojo.getLast() * myValue));
                GeminiProfile.super.stopRefreshAnimation(refreshImageView);

            }

            @Override
            public void onFailure(Call call, Throwable t) {

                GeminiProfile.super.stopRefreshAnimation(refreshImageView);

            }
        });
    }

    @Override
    public Double runInBack(Call call, Context context) {
        double value = 0;

        try {
            Response response = call.execute();

            if(response.code() == 200){
                GeminiPojo geminiPojo = (GeminiPojo) response.body();
                return geminiPojo.getLast();

            }

        }catch (IOException ex){
            ex.printStackTrace();
            return value;
        }


        return value;
    }
}
