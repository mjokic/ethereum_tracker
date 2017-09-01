package com.wifi.ethereumtracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import com.wifi.ethereumtracker.model.RetrofitTask;
import com.wifi.ethereumtracker.model.enumerations.CurrencyEnum;
import com.wifi.ethereumtracker.model.pojo.ResponsePojo;
import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.services.apiCalls.ApiService;

import java.security.cert.CertificateException;
import java.text.DecimalFormat;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private TextView textViewMyValue;
    private TextView textViewEtherValue;

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
        textViewEtherValue = (TextView) findViewById(R.id.textViewEtherValue);

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

        if(item.getItemId() == R.id.toolbarSettingsBtn){
            openOptions();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickRefresh(View view){
        // refresh ether value bellow..
        getEtherValue((ImageView) view);

    }


    private void openOptions(){
        // open options activity
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);
    }


    private void loadMyValuePrefs(){
        this.myValue = Double.parseDouble(sharedPreferences.getString("myValue", "1"));

        DecimalFormat format = new DecimalFormat("0.######");
        textViewMyValue.setText(String.valueOf(format.format(myValue)));

    }

    private String loadCurrencyPrefs(){
        String currency = sharedPreferences.getString("currencySettings", "USD");

        TextView textView = (TextView) findViewById(R.id.textViewCurrencySign);
        textView.setText(CurrencyEnum.getSign(currency));

        return currency;

    }


    private void getEtherValue(final ImageView refreshImageView){

//        Profile t = loadSourceProfile();
//
//        Call c = t.initialize(loadCurrencyPrefs());
//        t.runInFront(c, myValue, textViewEtherValue, refreshImageView, getApplicationContext());

        RetrofitTask retrofitTask = new RetrofitTask("cex", "usd");
        retrofitTask.runAsync(myValue, textViewEtherValue, refreshImageView, getApplicationContext());

    }

}
