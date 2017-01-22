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

public class Current extends Fragment {

    Vector<DataPoint> vector_current1 = new Vector<DataPoint>();
    Vector <DataPoint>vector_current2 = new Vector<DataPoint>();
    Vector <DataPoint>vector_current12 = new Vector<DataPoint>();

    double I1momentary;
    double I1average;
    double I2momentary;
    double I2average;
    double I1I2momentary;
    double I1I2average;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_current, container, false);

        int size_of_vector_current1 = vector_current1.size();
        int size_of_vector_current2 = vector_current2.size();
        int size_of_vector_current12 = vector_current12.size();
        DataPoint[] dataPointVector_current1 = new DataPoint[size_of_vector_current1];
        DataPoint[] dataPointVector_current2 = new DataPoint[size_of_vector_current2];
        DataPoint[] dataPointVector_current12 = new DataPoint[size_of_vector_current12];

        TextView textView_current_i1_momentary = (TextView) rootView.findViewById(R.id.text_current_i1_momentary_value);
        String string_current_i1_momentary = String.valueOf(I1momentary);
        textView_current_i1_momentary.setText(string_current_i1_momentary);
        TextView textView_current_i1_average = (TextView) rootView.findViewById(R.id.text_current_i1_average_value);
        String string_current_i1_average = String.valueOf(I1average);
        textView_current_i1_average.setText(string_current_i1_average);
        TextView textView_current_i2_momentary = (TextView) rootView.findViewById(R.id.text_current_i2_momentary_value);
        String string_current_i2_momentary = String.valueOf(I2momentary);
        textView_current_i2_momentary.setText(string_current_i2_momentary);
        TextView textView_current_i2_average = (TextView) rootView.findViewById(R.id.text_current_i2_average_value);
        String string_current_i2_average = String.valueOf(I2average);
        textView_current_i2_average.setText(string_current_i2_average);
        TextView textView_current_i1i2_momentary = (TextView) rootView.findViewById(R.id.text_current_i1i2_momentary_value);
        String string_current_i1i2_momentary = String.valueOf(I1I2momentary);
        textView_current_i1i2_momentary.setText(string_current_i1i2_momentary);
        TextView textView_current_i1i2_average = (TextView) rootView.findViewById(R.id.text_current_i1i2_average_value);
        String string_current_i1i2_average = String.valueOf(I1I2average);
        textView_current_i1i2_average.setText(string_current_i1i2_average);

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph_current);

        for(int i = 0; i < size_of_vector_current1; i++)
        {
            dataPointVector_current1[i] = vector_current1.get(i);
            dataPointVector_current2[i] = vector_current2.get(i);
            dataPointVector_current12[i] = vector_current12.get(i);
        }

        LineGraphSeries<DataPoint> seriesI1I2 = new LineGraphSeries<>(dataPointVector_current12);
        seriesI1I2.setColor(Color.BLUE);
        graph.addSeries(seriesI1I2);

        LineGraphSeries<DataPoint> seriesI1 = new LineGraphSeries<>(dataPointVector_current1);
        seriesI1.setColor(Color.GREEN);
        graph.addSeries(seriesI1);

        LineGraphSeries<DataPoint> seriesI2 = new LineGraphSeries<>(dataPointVector_current2);
        seriesI2.setColor(Color.RED);
        graph.addSeries(seriesI2);

        return rootView;
    }


    public void getData(DataManager object_datamanager){

        CurrentPacket packet = object_datamanager.getAverageCurrentData();
        // Jeżeli dobrze rozumiem to powyższa instrukcja pobiera średnie wartości parametrów i zapisuje w obiekcie packet
        DataPoint p1 = new DataPoint(packet.timestamp, packet.current1);
        vector_current1.add(p1);
        DataPoint p2 = new DataPoint(packet.timestamp, packet.current2);
        vector_current2.add(p2);
        DataPoint p12 = new DataPoint(packet.timestamp, packet.current1 + packet.current2);
        vector_current12.add(p12);
        //I1momentary = object_datamanager.current1;
        I1average = packet.current1;
        //I2momentary = object_datamanager.current2;
        I2average = packet.current2;
        //I1I2momentary = object_datamanager.current1 + object_datamanager.current2;
        I1I2average = packet.current1 + packet.current2;

        // Ale wtedy jak dostać się do chwilowych wartości?

        while(vector_current1.size() > 10)
        {
            vector_current1.remove(0);
        }
        while(vector_current2.size() > 10)
        {
            vector_current2.remove(0);
        }
        while(vector_current12.size() > 10)
        {
            vector_current12.remove(0);
        }
    }

}
