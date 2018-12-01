package com.missionsurvive.scenarios;

import com.missionsurvive.map.MapEditor;
import com.missionsurvive.objs.Helicopter;
import com.missionsurvive.objs.actors.L3B;
import com.missionsurvive.objs.actors.L5B;
import com.missionsurvive.objs.actors.L6B;
import com.missionsurvive.objs.triggers.TriggerL1B;
import com.missionsurvive.objs.triggers.TriggerEndLev6;
import com.missionsurvive.objs.triggers.TriggerHelicopter;
import com.missionsurvive.objs.actors.L1B;
import com.missionsurvive.objs.triggers.TriggerL3B;
import com.missionsurvive.objs.triggers.TriggerL5B;
import com.missionsurvive.objs.triggers.TriggerL6B;
import com.missionsurvive.utils.Progress;

public class SpawnScenario implements Spawn {

    public static final int DIFFICULTY_BEGINNER = 0;
    public static final int DIFFICULTY_EXPERIENCED = 1;

    public static final int LEVEL_1_SCENE = 1000;
    public static final int LEVEL_2_SCENE = 2000;
    public static final int LEVEL_3_SCENE = 3000;
    public static final int LEVEL_5_SCENE = 5000;
    public static final int LEVEL_6_SCENE = 6000;
    public static final int END_GAME_SCENE = 7000;

    private int scenarioId;
    private int direction;
    private int row, col;
    private int difficulty;

    private boolean isFirstTimeSpawn = true;

    public SpawnScenario(int scenarioId, int direction, String difficulty,
                         int row, int col){
        this.scenarioId = scenarioId;
        this.direction = direction;
        this.row = row;
        this.col = col;
        if(difficulty.equalsIgnoreCase(Progress.EXPERIENCED)){
            this.difficulty = DIFFICULTY_EXPERIENCED;
        }
        else{
            this.difficulty = DIFFICULTY_BEGINNER;
        }
    }

    @Override
    public void spawnBot(Scenario scenario, MapEditor mapEditor, float deltaTime) {
        ...
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
