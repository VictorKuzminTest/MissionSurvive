package com.missionsurvive.scenarios;

import com.missionsurvive.map.MapEditor;

public interface Spawn {

    public void spawnBot(Scenario scenario, MapEditor mapEditor, float deltaTime);

    public void setFirstTimeSpawn(boolean isFirstTimeSpawn);

    public int getBotId();

    public int getDirection();
}
