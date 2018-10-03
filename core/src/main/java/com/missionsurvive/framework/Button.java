package com.missionsurvive.framework;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.scenarios.commands.Command;

/**
 * Created by kuzmin on 25.04.18.
 */

public interface Button{

    public boolean onClick(boolean onClick);

    public void drawButton(SpriteBatch batch);

    //drawing button inside list of buttons: it disappears beyond list borders.
    public void drawButton(SpriteBatch batch,
                           int offsetStartX, int offsetStartY, int offsetWidth, int offsetHeight);

    public int getStartX();

    public int getStartY();

    public int getButtonWidth();

    public int getButtonHeight();

    public void setStartX(float x);

    public void setStartY(float y);

    public void setCommand(Command command);

    public Command getCommand();

    public void setState(int state);
}
