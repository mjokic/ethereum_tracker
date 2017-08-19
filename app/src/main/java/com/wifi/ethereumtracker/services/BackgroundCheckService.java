package com.wifi.ethereumtracker.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.wifi.ethereumtracker.broadcastReceivers.NotificationReceiver;
import com.wifi.ethereumtracker.model.pojo.CEXPojo;
import com.wifi.ethereumtracker.model.profiles.CexProfile;
import com.wifi.ethereumtracker.model.profiles.GeminiProfile;
import com.wifi.ethereumtracker.model.profiles.Profile;

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

                String currency = sharedPreferences.getString("currencySettings", "USD");

                Profile profile = loadSourceProfile();
                Call call = profile.initialize(currency);
                double value = profile.runInBack(call, getApplicationContext());

                sendNotificationBroadcast("Hey value is " + value);

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


    private void sendNotificationBroadcast(String messsage){
        Intent i = new Intent(getApplicationContext(), NotificationReceiver.class);
        i.putExtra("message", messsage);
        sendBroadcast(i);
    }


    private Profile loadSourceProfile(){
        String profileBaseUrl = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("sourceSettings", "https://cex.io/");

        Profile profile = null;

        if("https://cex.io/".equals(profileBaseUrl)){
            profile = new CexProfile();

        }else if("https://api.gemini.com/".equals(profileBaseUrl)){
            profile = new GeminiProfile();
        }

        return profile;

    }

}
