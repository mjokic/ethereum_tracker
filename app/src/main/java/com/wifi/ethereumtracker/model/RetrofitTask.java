package com.wifi.ethereumtracker.model;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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
import com.wifi.ethereumtracker.app.network.ApiService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("unchecked")
public class RetrofitTask {

    private final ApiService apiService;
    private final Call<ResponsePojo> call;

    private final Context context;

    public RetrofitTask(String source, String currency, Context context) {
        this.context = context;

        CertificatePinner certificatePinner = new CertificatePinner.Builder()
                .add("coinvalue.live", "sha256/OhfxxAo+3duMtfTAMgnPhh5N42+aoPAU9+mwTZQfdTc=")
                .add("coinvalue.live", "sha256/x9SZw6TwIqfmvrLZ/kz1o0Ossjmn728BnBKpUFqGNVM=")
                .add("coinvalue.live", "sha256/58qRu/uxh4gFezqAcERupSkRYBlBAvfcw7mEjGPLnNU=")
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .certificatePinner(certificatePinner)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        this.apiService = retrofit.create(ApiService.class);
//        this.call = apiService.getPrice(source, currency);
        this.call = null;
    }


    private void startRefreshAnimation(View view) {
        Animation myAnimation = AnimationUtils.loadAnimation(context, R.anim.refresh_animation);
        view.startAnimation(myAnimation);
    }

    private void stopRefreshAnimation(View view) {
        view.clearAnimation();
    }


    public void runAsync(final double myValue,
                         final String currency,
                         final AutoResizeTextView textViewEtherValue,
                         final TextView textView24HrChange,
                         final ImageView refreshImage) {

        HttpLoggingInterceptor.Logger.DEFAULT.log("Run async");

        startRefreshAnimation(refreshImage);

        this.call.enqueue(new Callback<ResponsePojo>() {
            @Override
            public void onResponse(@NonNull Call<ResponsePojo> call, @NonNull Response<ResponsePojo> response) {

                if (response.code() == 200) {
                    ResponsePojo responsePojo = response.body();

                    if (responsePojo == null) return;
                    double _24HrChange = responsePojo.getChange24hour();

                    if (_24HrChange >= 0) {
                        textView24HrChange.setTextColor(ContextCompat.getColor(context, R.color.positive));
                    } else {
                        textView24HrChange.setTextColor(ContextCompat.getColor(context, R.color.negative));
                        _24HrChange = _24HrChange * (-1);
                    }

                    double currentPrice = responsePojo.getCurrentPrice();

                    if (currency.equals("btc")) {
                        textViewEtherValue.setText(String.format(Locale.getDefault(), "%.5f", currentPrice * myValue));
                    } else {
                        textViewEtherValue.setText(String.format(Locale.getDefault(), "%.2f", currentPrice * myValue));
                    }

                    String result = String.valueOf(_24HrChange) + "%";
                    textView24HrChange.setText(result);


                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    sharedPreferences.edit().putInt("currentPrice", (int) currentPrice).apply();
                }

                stopRefreshAnimation(refreshImage);
            }

            @Override
            public void onFailure(@NonNull Call<ResponsePojo> call, @NonNull Throwable t) {
                t.printStackTrace();
                stopRefreshAnimation(refreshImage);
            }
        });

    }


    public ResponsePojo runSync() {
        HttpLoggingInterceptor.Logger.DEFAULT.log("Run sync");

        ResponsePojo responsePojo = null;

        try {
            Response response = this.call.execute();

            if (response.code() == 200) {
                responsePojo = (ResponsePojo) response.body();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }


        return responsePojo;

    }


    public boolean getSources() {
        HttpLoggingInterceptor.Logger.DEFAULT.log("Getting sources");

        boolean status = false;

//        Call call = this.apiService.getSources();
        Call call = null;
        Response response;
        try {
            response = call.execute();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

        if (response.code() == 200) {
            List<ProfileOld> lista = (List<ProfileOld>) response.body();

            DbHelper dbHelper = new DbHelper(context);
            dbHelper.saveProfiles(lista);

            status = true;
        }

        return status;
    }

}
