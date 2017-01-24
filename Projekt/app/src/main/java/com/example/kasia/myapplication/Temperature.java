package com.example.kasia.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Locale;
import java.util.Vector;

public class Temperature extends Fragment {

    GraphView graph;
    LineGraphSeries<DataPoint> seriesT1;
    LineGraphSeries<DataPoint> seriesT2;

    private double temperature1_average = 0;
    private double temperature2_average = 0;
    private long lastTimestamp = 0; // in seconds

    TextView temperatureTextView;

    boolean viewCreated = false;
    private final int SAMPLES_COUNT = 50;
    private final String TAG = "TEMPERATURE_FRAGMENT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.activity_temperature, container, false);

        temperatureTextView = (TextView) rootView.findViewById(R.id.temperatureText);
        graph = (GraphView) rootView.findViewById(R.id.graph_temperature);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(lastTimestamp - SAMPLES_COUNT/2);
        graph.getViewport().setMaxX(lastTimestamp);


        if(!viewCreated) {
            GridLabelRenderer glr = graph.getGridLabelRenderer();
            glr.setPadding(32); // should allow for 3 digits to fit on screen

            seriesT1 = new LineGraphSeries<>();
            seriesT1.setColor(Color.BLUE);

            seriesT2 = new LineGraphSeries<>();
            seriesT2.setColor(Color.RED);
        }

        graph.addSeries(seriesT1);
        graph.addSeries(seriesT2);

        viewCreated = true;
        return rootView;
    }

    public void getData(DataManager object_datamanager){
        if(!viewCreated) {
            return;
        }
        TemperaturePacket packet = object_datamanager.getAverageTemperatureData();
        DataPoint p1 = new DataPoint(packet.timestamp / 1000.0, packet.VESC1Temperature);
        DataPoint p2 = new DataPoint(packet.timestamp / 1000.0, packet.VESC2Temperature);
        temperature1_average = packet.VESC1Temperature;
        temperature2_average = packet.VESC2Temperature;
        lastTimestamp = (long) p1.getX();

        seriesT1.appendData(p1, true, SAMPLES_COUNT);
        seriesT2.appendData(p2, true, SAMPLES_COUNT);
        temperatureTextView.setText(String.format(Locale.UK, "%d | %d\n°C", (int)(temperature1_average + 0.5), (int)(temperature2_average + 0.5)));
    }

}

