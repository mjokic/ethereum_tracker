package com.wifi.ethereumtracker.ui.activities.graph.mvp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.app.model.Price;
import com.wifi.ethereumtracker.ext.MyMarker;
import com.wifi.ethereumtracker.ext.graphFormatters.CustomXAxisRenderer;
import com.wifi.ethereumtracker.ext.graphFormatters.DateXAxisValueFormatter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

@SuppressLint("ViewConstructor")
public class GraphView extends FrameLayout {

    @BindView(R.id.lineChart)
    LineChart lineChart;

    @BindView(R.id.spinner)
    Spinner spinner;

    @BindString(R.string.chart_no_data_text)
    String chartNoDataText;

    @BindColor(R.color.positive)
    int positive;

    public GraphView(Activity activity) {
        super(activity);
        inflate(activity, R.layout.activity_graph, this);
        ButterKnife.bind(this);

        lineChart.setNoDataText(chartNoDataText);
//        lineChart.setAutoScaleMinMaxEnabled(true);
        lineChart.setKeepPositionOnRotation(true);
    }


    public void setupGraph(List<Price> prices) {
        Collections.reverse(prices);
        long reference = prices.get(0).getDate().getTime();


        lineChart.setMarker(new MyMarker(getContext(), R.layout.custom_graph_marker, reference));


        List<Entry> list = new ArrayList<>();
        for (Price price : prices) {
            Timestamp timestamp = price.getDate();
            Long value = timestamp.getTime() - reference;
            list.add(new Entry(value.floatValue(), (float) price.getPrice()));
        }


        Collections.sort(list, new EntryXComparator());

        LineDataSet dataSet = new LineDataSet(list, null);
        dataSet.setColor(positive);
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setFillColor(positive);
        dataSet.setDrawFilled(true);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);


        lineChart.setXAxisRenderer(new CustomXAxisRenderer(lineChart.getViewPortHandler(),
                lineChart.getXAxis(), lineChart.getTransformer(YAxis.AxisDependency.LEFT)));

        lineChart.setDescription(null);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new DateXAxisValueFormatter(reference));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(330);
        xAxis.setGranularity(1f);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();

        yAxisLeft.setDrawGridLines(false);
        yAxisRight.setEnabled(false);

        lineChart.getLegend().setEnabled(false);
        lineChart.setExtraBottomOffset(10);
        lineChart.setExtraRightOffset(20);


        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }


    public void dataSetSetup(){

    }


    public Observable<?> getSpinnerObservable() {
        return RxAdapterView.itemSelections(spinner)
                .skipInitialValue();
    }
}
