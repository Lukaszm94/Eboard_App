package com.example.kasia.myapplication;

import android.graphics.Color;
import android.support.v4.app.Fragment;
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

public class Current extends Fragment {

    GraphView graph;
    LineGraphSeries<DataPoint> seriesI1;
    LineGraphSeries<DataPoint> seriesI2;

    private double I1average = 0;
    private double I2average = 0;
    private long lastTimestamp = 0; // in seconds

    TextView currentTextView;

    boolean viewCreated = false;
    private final int SAMPLES_COUNT = 50;
    private final String TAG = "CURRENT_FRAGMENT";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.activity_current, container, false);

        currentTextView = (TextView) rootView.findViewById(R.id.currentText);
        graph = (GraphView) rootView.findViewById(R.id.graph_current);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(lastTimestamp - SAMPLES_COUNT/2);
        graph.getViewport().setMaxX(lastTimestamp);

        if(!viewCreated) {
            GridLabelRenderer glr = graph.getGridLabelRenderer();
            glr.setPadding(32); // should allow for 3 digits to fit on screen

            seriesI1 = new LineGraphSeries<>();
            seriesI1.setColor(Color.BLUE);

            seriesI2 = new LineGraphSeries<>();
            seriesI2.setColor(Color.RED);
        }

        graph.addSeries(seriesI1);
        graph.addSeries(seriesI2);

        viewCreated = true;
        return rootView;
    }


    public void getData(DataManager object_datamanager) {

        if(!viewCreated) {
            return;
        }
        CurrentPacket packet = object_datamanager.getAverageCurrentData();
        DataPoint p1 = new DataPoint(packet.timestamp / 1000.0, packet.current1);
        DataPoint p2 = new DataPoint(packet.timestamp / 1000.0, packet.current2);
        I1average = packet.current1;
        I2average = packet.current2;
        lastTimestamp = (long) p1.getX();

        seriesI1.appendData(p1, true, SAMPLES_COUNT);
        seriesI2.appendData(p2, true, SAMPLES_COUNT);
        currentTextView.setText(String.format(Locale.UK, "%d | %d\nA", (int)(I1average + 0.5), (int)(I2average + 0.5)));
    }
}
