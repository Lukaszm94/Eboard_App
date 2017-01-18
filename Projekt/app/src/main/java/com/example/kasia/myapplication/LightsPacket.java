package com.example.kasia.myapplication;

/**
 * Created by Luke on 2017-01-18.
 */

public class LightsPacket {
    public byte brightness;
    public byte reactToBraking;
    public byte blinkingMode;

    private final byte LIGHTS_PACKET_HEADER = 'l';
    private final byte PACKET_END_CHAR = ';';

    LightsPacket() {
        this((byte)0,(byte)0,(byte)0);
    }

    LightsPacket(byte brightness, byte reactToBraking, byte blinkingMode) {
        this.brightness = brightness;
        this.reactToBraking = reactToBraking;
        this.blinkingMode = blinkingMode;
    }

    public byte[] toSerialPacket() {
        byte[] data = new byte[5];
        data[0] = LIGHTS_PACKET_HEADER;
        data[1] = brightness;
        data[2] = reactToBraking;
        data[3] = blinkingMode;
        data[4] = PACKET_END_CHAR;
        return data;
    }
}
