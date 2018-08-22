package com.missionsurvive.screens;

import com.badlogic.gdx.Screen;
import com.missionsurvive.MSGame;
import com.missionsurvive.map.Map;

/**
 * Created by kuzmin on 07.06.18.
 */

public class ScreenFactory {

    private MSGame game;

    public ScreenFactory(MSGame game){
        this.game = game;
    }

    public Screen newScreen(String screenName, Map map){
        Screen screen = null;
        if(screenName.equalsIgnoreCase("GameMenuScreen")){
            screen = new GameMenuScreen(game);
        }
        else if(screenName.equalsIgnoreCase("LoadingLevelScreen")){
            screen = new LoadingLevelScreen(game);
        }
        else if(screenName.equals("PlatformerScreen")){
            screen = new PlatformerScreen(game, map);
        }
        else if(screenName.equalsIgnoreCase("EndScreen")){
            screen = new EndScreen(game, map);
        }
        return screen;
    }
}
