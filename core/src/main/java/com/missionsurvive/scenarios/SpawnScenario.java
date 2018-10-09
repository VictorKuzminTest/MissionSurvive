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

/**
 * Created by kuzmin on 17.05.18.
 */

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
        switch (scenarioId){
            case LEVEL_1_SCENE:
                if(isFirstTimeSpawn){ //so we generate only one time
                    L1B l1b = null;
                    if(difficulty == DIFFICULTY_EXPERIENCED){
                        l1b = new L1B("l1b", mapEditor,
                                col * 16, row * 16, 40);
                    }
                    else{
                        l1b = new L1B("l1b", mapEditor,
                                col * 16, row * 16, 20);
                    }
                    scenario.addBot(l1b, 0);
                    scenario.addBot(
                            new TriggerL1B(l1b, scenario,col * 16, row * 16),
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
            case LEVEL_3_SCENE:
                if(isFirstTimeSpawn) {
                    TriggerL3B triggerL3B = null;
                    if(difficulty == DIFFICULTY_EXPERIENCED){
                        triggerL3B = new TriggerL3B(scenario,
                                col * 16, row * 16, 70);
                    }
                    else{
                        triggerL3B = new TriggerL3B(scenario,
                                col * 16, row * 16, 30);
                    }
                    scenario.addBot(triggerL3B, 0);
                    scenario.setScroll(false, false);

                    isFirstTimeSpawn = false;
                }
                break;
            case LEVEL_5_SCENE:
                if(isFirstTimeSpawn){
                    L5B l5B = null;
                    if(difficulty == DIFFICULTY_EXPERIENCED){
                        l5B = new L5B("l5b", mapEditor,
                                col * 16, row * 16, 50);
                    }
                    else{
                        l5B = new L5B("l5b", mapEditor,
                                col * 16, row * 16, 30);
                    }
                    scenario.addBot(l5B, 0);
                    scenario.addBot(
                            new TriggerL5B(l5B, scenario, col * 16, row * 16,
                                    difficulty),
                            0);
                    scenario.setScroll(false, false);

                    isFirstTimeSpawn = false;
                }
                break;
            case LEVEL_6_SCENE:
                if(isFirstTimeSpawn){
                    L6B l6b = new L6B("l6b", mapEditor,
                            col * 16, row *16);
                    scenario.addBot(l6b, 0);
                    scenario.addBot(new TriggerL6B(l6b, scenario,
                            col * 16, row * 16), 0);
                    scenario.setScroll(false, false);
                    isFirstTimeSpawn = false;
                }
                break;
            case END_GAME_SCENE:
                if(isFirstTimeSpawn){
                    scenario.addBot(new TriggerEndLev6(), 0);
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
