package com.missionsurvive.framework.impl;

import com.missionsurvive.framework.Vector;

/**
 * Created by kuzmin on 03.05.18.
 */

public class Vector2 extends Vector {

    public static float TO_RADIANS = (1 / 180.0f) * (float) Math.PI;
    public static float TO_DEGREES = (1 / (float) Math.PI) * 180;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Vector2(Vector2 other) {
        this.x = other.x;
        this.y = other.y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public Vector set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public Vector set(Vector other) {
        this.x = other.x;
        this.y = other.y;
        return this;
    }

    public Vector2 add(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }
    public Vector2 add(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }
    public Vector2 sub(int x, int y) {
        this.x -= x;
        this.y -= y;
        return this;
    }
    public Vector2 sub(Vector2 other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vector2 mul(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }
}
