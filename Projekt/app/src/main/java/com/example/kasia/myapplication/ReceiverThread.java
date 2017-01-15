package com.example.kasia.myapplication;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Luke on 2017-01-15.
 */

public class ReceiverThread extends Thread {
    private Thread t;
    private String threadName;
    private final String TAG = "ReceiverThread";

    private BluetoothSocket btSocket = null;
    private PacketParser parser;

    ReceiverThread(String name) {
        threadName = name;
        t = new Thread(this, threadName);
        parser = new PacketParser();
    }

    public void setDataManager(DataManager manager) {
        parser.setDataManager(manager);
    }

    public void run() {
        Log.i(TAG, "Running " + threadName);
        if(btSocket == null) {
            Log.i(TAG, "BT socket is null");
            return;
        }
        // erase initial data
        try {
            int initialBytesAvailable = btSocket.getInputStream().available();
            btSocket.getInputStream().skip(initialBytesAvailable);
        } catch (IOException e) {
            Log.i(TAG, "Unable to erase initial data from BT socket");
            return;
        }


        try {
            while(true) {
                receiveData();
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Log.i(TAG, "Thread " + threadName + " interrupted");
        }
        Log.i(TAG, "Thread " + threadName + " exiting");
    }

    public void start() {
        t.start();
    }

    public void setBTSocket(BluetoothSocket socket) {
        btSocket = socket;
    }

    private void receiveData() {
        byte buffer[] = new byte[1024];
        int numBytes;
        try {
            numBytes = btSocket.getInputStream().read(buffer);
            byte shortData[] = new byte[numBytes];
            for(int i = 0; i < numBytes; i++) {
                shortData[i] = buffer[i];
            }
            //Log.i(TAG, "Received " + numBytes + " bytes");
            //Log.i(TAG, "Data: " + shortData.toString());
            parser.appendBytes(shortData);
        } catch (IOException e) {
            Log.i(TAG, "Receive error");
        }
        for(int i = 0; i < 10; i++) {
            if(!parser.parse()) {
                break;
            }
        }
    }
}
