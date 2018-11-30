package com.missionsurvive.framework;

public interface Listener {

    public void attach(Observer observer);

    /**
     * Tracking touch events: touchDown, touchDragged, touchUp.
     * @param deltaTime
     * @param scaleX  scale factors to fit real screen size to logic screen size
     * @param scaleY
     */
    public void trackEvents(float deltaTime, float scaleX, float scaleY);

    public int getState();
}
