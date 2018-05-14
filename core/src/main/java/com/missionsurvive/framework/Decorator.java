package com.missionsurvive.framework;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.objs.GameObject;

/**
 * Created by kuzmin on 03.05.18.
 */

public abstract class Decorator {

    private GameObject decoratedObject;

    public Decorator (GameObject decoratedObject){
        this.decoratedObject = decoratedObject;
    }

    public void drawObject(SpriteBatch batch) {
        decoratedObject.drawObject(batch, 0, 0, 0, 0);
    }


    public void update(float deltaTime){

    }
}
