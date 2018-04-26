package com.missionsurvive.framework;

import com.missionsurvive.framework.TouchControl;

/**
 * Created by kuzmin on 25.04.18.
 */

public interface Button {

    public int isTouching(TouchControl touchControl);

    public void touch();

    public void drawButton();

    //drawing button inside list of buttons: it disappears beyond list borders.
    public void drawButton(int offsetStartX, int offsetStartY, int offsetWidth, int offsetHeight);

    public void setObject(Object object);

    public int getStartX();

    public int getStartY();

    public int getButtonWidth();

    public int getButtonHeight();

    public void setStartX(int x);

    public void setStartY(int y);
}
