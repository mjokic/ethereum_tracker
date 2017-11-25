package com.wifi.ethereumtracker.ext.services.BackgroundCheckJobService;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.google.gson.Gson;
import com.wifi.ethereumtracker.app.App;
import com.wifi.ethereumtracker.app.model.Currency;
import com.wifi.ethereumtracker.app.model.Source;
import com.wifi.ethereumtracker.app.network.ApiService;
import com.wifi.ethereumtracker.ext.broadcastReceivers.NotificationReceiver;
import com.wifi.ethereumtracker.ext.services.BackgroundCheckJobService.di.DaggerJobServiceComponent;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class BackgroundCheckService extends JobService {

    @Inject
    ApiService apiService;
    @Inject
    Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerJobServiceComponent.builder()
                .appComponent(((App) getApplication()).getComponent())
                .build()
                .inject(this);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        // get these from strings.xml (but probably won't ever need it)
        String defaultSource = "{\"id\":1,\"site\":\"cex\",\"currencies\":[{\"name\":\"BTC\",\"sign\":\"฿\"},{\"name\":\"EUR\",\"sign\":\"€\"},{\"name\":\"GBP\",\"sign\":\"£\"},{\"name\":\"USD\",\"sign\":\"$\"}]}";
        String defaultCurrency = "{\"name\":\"USD\",\"sign\":\"$\"}";

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String p = sharedPreferences.getString("sourceSettings", defaultSource);
        String currencyStr = sharedPreferences.getString("currencySettings", defaultCurrency);
        String myValue = sharedPreferences.getString("myValue", "1");
        double valueMin = Double.parseDouble(sharedPreferences.getString("valueMinNotify", "0"));
        double valueMax = Double.parseDouble(sharedPreferences.getString("valueMaxNotify", "0"));

        String site = gson.fromJson(p, Source.class).site();
        String currency = gson.fromJson(currencyStr, Currency.class).getName();

        apiService.getPrice(site, currency)
                .subscribeOn(Schedulers.io())
                .subscribe(rp -> {
                    if (rp == null) return;

                    double value = rp.getPrice() * Double.parseDouble(myValue);

                    String title;
                    String message;

                    if (value <= valueMin) {
                        title = "Price dropped bellow " + valueMin;
                        message = "Current value is " + value;

                    } else if (value >= valueMax) {
                        title = "Price went above " + valueMax;
                        message = "Current value is " + value;
                    } else {
                        return;
                    }

                    sendNotificationBroadcast(title, message);
                }, Timber::d);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private void sendNotificationBroadcast(String title, String message) {
        Intent i = new Intent(getApplicationContext(), NotificationReceiver.class);
        i.putExtra("title", title);
        i.putExtra("message", message);
        sendBroadcast(i);
    }

}
