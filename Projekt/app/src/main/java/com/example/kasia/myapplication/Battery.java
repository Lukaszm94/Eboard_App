package com.example.kasia.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Vector;

public class Battery extends Fragment {

    Vector<DataPoint> vector_battery = new Vector<DataPoint>();
    double voltage;
    double charge;
    double cell1;
    double cell2;
    double cell3;
    double cell4;
    double cell5;
    double cell6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_battery, container, false);

        int size_of_vector_battery = vector_battery.size();
        DataPoint[] dataPointVector_battery = new DataPoint[size_of_vector_battery];

        TextView textView_battery_charge = (TextView) rootView.findViewById(R.id.text_battery_charge_value);
        String string_battery_charge = String.valueOf(charge);
        textView_battery_charge.setText(string_battery_charge);
        TextView textView_battery_voltage = (TextView) rootView.findViewById(R.id.text_battery_voltage_value);
        String string_battery_voltage = String.valueOf(voltage);
        textView_battery_voltage.setText(string_battery_voltage);
        TextView textView_cell1 = (TextView) rootView.findViewById(R.id.text_battery_cell1_value);
        String string_battery_cell1 = String.valueOf(cell1);
        textView_cell1.setText(string_battery_cell1);
        TextView textView_cell2 = (TextView) rootView.findViewById(R.id.text_battery_cell2_value);
        String string_battery_cell2 = String.valueOf(cell2);
        textView_cell2.setText(string_battery_cell2);
        TextView textView_cell3 = (TextView) rootView.findViewById(R.id.text_battery_cell3_value);
        String string_battery_cell3 = String.valueOf(cell3);
        textView_cell3.setText(string_battery_cell3);
        TextView textView_cell4 = (TextView) rootView.findViewById(R.id.text_battery_cell4_value);
        String string_battery_cell4 = String.valueOf(cell4);
        textView_cell4.setText(string_battery_cell4);
        TextView textView_cell5 = (TextView) rootView.findViewById(R.id.text_battery_cell5_value);
        String string_battery_cell5 = String.valueOf(cell5);
        textView_cell5.setText(string_battery_cell5);
        TextView textView_cell6 = (TextView) rootView.findViewById(R.id.text_battery_cell6_value);
        String string_battery_cell6 = String.valueOf(cell6);
        textView_cell6.setText(string_battery_cell6);

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph_battery);

        for(int i = 0; i < size_of_vector_battery; i++)
        {
            dataPointVector_battery[i] = vector_battery.get(i);
        }

        LineGraphSeries<DataPoint> seriesBattery = new LineGraphSeries<>(dataPointVector_battery);
        seriesBattery.setColor(Color.BLUE);
        graph.addSeries(seriesBattery);

        return rootView;//return inflater.inflate(R.layout.battery, container, false);
    }

    public void getData(DataManager object_datamanager){

        BatteryPacket packet = object_datamanager.getAverageBatteryData();
        DataPoint p1 = new DataPoint(packet.timestamp, packet.CUBatteryVoltage); // Nie mam pojęcia czy tu ma być CUBatteryVoltage czy VESCBatteryVoltage, poprawisz sobie
        vector_battery.add(p1);
        voltage = packet.CUBatteryVoltage;
        //charge = packet.??;  Nie ogarniam Twoich parametrów.. tu ma być ampHoursCharged?
        cell1 = packet.cellsVoltage[0];
        cell2 = packet.cellsVoltage[1];
        cell3 = packet.cellsVoltage[2];
        cell4 = packet.cellsVoltage[3];
        cell5 = packet.cellsVoltage[4];
        cell6 = packet.cellsVoltage[5];
        while(vector_battery.size() > 10)
        {
            vector_battery.remove(0);
        }
    }

}