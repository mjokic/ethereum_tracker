package com.wifi.ethereumtracker.broadcastReceivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wifi.ethereumtracker.services.BackgroundCheckService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, BackgroundCheckService.class);
        context.startService(i);

    }
}
