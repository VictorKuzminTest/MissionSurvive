package com.missionsurvive.scenarios;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.TouchControl;

/**
 * Created by kuzmin on 04.05.18.
 */

public interface ControlScenario {

    public List<ControlPanel> getControlPanels();

    public void setControlPanels();

    public void drawPanels(SpriteBatch batch);

    public void touchPanels(TouchControl touchControl, Object object);

    public void touchingPanels(boolean touch);

    public boolean isTouchingPanels();

    public void action(Object object, int actionParameter);
}
