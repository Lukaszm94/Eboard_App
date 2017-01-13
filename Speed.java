package com.example.kasia.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

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

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_speed);
    }
}
