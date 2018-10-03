package com.missionsurvive.scenarios.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.screens.GameScreen;

/**
 * Created by kuzmin on 23.04.18.
 * This class was created for first tests to load map from text. It uses LibGdx methods.
 * It is better to use android native methods.
 */
public class LoadFromExternalLibGdx implements com.missionsurvive.scenarios.commands.Command {

    private MapEditor mapEditor;

    public LoadFromExternalLibGdx(MapEditor mapEditor){
        this.mapEditor = mapEditor;
    }

    @Override
    public String execute(String key, String fileName) {
        mapEditor.loadMap(loadFileFromExternalStorage(fileName));
        return null;
    }

    public String loadFileFromExternalStorage(String filename){
        String mapInString = null;
        if(isExternalStorageAvailable()){
            String directory = "trf/";
            if(isExternalDirectoryExists(directory)){
                mapInString = getMap(directory + filename);
            }
        }
        return mapInString;
    }


    public boolean isExternalStorageAvailable(){
        return Gdx.files.isExternalStorageAvailable();
    }

    public boolean isExternalDirectoryExists(String directory){
        if(Gdx.files.external(directory).isDirectory()){
            return true;
        }
        else{
            return false;
        }
    }

    public String getMap(String filePath){
        String mapInString = null;
        FileHandle fileHandle = Gdx.files.external(filePath);
        if(fileHandle != null){
            mapInString = fileHandle.readString();
        }
        return mapInString;
    }

    @Override
    public void setScreen(GameScreen gameScreen) {

    }
}
