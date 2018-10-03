package com.missionsurvive.scenarios.commands;

import com.badlogic.gdx.Screen;
import com.missionsurvive.screens.EditorScreen;
import com.missionsurvive.screens.GameScreen;

/**
 * Created by kuzmin on 21.05.18.
 */

public class NewMapCommand implements Command{
    private int width;
    private int height;
    private EditorScreen editorScreen;

    @Override
    public String execute(String key, String value) {
        if(key.equalsIgnoreCase("width")){
            width = Integer.parseInt(value);
        }
        else if(key.equalsIgnoreCase("height")){
            height = Integer.parseInt(value);
        }
        else if(key.equalsIgnoreCase("newMap")){
            editorScreen.newMap(width, height);
        }
        return null;
    }

    public void setScreen(Screen screen){
        editorScreen = (EditorScreen)screen;
    }

    @Override
    public void setScreen(GameScreen gameScreen) {

    }
}
