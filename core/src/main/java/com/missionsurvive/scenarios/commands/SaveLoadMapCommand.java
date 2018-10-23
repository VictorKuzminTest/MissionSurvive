package com.missionsurvive.scenarios.commands;

import com.missionsurvive.map.Map;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.scenarios.controlscenarios.EditorScenario;
import com.missionsurvive.screens.GameScreen;

/**
 * Created by kuzmin on 21.05.18.
 */

public class SaveLoadMapCommand implements Command{

    public static final int ACTION_SAVE = 1;
    public static final int ACTION_LOAD = 2;

    private Map map;

    private int actionId = -1;

    @Override
    public String execute(String key, String value) {
        switch(actionId){
            case ACTION_SAVE:
                return map.saveMap();

            case ACTION_LOAD:
                if(value != null){
                    map.loadMap(value);
                    EditorScenario.setMapLoading(true);
                }
                break;
        }
        return null;
    }

    public void setMap(Map map, int actionId) {
        this.map = map;
        this.actionId = actionId;
    }

    @Override
    public void setScreen(GameScreen gameScreen) {

    }
}
