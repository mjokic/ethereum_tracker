package com.wifi.ethereumtracker.ui.activities.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lb.auto_fit_textview.AutoResizeTextView;
import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.model.ProfileOld;
import com.wifi.ethereumtracker.model.RetrofitTask;
import com.wifi.ethereumtracker.model.enumerations.CurrencyEnum;
import com.wifi.ethereumtracker.ui.activities.preferences.PreferencesActivity;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private TextView textViewMyValue;
    private TextView textView24HrChange;
    private AutoResizeTextView textViewEtherValue;

    private double myValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        textViewMyValue = (TextView) findViewById(R.id.textViewMyValue);
        textViewEtherValue = (AutoResizeTextView) findViewById(R.id.textViewEtherValue);
        textView24HrChange = (TextView) findViewById(R.id.textView24HrChange);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadMyValuePrefs();

        ImageView imageViewRefresh = (ImageView) findViewById(R.id.imageViewRefresh);
        getEtherValue(imageViewRefresh);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.toolbarSettingsBtn) {
            openOptions();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickRefresh(View view) {
        // refresh ether value bellow..
        getEtherValue((ImageView) view);

    }


    private void openOptions() {
        // open options activity
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);
    }


    private void loadMyValuePrefs() {
        this.myValue = Double.parseDouble(sharedPreferences.getString("myValue", "1"));

        DecimalFormat format = new DecimalFormat("0.######");
        textViewMyValue.setText(String.valueOf(format.format(myValue)));

    }

    private String loadSourcePrefs() {
        String p = sharedPreferences.getString("sourceSettings", "null");
        ProfileOld profileOld = new Gson().fromJson(p, ProfileOld.class);

        if (p.equals("null")) {
            return "cex";
        }

        return profileOld.getSite();
    }

    private String loadCurrencyPrefs() {
        String currency = sharedPreferences.getString("currencySettings", "usd");

        TextView textView = (TextView) findViewById(R.id.textViewCurrencySign);
        textView.setText(CurrencyEnum.getSign(currency));

        return currency;

    }


    private void getEtherValue(final ImageView refreshImageView) {

        String source = loadSourcePrefs();
        String currency = loadCurrencyPrefs();

        RetrofitTask retrofitTask = new RetrofitTask(source, currency, this);
//        retrofitTask.runAsync(myValue, currency, textViewEtherValue, textView24HrChange, refreshImageView);

    }


    @Override
    public void onBackPressed() {
        // preventing to go back to splash screen
        finishAffinity();
    }
}
