package com.example.kasia.myapplication;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by Luke on 2017-01-15.
 */

public class PacketBuffer<T extends Packet> {
    public List<T> buffer;
    Semaphore mutex = new Semaphore(1);
    long maxPacketAge; // in ms

    private final String TAG = "PacketBuffer";

    PacketBuffer() {
        buffer = Collections.synchronizedList(new ArrayList<T>());
        maxPacketAge = 1000;
    }

    public void setMaxPacketAge(long newAge) {
        maxPacketAge = newAge;
        update(System.currentTimeMillis());
    }

    public synchronized void add(T data) {
        try {
            if(mutex.tryAcquire(10, TimeUnit.MILLISECONDS)) {
                buffer.add(data);
            }
            mutex.release();
        } catch(Exception e) {
            Log.i(TAG, "add exception");
        }
    }

    public synchronized int size() {
        return buffer.size();
    }

    public synchronized T getLast() {
        return buffer.get(buffer.size() - 1);
    }

    //removes old packets
    public synchronized void update(long currentTimestamp) {
        try {
            if (mutex.tryAcquire(10, TimeUnit.MILLISECONDS)) {
                List<T> copy = new ArrayList<T>(buffer);
                for (T packet : copy) {
                    long packetAge = currentTimestamp - packet.timestamp;
                    if (packetAge > maxPacketAge) {
                        buffer.remove(packet);
                    } /*else {
                        break;
                    }*/
                }
                mutex.release();
            }
        } catch(Exception e){
            Log.i(TAG, "update exception: " + e.getCause());
        }
    }
}
