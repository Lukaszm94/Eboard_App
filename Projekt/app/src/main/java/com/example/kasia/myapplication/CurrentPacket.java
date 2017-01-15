package com.example.kasia.myapplication;

import java.util.Locale;

/**
 * Created by Luke on 2017-01-15.
 */

public class CurrentPacket extends  Packet {
    public double current1;
    public double current2;

    CurrentPacket(long timestamp, double c1, double c2) {
        this.timestamp = timestamp;
        current1 = c1;
        current2 = c2;
    }

    @Override
    public String toString() {
        String str = "";
        str += String.format(Locale.UK, "%.2f,%.2f", current1, current2);
        return str;
    }
}
