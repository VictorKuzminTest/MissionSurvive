package com.missionsurvive.scenarios;

import com.missionsurvive.framework.TouchControl;
import com.missionsurvive.map.Map;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.objs.Bot;
import com.missionsurvive.objs.Tank;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.controlscenarios.ControlScenario;
import com.missionsurvive.scenarios.controlscenarios.EndCS;
import com.missionsurvive.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class EndGameScenario implements Scenario {

    private ControlScenario controlScenario;
    private List<Bot> bots = new ArrayList<Bot>();

    public EndGameScenario(MapEditor mapEditor, ControlScenario controlScenario){
        bots.add(new Tank("helicopter", mapEditor, 180, 175));
        this.controlScenario = controlScenario;
    }

    @Override
    public void update(Map map, TouchControl touchControl, float deltaTime) {
        for(int enemyNum = 0; enemyNum < bots.size(); enemyNum++){
            bots.get(enemyNum).moving(deltaTime,
                    null, null, 0, 0);
        }
        if(bots.get(0).isAction() == Tank.ACTION_AWAY){
            controlScenario.getControlPanels().get(EndCS.END_GAME_PANEL).setActivated(true);
        }
    }

    @Override
    public void collideObject() {

    }

    @Override
    public void placeObject(int x, int y) {

    }

    @Override
    public void addBot(Bot bot, int criteria) {

    }

    @Override
    public void removeBot(Bot bot, int criteria) {

    }

    @Override
    public List<Bot> getBots(int criteria) {
        return bots;
    }

    @Override
    public void setScroll(boolean horizontal, boolean vertical) {

    }

    @Override
    public void setControlScenario(ControlScenario controlScenario) {

    }

    @Override
    public ControlScenario getControlScenario() {
        return null;
    }

    @Override
    public GameScreen getGameScreen() {
        return null;
    }
}
