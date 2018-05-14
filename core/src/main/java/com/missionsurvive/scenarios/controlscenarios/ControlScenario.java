package com.missionsurvive.scenarios.controlscenarios;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.framework.TouchControl;

import java.util.List;

/**
 * Created by kuzmin on 04.05.18.
 */

public interface ControlScenario {

    public List<ControlPanel> getControlPanels();

    public void setControlPanels();

    public void drawPanels(SpriteBatch batch);

    public void onTouchPanels(float scaleX, float scaleY);

    public void touchingPanels(boolean touch);

    public boolean isTouchingPanels();
}
