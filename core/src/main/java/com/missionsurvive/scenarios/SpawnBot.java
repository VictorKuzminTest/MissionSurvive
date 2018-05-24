package com.missionsurvive.scenarios;

import com.missionsurvive.map.MapEditor;
import com.missionsurvive.objs.PowerUp;
import com.missionsurvive.objs.Wreckage;
import com.missionsurvive.objs.actors.Boss;
import com.missionsurvive.objs.actors.Enemy;
import com.missionsurvive.objs.actors.L3B;
import com.missionsurvive.objs.actors.L6B;
import com.missionsurvive.objs.actors.ShotgunZombie;
import com.missionsurvive.screens.PlatformerScreen;

/**
 * Created by kuzmin on 04.05.18.
 */

public class SpawnBot implements Spawn{

    public static final int ZOMBIE = 1;
    public static final int SHOTGUN_ZOMBIE = 2;
    public static final int LEVEL_1_BOSS = 3;
    public static final int LEVEL_3_BOSS = 4;
    public static final int LEVEL_6_BOSS = 5;
    public static final int POWER_UP_LIFE = 11;
    public static final int POWER_UP_GUN = 12;
    public static final int WRECKAGE = 20;

    private int whichBot;
    private int direction;
    private boolean isFirstTimeSpawn = true;
    private float spawnTickTime; private float spawnTick = 3.0f;
    private int row, col;

    public SpawnBot(int whichBot, int direction, int row, int col) {
        this.whichBot = whichBot;
        this.direction = direction;
        this.row = row;
        this.col = col;
    }

    @Override
    public void spawnBot(Scenario scenario, MapEditor mapEditor, float deltaTime){
        switch (whichBot) {
            case ZOMBIE:
                spawnZombie(scenario, mapEditor, deltaTime, col * 16 - 20, row * 16 - 70);
                break;
            case SHOTGUN_ZOMBIE:
                if(isFirstTimeSpawn){ //so we generate only one time
                    scenario.addBot(
                            new ShotgunZombie("shotgunzombie", mapEditor, col * 16 - 20, row * 16 - 70,
                                    direction),
                            SHOTGUN_ZOMBIE);
                    isFirstTimeSpawn = false;
                }
                break;
            case LEVEL_1_BOSS:
                if(isFirstTimeSpawn){ //so we generate only one time
                    scenario.addBot(
                            new Boss("boss", mapEditor, col * 16, row * 16),
                            LEVEL_1_BOSS);
                    isFirstTimeSpawn = false;
                }
                break;
            case LEVEL_3_BOSS:
                if(isFirstTimeSpawn){ //so we generate only one time
                    scenario.addBot(
                            new L3B("l3b", mapEditor, col * 16, row * 16),
                            LEVEL_3_BOSS);
                    isFirstTimeSpawn = false;
                }
                break;
            case LEVEL_6_BOSS:
                if(isFirstTimeSpawn){ //so we generate only one time
                    scenario.addBot(new L6B("l6b", mapEditor,
                            col * 16, row * 16), LEVEL_6_BOSS);
                    isFirstTimeSpawn = false;
                }
                break;
            case POWER_UP_LIFE:
                if(isFirstTimeSpawn){ //so we generate only one time
                    spawnPowerUp(scenario, mapEditor);
                    isFirstTimeSpawn = false;
                }
                break;
            case POWER_UP_GUN:
                if(isFirstTimeSpawn){ //so we generate only one time
                    spawnPowerUp(scenario, mapEditor);
                    isFirstTimeSpawn = false;
                }
                break;
            case WRECKAGE:
                if(isFirstTimeSpawn){ //so we generate only one time
                    scenario.addBot(new Wreckage("wreckage", mapEditor,
                                    col * 16,
                                    row * 16),
                            WRECKAGE);
                    isFirstTimeSpawn = false;
                }
                break;
            default:
                break;
        }
    }

    public void spawnPowerUp(Scenario scenario, MapEditor mapEditor){
        if(direction == PowerUp.DIRECTION_EAST){
            if(whichBot == POWER_UP_LIFE){
                scenario.addBot(new PowerUp("cuadcopter", mapEditor,
                                col * 16 - 480, row * 16, PowerUp.POWER_LIFE, direction),
                        0);
            }
            else{
                scenario.addBot(new PowerUp("cuadcopter", mapEditor,
                                col * 16 - 480, row * 16, PowerUp.POWER_GUN, direction),
                        0);
            }
        }
        else {
            if(whichBot == POWER_UP_LIFE){
                scenario.addBot(new PowerUp("cuadcopter", mapEditor,
                                col * 16, row * 16 + 320, PowerUp.POWER_LIFE, direction),
                        0);
            }
            else{
                scenario.addBot(new PowerUp("cuadcopter", mapEditor,
                                col * 16, row * 16 + 320, PowerUp.POWER_GUN, direction),
                        0);
            }
        }
    }

    public void spawnZombie(Scenario scenario, MapEditor mapEditor, float deltaTime, int x, int y){
        if(isFirstTimeSpawn){
            checkAndAddZombie(scenario, mapEditor, x, y);
        }
        else {
            spawnTickTime += deltaTime;
            while (spawnTickTime > spawnTick) {
                spawnTickTime -= spawnTick;
                checkAndAddZombie(scenario, mapEditor, x, y);
            }
        }
    }

    public void checkAndAddZombie(Scenario scenario, MapEditor mapEditor, int x, int y){
        if(scenario.getBots(ZOMBIE).size() < PlatformerScenario.MAX_NUM_ZOMBIES){
            scenario.addBot(new Enemy("zsuit", mapEditor, x , y, direction),
                    ZOMBIE);
            isFirstTimeSpawn = false;
        }
    }

    @Override
    public void setFirstTimeSpawn(boolean isFirstTimeSpawn){
        this.isFirstTimeSpawn = isFirstTimeSpawn;
    }

    @Override
    public int getBotId(){
        return whichBot;
    }

    public int getRow(){return row;}

    public int getCol(){return col;}

    @Override
    public int getDirection(){
        return direction;
    }

}
