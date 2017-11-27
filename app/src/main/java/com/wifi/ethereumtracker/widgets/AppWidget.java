package com.wifi.ethereumtracker.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.app.App;
import com.wifi.ethereumtracker.app.model.Currency;
import com.wifi.ethereumtracker.app.model.Source;
import com.wifi.ethereumtracker.app.network.ApiService;
import com.wifi.ethereumtracker.widgets.di.DaggerAppWidgetComponent;

import javax.inject.Inject;

import butterknife.BindString;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link AppWidgetConfigureActivity AppWidgetConfigureActivity}
 */
public class AppWidget extends AppWidgetProvider {

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
                                int appWidgetId) {
        CharSequence widgetText = AppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setTextViewText(R.id.appwidget_text, "Herro");

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

        Timber.d("Gson je null? %s", gson == null);
        Timber.d("SP je null? %s", sharedPreferences == null);
        Timber.d("API je null? %s", apiService == null);

        String sourceJson = sharedPreferences.getString("sourceSettings", defaultSource);
        Source source = gson.fromJson(sourceJson, Source.class);

        String currencyJson = sharedPreferences.getString("currencySettings", defaultCurrency);
        Currency currency = gson.fromJson(currencyJson, Currency.class);

        apiService.getPrice(source.site(), currency.getName())
                .subscribeOn(Schedulers.io())
                .subscribe(price -> Timber.d(price.toString()));

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            AppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Timber.d("onEnabled called!");

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

