package com.wifi.ethereumtracker.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.app.model.Source;
import com.wifi.ethereumtracker.model.enumerations.CurrencyEnum;
import com.wifi.ethereumtracker.services.WidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    private static final String WIDGET_CLICKED = "widget_clicked";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String myValue = sharedPreferences.getString("myValue", "1");

        String p = sharedPreferences.getString("sourceSettings", null);
//        ProfileOld profileOld = new Gson().fromJson(p, ProfileOld.class);
        Source source = new Gson().fromJson(p, Source.class); // change this!

        String sourceS;
        if (source == null) {
            sourceS = "cex";
        } else {
            sourceS = source.site();
        }

        String currency = sharedPreferences.getString("currencySettings", "usd");

        views.setTextViewText(R.id.textViewEthValue, myValue);
        views.setTextViewText(R.id.textViewCurrencySign, CurrencyEnum.getSign(currency));

        Intent intent = new Intent(context.getApplicationContext(), WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.putExtra("source", sourceS);
        intent.putExtra("currency", currency);
        intent.putExtra("myValue", myValue);
        context.startService(intent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        ComponentName watchWidget = new ComponentName(context, AppWidget.class);

        remoteViews.setOnClickPendingIntent(R.id.widgetLayout,
                getPendingSelfIntent(context, WIDGET_CLICKED));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        // called on resize
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // not sure if this is correct way to update widget on click
        if (WIDGET_CLICKED.equals(intent.getAction())) {
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            int ids[] =
                    AppWidgetManager
                            .getInstance(context).getAppWidgetIds(new ComponentName(context, AppWidget.class));

            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(intent);

        }

    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

}

