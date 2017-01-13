package com.example.kasia.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent intent_settings = getIntent();

        /*EditText editText_settings_N = (EditText) findViewById(R.id.edit_settings_N);
        String text_settings_N = editText_settings_N.getText().toString();*/

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_settings);

    }

    /*public void update_data(View view)
    {

    }*/

}
