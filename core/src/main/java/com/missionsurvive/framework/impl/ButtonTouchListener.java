package com.missionsurvive.framework.impl;

import com.badlogic.gdx.Gdx;
import com.missionsurvive.framework.Button;
import com.missionsurvive.framework.Listener;
import com.missionsurvive.framework.Observer;
import com.missionsurvive.geom.GeoHelper;

import java.util.ArrayList;

/**
 * Created by kuzmin on 26.04.18.
 */

public class ButtonTouchListener implements Listener{

    public static final int STATE_NONE = 0;
    public static final int STATE_DOWN = 1;
    public static final int STATE_DRAGGED_INSIDE = 2;
    public static final int STATE_DRAGGED_OUTSIDE = 3;
    public static final int STATE_UP_INSIDE = 4;
    public static final int STATE_UP_OUTSIDE = 5;

    private ArrayList<Button> buttons = new ArrayList<Button>();
    private Button currentButton;

    private int state;
    private float lastX, lastY;

    @Override
    public void attach(Observer observer) {
        buttons.add((Button)observer);
    }

    /**
     * Tracking touch events: touchDown, touchDragged, touchUp.
     */
    public void trackEvents(float scaleX, float scaleY){
        if(Gdx.input.justTouched()){
            lastX = Gdx.input.getX(0) * scaleX;
            lastY =  Gdx.input.getY(0) * scaleY;
            getTouchDownEvent((int)lastX, (int)lastY);
        }
        else{
            if(Gdx.input.isTouched(0)){
                lastX = Gdx.input.getX(0) * scaleX;
                lastY =  Gdx.input.getY(0) * scaleY;
                getTouchDraggedEvent((int)lastX, (int)lastY);
            }
            else{
                getTouchUpEvent((int)lastX, (int)lastY);
            }
        }
    }

    public void getTouchDownEvent(int eventX, int eventY) {
        state = STATE_NONE;
        int numButtons = buttons.size();
        Button button;
        for(int i = 0; i < numButtons; i++){
            button = buttons.get(i);
            if(GeoHelper.inBoundsVolume(eventX, eventY, button.getStartX(), button.getStartY(),
                    button.getButtonWidth(), button.getButtonHeight())){
                currentButton = button;
                state = STATE_DOWN;
                return;
            }
        }
    }

    public void getTouchDraggedEvent(int touchGraggedX, int touchDraggedY) {
        if(currentButton != null){
            if(GeoHelper.inBoundsVolume(touchGraggedX, touchDraggedY,
                    currentButton.getStartX(), currentButton.getStartY(),
                    currentButton.getButtonWidth(),
                    currentButton.getButtonHeight())){
                state = STATE_DRAGGED_INSIDE;
            }
            else{
                state = STATE_DRAGGED_OUTSIDE;
            }
        }
    }

    public void getTouchUpEvent(int touchUpX, int touchUpY) {
        if(currentButton != null){
            if(GeoHelper.inBoundsVolume(touchUpX, touchUpY,
                    currentButton.getStartX(), currentButton.getStartY(),
                    currentButton.getButtonWidth(),
                    currentButton.getButtonHeight())){
                state = STATE_UP_INSIDE;
                currentButton.onClick(true);
            }
            else{
                state = STATE_UP_OUTSIDE;
            }
        }
        currentButton = null;
    }

    public int getState(){
        return state;
    }

    public Button getCurrentButton(){
        return currentButton;
    }

    public ArrayList<Button> getButtons(){
        return buttons;
    }

}
