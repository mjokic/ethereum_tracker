package com.wifi.ethereumtracker.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wifi.ethereumtracker.R;

public class SplashScreenActivity extends AppCompatActivity {
/*
    This activity is the first one to start.
    On its start get sources from API
 */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        try {
            Thread.sleep(2000);

            startActivity(new Intent(this, MainActivity.class));

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
