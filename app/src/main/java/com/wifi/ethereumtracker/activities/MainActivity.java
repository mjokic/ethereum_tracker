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

import com.wifi.ethereumtracker.pojo.CEXPojo;
import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.services.apiCalls.CEXioInterface;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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


    private void loadPrefs(){
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
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

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://cex.io/")
                .client(client)
                .build();

        CEXioInterface cex = retrofit.create(CEXioInterface.class);
        Call<CEXPojo> call = cex.getLastPriceUSD();

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

}
