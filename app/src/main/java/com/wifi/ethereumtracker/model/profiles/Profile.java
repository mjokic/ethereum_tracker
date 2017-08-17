package com.wifi.ethereumtracker.model.profiles;


import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.model.other.TLSSocketFactory;

import java.security.cert.CertificateException;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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


        // Fixing SSL issue on Android version < 5.0
        // https://stackoverflow.com/q/43949160

        //region TrustManager array:
        final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        } };
        //endregion:

        OkHttpClient client;
        try {
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(new TLSSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .addInterceptor(interceptor).build();
        }catch (Exception ex){
            ex.printStackTrace();
            return;
        }

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
