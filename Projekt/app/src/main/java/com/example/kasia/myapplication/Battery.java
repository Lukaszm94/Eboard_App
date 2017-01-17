package com.example.kasia.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Battery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        Intent intent_battery = getIntent();

        TextView textView_battery_charge = (TextView) findViewById(R.id.text_battery_charge_value);
        textView_battery_charge.setText("1");
        TextView textView_battery_voltage = (TextView) findViewById(R.id.text_battery_voltage_value);
        textView_battery_voltage.setText("2");
        TextView textView_cell1 = (TextView) findViewById(R.id.text_battery_cell1_value);
        textView_cell1.setText("3");
        TextView textView_cell2 = (TextView) findViewById(R.id.text_battery_cell2_value);
        textView_cell2.setText("4");
        TextView textView_cell3 = (TextView) findViewById(R.id.text_battery_cell3_value);
        textView_cell3.setText("5");
        TextView textView_cell4 = (TextView) findViewById(R.id.text_battery_cell4_value);
        textView_cell4.setText("6");
        TextView textView_cell5 = (TextView) findViewById(R.id.text_battery_cell5_value);
        textView_cell5.setText("7");
        TextView textView_cell6 = (TextView) findViewById(R.id.text_battery_cell6_value);
        textView_cell6.setText("8");

        GraphView graph = (GraphView) findViewById(R.id.graph_battery);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);



        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_battery);

    }
}