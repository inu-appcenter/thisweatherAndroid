package com.example.thisweather.util;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.example.thisweather.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class Utils {

    public static String getNoPoint(String data) {
        if (!data.equals("")) {
            double n = Double.parseDouble(data);
            return Math.round(n) + "";
        }
        else{
            return "";
        }
    }

    public static void setChart(ArrayList<Entry> value, LineChart lineChart, Context context) {
        lineChart.setLogEnabled(true);
        lineChart.setTouchEnabled(false);
        lineChart.setPinchZoom(false);

        LineDataSet dataSet = new LineDataSet(value, null);
        dataSet.setLabel(null);

        dataSet.setLineWidth(0.7f);
        dataSet.setColor(ContextCompat.getColor(context, R.color.chartGray));
        dataSet.setCircleColor(ContextCompat.getColor(context, R.color.mainOrange));
        dataSet.setCircleRadius(3);
        dataSet.setCircleHoleColor(Color.WHITE);
        dataSet.setCircleHoleRadius(2.3f);
        dataSet.setDrawCircleHole(true);
        dataSet.setDrawCircles(true);
        dataSet.setDrawValues(false);
        dataSet.setDrawIcons(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(false);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setEnabled(false);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setEnabled(false);

        LineData lineData = new LineData();
        lineData.addDataSet(dataSet);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setNoDataText("");
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

}
