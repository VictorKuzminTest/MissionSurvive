package com.missionsurvive.scenarios.commands;

import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;

/**
 * Created by kuzmin on 17.05.18.
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

}
