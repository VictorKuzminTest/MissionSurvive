package com.missionsurvive.framework.impl;

import com.badlogic.gdx.graphics.Pixmap;
import com.missionsurvive.framework.Animation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuzmin on 03.05.18.
 */

public class ObjAnimation implements Animation {

    public static final int FORTH = 0, BACK = 1;

    private List<Animation> animation;

    private int setOfFrames;
    private int currentFrame; //we also need to keep track of the current frame.
    private int[] actionFrames;  //an array that contains an info about hero's actions and quantity of action frames.
    private int direction; //current direction to animate set frames.

    public ObjAnimation(int[] actionFrames, int spriteWidth, int spriteHeight){
        currentFrame = 0;

        this.actionFrames = actionFrames;
        animation = new ArrayList<Animation>();
    }

    @Override
    public void setSetOfFrames(int setOfFrames){
        this.setOfFrames = setOfFrames;
    }

    @Override
    public int getSetOfFrames(){
        return setOfFrames;
    }

    /**
     * Automatic set of current animation frame.
     */
    public void nextFrame(){
        currentFrame++;

        if(currentFrame > actionFrames[setOfFrames] - 1){
            currentFrame = 0;
        }
    }

    @Override
    public void setCurrentFrame(int currentFrame){ //РУЧНАЯ УСТАНОВКА КОНКРЕТНОГО ФРЕЙМА АНИМАЦИИ.
        this.currentFrame = currentFrame;
    }


    @Override
    public int getCurrentFrame(){
        return currentFrame;
    }


    /**
     * This is universal method for animation,
     * when you choose the range of frames inside the setOfFrames to animate current action.
     * @param startFrame
     * @param numFrames
     */
    @Override
    public void animate(int startFrame, int numFrames){
        currentFrame++;
        if(currentFrame > startFrame + (numFrames - 1)) { //if next frame of animation is greater then number of animation frames for current action...
            this.currentFrame = startFrame;
        }
    }


    /**
     * Universal method for back and forth animation.
     * @param startFrame of animation
     * @param numFrames  to animate
     * @param step  num frames to switch inside set of frames(usually it is 1)
     */
    public void animateBackAndForth(int startFrame, int numFrames, int step){
        switch (direction) {
            case BACK: currentFrame -= step;
                if(currentFrame < startFrame){
                    currentFrame = startFrame + step;
                    direction = FORTH;
                }
                break;
            case FORTH: currentFrame += step;
                int endFrame = startFrame + (numFrames - 1);
                if(currentFrame > endFrame){
                    currentFrame = endFrame - step;
                    direction = BACK;
                }
                break;
        }
    }

    /**
     * if next frame of animation is not greater then number of animation frames for current action,
     * than we skip frames.
     * @param startFrame
     * @param numFrames
     */
    public void animateOneTime(int startFrame, int numFrames){
        if(!(currentFrame >= startFrame + (numFrames - 1))) {
            currentFrame++;
        }
    }

    @Override
    public int[] getActionFrames(){
        return actionFrames;
    }


    public void setDirection(int direction){
        this.direction = direction;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
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
