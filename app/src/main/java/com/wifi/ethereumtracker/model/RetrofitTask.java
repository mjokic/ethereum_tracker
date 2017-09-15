package com.wifi.ethereumtracker.model;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lb.auto_fit_textview.AutoResizeTextView;
import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.db.DbHelper;
import com.wifi.ethereumtracker.model.pojo.ResponsePojo;
import com.wifi.ethereumtracker.services.apiCalls.ApiService;

import java.io.IOException;
import java.util.List;

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
    private ApiService apiService;
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


        this.apiService = retrofit.create(ApiService.class);
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
                         final String currency,
                         final AutoResizeTextView textViewEtherValue,
                         final TextView textView24HrChange,
                         final ImageView refreshImage,
                         final Context context){

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

                    double currentPrice = responsePojo.getCurrentPrice();

                    if(currency.equals("btc")){
                        textViewEtherValue.setText(String.format("%.5f", currentPrice * myValue));
                    }else{
                        textViewEtherValue.setText(String.format("%.2f", currentPrice * myValue));
                    }

                    textView24HrChange.setText(String.valueOf(_24HrChange) + "%");



                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    sharedPreferences.edit().putInt("currentPrice", (int)currentPrice).apply();
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


    public boolean getSources(Context context){
        boolean status = false;

        Call call = this.apiService.getSources();
        Response response;
        try {
            response = call.execute();
        }catch (IOException ex){
            ex.printStackTrace();
            return status;
        }

        if(response.code() == 200){
            List<Profile> lista = (List<Profile>) response.body();

            DbHelper dbHelper = new DbHelper(context);
            dbHelper.saveProfiles(lista);

            status = true;
        }


        return status;
    }

}
