package com.wifi.ethereumtracker.ui.activities.splash.mvp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.FrameLayout;

import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.ui.activities.main.MainActivity;

public class SplashView extends FrameLayout {

    public SplashView(Context context) {
        super(context);
        inflate(context, R.layout.activity_splash_screen, this);
    }


    public void startMainActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        getContext().startActivity(intent);
    }

    public void displayErrorDialog(){
        new AlertDialog.Builder(getContext(), R.style.ErrorAlertDialogStyle)
                .setCancelable(false)
                .setTitle(R.string.error_dialog_title)
                .setMessage(R.string.error_dialog_msg)
                .create()
                .show();
    }
}
