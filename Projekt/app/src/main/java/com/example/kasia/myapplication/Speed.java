package com.example.kasia.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Vector;

public class Speed extends Fragment {

    Vector<DataPoint> vector_speed = new Vector<DataPoint>();

    double speed_momentary;
    double speed_average;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_speed, container, false);

        int size_of_vector_speed = vector_speed.size();
        DataPoint[] dataPointVector_speed = new DataPoint[size_of_vector_speed];

        TextView textView_speed_momentary = (TextView) rootView.findViewById(R.id.text_speed_momentary_value);
        String string_speed_momentary = String.valueOf(speed_momentary);
        textView_speed_momentary.setText(string_speed_momentary);
        TextView textView_speed_average = (TextView) rootView.findViewById(R.id.text_speed_average_value);
        String string_speed_average = String.valueOf(speed_average);
        textView_speed_average.setText(string_speed_average);

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph_speed);

        for(int i = 0; i < size_of_vector_speed; i++)
        {
            dataPointVector_speed[i] = vector_speed.get(i);
        }

        LineGraphSeries<DataPoint> seriesSpeed = new LineGraphSeries<>(dataPointVector_speed);
        seriesSpeed.setColor(Color.BLUE);
        graph.addSeries(seriesSpeed);

        return rootView;//return inflater.inflate(R.layout.speed, container, false);
    }

    public void getData(DataManager object_datamanager){

        SpeedPacket packet = object_datamanager.getAverageSpeedData();
        DataPoint p1 = new DataPoint(packet.timestamp, packet.speed);
        vector_speed.add(p1);
        //speed_momentary = packet.speed;
        speed_average = packet.speed;
        while(vector_speed.size() > 10)
        {
            vector_speed.remove(0);
        }
    }

}
