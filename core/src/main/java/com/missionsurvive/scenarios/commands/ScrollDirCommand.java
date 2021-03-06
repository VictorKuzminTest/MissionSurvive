package com.missionsurvive.scenarios.commands;

import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.screens.GameScreen;

/**
 * Changes a scroll of a map to horizontal, vertical etc.
 */
public class ScrollDirCommand implements Command{

    private PlatformerScenario platformerScenario;

    @Override
    public String execute(String key, String value) {
        if(platformerScenario.isHorizontal()){
            platformerScenario.setScroll(false, true);
        }
        else{
            platformerScenario.setScroll(true, false);
        }
        return null;
    }

    public void setScenario(Scenario scenario){
        platformerScenario = (PlatformerScenario)scenario;
    }

    @Override
    public void setScreen(GameScreen gameScreen) {

    }
}
