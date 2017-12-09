package com.wifi.ethereumtracker.ext.graphAccessories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.wifi.ethereumtracker.R;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ViewConstructor")
public class CustomChartMarker extends MarkerView {

    private final Long reference;
    @BindView(R.id.textViewMarkerPrice)
    TextView textViewMarkerPrice;
    @BindView(R.id.textViewMarkerDate)
    TextView textViewMarker;


    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context        context
     * @param layoutResource the layout resource to use for the MarkerView
     * @param reference      reference to original timestamp
     */
    public CustomChartMarker(Context context, int layoutResource, Long reference) {
        super(context, layoutResource);
        this.reference = reference;
        ButterKnife.bind(this);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        Float value = e.getX() + reference.floatValue();
        long time = value.longValue();

        Timestamp timestamp = new Timestamp(time);
        Date date = new Date(timestamp.getTime());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss\nyyyy-MM-dd",
                Locale.getDefault());

        textViewMarkerPrice.setText(String.valueOf(e.getY()));
        textViewMarker.setText(sdf.format(date));

        super.refreshContent(e, highlight);
    }


    @Override
    public MPPointF getOffset() {
        return new MPPointF(-getWidth() / 2, -getHeight() * 1.2f);
    }

    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
        return getOffset();
    }

}
