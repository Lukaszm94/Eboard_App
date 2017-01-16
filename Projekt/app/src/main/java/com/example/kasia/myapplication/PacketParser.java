package com.example.kasia.myapplication;

import android.util.Log;

import java.nio.ByteBuffer;

/**
 * Created by Luke on 2017-01-15.
 */

public class PacketParser {

    private ByteBuffer buffer;
    private final String TAG = "PacketParser";
    private DataManager dataManager = null;

    private static final char CURRENT_PACKET_HEADER = 'a';
    private static final int CURRENT_PACKET_DATA_SIZE = 4;
    private static final char TEMPERATURE_PACKET_HEADER = 'b';
    private static final int TEMPERATURE_PACKET_DATA_SIZE = 8;
    private static final char BATTERY_PACKET_HEADER = 'c';
    private static final int BATTERY_PACKET_DATA_SIZE = (2*BatteryPacket.BATTERY_CELLS_COUNT + 9);
    private static final char SPEED_PACKET_HEADER = 'd';
    private static final int SPEED_PACKET_DATA_SIZE = 2;

    private static final char PACKET_END_CHAR = ';';
    private static final int PACKET_OVERHEAD_SIZE = 2;


    PacketParser() {
        buffer = ByteBuffer.allocate(0);
    }

    public void setDataManager(DataManager manager) {
        dataManager = manager;
    }

    public void appendBytes(byte[] data) {
        //Log.i(TAG, "Append bytes: buffer capacity: " + buffer.capacity() + ", new data length= " + data.length);
        ByteBuffer newData = ByteBuffer.wrap(data);
        buffer.clear();
        newData.clear();
        ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() + newData.capacity());
        newBuffer = newBuffer.put(buffer);
        newBuffer = newBuffer.put(newData);
        buffer = newBuffer;
        //Log.i(TAG, "After appending: buffer capacity= " + buffer.capacity());
    }
    // returns true if managed to parse some data
    public boolean parse() {
        if(buffer == null) {
            return false;
        }
        if(buffer.capacity() == 0) {
            return false;
        }
        buffer.position(0);
        char firstChar = (char) buffer.get(0);
        int bufferSize = buffer.capacity();
        if(firstChar == CURRENT_PACKET_HEADER) {
            if(bufferSize > (CURRENT_PACKET_DATA_SIZE + PACKET_OVERHEAD_SIZE - 1)) {
                char endChar = (char) buffer.get(CURRENT_PACKET_DATA_SIZE + PACKET_OVERHEAD_SIZE - 1);
                if(endChar != PACKET_END_CHAR) {
                    removeDataFromBufferUntil(PACKET_END_CHAR);
                    Log.i(TAG, "No end char found");
                    return false;
                }
                // extract data from packet
                CurrentPacket packet = extractCurrentPacketFromBuffer();
                //Log.i(TAG, "Current1= " + packet.current1 + ", current2= " + packet.current2);
                if(dataManager != null) {
                    dataManager.newCurrentPacket(packet);
                }
                removeDataFromBufferUntil(PACKET_END_CHAR);
                return true;
            } else {
                //Log.i(TAG, "Waiting for more data");
                return false;
            }
        } else if(firstChar == TEMPERATURE_PACKET_HEADER) {
            if(bufferSize > (TEMPERATURE_PACKET_DATA_SIZE + PACKET_OVERHEAD_SIZE - 1)) {
                char endChar = (char) buffer.get(TEMPERATURE_PACKET_DATA_SIZE + PACKET_OVERHEAD_SIZE - 1);
                if(endChar != PACKET_END_CHAR) {
                    removeDataFromBufferUntil(PACKET_END_CHAR);
                    Log.i(TAG, "No end char found");
                    return false;
                }
                // extract data from packet
                TemperaturePacket packet = extractTemperaturePacketFromBuffer();
                //Log.i(TAG, "VESC1= " + packet.VESC1Temperature + ", VESC2= " + packet.VESC2Temperature + ", PS= " + packet.powerSwitchTemperature + ", DUC= " + packet.driversUnitCaseTemperature);
                if(dataManager != null) {
                    dataManager.newTemperaturePacket(packet);
                }
                removeDataFromBufferUntil(PACKET_END_CHAR);
                return true;
            } else {
                //Log.i(TAG, "Waiting for more data");
                return false;
            }
        } else if(firstChar == BATTERY_PACKET_HEADER) {
            if(bufferSize > (BATTERY_PACKET_DATA_SIZE + PACKET_OVERHEAD_SIZE - 1)) {
                char endChar = (char) buffer.get(BATTERY_PACKET_DATA_SIZE + PACKET_OVERHEAD_SIZE - 1);
                if(endChar != PACKET_END_CHAR) {
                    removeDataFromBufferUntil(PACKET_END_CHAR);
                    Log.i(TAG, "No end char found");
                    return false;
                }
                // extract data from packet
                BatteryPacket packet = extractBatteryPacketFromBuffer();
                //Log.i(TAG, "CUVoltage= " + packet.CUBatteryVoltage + ", VESCVoltage= " + packet.VESCBatteryVoltage + ", Ahdrawn= " + packet.ampHoursDrawn);
                if(dataManager != null) {
                    dataManager.newBatteryPacket(packet);
                }
                removeDataFromBufferUntil(PACKET_END_CHAR);
                return true;
            } else {
                //Log.i(TAG, "Waiting for more data");
                return false;
            }
        } else if(firstChar == SPEED_PACKET_HEADER) {
            if(bufferSize > (SPEED_PACKET_DATA_SIZE + PACKET_OVERHEAD_SIZE - 1)) {
                char endChar = (char) buffer.get(SPEED_PACKET_DATA_SIZE + PACKET_OVERHEAD_SIZE - 1);
                if(endChar != PACKET_END_CHAR) {
                    removeDataFromBufferUntil(PACKET_END_CHAR);
                    Log.i(TAG, "No end char found");
                    return false;
                }
                // extract data from packet
                SpeedPacket packet = extractSpeedPacketFromBuffer();
                //Log.i(TAG, "Speed= " + packet.speed);
                if(dataManager != null) {
                    dataManager.newSpeedPacket(packet);
                }
                removeDataFromBufferUntil(PACKET_END_CHAR);
                return true;
            } else {
                //Log.i(TAG, "Waiting for more data");
                return false;
            }
        }
        removeDataFromBufferUntil(PACKET_END_CHAR);
        return false;
    }

    private void removeDataFromBufferUntil(char c) {
        int index = findCharInBuffer(c);
        if(index < 0) { // char not found
            buffer = ByteBuffer.allocate(0);
            return;
        }
        if(index == (buffer.capacity() - 1)) { // c is last char in the buffer
            buffer = ByteBuffer.allocate(0);
            return;
        }
        buffer = popFront(index + 1);
    }

    private int findCharInBuffer(char c) {
        for(int i = 0; i < buffer.capacity(); i++) {
            char currentChar = (char) buffer.get(i);
            if(currentChar == c) {
                return i;
            }
        }
        return -1;
    }

    private ByteBuffer popFront(int bytes) {
        int newLength = buffer.capacity() - bytes;
        if(newLength <= 0) {
            return ByteBuffer.allocate(0);
        }
        byte[] rawData = buffer.array();
        byte[] newData = new byte[newLength];
        for(int i = bytes, j = 0; i < rawData.length; i++, j++) {
            newData[j] = rawData[i];
        }
        ByteBuffer newBuffer = ByteBuffer.allocate(newLength);
        newBuffer.put(newData);
        return newBuffer;
    }

    private CurrentPacket extractCurrentPacketFromBuffer() {
        buffer.position(1);
        short current1 = buffer.getShort();
        short current2 = buffer.getShort();
        buffer.clear();
        CurrentPacket packet = new CurrentPacket(System.currentTimeMillis(), current1 / 10.0, current2 / 10.0);
        return packet;
    }

    private TemperaturePacket extractTemperaturePacketFromBuffer() {
        buffer.position(1);
        short vesc1 = buffer.getShort();
        short vesc2 = buffer.getShort();
        short ps = buffer.getShort();
        short duc = buffer.getShort();
        buffer.clear();
        TemperaturePacket packet = new TemperaturePacket(System.currentTimeMillis(), vesc1 / 100.0, vesc2 / 100.0, ps / 100.0, duc / 100.0);
        return packet;
    }

    private BatteryPacket extractBatteryPacketFromBuffer() {
        BatteryPacket packet = new BatteryPacket();
        buffer.position(1);
        for(int i = 0; i < BatteryPacket.BATTERY_CELLS_COUNT; i++) {
            packet.cellsVoltage[i] = buffer.getShort() / 100.0;
        }
        packet.CUBatteryVoltage = buffer.getShort() / 100.0;
        packet.VESCBatteryVoltage = buffer.getShort() / 100.0;
        packet.ampHoursDrawn = buffer.getShort() / 1000.0;
        packet.ampHoursCharged = buffer.getShort() / 1000.0;
        packet.timestamp = System.currentTimeMillis();
        buffer.clear();
        return packet;
    }

    private SpeedPacket extractSpeedPacketFromBuffer() {
        SpeedPacket packet = new SpeedPacket();
        buffer.position(1);
        packet.speed = buffer.getShort() / 100.0;
        packet.timestamp = System.currentTimeMillis();
        buffer.clear();
        return packet;
    }
}
