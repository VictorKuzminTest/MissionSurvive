package com.missionsurvive.scenarios.controlscenarios;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.ControlPanel;

import java.util.List;

public interface ControlScenario {

    public List<ControlPanel> getControlPanels();

    public void setControlPanels();

    public void drawPanels(SpriteBatch batch);

    public boolean onTouchPanels(float deltaTime, float scaleX, float scaleY);

    public void action(Object object);
}
