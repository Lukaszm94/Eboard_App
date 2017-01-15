package com.example.kasia.myapplication;

/**
 * Created by Luke on 2017-01-15.
 */

public class BigPacket extends Packet {
    public CurrentPacket current;
    public BatteryPacket battery;
    public TemperaturePacket temperature;
    public SpeedPacket speed;

    BigPacket(long t, CurrentPacket c, BatteryPacket b, TemperaturePacket temp, SpeedPacket s) {
        timestamp = t;
        current = c;
        battery = b;
        temperature = temp;
        speed = s;
    }

    public String toString() {
        String str = Long.toString(timestamp) + "," + current.toString() + "," + battery.toString() + "," + temperature.toString() + "," + speed.toString();
        return str;
    }
}
