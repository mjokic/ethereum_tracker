package com.wifi.ethereumtracker.ui.activities.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.wifi.ethereumtracker.app.App;
import com.wifi.ethereumtracker.ui.activities.main.di.DaggerMainComponent;
import com.wifi.ethereumtracker.ui.activities.main.di.MainModule;
import com.wifi.ethereumtracker.ui.activities.main.mvp.MainPresenter;
import com.wifi.ethereumtracker.ui.activities.main.mvp.MainView;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity {

    @Inject
    MainPresenter presenter;

    @Inject
    MainView view;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerMainComponent.builder()
                .appComponent(((App) getApplication()).getComponent())
                .mainModule(new MainModule(this))
                .build()
                .inject(this);

        setContentView(view);
        presenter.onCreate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        view.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        view.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        // preventing to go back to splash screen
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

}
