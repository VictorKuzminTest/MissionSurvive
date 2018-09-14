package com.missionsurvive.screens;

import com.badlogic.gdx.Screen;
import com.missionsurvive.MSGame;
import com.missionsurvive.map.Map;
import com.missionsurvive.scenarios.PlayScript;
import com.missionsurvive.scenarios.controlscenarios.ChooseStageListingBuilder;

/**
 * Created by kuzmin on 07.06.18.
 */

public class ScreenFactory {

    private MSGame game;
    private PlayScript playScript;

    public ScreenFactory(MSGame game, PlayScript playScript){
        this.game = game;
        this.playScript = playScript;
    }

    public Screen newScreen(String screenName, Map map, String value){
        Screen screen = null;
        if(screenName.equalsIgnoreCase("GameMenuScreen")){
            screen = new GameMenuScreen(game, playScript);
        }
        else if(screenName.equalsIgnoreCase("LoadingLevelScreen")){
            screen = new LoadingLevelScreen(game, playScript, value);
        }
        else if(screenName.equalsIgnoreCase("PlatformerScreen")){
            screen = new PlatformerScreen(game, playScript, map);
        }
        else if(screenName.equalsIgnoreCase("ScrollerScreen")){
            screen = new ScrollerScreen(game, playScript);
        }
        else if(screenName.equalsIgnoreCase("EndScreen")){
            screen = new EndScreen(game, playScript, map);
        }
        else if(screenName.equalsIgnoreCase("EditorScreen")){
            screen = new EditorScreen(game, playScript);
        }
        return screen;
    }
}
