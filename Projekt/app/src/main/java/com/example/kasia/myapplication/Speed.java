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

public class Speed extends Fragment {

    double speed_average;
    private long lastTimestamp = 0;

    TextView textView_speed_average;
    GraphView graph;
    LineGraphSeries<DataPoint> seriesSpeed = null;

    private final int SAMPLES_COUNT = 50;
    private final String TAG = "SpeedFragment";
    private boolean viewCreated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.activity_speed, container, false);

        textView_speed_average = (TextView) rootView.findViewById(R.id.text_speed_average_value);

        graph = (GraphView) rootView.findViewById(R.id.graph_speed);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(lastTimestamp - SAMPLES_COUNT / 2);
        graph.getViewport().setMaxX(lastTimestamp);

        if(!viewCreated) {
            GridLabelRenderer glr = graph.getGridLabelRenderer();
            glr.setPadding(32); // should allow for 3 digits to fit on screen

            seriesSpeed = new LineGraphSeries<>();
            seriesSpeed.setColor(Color.BLUE);
        }

        graph.addSeries(seriesSpeed);
        viewCreated = true;

        return rootView;
    }

    public void getData(DataManager object_datamanager){

        SpeedPacket packet = object_datamanager.getAverageSpeedData();
        DataPoint p1 = new DataPoint(packet.timestamp / 1000.0, packet.speed);
        speed_average = packet.speed;
        lastTimestamp = (long) p1.getX();

        if(viewCreated) {
            seriesSpeed.appendData(p1, true, SAMPLES_COUNT + 5);
            String string_speed_average = String.format(Locale.UK, "%.1f m/s", speed_average);
            textView_speed_average.setText(string_speed_average);
        }
    }

}
