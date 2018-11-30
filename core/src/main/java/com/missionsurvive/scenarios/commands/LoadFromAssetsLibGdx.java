package com.missionsurvive.scenarios.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.missionsurvive.screens.GameScreen;

public class LoadFromAssetsLibGdx implements Command{

    @Override
    public String execute(String key, String path) {
        return getMap(path);
    }

    public String getMap(String path){
        String mapInString = null;
        FileHandle fileHandle = Gdx.files.internal(path);
        if(fileHandle != null){
            mapInString = fileHandle.readString();
        }
        return mapInString;
    }

    @Override
    public void setScreen(GameScreen gameScreen) {

    }
}
