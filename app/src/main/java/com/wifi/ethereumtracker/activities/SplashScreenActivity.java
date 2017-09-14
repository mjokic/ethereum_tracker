package com.wifi.ethereumtracker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.services.asyncTasks.SplashAsyncTask;


public class SplashScreenActivity extends AppCompatActivity {
/*
    This activity is the first one to start.
    On its start get sources from API
 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SplashAsyncTask sat = new SplashAsyncTask();
        sat.execute(this);

    }


}
