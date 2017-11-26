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
import com.wifi.ethereumtracker.app.model.Currency;
import com.wifi.ethereumtracker.ui.activities.preferences.PreferencesActivity;

import java.text.DecimalFormat;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import me.grantland.widget.AutofitHelper;

@SuppressLint("ViewConstructor")
public class MainView extends FrameLayout {

    private final Activity activity;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.imageViewRefresh)
    ImageView imageViewRefresh;

    @BindView(R.id.textViewCurrencySign)
    TextView textViewCurrencySign;

    @BindView(R.id.textViewEtherValue)
    TextView textViewEtherValue;

    @BindView(R.id.textView24HrChange)
    TextView textView24HrChange;

    @BindView(R.id.textViewMyValue)
    TextView textViewMyValue;

    @BindColor(R.color.positive)
    int positiveColor;

    @BindColor(R.color.negative)
    int negativeColor;

    @BindString(R.string.defaultSource)
    String defaultSource;

    @BindString(R.string.defaultCurrency)
    String defaultCurrency;


    public MainView(AppCompatActivity activity) {
        super(activity);
        this.activity = activity;
        inflate(activity, R.layout.activity_main, this);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        activity.setSupportActionBar(toolbar);

        AutofitHelper.create(textViewEtherValue);
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


    public Observable<Object> onRefreshButtonClick() {
        return RxView.clicks(imageViewRefresh);
    }

    public void clickRefreshButton() {
        imageViewRefresh.performClick();
    }


    public void setTextViewValues(Double myValue,
                                  Double ethValue,
                                  Double change,
                                  Currency currency) {
        String changeStr = change + "%";
        textViewMyValue.setText(new DecimalFormat("0.######").format(myValue));
        textViewEtherValue.setText(String.valueOf(ethValue));
        textView24HrChange.setText(changeStr);
        textViewCurrencySign.setText(currency.getSign()); // get the sign from the object

        if (change <= 0) {
            textView24HrChange.setTextColor(negativeColor);
        } else {
            textView24HrChange.setTextColor(positiveColor);
        }
    }


    public void startAnimation() {
        Animation myAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.refresh_animation);
        imageViewRefresh.startAnimation(myAnimation);
    }

    public void stopAnimation() {
        imageViewRefresh.clearAnimation();
    }


    private void openSettingsActivity() {
        Intent intent = new Intent(getContext(), PreferencesActivity.class);
        getContext().startActivity(intent);
    }


    public String getDefaultSource() {
        return this.defaultSource;
    }

    public String getDefaultCurrency() {
        return this.defaultCurrency;
    }
}
