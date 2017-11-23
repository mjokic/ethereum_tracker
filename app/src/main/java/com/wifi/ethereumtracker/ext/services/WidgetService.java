package com.wifi.ethereumtracker.ext.services;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.wifi.ethereumtracker.R;


public class WidgetService extends Service {

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                int widgetId = intent
                        .getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);

                String source = intent.getStringExtra("source");
                String currency = intent.getStringExtra("currency");
                String myValue = intent.getStringExtra("myValue");

//                RetrofitTask rt = new RetrofitTask(source, currency, getApplicationContext());
//                Price rp = rt.runSync();
//
//                String price = Double.toString(rp.getCurrentPrice() * Double.parseDouble(myValue));
                String price = "1337";

                RemoteViews remoteViews = new RemoteViews("com.wifi.ethereumtracker", R.layout.app_widget);
                remoteViews.setTextViewText(R.id.textViewEthPrice, price);

                appWidgetManager.updateAppWidget(widgetId, remoteViews);

            }
        }).start();


        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
