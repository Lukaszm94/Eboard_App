package com.example.kasia.myapplication;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    //public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private ProgressDialog progress;
    String address = null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    static final String TAG = "MainActivity";

    PacketParser parser;
    ReceiverThread receiver;
    DataManager dataManager;
    private Timer autoUpdate;

    final Battery fragment_battery = new Battery();
    final Current fragment_current = new Current();
    final Speed fragment_speed = new Speed();
    final Temperature fragment_temperature = new Temperature();
    final Lights fragment_lights = new Lights();
    final Settings fragment_settings = new Settings();

    /*public MainActivity() {

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "MainActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if(savedInstanceState == null) { // app is being started
            Log.i(TAG, "Initialize shit");
            Intent newint = getIntent();
            address = newint.getStringExtra(DeviceListActivity.EXTRA_ADDRESS); //receive the address of the bluetooth device
            parser = new PacketParser();
            dataManager = new DataManager(this);
            receiver = new ReceiverThread("Receiver");
            receiver.setDataManager(dataManager);
            new ConnectBT().execute(); //Call the class to connect
        } else {
            Log.i(TAG, "No need to initialize");
        }

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.Frame_battery, fragment_battery);
        fragmentTransaction.commit();

        Button button_battery = (Button) findViewById(R.id.batteryButton);
        button_battery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                FragmentTransaction fragmentTransaction_battery = fragmentManager.beginTransaction();
                fragmentTransaction_battery.replace(R.id.Frame_battery, fragment_battery);
                fragmentTransaction_battery.commit();
            }
        });

        Button button_current = (Button) findViewById(R.id.currentButton);
        button_current.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                FragmentTransaction fragmentTransaction_current = fragmentManager.beginTransaction();
                fragmentTransaction_current.replace(R.id.Frame_battery, fragment_current);
                fragmentTransaction_current.commit();
            }
        });


        Button button_speed = (Button) findViewById(R.id.speedButton);
        button_speed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                FragmentTransaction fragmentTransaction_speed = fragmentManager.beginTransaction();
                fragmentTransaction_speed.replace(R.id.Frame_battery, fragment_speed);
                fragmentTransaction_speed.commit();
            }
        });

        Button button_temperature = (Button) findViewById(R.id.temperatureButton);
        button_temperature.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                FragmentTransaction fragmentTransaction_temperature = fragmentManager.beginTransaction();
                fragmentTransaction_temperature.replace(R.id.Frame_battery, fragment_temperature);
                fragmentTransaction_temperature.commit();
            }
        });

        Button button_lights = (Button) findViewById(R.id.lightsButton);
        button_lights.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                FragmentTransaction fragmentTransaction_lights = fragmentManager.beginTransaction();
                fragmentTransaction_lights.replace(R.id.Frame_battery, fragment_lights);
                fragmentTransaction_lights.commit();
            }
        });

        Button button_settings = (Button) findViewById(R.id.settingsButton);
        button_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                FragmentTransaction fragmentTransaction_settings = fragmentManager.beginTransaction();
                fragmentTransaction_settings.replace(R.id.Frame_battery, fragment_settings);
                fragmentTransaction_settings.commit();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });
            }
        }, 0, 500);
    }

    @Override
    public void onPause() {
        autoUpdate.cancel();
        super.onPause();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void updateUI() {
        //Log.i(TAG, "Update UI");
        //updateBatteryData(dataManager.getAverageBatteryData());
        //updateCurrentData(dataManager.getAverageCurrentData());
        //TODO update fragments data when fragments code is ready

        fragment_current.getData(dataManager);
        fragment_battery.getData(dataManager);
        fragment_speed.getData(dataManager);
        fragment_temperature.getData(dataManager);
    }

    private void disconnectBT()
    {
        if (btSocket!=null) { //If the btSocket is busy
            try {
                btSocket.close(); //close connection
            } catch (IOException e) {
                Log.e(TAG, "Error");
            }
        }
        finish(); //return to the first layout
    }

    private void startReceiving() {
        receiver.setBTSocket(btSocket);
        receiver.start();
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void> { // UI thread
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) { //while the progress dialog is shown, the connection is done in background
            try {
                Log.i(TAG, "ConnectBT doInBackground");
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice tmp = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = tmp.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) { //after the doInBackground, it checks if everything went fine
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                showMessage("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                showMessage("Connected.");
                dataManager.start();
                startReceiving();
                Lights.setBluetoothSocket(btSocket);
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }


}
