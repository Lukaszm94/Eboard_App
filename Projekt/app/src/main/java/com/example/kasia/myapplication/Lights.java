package com.example.kasia.myapplication;

import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class Lights extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights);
        Intent intent_lights = getIntent();

        SeekBar seekBar_front = (SeekBar) findViewById(R.id.slider_front);
        int seekBar_front_value = seekBar_front.getProgress();
        SeekBar seekBar_back = (SeekBar) findViewById(R.id.slider_back);
        int seekBar_back_value = seekBar_front.getProgress();

        List <String> spinnerArray = new ArrayList <String>();
        spinnerArray.add("Mode 1");
        spinnerArray.add("Mode 2");
        spinnerArray.add("Mode 3");

        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner_front = (Spinner) findViewById(R.id.spinner_front);
        spinner_front.setAdapter(adapter);
        Spinner spinner_back = (Spinner) findViewById(R.id.spinner_back);
        spinner_back.setAdapter(adapter);

        spinner_front.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int position, long id)
            {
                switch (position) {
                    case 0:
                        // 1
                        break;
                    case 1:
                        // 2
                        break;
                    case 2:
                        // 3
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        spinner_back.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int position, long id)
            {
                switch (position) {
                    case 0:
                        // 1
                        break;
                    case 1:
                        // 2
                        break;
                    case 2:
                        // 3
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_lights);

    }
}
