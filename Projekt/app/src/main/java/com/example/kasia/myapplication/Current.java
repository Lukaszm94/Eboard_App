package com.example.kasia.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Current extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);
        Intent intent_current = getIntent();

        TextView textView_current_i1_momentary = (TextView) findViewById(R.id.text_current_i1_momentary_value);
        textView_current_i1_momentary.setText("1");
        TextView textView_current_i1_average = (TextView) findViewById(R.id.text_current_i1_average_value);
        textView_current_i1_average.setText("2");
        TextView textView_current_i2_momentary = (TextView) findViewById(R.id.text_current_i2_momentary_value);
        textView_current_i2_momentary.setText("3");
        TextView textView_current_i2_average = (TextView) findViewById(R.id.text_current_i2_average_value);
        textView_current_i2_average.setText("4");
        TextView textView_current_i1i2_momentary = (TextView) findViewById(R.id.text_current_i1i2_momentary_value);
        textView_current_i1i2_momentary.setText("5");
        TextView textView_current_i1i2_average = (TextView) findViewById(R.id.text_current_i1i2_average_value);
        textView_current_i1i2_average.setText("6");

        GraphView graph = (GraphView) findViewById(R.id.graph_current);
        LineGraphSeries<DataPoint> seriesI1I2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 2),
                new DataPoint(1, 6),
                new DataPoint(2, 7),
                new DataPoint(3, 3),
                new DataPoint(4, 7)
        });
        seriesI1I2.setColor(Color.BLUE);
        graph.addSeries(seriesI1I2);
        LineGraphSeries<DataPoint> seriesI1 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        seriesI1.setColor(Color.GREEN);
        graph.addSeries(seriesI1);
        LineGraphSeries<DataPoint> seriesI2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 5),
                new DataPoint(1, 1),
                new DataPoint(2, 1),
                new DataPoint(3, 4),
                new DataPoint(4, 5)
        });
        seriesI2.setColor(Color.RED);
        graph.addSeries(seriesI2);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_current);
    }
}
