package com.missionsurvive.scenarios;

import com.missionsurvive.map.MapEditor;
import com.missionsurvive.objs.Helicopter;
import com.missionsurvive.scenarios.Spawn;

/**
 * Created by kuzmin on 17.05.18.
 */

public class SpawnScenario implements Spawn {

    public static final int LEVEL_2_SCENE = 1000;

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
            case LEVEL_2_SCENE:
                /*if(isFirstTimeSpawn) { //so we generate only one time
                    game.getCurrentScreen().getScenario().addBot(
                            new Helicopter("helicopter", game, mapEditor,
                                    col * (16 - 1), row * (16 - 1), direction),
                            LEVEL_2_SCENE);

                    isFirstTimeSpawn = false;
                }*/
                break;
        }
    }

    @Override
    public void setFirstTimeSpawn(boolean isFirstTimeSpawn) {
        this.isFirstTimeSpawn = isFirstTimeSpawn;
    }

    @Override
    public int getBotId() {
        return 0;
    }

    @Override
    public int getDirection() {
        return 0;
    }
}
