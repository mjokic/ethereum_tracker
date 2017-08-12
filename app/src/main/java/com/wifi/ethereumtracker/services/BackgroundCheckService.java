package com.wifi.ethereumtracker.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.wifi.ethereumtracker.broadcastReceivers.NotificationReceiver;
import com.wifi.ethereumtracker.model.pojo.CEXPojo;
import com.wifi.ethereumtracker.model.profiles.CexProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundCheckService extends Service {

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Thread thread = new Thread(){
            public void run(){

                try {
                    while(true) {
                        // every few seconds check value
                        Thread.sleep(60000);

                        test();

                        System.out.println("HEY!");

                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        };
        thread.start();


        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    private void test(){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String currency = sharedPreferences.getString("currencySettings", "USD");

        CexProfile profile = new CexProfile();
        Call<CEXPojo> call = profile.initialize(currency);

        call.enqueue(new Callback<CEXPojo>() {
            @Override
            public void onResponse(Call<CEXPojo> call, Response<CEXPojo> response) {

                CEXPojo cexPojo = response.body();

                Intent i = new Intent(getApplicationContext(), NotificationReceiver.class);

                double price = cexPojo.getLprice();

                if(price < 316){
                    i.putExtra("message", "HEY, price is " + price);

                }else{
                    i.putExtra("message", "HEY RICH " + price);

                }

                sendBroadcast(i);

            }

            @Override
            public void onFailure(Call<CEXPojo> call, Throwable t) {
            }
        });
    }

}
