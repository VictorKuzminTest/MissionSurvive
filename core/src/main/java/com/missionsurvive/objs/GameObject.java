package com.missionsurvive.objs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.Decorator;

/**
 * Created by kuzmin on 01.05.18.
 */

public interface GameObject {

    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY);

    public void drawObject(SpriteBatch batch, int screenX, int screenY);

    public boolean onTouch();

    public void update(float deltaTime);

    public Decorator getDecorator();
}
