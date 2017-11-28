package com.wifi.ethereumtracker.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.app.App;
import com.wifi.ethereumtracker.app.model.Currency;
import com.wifi.ethereumtracker.app.model.Price;
import com.wifi.ethereumtracker.app.model.Source;
import com.wifi.ethereumtracker.app.network.ApiService;
import com.wifi.ethereumtracker.widgets.di.DaggerAppWidgetComponent;

import javax.inject.Inject;

import butterknife.BindString;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    private static final String WIDGET_CLICKED = "widgetClick";

    @Inject
    Gson gson;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    ApiService apiService;

    @BindString(R.string.defaultSource)
    String defaultSource;

    @BindString(R.string.defaultCurrency)
    String defaultCurrency;


    public AppWidget() {
        super();
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, double myValue, Price price) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setTextViewText(R.id.textViewMyValue, String.valueOf(myValue));
        views.setTextViewText(R.id.textViewEthPrice, String.valueOf(myValue * price.getPrice()));
        views.setTextViewText(R.id.textViewCurrencySign, price.getCurrency().getSign());

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        DaggerAppWidgetComponent.builder()
                .appComponent(((App) context.getApplicationContext()).getComponent())
                .build()
                .inject(this);

        // butterknife injection
        new AppWidget_ViewBinding(this, context);

        String sourceJson = sharedPreferences.getString("sourceSettings", defaultSource);
        Source source = gson.fromJson(sourceJson, Source.class);

        String currencyJson = sharedPreferences.getString("currencySettings", defaultCurrency);
        Currency currency = gson.fromJson(currencyJson, Currency.class);

        double myValue = Double.parseDouble(sharedPreferences.getString("myValue", "1"));

        apiService.getPrice(source.site(), currency.getName())
                .subscribeOn(Schedulers.io())
                .subscribe(price -> {
                    // There may be multiple widgets active, so update all of them
                    for (int appWidgetId : appWidgetIds) {
                        updateAppWidget(context, appWidgetManager, appWidgetId, myValue, price);
                    }
                }, Timber::d);

        RemoteViews remoteViews;
        ComponentName watchWidget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        watchWidget = new ComponentName(context, AppWidget.class);

        // setting onClick event
        remoteViews.setOnClickPendingIntent(R.id.widgetLayout,
                getPendingSelfIntent(context, WIDGET_CLICKED));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (WIDGET_CLICKED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            int ids[] = appWidgetManager
                    .getAppWidgetIds(new ComponentName(context, AppWidget.class));

            onUpdate(context, appWidgetManager, ids);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

}

