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
import com.wifi.ethereumtracker.ext.graphFormatters.CustomXAxisRenderer;
import com.wifi.ethereumtracker.ext.graphFormatters.DateXAxisValueFormatter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

@SuppressLint("ViewConstructor")
public class GraphView extends FrameLayout {

    @BindView(R.id.lineChart)
    LineChart lineChart;

    @BindView(R.id.spinner)
    Spinner spinner;


    public GraphView(Activity activity) {
        super(activity);
        inflate(activity, R.layout.activity_graph, this);
        ButterKnife.bind(this);
    }


    public void setupGraph(List<Price> prices) {
        float reference = prices.get(0).getDate().getTime();

        List<Entry> list = new ArrayList<>();
        for (Price price : prices) {
            Timestamp timestamp = price.getDate();
            list.add(new Entry(timestamp.getTime() - reference, (float) price.getPrice()));
        }

        Collections.sort(list, new EntryXComparator());

        LineDataSet dataSet = new LineDataSet(list, null);

        lineChart.setXAxisRenderer(new CustomXAxisRenderer(lineChart.getViewPortHandler(),
                lineChart.getXAxis(), lineChart.getTransformer(YAxis.AxisDependency.LEFT)));

        lineChart.setDescription(null);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new DateXAxisValueFormatter(reference));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(330);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();

        yAxisLeft.setDrawGridLines(false);
        yAxisRight.setEnabled(false);

        lineChart.getLegend().setEnabled(false);
        lineChart.setExtraBottomOffset(10);
        lineChart.setExtraRightOffset(50);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }


    public Observable<?> getSpinnerObservable(){
        return RxAdapterView.itemSelections(spinner)
                .skipInitialValue();
    }

}
