package com.cncoding.teazer.utilities.common;

import android.graphics.Color;

/**
 * Created by Prem$ on 3/1/2018.
 *
 * This is a color animator that has a correct interpolation in HSV 3D space.
 *
 * The algorithm makes a standard 3D math interpolation between
 * two 3D points and thus it allows to make a visually perfect color shift.
 *
 * The usual direct interpolation of HSV values makes stranger behavior,
 * in example you can see red color while you're interpolating from
 * blue to white.
 */
public class AnimatedColor {

    private static final float ERROR = 0.001f;
    private static float[] vector = new float[3];
    private static float[] hsv = new float[3];

    private float[] vector0;
    private float[] vector1;
    private int start;
    private int end;

    public AnimatedColor(int start, int end) {
        this.start = start;
        this.end = end;
        setVector0();
        setVector1();
    }

    private void setVector0() {
        this.vector0 = toVector(toHSV(start));
    }

    private void setVector1() {
        this.vector1 = toVector(toHSV(end));
    }

    public void setStart(int start) {
        this.start = start;
        setVector0();
    }

    public void setEnd(int end) {
        this.end = end;
        setVector1();
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int with(float delta) {
        if (delta <= 0)
            return start;
        if (delta >= 1)
            return end;
        return Color.HSVToColor(toHSV(move(vector0, vector1, delta)));
    }

    private static float[] move(float[] vector0, float[] vector1, float delta) {
        vector[0] = (vector1[0] - vector0[0]) * delta + vector0[0];
        vector[1] = (vector1[1] - vector0[1]) * delta + vector0[1];
        vector[2] = (vector1[2] - vector0[2]) * delta + vector0[2];
        return vector;
    }

    private static float[] toHSV(int color) {
        Color.colorToHSV(color, hsv);
        return hsv;
    }

    private static float[] toVector(float[] hsv) {
        double rad = Math.PI * hsv[0] / 180;
        vector[0] = (float) Math.cos(rad) * hsv[1];
        vector[1] = (float) Math.sin(rad) * hsv[1];
        vector[2] = hsv[2];
        return vector;
    }

    private static float[] toHSV(float[] vector) {
        hsv[1] = (float) Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1]);
        hsv[0] = hsv[1] < ERROR ? 0 : (float) (Math.atan2(vector[1] / hsv[1], vector[0] / hsv[1]) * 180 / Math.PI);
        if (hsv[0] < 0)
            hsv[0] += 360f;
        hsv[2] = vector[2];
        return hsv;
    }
}