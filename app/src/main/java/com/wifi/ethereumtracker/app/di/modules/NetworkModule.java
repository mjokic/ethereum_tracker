package com.wifi.ethereumtracker.app.di.modules;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.app.di.AppScope;
import com.wifi.ethereumtracker.services.apiCalls.ApiService;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.ButterKnife;
import dagger.Module;
import dagger.Provides;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    private final Context context;

    @BindString(R.string.base_url)
    String baseUrl;

    @BindArray(R.array.cert_pinner_keys)
    String[] cert_pinner_keys;

    public NetworkModule(Context context){
        this.context = context;
    }

    @AppScope
    @Provides
    ApiService providesApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @AppScope
    @Provides
    Retrofit providesRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    @AppScope
    @Provides
    OkHttpClient providesOkHttpClient(CertificatePinner certificatePinner) {
        return new OkHttpClient.Builder()
                .certificatePinner(certificatePinner)
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
