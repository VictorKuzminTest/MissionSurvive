package com.missionsurvive.framework;

import com.missionsurvive.objs.GameObject;

/**
 * Created by kuzmin on 03.05.18.
 */

public abstract class Decorator {

    private GameObject decoratedObject;

    public Decorator (GameObject decoratedObject){
        this.decoratedObject = decoratedObject;
    }

    public void drawObject(Graphics g, Pixmap[] pixmaps) {
        decoratedObject.drawObject(g, pixmaps, 0, 0, 0, 0);
    }


    public void update(float deltaTime){

    }
}
