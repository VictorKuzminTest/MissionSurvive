package com.missionsurvive.geom;

/**
 * Created by kuzmin on 03.05.18.
 */

public class Hitbox {

    private int hitboxWidth;
    private int hitboxHeight;
    /**
     * "half...", because basically we need to know the half of "real" object width-height
     * to calculate some bounding (colliding) points
     */
    private int halfHeight;
    private int halfWidth;
    private int centerX, centerY, top, bottom, left, right;
    private int offsetX, offsetY; //XY offset for hitbox (offset from sprite coordinates on screen).

    public Hitbox(int x, int y, int hitboxWidth, int hitboxHeight, int offsetX, int offsetY){
        setHitbox(x, y, hitboxWidth, hitboxHeight, offsetX, offsetY);
    }

    public void setHitbox(int x, int y, int hitboxWidth, int hitboxHeight, int offsetX, int offsetY){
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        halfHeight = hitboxHeight / 2;
        halfWidth = hitboxWidth / 2;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        setPos(x, y);
    }

    /**
     * Sets the position of objects hitbox in space (calculates its main coordinates).
     * @param x X coordinate (absolute) to draw on screen.
     * @param y Y coordinate (absolute) to draw on screen.
     */
    public void setPos(int x, int y){
        left = x + offsetX;
        top = y + offsetY;
        bottom = top + hitboxHeight;
        right = left + hitboxWidth;

        centerX = left + halfWidth;
        centerY = top + halfHeight;
    }

    public int getRight(){
        return right;
    }

    public int getLeft(){
        return left;
    }

    public int getTop(){
        return top;
    }

    public int getBottom(){
        return bottom;
    }

    public int getCenterX(){
        return centerX;
    }

    public int getCenterY(){
        return centerY;
    }

    public int getHitboxWidth(){
        return hitboxWidth;
    }

    public int getHitboxHeight(){
        return hitboxHeight;
    }

    public int getHalfWidth(){
        return halfWidth;
    }

    public int getHalfHeight(){
        return halfHeight;
    }
}
