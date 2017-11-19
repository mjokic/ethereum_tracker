package com.wifi.ethereumtracker.ui.activities.main.mvp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.ui.activities.preferences.PreferencesActivity;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

@SuppressLint("ViewConstructor")
public class MainView extends FrameLayout {

    private final Activity activity;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.imageViewRefresh)
    ImageView imageViewRefresh;

    @BindView(R.id.textViewEtherValue)
    TextView textViewEtherValue;

    @BindView(R.id.textView24HrChange)
    TextView textView24HrChange;

    @BindView(R.id.textViewMyValue)
    TextView textViewMyValue;

    @BindColor(R.color.positive)
    int positiveColor;


    public MainView(AppCompatActivity activity) {
        super(activity);
        this.activity = activity;
        inflate(activity, R.layout.activity_main, this);
        ButterKnife.bind(this);


        toolbar.setTitle("");
        activity.setSupportActionBar(toolbar);
    }


    public void onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = activity.getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_items, menu);
    }

    public void onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbarSettingsBtn) {
            openSettingsActivity();
        }
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(getContext(), PreferencesActivity.class);
        getContext().startActivity(intent);
    }


    public Observable<Object> onRefreshButtonClick() {
        return RxView.clicks(imageViewRefresh);
    }

    public void clickRefreshButton(){
        imageViewRefresh.performClick();
    }


    public void setTextViewValues(Double value, Double change) {
        String changeStr = change + "%";
        textViewEtherValue.setText(String.valueOf(value));
        textView24HrChange.setText(changeStr);
        textView24HrChange.setTextColor(positiveColor);
    }

    public void startAnimation() {
        Animation myAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.refresh_animation);
        imageViewRefresh.startAnimation(myAnimation);
    }

    public void stopAnimation() {
        imageViewRefresh.clearAnimation();
    }

}