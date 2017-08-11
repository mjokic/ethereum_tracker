package com.wifi.ethereumtracker.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wifi.ethereumtracker.model.pojo.CEXPojo;
import com.wifi.ethereumtracker.model.profiles.CexProfile;
import com.wifi.ethereumtracker.model.profiles.GeminiProfile;
import com.wifi.ethereumtracker.R;

import java.lang.reflect.Constructor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView textViewEtherValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        textViewEtherValue = (TextView) findViewById(R.id.textViewEtherValue);
        ImageView imageViewRefresh = (ImageView) findViewById(R.id.imageViewRefresh);

        getEtherValue(imageViewRefresh);

    }

    @Override
    protected void onResume() {
        super.onResume();
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

    public void onClickCustomAmount(View view){
        // open dialog to enter custom amount
    }


    private void openOptions(){
        // open options activity
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);
    }


    private String loadCurrencyPrefs(){
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        return sharedPreferences.getString("currencySettings", "USD");

    }

    private String loadSourceProfile(){
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

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
                textViewEtherValue.setText(String.format("%.2f", cexPojo.getLprice()));

                stopRefreshAnimation(refreshImageView);

            }

            @Override
            public void onFailure(Call<CEXPojo> call, Throwable t) {
                stopRefreshAnimation(refreshImageView);
            }
        });


    }

//    private void getEtherValue(final View refreshImageView){
//        startRefreshAnimation(refreshImageView);
//
//
//        try {
//            Class myClass = Class.forName("CexProfile");
//            Class[] types = {Double.TYPE, this.getClass()};
//
//            Constructor constructor = myClass.getConstructor(types);
//            Object[] parameters = {new Double(0), this};
//            Object instanceOfMyClass = constructor.newInstance(parameters);
//
//            myClass aa = (myClass) instanceOfMyClass;
//
//
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//
//    }

}
