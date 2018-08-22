package com.missionsurvive.scenarios;

import com.missionsurvive.map.MapEditor;
import com.missionsurvive.objs.Helicopter;
import com.missionsurvive.objs.triggers.TriggerBoss;
import com.missionsurvive.objs.triggers.TriggerEndGame;
import com.missionsurvive.objs.triggers.TriggerHelicopter;
import com.missionsurvive.objs.actors.L1B;

/**
 * Created by kuzmin on 17.05.18.
 */

public class SpawnScenario implements Spawn {

    public static final int LEVEL_1_SCENE = 1000;
    public static final int LEVEL_2_SCENE = 2000;
    public static final int LEVEL_3_SCENE = 3000;
    public static final int LEVEL_5_SCENE = 5000;
    public static final int LEVEL_6_SCENE = 6000;
    public static final int END_GAME_SCENE = 7000;

    private int scenarioId;
    private int direction;
    private int row, col;

    private boolean isFirstTimeSpawn = true;

    public SpawnScenario(int scenarioId, int direction, int row, int col){
        this.scenarioId = scenarioId;
        this.direction = direction;
        this.row = row;
        this.col = col;
    }

    @Override
    public void spawnBot(Scenario scenario, MapEditor mapEditor, float deltaTime) {
        switch (scenarioId){
            case LEVEL_1_SCENE:
                if(isFirstTimeSpawn){ //so we generate only one time
                    L1B l1b = new L1B("l1b", mapEditor, col * 16, row * 16);
                    scenario.addBot(l1b, 0);
                    scenario.addBot(
                            new TriggerBoss(l1b, scenario, col * 16, row * 16),
                            0);
                    scenario.setScroll(false, false);

                    isFirstTimeSpawn = false;
                }
                break;
            case LEVEL_2_SCENE:
                if(isFirstTimeSpawn) {
                    Helicopter helicopter = new Helicopter("helicopter",
                            mapEditor, col * 16, row * 16);
                    scenario.addBot(helicopter, 0);
                    scenario.addBot(
                            new TriggerHelicopter(helicopter,
                                    scenario, col * 16, row * 16),
                            0);
                    scenario.setScroll(false, false);

                    isFirstTimeSpawn = false;
                }
                break;
            case END_GAME_SCENE:
                if(isFirstTimeSpawn){
                    scenario.addBot(new TriggerEndGame(), 0);
                    isFirstTimeSpawn = false;
                }
                break;
            }
    }

    @Override
    public void setFirstTimeSpawn(boolean isFirstTimeSpawn) {
        this.isFirstTimeSpawn = isFirstTimeSpawn;
    }

    @Override
    public int getBotId() {
        return scenarioId;
    }

    @Override
    public int getDirection() {
        return 0;
    }
}
