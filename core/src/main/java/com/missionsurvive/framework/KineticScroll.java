package com.missionsurvive.framework;

import com.missionsurvive.framework.impl.ListButtons;

import java.util.List;

/**
 * Created by kuzmin on 02.06.18.
 */
public class KineticScroll {
    public static final float DELTA_TRACE_TICK = 0.05f;
    public static final float BRAKING_SPEED = 0.5f;
    public static final float SCROLLING_TICK = 0.03f;

    private ListButtons currentList;

    private float lastX;
    private float lastY;
    private float tracingTickTime;
    private float scrollingTickTime;
    private float speedX;
    private float speedY;

    public void onTouchDownList(ListButtons currentList, float downX, float downY){
        this.currentList = currentList;
        lastX = downX;
        lastY = downY;
        speedX = 0;
        speedY = 0;
    }

    public void scrolling(float delta){
        if(currentList != null){
            if(speedX != 0 && speedY != 0){
                scrollingTickTime += delta;
                while(scrollingTickTime > SCROLLING_TICK){
                    scrollingTickTime -= SCROLLING_TICK;

                    brakingX();
                    brakingY();
                    currentList.scrollButtons(speedX, speedY);
                }
            }
            else{
                currentList = null;
                scrollingTickTime = 0;
            }
        }
    }

    public void traceTouch(float deltaTime, float touchX, float touchY) {
        tracingTickTime += deltaTime;
        while(tracingTickTime >= DELTA_TRACE_TICK){
            tracingTickTime -= DELTA_TRACE_TICK;

            speedX = touchX - lastX;
            speedY = touchY - lastY;

            lastX = touchX;
            lastY = touchY;
        }
    }

    public void setSpeedXY(float speedX, float speedY){
        this.speedX = speedX;
        this.speedY = speedY;
    }

    public float getSpeedX(){
        return speedX;
    }

    public float getSpeedY(){
        return speedY;
    }

    public void brakingX() {
        if(speedX < 0){
            speedX += BRAKING_SPEED;
            if(speedX >= 0){
                speedX = 0;
            }
        }
        else if(speedX > 0){
            speedX -= BRAKING_SPEED;
            if(speedX <= 0){
                speedX = 0;
            }
        }
    }

    public void brakingY() {
        if(speedY < 0){
            speedY += BRAKING_SPEED;
            if(speedY >= 0){
                speedY = 0;
            }
        }
        else if(speedY > 0){
            speedY -= BRAKING_SPEED;
            if(speedY <= 0){
                speedY = 0;
            }
        }
    }

}
