package com.wifi.ethereumtracker.model.profiles;


import android.content.Context;
import android.content.Intent;
import android.gesture.GestureUtils;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wifi.ethereumtracker.broadcastReceivers.NotificationReceiver;
import com.wifi.ethereumtracker.model.pojo.CEXPojo;
import com.wifi.ethereumtracker.services.apiCalls.CEXioInterface;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestProfile extends Profile {


    public TestProfile(){
        super("https://cex.io/");
    }

    @Override
    public Call initialize(String option) {
        // example
        CEXioInterface ceXioInterface = super.retrofit.create(CEXioInterface.class);

        Call call;

        switch (option){
            case "EUR":
                call = ceXioInterface.getLastPriceEUR();
                break;
            case "GBP":
                call = ceXioInterface.getLastPriceGBP();
                break;
            default:
                call = ceXioInterface.getLastPriceUSD();
        }

        return call;
    }


    @Override
    public void runInFront(Call call, final double myValue, final TextView etherValue , final ImageView refreshImageView, Context context) {

        if(refreshImageView != null && context != null){
            super.startRefreshAnimation(refreshImageView, context);
        }


        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                CEXPojo cexPojo = (CEXPojo) response.body();

                etherValue.setText(String.format("%.2f", cexPojo.getLprice() * myValue));
                TestProfile.super.stopRefreshAnimation(refreshImageView);

            }

            @Override
            public void onFailure(Call call, Throwable t) {

                TestProfile.super.stopRefreshAnimation(refreshImageView);

            }
        });


    }

    @Override
    public Double runInBack(Call call, final Context context) {

        double value = 0;

        try {
            Response response = call.execute();

            if(response.code() == 200){
                CEXPojo cexPojo = (CEXPojo)response.body();
                return cexPojo.getLprice();

            }

        }catch (IOException ex){
            ex.printStackTrace();
            return value;
        }


        return value;

    }
}
