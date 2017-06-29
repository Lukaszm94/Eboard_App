package com.example.kasia.myapplication;

/**
 * Created by Luke on 2017-01-18.
 */

public class LightsPacket {
    public byte frontBrightness;
    public byte frontBlinkingMode;
    public byte rearBrightness;
    public byte rearBlinkingMode;
    public byte reactToBraking;

    private final byte LIGHTS_PACKET_HEADER = 'l';
    private final byte PACKET_END_CHAR = ';';

    LightsPacket() {
        this((byte)0,(byte)0,(byte)0,(byte)0,(byte)0);
    }

    LightsPacket(byte frontBrightness, byte frontBlinkingMode, byte rearBrightness, byte rearBlinkingMode, byte reactToBraking) {
        this.frontBrightness = frontBrightness;
        this.frontBlinkingMode = frontBlinkingMode;
        this.rearBrightness = rearBrightness;
        this.rearBlinkingMode = rearBlinkingMode;
        this.reactToBraking = reactToBraking;
    }

    public byte[] toSerialPacket() {
        byte[] data = new byte[7];
        data[0] = LIGHTS_PACKET_HEADER;
        data[1] = frontBrightness;
        data[2] = frontBlinkingMode;
        data[3] = rearBrightness;
        data[4] = rearBlinkingMode;
        data[5] = reactToBraking;
        data[6] = PACKET_END_CHAR;
        return data;
    }
}
