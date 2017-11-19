package com.wifi.ethereumtracker.ui.activities.splash.mvp;

import android.content.Context;
import android.content.Intent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.ui.activities.main.MainActivity;

public class SplashView extends FrameLayout {

    public SplashView(Context context) {
        super(context);
        inflate(context, R.layout.activity_splash_screen, this);
    }


    public void startMainActivity(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        getContext().startActivity(intent);
    }

    public void displayToast(){
        Toast.makeText(getContext(), "Oppening main...", Toast.LENGTH_SHORT).show();
    }

}
