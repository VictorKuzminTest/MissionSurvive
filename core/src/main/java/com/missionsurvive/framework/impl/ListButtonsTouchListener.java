package com.missionsurvive.framework.impl;

import com.badlogic.gdx.Gdx;
import com.missionsurvive.framework.Button;
import com.missionsurvive.framework.Listener;
import com.missionsurvive.framework.Observer;
import com.missionsurvive.geom.GeoHelper;

import java.util.ArrayList;

/**
 * Created by kuzmin on 30.04.18.
 */

public class ListButtonsTouchListener implements Listener {

    public static final int SCROLLING_THRESHOLD = 5;

    private ArrayList<ListButtons> lists = new ArrayList<ListButtons>();
    private ListButtons currentList;

    private float lastX, lastY;
    private float downX, downY; //using to track the threshold of scrolling the list

    private boolean scrolling;

    @Override
    public void attach(Observer observer) {
        lists.add((ListButtons)observer);
    }

    @Override
    public void trackEvents(float scaleX, float scaleY){
        if(Gdx.input.justTouched()){
            lastX = Gdx.input.getX(0) * scaleX;
            lastY =  Gdx.input.getY(0) * scaleY;
            downX = lastX;
            downY = lastY;
            getTouchDownEvent((int)lastX, (int)lastY);
        }
        else{
            if(Gdx.input.isTouched(0)){
                lastX = Gdx.input.getX(0) * scaleX;
                lastY =  Gdx.input.getY(0) * scaleY;
                float deltaX = Gdx.input.getDeltaX(0) * scaleX;
                float deltaY =  Gdx.input.getDeltaY(0) * scaleY;
                float thresholdX = downX - lastX;
                float thresholdY = downY - lastY;
                getTouchDraggedEvent((int)thresholdX, (int)thresholdY, deltaX, deltaY);
            }
            else{
                getTouchUpEvent((int)lastX, (int)lastY);
            }
        }
    }

    public void getTouchDownEvent(int eventX, int eventY) {
        scrolling = false;
        int numLists = lists.size();
        ListButtons list;
        for(int i = 0; i < numLists; i++){
            list = lists.get(i);
            if(GeoHelper.inBoundsVolume(eventX, eventY, list.getStartX(), list.getStartY(),
                    list.getListWidth(), list.getListHeight())){
                currentList = list;
                return;
            }
        }
    }

    /**
     * If The list it is not currently scrolling, we check for scrolling threshold from previous touch event.
     * Else, we just're just scrolling this list.
     * @param thresholdX threshold x coord to check
     * @param thresholdY threshold y coord to check
     * @param deltaX delta x for scrolling
     * @param deltaY delta y for scrolling
     */
    public void getTouchDraggedEvent(int thresholdX, int thresholdY, float deltaX, float deltaY) {
        if(currentList != null){
            if(!scrolling){
                if(Math.abs(thresholdX) > SCROLLING_THRESHOLD || Math.abs(thresholdY) > SCROLLING_THRESHOLD) {
                    scrolling = true;
                }
            }
            else{
                currentList.scrollButtons(deltaX, deltaY);
            }
        }
    }

    /**
     * If we are not currently scrolling the list, we go through the buttons inside this list, click it.
     * @param touchUpX
     * @param touchUpY
     */
    public void getTouchUpEvent(int touchUpX, int touchUpY) {
        if(currentList != null){
            if(GeoHelper.inBoundsVolume(touchUpX, touchUpY,
                    currentList.getStartX(), currentList.getStartY(),
                    currentList.getListWidth(),
                    currentList.getListHeight())){

                if(!scrolling){
                    int numButtons = currentList.getButtons().size();
                    for(int whichButton = 0; whichButton < numButtons; whichButton++){
                        Button button = currentList.getButtons().get(whichButton);

                        //if button is beyond current list, continue:
                        if(!GeoHelper.overlapRectangles(button.getStartX(), button.getStartY(),
                                button.getButtonWidth(), button.getButtonHeight(),
                                currentList.getStartX(), currentList.getStartY(),
                                currentList.getListWidth(), currentList.getListHeight())) {
                            continue;
                        }
                        else{
                            if(GeoHelper.inBoundsVolume(touchUpX, touchUpY,
                                    button.getStartX(), button.getStartY(),
                                    button.getButtonWidth(), button.getButtonHeight())){
                                button.onClick(true);
                            }
                        }
                    }
                }
            }
        }
        scrolling = false;
        currentList = null;
    }

    public boolean isScrolling(){
        return scrolling;
    }

}
