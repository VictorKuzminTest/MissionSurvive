package com.missionsurvive.scenarios.commands;

import com.badlogic.gdx.Screen;
import com.missionsurvive.MSGame;
import com.missionsurvive.screens.MainMenuScreen;
import com.missionsurvive.screens.PlatformerScreen;
import com.missionsurvive.screens.ScrollerScreen;
import com.missionsurvive.utils.Assets;

/**
 * Created by kuzmin on 21.05.18.
 */

public class NewMapCommand implements Command{
    private int width;
    private int height;
    private PlatformerScreen platformerScreen;

    @Override
    public String execute(String key, String value) {
        if(key.equalsIgnoreCase("width")){
            width = Integer.parseInt(value);
        }
        else if(key.equalsIgnoreCase("height")){
            height = Integer.parseInt(value);
        }
        else if(key.equalsIgnoreCase("newMap")){
            platformerScreen.newMap(width, height);
        }
        return null;
    }

    public void setScreen(Screen screen){
        platformerScreen = (PlatformerScreen)screen;
    }

}
