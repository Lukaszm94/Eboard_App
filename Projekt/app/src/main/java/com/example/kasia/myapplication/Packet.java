package com.example.kasia.myapplication;

/**
 * Created by Luke on 2017-01-15.
 */

public class Packet {
    public long timestamp;

    @Override
    public String toString() {
        String str = Long.toString(timestamp);
        return str;
    }
}
