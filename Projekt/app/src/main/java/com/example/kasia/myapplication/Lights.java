package com.example.kasia.myapplication;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import android.support.v4.app.Fragment;


public class Lights extends Fragment {
    Spinner spinner_front;
    Spinner spinner_back;
    SeekBar seekBar_front;
    SeekBar seekBar_back;
    Button updateButton;
    private static WeakReference<BluetoothSocket> btSocketReference;

    private final String TAG = "Lights";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_lights, container, false);

        seekBar_front = (SeekBar) rootView.findViewById(R.id.slider_front);
        seekBar_back = (SeekBar) rootView.findViewById(R.id.slider_back);

        List <String> spinnerArray = new ArrayList <String>();
        spinnerArray.add("Solid");
        spinnerArray.add("Blink 50/50");
        spinnerArray.add("Blink 20/80");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_front = (Spinner) rootView.findViewById(R.id.spinner_front);
        spinner_front.setAdapter(adapter);
        spinner_back = (Spinner) rootView.findViewById(R.id.spinner_back);
        spinner_back.setAdapter(adapter);

        updateButton = (Button) rootView.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_data(v);
            }
        });



        return rootView;
    }

    public static void setBluetoothSocket(BluetoothSocket socket) {
        btSocketReference = new WeakReference<>(socket);
    }

    public void update_data(View v) {
        int mode = spinner_back.getSelectedItemPosition();
        int brightness = seekBar_back.getProgress();
        LightsPacket packet = new LightsPacket((byte) brightness, (byte) 1, (byte) mode);

        if(btSocketReference == null) {
            return;
        }
        String message = "Update OK";
        try {
            btSocketReference.get().getOutputStream().write(packet.toSerialPacket());
        } catch (IOException e) {
            message = "Update failed";
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
