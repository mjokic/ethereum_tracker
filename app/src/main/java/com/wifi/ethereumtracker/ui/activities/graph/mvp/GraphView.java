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
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.wifi.ethereumtracker.R;
import com.wifi.ethereumtracker.app.model.Price;
import com.wifi.ethereumtracker.ext.graphAccessories.CustomChartMarker;
import com.wifi.ethereumtracker.ext.graphAccessories.CustomXAxisRenderer;
import com.wifi.ethereumtracker.ext.graphAccessories.DateXAxisValueFormatter;

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

    private long reference = 0;


    public GraphView(Activity activity) {
        super(activity);
        inflate(activity, R.layout.activity_graph, this);
        ButterKnife.bind(this);

        setupGraph();
    }


    private void setupGraph() {
        lineChart.setXAxisRenderer(new CustomXAxisRenderer(lineChart.getViewPortHandler(),
                lineChart.getXAxis(), lineChart.getTransformer(YAxis.AxisDependency.LEFT)));
        lineChart.setDescription(null);
        lineChart.getLegend().setEnabled(false);
        lineChart.setExtraBottomOffset(10);
        lineChart.setExtraRightOffset(20);
        lineChart.setNoDataText(chartNoDataText);
        lineChart.setAutoScaleMinMaxEnabled(true);
        lineChart.setKeepPositionOnRotation(true);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(330);
        xAxis.setGranularity(1f);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();

        yAxisLeft.setDrawGridLines(false);
        yAxisRight.setEnabled(false);
    }


    public void dataSetSetup(List<Price> prices) {
        Collections.reverse(prices);
        reference = prices.get(0).getDate().getTime();

        // setting custom value formatter
        lineChart.getXAxis().setValueFormatter(new DateXAxisValueFormatter(reference));

        setupMarker();

        List<Entry> list = new ArrayList<>();
        LineDataSet dataSet = new LineDataSet(list, null);
        dataSet.setColor(positive);
        dataSet.setFillColor(positive);
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setDrawFilled(true);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setDrawHighlightIndicators(false);

        for (Price price : prices) {
            Timestamp timestamp = price.getDate();
            Long value = timestamp.getTime() - reference;
            dataSet.addEntry(new Entry(value.floatValue(), (float) price.getPrice()));
        }

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private void setupMarker() {
        CustomChartMarker marker = new CustomChartMarker(getContext().getApplicationContext(),
                R.layout.custom_graph_marker, reference);
        lineChart.setMarker(marker);
    }


    public Observable<?> getSpinnerObservable() {
        return RxAdapterView.itemSelections(spinner)
                .skipInitialValue();
    }
}
