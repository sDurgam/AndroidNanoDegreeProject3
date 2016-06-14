package com.sam_chordas.android.stockhawk.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.api.GetHistoricDataCall;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
/**
 * Created by durga on 6/12/16.
 */
public class DisplayStockGraphActivity extends AppCompatActivity
{
    String symbol;
//    SparkView sparkView;
    TextView scrubInfoTextView;
    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_selectedstock);
        if(getIntent().getExtras() != null && getIntent().getExtras().getString("selectedsymbol") != null)
        {
            symbol = getIntent().getExtras().getString("selectedsymbol");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        lineChart = (LineChart) findViewById(R.id.chart);
        GetHistoricDataCall historicObj = new GetHistoricDataCall(this, symbol);
        historicObj.FetchData();
    }


    public void openChart(TreeMap<String, Double>stockData)
    {
        float[] data = new float[stockData.size()];
        int i =0;
        ArrayList<String> labels = new ArrayList<String>();
        ArrayList<Entry> entries = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat();
        for(Map.Entry<String,Double> entry : stockData.entrySet())
        {
            labels.add(entry.getKey());
            data[i] = Float.valueOf(entry.getValue().toString());
            // creating list of entry
            entries.add(new Entry(data[i], i));
            i++;
        }
        lineChart.getAxisLeft().setTextColor(Color.WHITE);
        lineChart.getAxisRight().setTextColor(Color.WHITE);
        lineChart.getXAxis().setTextColor(Color.WHITE);
        lineChart.getXAxis().setTextColor(Color.WHITE);
        LineDataSet dataset = new LineDataSet(entries, "Closing Price");
        LineData linedata = new LineData(labels, dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);
        lineChart.setData(linedata);
        lineChart.animateY(500);
        lineChart.setDescription(symbol);
    }
}
