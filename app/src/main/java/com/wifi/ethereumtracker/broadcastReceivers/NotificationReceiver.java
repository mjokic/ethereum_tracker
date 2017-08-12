package com.wifi.ethereumtracker.broadcastReceivers;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.wifi.ethereumtracker.activities.MainActivity;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String message = intent.getStringExtra("message");

        Notification.Builder nBuilder = new Notification.Builder(context)
                .setContentTitle("Title")
                .setContentText(message)
                .setSmallIcon(android.R.mipmap.sym_def_app_icon);

        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);


        manager.notify(0, nBuilder.build());

    }
}
