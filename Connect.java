package com.example.kasia.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

public class Connect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Intent intent_connect = getIntent();
        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText("Connect");

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_connect);
        layout.addView(textView);
    }
}
