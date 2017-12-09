package com.wifi.ethereumtracker.ext.graphFormatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateXAxisValueFormatter implements IAxisValueFormatter {

    private Long reference;

    public DateXAxisValueFormatter(Long reference){
        this.reference = reference;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Float time = value + reference.floatValue();
        Timestamp timestamp = new Timestamp(time.longValue());
        Date date = new Date(timestamp.getTime());
        return new SimpleDateFormat("HH:mm\nyyyy-MM-dd", Locale.getDefault()).format(date);
    }

}
