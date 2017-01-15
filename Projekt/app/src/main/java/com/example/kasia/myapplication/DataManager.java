package com.example.kasia.myapplication;

import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Luke on 2017-01-15.
 */

public class DataManager extends Thread {
    //containers
    private PacketBuffer<CurrentPacket> currentBuffer;
    private PacketBuffer<BatteryPacket> batteryBuffer;
    private PacketBuffer<TemperaturePacket> temperatureBuffer;
    private PacketBuffer<SpeedPacket> speedBuffer;
    //logged data struct container
    private ArrayList<BigPacket> logBuffer;
    //autosave data
    private long lastAutosaveTimestamp; // in ms
    private long autosaveInterval; // in ms
    //
    private MainActivity mainActivity;
    private Thread t;
    private String threadName;
    private String TAG = "DataManager";

    DataManager(MainActivity main) {
        threadName = "DataManager";
        t = new Thread(this, threadName);
        currentBuffer = new PacketBuffer<>();
        batteryBuffer = new PacketBuffer<>();
        temperatureBuffer = new PacketBuffer<>();
        speedBuffer = new PacketBuffer<>();
        logBuffer = new ArrayList<>();
        lastAutosaveTimestamp = System.currentTimeMillis();
        autosaveInterval = 120000;
        mainActivity = main;
    }

    public synchronized void newCurrentPacket(CurrentPacket packet) {
        currentBuffer.add(packet);
    }

    public synchronized void newBatteryPacket(BatteryPacket packet) {
        batteryBuffer.add(packet);
    }

    public synchronized void newTemperaturePacket(TemperaturePacket packet) {
        temperatureBuffer.add(packet);
    }

    public synchronized void newSpeedPacket(SpeedPacket packet) {
        speedBuffer.add(packet);
    }

    public CurrentPacket getAverageCurrentData() {
        currentBuffer.update(System.currentTimeMillis());
        CurrentPacket avgPacket = new CurrentPacket(System.currentTimeMillis(), 0, 0);
        int samples = currentBuffer.buffer.size();
        //Log.i(TAG, "Current samples count: " + samples);
        if(samples == 0) {
            return avgPacket;
        }
        for(CurrentPacket packet : currentBuffer.buffer) {
            avgPacket.current1 += packet.current1;
            avgPacket.current2 += packet.current2;
        }
        avgPacket.current1 /= samples;
        avgPacket.current2 /= samples;
        return avgPacket;
    }

    public BatteryPacket getAverageBatteryData() {
        batteryBuffer.update(System.currentTimeMillis());
        BatteryPacket avgPacket = new BatteryPacket();
        int samples = batteryBuffer.buffer.size();
        if(samples == 0) {
            return avgPacket;
        }
        for(BatteryPacket packet : batteryBuffer.buffer) {
            for(int i = 0; i < BatteryPacket.BATTERY_CELLS_COUNT; i++) {
                avgPacket.cellsVoltage[i] += packet.cellsVoltage[i];
            }
            avgPacket.CUBatteryVoltage += packet.CUBatteryVoltage;
            avgPacket.VESCBatteryVoltage += packet.VESCBatteryVoltage;
            avgPacket.ampHoursDrawn += packet.ampHoursDrawn;
            avgPacket.ampHoursCharged += packet.ampHoursCharged;
        }
        for(int i = 0; i < BatteryPacket.BATTERY_CELLS_COUNT; i++) {
            avgPacket.cellsVoltage[i] /= samples;
        }
        avgPacket.CUBatteryVoltage /= samples;
        avgPacket.VESCBatteryVoltage /= samples;
        avgPacket.ampHoursCharged /= samples;
        avgPacket.ampHoursDrawn /= samples;
        return avgPacket;
    }

    public TemperaturePacket getAverageTemperatureData() {
        temperatureBuffer.update(System.currentTimeMillis());
        TemperaturePacket avgPacket = new TemperaturePacket(System.currentTimeMillis(), 0, 0, 0, 0);
        int samples = temperatureBuffer.buffer.size();
        if(samples == 0) {
            return avgPacket;
        }
        for(TemperaturePacket packet : temperatureBuffer.buffer) {
            avgPacket.VESC1Temperature += packet.VESC1Temperature;
            avgPacket.VESC2Temperature += packet.VESC2Temperature;
            avgPacket.driversUnitCaseTemperature += packet.driversUnitCaseTemperature;
            avgPacket.powerSwitchTemperature += packet.powerSwitchTemperature;
        }
        avgPacket.VESC1Temperature /= samples;
        avgPacket.VESC2Temperature /= samples;
        avgPacket.driversUnitCaseTemperature /= samples;
        avgPacket.powerSwitchTemperature /= samples;
        return avgPacket;
    }

    public SpeedPacket getAverageSpeedData() {
        speedBuffer.update(System.currentTimeMillis());
        SpeedPacket avgPacket = new SpeedPacket(System.currentTimeMillis(), 0);
        int samples = speedBuffer.buffer.size();
        if(samples == 0) {
            return avgPacket;
        }
        for(SpeedPacket packet : speedBuffer.buffer) {
            avgPacket.speed += packet.speed;
        }
        avgPacket.speed /= samples;
        return avgPacket;
    }

    public void run() {
        Log.i(TAG, "Starting data manager thread");
        try {
            while(true) {
                //save data from individual containers to common data struct
                appendLog();
                //if x seconds elapsed, autosave
                long dt = System.currentTimeMillis() - lastAutosaveTimestamp;
                if(dt > autosaveInterval) {
                    performAutosave();
                    lastAutosaveTimestamp = System.currentTimeMillis();
                }
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Log.i(TAG, "Thread " + threadName + " interrupted");
        }
    }

    public void start() {
        t.start();
    }

    private void appendLog() {
        long t = System.currentTimeMillis();
        BigPacket packet = new BigPacket(t, getAverageCurrentData(), getAverageBatteryData(), getAverageTemperatureData(), getAverageSpeedData());
        logBuffer.add(packet);
    }

    private synchronized void performAutosave() {
        Log.i(TAG, "Starting autosave");
        if(!isExternalStorageWritable()) {
            Log.i(TAG, "External storage is not writable!");
            return;
        }
        Calendar c = Calendar.getInstance();
        String day = Integer.toString(c.get(Calendar.YEAR)) + "_" + Integer.toString(c.get(Calendar.MONTH) + 1) + "_" + Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        String time = Integer.toString(c.get(Calendar.HOUR_OF_DAY)) + "_" + Integer.toString(c.get(Calendar.MINUTE)) + "_" + Integer.toString(c.get(Calendar.SECOND));
        File path = getDocumentsStorageDir("EboardLog/" + day);
        Log.i(TAG, "Documents path= " + path.toString());
        File file = new File(path, "data_" + time + ".csv");
        Log.i(TAG, "File path= " + file.toString());
        file.setReadable(true, false);

        try {
            //outputStream = openFileOutput("", Context.MODE_PRIVATE);
            FileOutputStream outputStream = new FileOutputStream(file);
            String header = "TIMESTAMP,VESC1_CURRENT,VESC2_CURRENT,";
            for(int i = 0; i < BatteryPacket.BATTERY_CELLS_COUNT; i++) {
                header += "CELL" + (i + 1) + "_VOLTAGE,";
            }
            header += "CU_VOLTAGE,VESC_VOLTAGE,AMP_HOURS_DRAWN,AMP_GOURS_CHARGED,VESC1_TEMP,VESC2_TEMP,PS_TEMP,DUC_TEMP,SPEED\n";
            outputStream.write(header.getBytes());
            for(BigPacket packet : logBuffer) {
                String str = packet.toString() + "\n";
                outputStream.write(str.getBytes());
            }
            outputStream.close();
            Log.i(TAG, "Autosave ok");
        } catch(Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Autosave exception");
        }
        MediaScannerConnection.scanFile(mainActivity, new String[] {file.toString()}, null, null);
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private File getDocumentsStorageDir(String documentName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), documentName);
        if(!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }
}

