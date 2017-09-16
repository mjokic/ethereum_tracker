package com.wifi.ethereumtracker.broadcastReceivers;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean b = sharedPreferences.getBoolean("enableNotificationsSettings", false);
        System.out.println(b + " <-- HERRO!");

        if(!b) return;


        String tmp = sharedPreferences.getString("checkInterval", "1800000");
        System.out.println(tmp);

        Intent i = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                i, 0);

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        int waitInterval = Integer.parseInt(tmp);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().getTimeInMillis() + waitInterval,
                waitInterval, pendingIntent);

    }
}
