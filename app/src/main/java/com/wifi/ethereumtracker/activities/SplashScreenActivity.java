package com.wifi.ethereumtracker.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.db.DbHelper;
import com.wifi.ethereumtracker.model.Profile;
import com.wifi.ethereumtracker.services.apiCalls.ApiService;

import java.util.List;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashScreenActivity extends AppCompatActivity {
/*
    This activity is the first one to start.
    On its start get sources from API
 */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        // request
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
                .baseUrl("https://coinvalue.live/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Profile>> call = apiService.getSources();

        call.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {

                if(response.code() == 200){
                    List<Profile> lista = response.body();

                    DbHelper dbHelper = new DbHelper(getApplicationContext());
                    dbHelper.saveProfiles(lista);

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }

            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }


}
