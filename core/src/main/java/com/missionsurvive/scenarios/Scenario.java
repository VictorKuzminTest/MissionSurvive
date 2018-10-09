package com.missionsurvive.scenarios;

import com.badlogic.gdx.Game;
import com.missionsurvive.framework.TouchControl;
import com.missionsurvive.map.Map;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.Bot;
import com.missionsurvive.scenarios.controlscenarios.ControlScenario;
import com.missionsurvive.screens.GameScreen;

import java.util.List;

/**
 * Created by kuzmin on 03.05.18.
 */

public interface Scenario {

    public abstract void update(Map map, TouchControl touchControl, float deltaTime);

    public abstract void collideObject();

    public abstract void placeObject(int x, int y);

    public abstract void addBot(Bot bot, int criteria);

    public abstract void removeBot(Bot bot, int criteria);

    public abstract List<Bot> getBots(int criteria);

    public abstract void setScroll(boolean horizontal, boolean vertical);

    public void setControlScenario(ControlScenario controlScenario);

    public ControlScenario getControlScenario();

    public GameScreen getGameScreen();
}
