package com.sam_chordas.android.stockhawk.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
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
    TextView scrubInfoTextView;
    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_selectedstock);
        AppBarLayout appbarLayout = (AppBarLayout)findViewById(R.id.detailstock_appbar);
        Toolbar toolbar = (android.support.v7.widget.Toolbar)appbarLayout.findViewById(R.id.detailstock_toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        String selectedsymbolStr = getResources().getString(R.string.selectedsymbol);
        if(getIntent().getExtras() != null && getIntent().getExtras().getString(selectedsymbolStr) != null)
        {
            symbol = getIntent().getExtras().getString(selectedsymbolStr);
            lineChart = (LineChart) findViewById(R.id.chart);
            AccessibilityManager manager = (AccessibilityManager)getSystemService(Context.ACCESSIBILITY_SERVICE);
            if (manager.isEnabled()) {
                AccessibilityEvent e = AccessibilityEvent.obtain();
                e.setEventType(AccessibilityEvent.TYPE_ANNOUNCEMENT);
                e.setClassName(getClass().getName());
                e.setPackageName(getPackageName());
                e.getText().add(getResources().getString(R.string.detailed_stock_message) + symbol);
                manager.sendAccessibilityEvent(e);
            }
            GetHistoricDataCall historicObj = new GetHistoricDataCall(this, symbol);
            historicObj.FetchData();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
//        lineChart.getAxisLeft().setTextColor(Color.WHITE);
//        lineChart.getAxisRight().setTextColor(Color.WHITE);
//        lineChart.getXAxis().setTextColor(Color.WHITE);
//        lineChart.getXAxis().setTextColor(Color.WHITE);
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
