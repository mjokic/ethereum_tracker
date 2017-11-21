package com.wifi.ethereumtracker.ui.fragments.preferences;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.SwitchPreferenceCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;
import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.app.App;
import com.wifi.ethereumtracker.app.di.modules.DatabaseModule;
import com.wifi.ethereumtracker.app.model.Source;
import com.wifi.ethereumtracker.broadcastReceivers.AlarmReceiver;
import com.wifi.ethereumtracker.ext.AutoValueAdapterFactory;
import com.wifi.ethereumtracker.ui.fragments.preferences.di.DaggerPreferencesFragmentComponent;
import com.wifi.ethereumtracker.ui.fragments.preferences.mvp.PreferencesFragmentPresenter;
import com.wifi.ethereumtracker.widgets.AppWidget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

public class PreferencesFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener {

    @Inject
    PreferencesFragmentPresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;

    private ListPreference listPreferenceCurrencySettings;
    private EditTextPreference minNotifyValueEditTextPref;
    private EditTextPreference maxNotifyValueEditTextPref;
    private ListPreference listPreferenceCheckInterval;
    private ListPreference listPreferenceSourceSettings;

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        DaggerPreferencesFragmentComponent.builder()
                .appComponent(((App) getActivity().getApplication()).getComponent())
                .databaseModule(new DatabaseModule(getContext()))
                .build()
                .inject(this);


        listPreferenceSourceSettings = (ListPreference) findPreference("sourceSettings");
        setListPrefSourceEntries(presenter.getSources());

        listPreferenceCurrencySettings = (ListPreference) findPreference("currencySettings");
        listPreferenceCurrencySettings.setOnPreferenceChangeListener(this);


        // checking if myValue is valid
        EditTextPreference myValueEditTextPref = (EditTextPreference) findPreference("myValue");
        myValueEditTextPref.setOnPreferenceChangeListener(this);


        SwitchPreferenceCompat switchPreferenceEnableNotifications = (SwitchPreferenceCompat)
                findPreference("enableNotificationsSettings");
        switchPreferenceEnableNotifications.setOnPreferenceChangeListener(this);


        listPreferenceCheckInterval = (ListPreference) findPreference("checkInterval");
        listPreferenceCheckInterval.setOnPreferenceChangeListener(this);


        minNotifyValueEditTextPref = (EditTextPreference) findPreference("valueMinNotify");
        minNotifyValueEditTextPref.setOnPreferenceChangeListener(this);

        maxNotifyValueEditTextPref = (EditTextPreference) findPreference("valueMaxNotify");
        maxNotifyValueEditTextPref.setOnPreferenceChangeListener(this);


        // load profileOlds list from database and send them under as parameter
//        setListPrefSourceEntries(listPreferenceSourceSettings, sources);
        listPreferenceSourceSettings.setOnPreferenceChangeListener(this);

    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {

        switch (preference.getKey()) {
            case "sourceSettings":
                updateWidget();
                return sourceSettingsOnChange(o);
            case "currencySettings":
                updateWidget();
                return true;
            case "myValue":
                updateWidget();
                return myValueSettingsOnChange(o);
            case "enableNotificationsSettings":
                return enableNotfOnChange(o);
            case "checkInterval":
                return intervalSettOnChange(o);
            case "valueMinNotify":
            case "valueMaxNotify":
                return minmaxValueOnChange(o);

        }

        return true;
    }


    // onPreferenceChange methods
    private boolean sourceSettingsOnChange(Object object) {
        String s = (String) object;
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new AutoValueAdapterFactory())
                .create();
        Source source = gson.fromJson(s, Source.class);
        setListPrefCurrencyEntries(listPreferenceCurrencySettings, source.currencies());

        return true;
    }

    private boolean myValueSettingsOnChange(Object object) {
        try {
            Double.parseDouble((String) object);
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    private boolean enableNotfOnChange(Object object) {
        int currentPrice = getPreferenceManager()
                .getSharedPreferences().getInt("currentPrice", 0);
        int tenPercent = calculate10Percent(currentPrice);

        // set default values
        minNotifyValueEditTextPref.setText(String.valueOf(currentPrice - tenPercent));
        maxNotifyValueEditTextPref.setText(String.valueOf(currentPrice + tenPercent));

        boolean status = (boolean) object;

        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0,
                intent, 0);

        AlarmManager alarmManager = (AlarmManager)
                getActivity().getSystemService(Context.ALARM_SERVICE);

        int waitInterval = Integer.parseInt(listPreferenceCheckInterval.getValue());

        if (status) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    Calendar.getInstance().getTimeInMillis() + waitInterval,
                    waitInterval, pendingIntent);

        } else {
            alarmManager.cancel(pendingIntent);
        }

        return true;
    }

    private boolean intervalSettOnChange(Object object) {
        // when interval changes cancel alarm manger and start new one,
        // so new interval gets applied

        int waitInterval = Integer.parseInt((String) object);

        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0,
                intent, 0);

        AlarmManager alarmManager = (AlarmManager)
                getActivity().getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().getTimeInMillis() + waitInterval,
                waitInterval, pendingIntent);

        return true;
    }

    private boolean minmaxValueOnChange(Object object) {
        try {
            Double.parseDouble((String) object);
        } catch (Exception ex) {
            return false;
        }

        return true;
    }


    public void setListPrefSourceEntries(List<Source> sources) {

        List<String> tmp = new ArrayList<>();
        List<String> tmp2 = new ArrayList<>();

        for (Source source : sources) {
            tmp.add(source.site());
            tmp2.add(new GsonBuilder()
                    .registerTypeAdapterFactory(new AutoValueAdapterFactory())
                    .create()
                    .toJson(source));
        }

        CharSequence[] csEntries = tmp.toArray(new CharSequence[tmp.size()]);
        CharSequence[] csEntryVals = tmp2.toArray(new CharSequence[tmp2.size()]);

        listPreferenceSourceSettings.setEntries(csEntries);
        listPreferenceSourceSettings.setEntryValues(csEntryVals);


        if (listPreferenceSourceSettings.getValue() == null) {
            listPreferenceSourceSettings.setValue(tmp2.get(0));
        }

//        setListPrefCurrencyEntries(listPreferenceCurrencySettings,
//                new Gson().fromJson(listPreferenceSourceSettings.getValue(), ProfileOld.class).getCurrencies());
    }

    private void setListPrefCurrencyEntries(ListPreference lp, List<String> values) {
        CharSequence[] cs = values.toArray(new CharSequence[values.size()]);
        lp.setEntries(cs);
        lp.setEntryValues(cs);

        if (lp.getValue() == null || !values.contains(lp.getValue())) {
            lp.setValue(values.get(0));
        }

    }

    private int calculate10Percent(int value) {
        return value * 10 / 100;
    }


    private void updateWidget() {
        Intent intent = new Intent(getActivity().getApplicationContext(), AppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:

        // have no idea what this does, i've just c+p from SO
        int ids[] =
                AppWidgetManager
                        .getInstance(getActivity().getApplication()).getAppWidgetIds(new ComponentName(getActivity().getApplication(), AppWidget.class));

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getActivity().sendBroadcast(intent);

    }

}
