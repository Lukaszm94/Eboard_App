package com.example.kasia.myapplication;

import java.util.Locale;

/**
 * Created by Luke on 2017-01-15.
 */

public class SpeedPacket extends Packet {
    public double speed; // in m/s
    SpeedPacket() {
        timestamp = 0;
        speed = 0;
    }
    SpeedPacket(long t, double s) {
        timestamp = t;
        speed = s;
    }

    @Override
    public String toString() {
        String str = "";
        str += String.format(Locale.UK, "%.2f", speed);
        return str;
    }
}
