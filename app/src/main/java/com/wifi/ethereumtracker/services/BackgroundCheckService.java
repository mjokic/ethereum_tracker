package com.wifi.ethereumtracker.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.wifi.ethereumtracker.broadcastReceivers.NotificationReceiver;
import com.wifi.ethereumtracker.model.Profile;
import com.wifi.ethereumtracker.model.RetrofitTask;
import com.wifi.ethereumtracker.model.pojo.CEXPojo;
import com.wifi.ethereumtracker.model.pojo.ResponsePojo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundCheckService extends Service {

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Thread thread = new Thread(){
            public void run(){

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                String p = sharedPreferences.getString("sourceSettings", "cex");
                String currency = sharedPreferences.getString("currencySettings", "usd");
                double valueMin = Double.parseDouble(sharedPreferences.getString("valueMinNotify", "0"));
                double valueMax = Double.parseDouble(sharedPreferences.getString("valueMaxNotify", "0"));

//                Profile profile = loadSourceProfile();
//                Call call = profile.initialize(currency);
//                double value = profile.runInBack(call, getApplicationContext());

                Profile profile = new Gson().fromJson(p, Profile.class);
                String source = profile.getSite();

                RetrofitTask retrofitTask = new RetrofitTask(source, currency);
                ResponsePojo responsePojo = retrofitTask.runSync();
                double value = responsePojo.getCurrentPrice();

                String title;
                String message;

                if(value <= valueMin){
                    title = "Price dropped bellow " + valueMin;
                    message = "Current value is " + value;

                }else if(value >= valueMax){
                    title = "Price went above " + valueMax;
                    message = "Current value is " + value;
                }else {
                    return;
                }

                sendNotificationBroadcast(title, message);

            }
        };
        thread.start();

        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void sendNotificationBroadcast(String title, String message){
        Intent i = new Intent(getApplicationContext(), NotificationReceiver.class);
        i.putExtra("title", title);
        i.putExtra("message", message);
        sendBroadcast(i);
    }

}
