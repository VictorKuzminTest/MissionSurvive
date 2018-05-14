package com.missionsurvive.framework.impl;

import com.missionsurvive.framework.Animation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuzmin on 03.05.18.
 */

public class Animation2 implements Animation {

    private List<Animation> animation;

    private int assetX;
    private int assetY;
    private int currentFrame;
    private int setOfFrames;

    public Animation2(int assetX, int assetY,
                      int spriteWidth, int spriteHeight){
        this.assetX = assetX;
        this.assetY = assetY;
        animation = new ArrayList<Animation>();
    }

    @Override
    public void animate(int startFrame, int endFrame) {
        if(!(currentFrame == endFrame)){
            int step = (endFrame - startFrame) > 0 ? 1 : -1;
            currentFrame += step;
        }
    }

    @Override
    public int getCurrentFrame() {
        return currentFrame;
    }

    @Override
    public int getSetOfFrames() {
        return setOfFrames;
    }

    @Override
    public void setSetOfFrames(int setOfFrames) {
        this.setOfFrames = setOfFrames;
    }

    @Override
    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    @Override
    public int[] getActionFrames() {
        return null;
    }

    @Override
    public int getX() {
        return assetX;
    }

    @Override
    public int getY() {
        return assetY;
    }

    @Override
    public void addChild(Animation animation) {
        this.animation.add(animation);
    }

    @Override
    public List<Animation> getChildren() {
        return animation;
    }
}
