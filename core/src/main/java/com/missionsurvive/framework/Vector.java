package com.missionsurvive.framework;

/**
 * Created by kuzmin on 03.05.18.
 */

public abstract class Vector {

    public int x, y;

    public abstract int getX();

    public abstract int getY();

    public abstract void setX(int x);

    public abstract void setY(int y);

    public abstract Vector set(int x, int y);

    public abstract Vector set(Vector other);
}
