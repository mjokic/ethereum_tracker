package com.wifi.ethereumtracker.ui.fragments.preferences;


import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.SwitchPreferenceCompat;

import com.google.gson.Gson;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;
import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.app.di.modules.DatabaseModule;
import com.wifi.ethereumtracker.app.model.Currency;
import com.wifi.ethereumtracker.app.model.Source;
import com.wifi.ethereumtracker.ext.Util;
import com.wifi.ethereumtracker.ui.fragments.preferences.di.DaggerPreferencesFragmentComponent;
import com.wifi.ethereumtracker.ui.fragments.preferences.mvp.PreferencesFragmentPresenter;
import com.wifi.ethereumtracker.widgets.AppWidget;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PreferencesFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener {

    @Inject
    PreferencesFragmentPresenter presenter;
    @Inject
    Gson gson;

    private ListPreference listPreferenceSourceSettings;
    private ListPreference listPreferenceCurrencySettings;
    private EditTextPreference minNotifyValueEditTextPref;
    private EditTextPreference maxNotifyValueEditTextPref;
    private Context context;

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        this.context = getContext();

        DaggerPreferencesFragmentComponent.builder()
                .databaseModule(new DatabaseModule(context))
                .build()
                .inject(this);


        listPreferenceCurrencySettings = (ListPreference) findPreference("currencySettings");
        listPreferenceCurrencySettings.setOnPreferenceChangeListener(this);


        // checking if 'myValue' is valid
        EditTextPreference myValueEditTextPref = (EditTextPreference) findPreference("myValue");
        myValueEditTextPref.setOnPreferenceChangeListener(this);


        SwitchPreferenceCompat switchPreferenceEnableNotifications = (SwitchPreferenceCompat)
                findPreference("enableNotificationsSettings");
        switchPreferenceEnableNotifications.setOnPreferenceChangeListener(this);


        ListPreference listPreferenceCheckInterval = (ListPreference) findPreference("checkInterval");
        listPreferenceCheckInterval.setOnPreferenceChangeListener(this);


        minNotifyValueEditTextPref = (EditTextPreference) findPreference("valueMinNotify");
        minNotifyValueEditTextPref.setOnPreferenceChangeListener(this);

        maxNotifyValueEditTextPref = (EditTextPreference) findPreference("valueMaxNotify");
        maxNotifyValueEditTextPref.setOnPreferenceChangeListener(this);


        listPreferenceSourceSettings = (ListPreference) findPreference("sourceSettings");
        listPreferenceSourceSettings.setOnPreferenceChangeListener(this);
        setListPrefSourceEntries(presenter.getSources()); // setting source entries
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {

        switch (preference.getKey()) {
            case "sourceSettings":
                updateWidget();
                Source source = gson.fromJson((String) o, Source.class);
                setListPrefCurrencyEntries(source.currencies());
                return true;
            case "currencySettings":
                updateWidget();
                return true;
            case "myValue":
                updateWidget();
                return numberInputIsValid(o);
            case "enableNotificationsSettings":
                return enableNotfOnChange(o);
            case "checkInterval":
                return intervalSettOnChange(o);
            case "valueMinNotify":
            case "valueMaxNotify":
                return numberInputIsValid(o);

        }

        return true;
    }


    /**
     * Checking if input is valid number less then 0
     *
     * @param object User input
     * @return If input is invalid return false and don't save changes
     */
    private boolean numberInputIsValid(Object object) {
        try {
            double d = Double.parseDouble((String) object);
            if (d <= 0) {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    private boolean enableNotfOnChange(Object object) {
        double currentPrice = Double.parseDouble(getPreferenceManager()
                .getSharedPreferences().getString("currentPrice", "0"));
        double tenPercent = Util.calculate10Percent(currentPrice);

        // set default values
        minNotifyValueEditTextPref.setText(String.valueOf(currentPrice - tenPercent));
        maxNotifyValueEditTextPref.setText(String.valueOf(currentPrice + tenPercent));

        boolean status = (boolean) object;

        if (status) {
            // start
            Util.schedule(context, 30000);

        } else {
            // cancel
            Util.stop(1);
        }

        return true;
    }

    private boolean intervalSettOnChange(Object object) {
        // when interval changes cancel alarm manger and start new one,
        // so new interval gets applied
        int interval = Integer.parseInt((String) object);

        Util.stop(1);
        Util.schedule(context, interval);
        return true;
    }


    /**
     * Populating ListPreference with sources
     *
     * @param sources Sources downloaded via API
     */
    private void setListPrefSourceEntries(List<Source> sources) {
        List<String> entries = new ArrayList<>();
        List<String> entryValue = new ArrayList<>();

        for (Source source : sources) {
            entries.add(source.site());
            entryValue.add(gson.toJson(source));
        }

        CharSequence[] csEntries = entries.toArray(new CharSequence[entries.size()]);
        CharSequence[] csEntryVals = entryValue.toArray(new CharSequence[entryValue.size()]);

        listPreferenceSourceSettings.setEntries(csEntries);
        listPreferenceSourceSettings.setEntryValues(csEntryVals);


        if (listPreferenceSourceSettings.getValue() == null) {
            listPreferenceSourceSettings.setValue(entryValue.get(0));
        }

        setListPrefCurrencyEntries(gson
                .fromJson(listPreferenceSourceSettings.getValue(), Source.class)
                .currencies());
    }

    /**
     * Populating currencies for selected Source object
     *
     * @param currencies Currencies from Source object
     */
    private void setListPrefCurrencyEntries(List<Currency> currencies) {
        CharSequence[] csEntries = new CharSequence[currencies.size()];
        CharSequence[] csValues = new CharSequence[currencies.size()];

        for (int i = 0; i < currencies.size(); i++) {
            csEntries[i] = currencies.get(i).getName();
            csValues[i] = gson.toJson(currencies.get(i));
        }


        listPreferenceCurrencySettings.setEntries(csEntries);
        listPreferenceCurrencySettings.setEntryValues(csValues);

        Currency c = gson.fromJson(listPreferenceCurrencySettings.getValue(), Currency.class);

        if (listPreferenceCurrencySettings.getValue() == null || !currencies.contains(c)) {
            listPreferenceCurrencySettings.setValue(csValues[0].toString());
        } else {
            listPreferenceCurrencySettings.setValue(gson.toJson(c));
        }

    }


    private void updateWidget() {
        Activity activity = getActivity();
        if (activity != null) {
            Context context = activity.getApplication();

            Intent intent = new Intent(getActivity().getApplicationContext(), AppWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
            // since it seems the onUpdate() is only fired on that:

            // have no idea what this does, i've just c+p from SO
            int ids[] = AppWidgetManager.getInstance(context)
                    .getAppWidgetIds(new ComponentName(context, AppWidget.class));

            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            getActivity().sendBroadcast(intent);
        }
    }

}
