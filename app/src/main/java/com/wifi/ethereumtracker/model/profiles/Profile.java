package com.wifi.ethereumtracker.model.profiles;


import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wifi.ethereumtracker.R;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile {

    Retrofit retrofit;

    Profile(String baseUrl){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

    }


    void startRefreshAnimation(View view, Context context){
        Animation myAnimation =  AnimationUtils.loadAnimation(context, R.anim.refresh_animation);
        view.startAnimation(myAnimation);
    }

    void stopRefreshAnimation(View view){
        view.clearAnimation();
    }


    public Call initialize(String option){
        return null;
    }
    public void runInFront(Call call, double myValue,TextView etherValue, ImageView refreshImageView, Context context){
    }
    public Double runInBack(Call call, Context context){
        return null;
    }

}
