package com.missionsurvive.scenarios.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by kuzmin on 09.06.18.
 */

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
}
