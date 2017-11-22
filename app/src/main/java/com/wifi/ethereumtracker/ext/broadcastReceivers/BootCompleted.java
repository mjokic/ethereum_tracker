package com.wifi.ethereumtracker.ext.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.wifi.ethereumtracker.ext.Util;

public class BootCompleted extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            int interval = PreferenceManager.getDefaultSharedPreferences(context)
                    .getInt("checkInterval", 300000);
            Util.schedule(context, interval);
        }
    }
}
