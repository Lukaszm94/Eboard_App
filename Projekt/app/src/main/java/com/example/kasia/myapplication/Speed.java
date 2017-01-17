package com.example.kasia.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Speed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);
        Intent intent_speed = getIntent();

        TextView textView_speed_momentary = (TextView) findViewById(R.id.text_speed_momentary_value);
        textView_speed_momentary.setText("1");
        TextView textView_speed_average = (TextView) findViewById(R.id.text_speed_average_value);
        textView_speed_average.setText("2");

        GraphView graph = (GraphView) findViewById(R.id.graph_speed);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_speed);
    }
}
