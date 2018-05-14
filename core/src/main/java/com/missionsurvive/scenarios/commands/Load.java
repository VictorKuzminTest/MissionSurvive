package com.missionsurvive.scenarios.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.missionsurvive.map.MapEditor;

/**
 * Created by kuzmin on 23.04.18.
 */
public class Load implements com.missionsurvive.scenarios.commands.Command {

    private MapEditor mapEditor;

    public Load(MapEditor mapEditor){
        this.mapEditor = mapEditor;
    }

    @Override
    public void execute(String fileName) {
        mapEditor.loadMap(loadFileFromExternalStorage(fileName));
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
}
