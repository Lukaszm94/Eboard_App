package com.example.kasia.myapplication;

import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by Luke on 2017-01-15.
 */

public class DataManager extends Thread {
    //containers
    private PacketBuffer<CurrentPacket> currentBuffer;
    private PacketBuffer<BatteryPacket> batteryBuffer;
    private PacketBuffer<TemperaturePacket> temperatureBuffer;
    private PacketBuffer<SpeedPacket> speedBuffer;
    long timestampOffset = 0;
    //buffers mutex
    Semaphore buffersMutex = new Semaphore(1);
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
        timestampOffset = System.currentTimeMillis();
        autosaveInterval = 120000;
        mainActivity = main;
    }

    public synchronized void newCurrentPacket(CurrentPacket packet) {
        if(acquireMutex()) {
            currentBuffer.add(packet);
            releaseMutex();
        }
    }

    public synchronized void newBatteryPacket(BatteryPacket packet) {
        if(acquireMutex()) {
            batteryBuffer.add(packet);
            releaseMutex();
        }
    }

    public synchronized void newTemperaturePacket(TemperaturePacket packet) {
        if(acquireMutex()) {
            temperatureBuffer.add(packet);
            releaseMutex();
        }
    }

    public synchronized void newSpeedPacket(SpeedPacket packet) {
        if(acquireMutex()) {
            speedBuffer.add(packet);
            releaseMutex();
        }
    }

    public CurrentPacket getAverageCurrentData() {
        CurrentPacket avgPacket = new CurrentPacket(System.currentTimeMillis() - timestampOffset, 0, 0);
        if(!acquireMutex()) {
            Log.i(TAG, "Unable to acquire mutex to calculate average current data");
            return avgPacket;
        } else {
            Log.i(TAG, "getAverageCurrentData, mutex acquired");
        }
        currentBuffer.update(System.currentTimeMillis());
        int samples = currentBuffer.buffer.size();
        //Log.i(TAG, "Current samples count: " + samples);
        if(samples == 0) {
            releaseMutex();
            return avgPacket;
        }
        for(CurrentPacket packet : currentBuffer.buffer) {
            avgPacket.current1 += packet.current1;
            avgPacket.current2 += packet.current2;
        }
        releaseMutex();
        avgPacket.current1 /= samples;
        avgPacket.current2 /= samples;
        return avgPacket;
    }

    public BatteryPacket getAverageBatteryData() {
        BatteryPacket avgPacket = new BatteryPacket();
        avgPacket.timestamp = System.currentTimeMillis() - timestampOffset;
        if(!acquireMutex()) {
            return avgPacket;
        }
        batteryBuffer.update(System.currentTimeMillis());
        int samples = batteryBuffer.buffer.size();
        if(samples == 0) {
            releaseMutex();
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
        releaseMutex();
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
        TemperaturePacket avgPacket = new TemperaturePacket(System.currentTimeMillis() - timestampOffset, 0, 0, 0, 0);
        if(!acquireMutex()) {
            return avgPacket;
        }
        temperatureBuffer.update(System.currentTimeMillis());
        int samples = temperatureBuffer.buffer.size();
        if(samples == 0) {
            releaseMutex();
            return avgPacket;
        }
        for(TemperaturePacket packet : temperatureBuffer.buffer) {
            avgPacket.VESC1Temperature += packet.VESC1Temperature;
            avgPacket.VESC2Temperature += packet.VESC2Temperature;
            avgPacket.driversUnitCaseTemperature += packet.driversUnitCaseTemperature;
            avgPacket.powerSwitchTemperature += packet.powerSwitchTemperature;
        }
        releaseMutex();
        avgPacket.VESC1Temperature /= samples;
        avgPacket.VESC2Temperature /= samples;
        avgPacket.driversUnitCaseTemperature /= samples;
        avgPacket.powerSwitchTemperature /= samples;
        return avgPacket;
    }

    public SpeedPacket getAverageSpeedData() {
        SpeedPacket avgPacket = new SpeedPacket(System.currentTimeMillis() - timestampOffset, 0);
        if(!acquireMutex()) {
            return avgPacket;
        }
        speedBuffer.update(System.currentTimeMillis());
        int samples = speedBuffer.buffer.size();
        if(samples == 0) {
            releaseMutex();
            return avgPacket;
        }
        for(SpeedPacket packet : speedBuffer.buffer) {
            avgPacket.speed += packet.speed;
        }
        releaseMutex();
        avgPacket.speed /= samples;
        return avgPacket;
    }

    public CurrentPacket getNewestCurrentPacket() {
        currentBuffer.update(System.currentTimeMillis());
        if(currentBuffer.size() == 0) {
            return new CurrentPacket(System.currentTimeMillis(), 0, 0);
        }
        return currentBuffer.getLast();
    }

    public BatteryPacket getNewestBatteryPacket() {
        batteryBuffer.update(System.currentTimeMillis());
        if(batteryBuffer.size() == 0) {
            return new BatteryPacket();
        }
        return batteryBuffer.getLast();
    }

    public TemperaturePacket getNewestTemperaturePacket() {
        temperatureBuffer.update(System.currentTimeMillis());
        if(temperatureBuffer.size() == 0) {
            return new TemperaturePacket(System.currentTimeMillis(),0,0,0,0);
        }
        return temperatureBuffer.getLast();
    }

    public SpeedPacket getNewestSpeedPacket() {
        speedBuffer.update(System.currentTimeMillis());
        if(speedBuffer.size() == 0) {
            return new SpeedPacket();
        }
        return speedBuffer.getLast();
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
        /*if(!acquireMutex()) {
            Log.i(TAG, "Unable to append log, cannot acquire mutex");
            return;
        }*/
        long t = System.currentTimeMillis();
        BigPacket packet = new BigPacket(t, getAverageCurrentData(), getAverageBatteryData(), getAverageTemperatureData(), getAverageSpeedData());
        logBuffer.add(packet);
        //releaseMutex();
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

    private boolean acquireMutex() {
        try {
            if(buffersMutex.tryAcquire(10, TimeUnit.MILLISECONDS)) {
                return true;
            }
        } catch(Exception e) {
            Log.i(TAG, "Acquire mutex exception");
        }
        Log.i(TAG, "acquireMutex: failed");
        return false;
    }

    private void releaseMutex() {
        buffersMutex.release();
    }
}

