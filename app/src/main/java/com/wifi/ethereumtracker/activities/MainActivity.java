package com.wifi.ethereumtracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.SharedElementCallback;
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
import com.wifi.ethereumtracker.model.enumerations.CurrencyEnum;
import com.wifi.ethereumtracker.model.pojo.CEXPojo;
import com.wifi.ethereumtracker.model.profiles.CexProfile;
import com.wifi.ethereumtracker.model.profiles.GeminiProfile;
import com.wifi.ethereumtracker.R;

import java.lang.reflect.Constructor;
import java.text.DecimalFormat;
import java.util.Currency;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private TextView textViewMyValue;
    private TextView textViewEtherValue;

    private double myValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateAndroidSecurityProvider(this);

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
        startRefreshAnimation(view);

        // refresh ether value bellow..
        getEtherValue(view);

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

    private String loadSourceProfile(){
        String profileBaseUrl = sharedPreferences.getString("sourceSettings", "https://cex.io/");

        Object profile;

        if("https://cex.io".equals(profileBaseUrl)){
            profile = new CexProfile();

        }else if("https://api.gemini.com/".equals(profileBaseUrl)){
            profile = new GeminiProfile();
        }

        return null;

    }

    private void startRefreshAnimation(View view){
        Animation myAnimation =  AnimationUtils.loadAnimation(this, R.anim.refresh_animation);
        view.startAnimation(myAnimation);
    }

    private void stopRefreshAnimation(View view){
        view.clearAnimation();
    }


    private void getEtherValue(final View refreshImageView){
        startRefreshAnimation(refreshImageView);

        CexProfile profile = new CexProfile();
        Call<CEXPojo> call = profile.initialize(loadCurrencyPrefs());

        call.enqueue(new Callback<CEXPojo>() {
            @Override
            public void onResponse(Call<CEXPojo> call, Response<CEXPojo> response) {

                CEXPojo cexPojo = response.body();

                textViewEtherValue.setText(String.format("%.2f", cexPojo.getLprice() * myValue));


                stopRefreshAnimation(refreshImageView);

            }

            @Override
            public void onFailure(Call<CEXPojo> call, Throwable t) {
                stopRefreshAnimation(refreshImageView);
            }
        });


    }


    // Fixing SSL issue on Android version < 5.0
    // https://stackoverflow.com/a/36892715
    private void updateAndroidSecurityProvider(Activity callingActivity) {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("SecurityException", "Google Play Services not available.");
        }
    }

}
