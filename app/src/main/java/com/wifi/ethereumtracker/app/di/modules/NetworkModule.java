package com.wifi.ethereumtracker.app.di.modules;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.app.di.AppScope;
import com.wifi.ethereumtracker.app.network.ApiService;
import com.wifi.ethereumtracker.ext.RetrofitGsonTypeAdapterFactory;

import butterknife.BindArray;
import butterknife.BindString;
import dagger.Module;
import dagger.Provides;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

//    @BindString(R.string.base_url)
    String baseUrl = "http://192.168.0.14:8080";

    @BindArray(R.array.cert_pinner_keys)
    String[] cert_pinner_keys;

    public NetworkModule(Context context) {
        new NetworkModule_ViewBinding(this, context);
    }

    @AppScope
    @Provides
    ApiService providesApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @AppScope
    @Provides
    Retrofit providesRetrofit(Gson gson, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    @AppScope
    @Provides
    Gson providesGson(){
        return new GsonBuilder()
                .registerTypeAdapterFactory(RetrofitGsonTypeAdapterFactory.create())
                .create();
    }

    @AppScope
    @Provides
    OkHttpClient providesOkHttpClient(CertificatePinner certificatePinner) {
        return new OkHttpClient.Builder()
//                .certificatePinner(certificatePinner)
                .build();
    }

    @AppScope
    @Provides
    CertificatePinner providesCertificatePinner() {
        return new CertificatePinner.Builder()
                .add(baseUrl.replace("http://", ""), cert_pinner_keys[0])
                .add(baseUrl.replace("http://", ""), cert_pinner_keys[1])
                .add(baseUrl.replace("http://", ""), cert_pinner_keys[2])
                .build();
    }

}
