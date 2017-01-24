package com.example.kasia.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

public class Battery extends Fragment {

    TextView textView_voltage;
    TextView textView_ampHoursDrawn;
    GraphView graph;
    LineGraphSeries<DataPoint> seriesVoltage = null;

    private final int SAMPLES_COUNT = 50;
    private final String TAG = "BatteryFragment";
    private boolean viewCreated = false;
    double voltage;
    double ampHoursDrawn;
    private long lastTimestamp = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.activity_battery, container, false);

        textView_voltage = (TextView) rootView.findViewById(R.id.voltageText);
        textView_ampHoursDrawn = (TextView) rootView.findViewById(R.id.ampHoursDrawnText);

        graph = (GraphView) rootView.findViewById(R.id.graph_battery);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(lastTimestamp - SAMPLES_COUNT / 2);
        graph.getViewport().setMaxX(lastTimestamp);

        if(!viewCreated) {
            GridLabelRenderer glr = graph.getGridLabelRenderer();
            glr.setPadding(32); // should allow for 3 digits to fit on screen

            seriesVoltage = new LineGraphSeries<>();
            seriesVoltage.setColor(Color.BLUE);
        }

        graph.addSeries(seriesVoltage);
        viewCreated = true;

        return rootView;
    }

    public void getData(DataManager object_datamanager){
        if(!viewCreated) {
            return;
        }
        BatteryPacket packet = object_datamanager.getAverageBatteryData();
        DataPoint p = new DataPoint(packet.timestamp / 1000.0, packet.VESCBatteryVoltage);
        voltage = packet.VESCBatteryVoltage;
        ampHoursDrawn = packet.ampHoursDrawn;
        lastTimestamp = (long) p.getX();

        seriesVoltage.appendData(p, true, SAMPLES_COUNT);
        textView_voltage.setText(String.format(Locale.UK, "%.1f V", voltage));
        textView_ampHoursDrawn.setText(String.format(Locale.UK, "%.1f Ah", ampHoursDrawn));
    }

}