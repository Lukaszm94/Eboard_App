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

public class Temperature extends Fragment {

    Vector<DataPoint> vector_temperature1 = new Vector<DataPoint>();
    Vector<DataPoint> vector_temperature2 = new Vector<DataPoint>();
    double temperature1_momentary;
    double temperature1_average;
    double temperature2_momentary;
    double temperature2_average;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_temperature, container, false);

        int size_of_vector_temperature1 = vector_temperature1.size();
        DataPoint[] dataPointVector_temperature1 = new DataPoint[size_of_vector_temperature1];
        int size_of_vector_temperature2 = vector_temperature2.size();
        DataPoint[] dataPointVector_temperature2 = new DataPoint[size_of_vector_temperature2];

        TextView textView_temperature1_momentary = (TextView) rootView.findViewById(R.id.text_temperature1_momentary_value);
        String string_temperature1_momentary = String.valueOf(temperature1_momentary);
        textView_temperature1_momentary.setText(string_temperature1_momentary);
        TextView textView_temperature1_average = (TextView) rootView.findViewById(R.id.text_temperature1_average_value);
        String string_temperature1_average = String.valueOf(temperature1_average);
        textView_temperature1_average.setText(string_temperature1_average);
        TextView textView_temperature2_momentary = (TextView) rootView.findViewById(R.id.text_temperature2_momentary_value);
        String string_temperature2_momentary = String.valueOf(temperature2_momentary);
        textView_temperature2_momentary.setText(string_temperature2_momentary);
        TextView textView_temperature2_average = (TextView) rootView.findViewById(R.id.text_temperature2_average_value);
        String string_temperature2_average = String.valueOf(temperature2_average);
        textView_temperature2_average.setText(string_temperature2_average);

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph_temperature);

        for(int i = 0; i < size_of_vector_temperature1; i++)
        {
            dataPointVector_temperature1[i] = vector_temperature1.get(i);
            dataPointVector_temperature2[i] = vector_temperature2.get(i);
        }

        LineGraphSeries<DataPoint> seriesTemp1 = new LineGraphSeries<>(dataPointVector_temperature1);
        seriesTemp1.setColor(Color.BLUE);
        graph.addSeries(seriesTemp1);

        LineGraphSeries<DataPoint> seriesTemp2 = new LineGraphSeries<>(dataPointVector_temperature2);
        seriesTemp2.setColor(Color.RED);
        graph.addSeries(seriesTemp2);

        return rootView; //inflater.inflate(R.layout.temperature, container, false);
    }

    public void getData(DataManager object_datamanager){

        TemperaturePacket packet = object_datamanager.getAverageTemperatureData();
        DataPoint p1 = new DataPoint(packet.timestamp, packet.VESC1Temperature);
        vector_temperature1.add(p1);
        DataPoint p2 = new DataPoint(packet.timestamp, packet.VESC2Temperature);
        vector_temperature2.add(p2);
        //temperature1_momentary = packet.VESC1Temperature;
        temperature1_average = packet.VESC1Temperature;
        //temperature2_momentary = packet.VESC2Temperature;
        temperature2_average = packet.VESC2Temperature;
        while(vector_temperature1.size() > 10)
        {
            vector_temperature1.remove(0);
        }
        while(vector_temperature2.size() > 10)
        {
            vector_temperature2.remove(0);
        }
    }

}

