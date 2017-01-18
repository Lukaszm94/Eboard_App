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
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.battery:
            {
                Intent intent_battery = new Intent(this, Battery.class);
                startActivity(intent_battery);
                //break;
            }
            return true;

            case R.id.connect:
            {
                Intent intent_connect = new Intent(this, Connect.class);
                startActivity(intent_connect);
                //return true;
            }
            return true;
            case R.id.current:
            {
                Intent intent_current = new Intent(this, Current.class);
                startActivity(intent_current);
                //return true;
            }
            return true;
            case R.id.speed:
            {
                Intent intent_speed = new Intent(this, Speed.class);
                startActivity(intent_speed);
                //return true;
            }
            return true;
            case R.id.temperature:
            {
                Intent intent_temperature = new Intent(this, Temperature.class);
                startActivity(intent_temperature);
                //return true;
            }
            return true;
            case R.id.map:
            {
                //Intent intent_battery = new Intent(this, Battery.class);
                //startActivity(intent_battery);
                return true;
            }
            //return true;
            case R.id.lights:
            {
                Intent intent_lights = new Intent(this, Lights.class);
                startActivity(intent_lights);
                //return true;
            }
            return true;
            case R.id.settings:
            {
                Intent intent_settings = new Intent(this, Settings.class);
                startActivity(intent_settings);
                //return true;
            }
            return true;

            default:
                return super.onOptionsItemSelected(item);

        }
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
