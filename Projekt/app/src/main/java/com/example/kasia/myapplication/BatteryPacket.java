package com.example.kasia.myapplication;

import java.util.Locale;

/**
 * Created by Luke on 2017-01-15.
 */

public class BatteryPacket extends Packet {
    public static final int BATTERY_CELLS_COUNT = 6;

    public double cellsVoltage[];
    public double CUBatteryVoltage;
    public double VESCBatteryVoltage;
    public double ampHoursDrawn;
    public double ampHoursCharged;

    BatteryPacket() {
        timestamp = System.currentTimeMillis();
        cellsVoltage = new double[BATTERY_CELLS_COUNT];
        timestamp = 0;
        CUBatteryVoltage = 0;
        VESCBatteryVoltage = 0;
        ampHoursCharged = 0;
        ampHoursDrawn = 0;
    }
    BatteryPacket(long t, double cellsV[], double cu, double vesc, double ahdrawn, double ahcharged) {
        timestamp = t;
        cellsVoltage = cellsV;
        CUBatteryVoltage = cu;
        VESCBatteryVoltage = vesc;
        ampHoursDrawn = ahdrawn;
        ampHoursCharged = ahcharged;
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < BATTERY_CELLS_COUNT; i++) {
            str += String.format(Locale.UK, "%.2f,", cellsVoltage[i]); // comma as delimeter
        }
        str += String.format(Locale.UK, "%.2f,%.2f,%.2f,%.2f", CUBatteryVoltage, VESCBatteryVoltage, ampHoursDrawn, ampHoursDrawn);
        return str;
    }
}
