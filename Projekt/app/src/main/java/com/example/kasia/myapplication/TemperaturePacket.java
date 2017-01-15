package com.example.kasia.myapplication;

import java.util.Locale;

/**
 * Created by Luke on 2017-01-15.
 */

public class TemperaturePacket extends Packet {
    public double VESC1Temperature;
    public double VESC2Temperature;
    public double powerSwitchTemperature;
    public double driversUnitCaseTemperature;

    TemperaturePacket(long t, double vesc1, double vesc2, double ps, double duc) {
        timestamp = t;
        VESC1Temperature = vesc1;
        VESC2Temperature = vesc2;
        powerSwitchTemperature = ps;
        driversUnitCaseTemperature = duc;
    }

    @Override
    public String toString() {
        String str = "";
        str += String.format(Locale.UK, "%.2f,%.2f,%.2f,%.2f", VESC1Temperature, VESC2Temperature, powerSwitchTemperature, driversUnitCaseTemperature);
        return str;
    }
}
