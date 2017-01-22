package com.example.kasia.myapplication;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;

public class Settings extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_settings, container, false);

        EditText editText_settings_N = (EditText) rootView.findViewById(R.id.edit_settings_N);
        String text_settings_N = editText_settings_N.getText().toString();

        return rootView;//return inflater.inflate(R.layout.settings, container, false);
    }

}