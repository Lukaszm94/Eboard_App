package com.example.kasia.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

public class Temperature extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        Intent intent_temperature = getIntent();

        TextView textView_temperature_momentary = (TextView) findViewById(R.id.text_temperature_momentary_value);
        textView_temperature_momentary.setText("1");
        TextView textView_temperature_average = (TextView) findViewById(R.id.text_temperature_average_value);
        textView_temperature_average.setText("2");

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_temperature);
    }
}
