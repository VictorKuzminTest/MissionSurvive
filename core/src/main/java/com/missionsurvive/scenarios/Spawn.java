package com.missionsurvive.scenarios;

import com.missionsurvive.map.MapEditor;

/**
 * Created by kuzmin on 04.05.18.
 */

public interface Spawn {

    public void spawnBot(MapEditor mapEditor, float deltaTime);

    public void setFirstTimeSpawn(boolean isFirstTimeSpawn);

    public int getBotId();

    public int getDirection();
}
