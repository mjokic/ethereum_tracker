package com.wifi.ethereumtracker.ext.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.wifi.ethereumtracker.ext.Util;

public class BootCompleted extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean status = preferences.getBoolean("enableNotificationsSettings", false);

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) && status) {
            String intervalStr = preferences.getString("checkInterval", "1800000");
            int interval = Integer.parseInt(intervalStr);
            Util.schedule(context, interval);
        }
    }
}
