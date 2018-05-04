package com.missionsurvive.framework.impl;

import com.missionsurvive.framework.Decorator;
import com.missionsurvive.objs.GameObject;

/**
 * Created by kuzmin on 03.05.18.
 */

public class BlinkingDecorator extends Decorator {

    private float blinkingTick = 0.2f;
    private float blinkingTickTime = 0;

    private boolean blink = true;

    public BlinkingDecorator(GameObject decoratedObject){
        super(decoratedObject);
    }

    @Override
    public void drawObject(Graphics g, Pixmap[] pixmaps) {
        if(blink){
            super.drawObject(g, pixmaps);
        }
    }

    @Override
    public void update(float deltaTime) {
        blinking(deltaTime);
    }

    public void blinking(float deltaTime) {
        blinkingTickTime += deltaTime;
        while(blinkingTickTime > blinkingTick){
            blinkingTickTime -= blinkingTick;

            blink = blink == true ? false : true;
        }
    }

    public void setBlinkingTick(float blinkingTick){
        this.blinkingTick = blinkingTick;
    }
}
