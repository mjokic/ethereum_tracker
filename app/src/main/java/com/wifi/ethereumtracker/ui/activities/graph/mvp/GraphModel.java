package com.wifi.ethereumtracker.ui.activities.graph.mvp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.app.model.Currency;
import com.wifi.ethereumtracker.app.model.Price;
import com.wifi.ethereumtracker.app.model.Source;
import com.wifi.ethereumtracker.app.network.ApiService;

import java.util.List;

import butterknife.BindString;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class GraphModel {

    @BindString(R.string.defaultSource)
    String defaultSource;

    @BindString(R.string.defaultCurrency)
    String defaultCurrency;

    private final ApiService apiService;
    private final SharedPreferences sharedPreferences;
    private final Gson gson;


    public GraphModel(ApiService apiService, SharedPreferences sharedPreferences,
                      Gson gson, Context context) {
        this.apiService = apiService;
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
        new GraphModel_ViewBinding(this, context);
    }

    Observable<List<Price>> getPrices(Source source, Currency currency, int time) {
        return apiService.getPrices(source.site(), currency.getName(), time)
                .subscribeOn(Schedulers.io());
    }

    Source getSourcePreference() {
        return gson.fromJson(sharedPreferences.getString("sourceSettings", defaultSource),
                Source.class);
    }


    Currency getCurrencyPreference() {
        return gson.fromJson(sharedPreferences.getString("currencySettings", defaultCurrency),
                Currency.class);
    }

}
