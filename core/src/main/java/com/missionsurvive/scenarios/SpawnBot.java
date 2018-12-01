package com.missionsurvive.scenarios;

import com.missionsurvive.map.MapEditor;
import com.missionsurvive.objs.PowerUp;
import com.missionsurvive.objs.Wreckage;
import com.missionsurvive.objs.actors.SoldierZombie;
import com.missionsurvive.objs.actors.Zombie;
import com.missionsurvive.objs.actors.ShotgunZombie;

import java.util.Random;

public class SpawnBot implements Spawn{

    public static final int ZOMBIE = 1;
    public static final int SHOTGUN_ZOMBIE = 2;
    public static final int SOLDIER_ZOMBIE = 3;
    public static final int POWER_UP_LIFE = 11;
    public static final int POWER_UP_GUN = 12;
    public static final int WRECKAGE = 20;

    private Random random = new Random();

    private int whichBot;
    private int direction;
    private int row, col;

    private boolean isFirstTimeSpawn = true;

    private float spawnTickTime;
    private float spawnTick = 2.0f;

    public SpawnBot(int whichBot, int direction, int row, int col) {
        this.whichBot = whichBot;
        this.direction = direction;
        this.row = row;
        this.col = col;
    }

    @Override
    public void spawnBot(Scenario scenario, MapEditor mapEditor, float deltaTime){
        ...
    }

    public void spawnPowerUp(Scenario scenario, MapEditor mapEditor){
        ...
    }

    public void spawnZombie(Scenario scenario, MapEditor mapEditor, float deltaTime, int x, int y){
        ...
    }

    public void checkAndAddZombie(Scenario scenario, MapEditor mapEditor, int x, int y){
        ...
    }

    private int getSpeed(){
        int speed = random.nextInt(2) == 0 ? 2 : 3;
        return speed;
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
