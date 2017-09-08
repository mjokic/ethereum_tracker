package com.wifi.ethereumtracker.model;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.model.pojo.CEXPojo;
import com.wifi.ethereumtracker.model.pojo.ResponsePojo;
import com.wifi.ethereumtracker.services.apiCalls.ApiService;

import java.io.IOException;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitTask {

    private String baseUrl = "https://coinvalue.live/";
    private Call<ResponsePojo> call;

    public RetrofitTask(String source, String currency){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        CertificatePinner certificatePinner = new CertificatePinner.Builder()
                .add("coinvalue.live", "sha256/OhfxxAo+3duMtfTAMgnPhh5N42+aoPAU9+mwTZQfdTc=")
                .add("coinvalue.live", "sha256/x9SZw6TwIqfmvrLZ/kz1o0Ossjmn728BnBKpUFqGNVM=")
                .add("coinvalue.live", "sha256/58qRu/uxh4gFezqAcERupSkRYBlBAvfcw7mEjGPLnNU=")
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .certificatePinner(certificatePinner)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        ApiService apiService = retrofit.create(ApiService.class);
        this.call = apiService.getPrice(source, currency);
    }




    void startRefreshAnimation(View view, Context context){
        Animation myAnimation =  AnimationUtils.loadAnimation(context, R.anim.refresh_animation);
        view.startAnimation(myAnimation);
    }

    void stopRefreshAnimation(View view){
        view.clearAnimation();
    }


    public void runAsync(final double myValue,
                         final TextView textViewEtherValue,
                         final TextView textView24HrChange,
                         final ImageView refreshImage, final Context context){

        startRefreshAnimation(refreshImage, context);

        this.call.enqueue(new Callback<ResponsePojo>() {
            @Override
            public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {

                if(response.code() == 200) {
                    ResponsePojo responsePojo = response.body();

                    double _24HrChange = responsePojo.getChange24hour();

                    if(_24HrChange >= 0){
                        textView24HrChange.setTextColor(ContextCompat.getColor(context, R.color.positive));
                    }else{
                        textView24HrChange.setTextColor(ContextCompat.getColor(context, R.color.negative));
                        _24HrChange = _24HrChange * (-1);
                    }

                    textViewEtherValue.setText(String.format("%.2f", responsePojo.getCurrentPrice() * myValue));
                    textView24HrChange.setText(String.valueOf(_24HrChange) + "%");
                }

                stopRefreshAnimation(refreshImage);
            }

            @Override
            public void onFailure(Call<ResponsePojo> call, Throwable t) {
                t.printStackTrace();
                stopRefreshAnimation(refreshImage);
            }
        });

    }


    public ResponsePojo runSync(){
        ResponsePojo responsePojo = null;

        try {
            Response response = this.call.execute();

            if(response.code() == 200){
                responsePojo = (ResponsePojo) response.body();
            }

        }catch (IOException ex){
            ex.printStackTrace();
            return null;
        }


        return responsePojo;

    }

}
