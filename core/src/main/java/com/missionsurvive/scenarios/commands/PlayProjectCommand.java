package com.missionsurvive.scenarios.commands;

import com.badlogic.gdx.Screen;
import com.missionsurvive.screens.PlatformerScreen;
import com.missionsurvive.screens.ScrollerScreen;

/**
 * Created by kuzmin on 08.05.18.
 */

public class PlayProjectCommand implements  Command{

    public static final int PLAY_SCROLLER = 0;
    public static final int PLAY_PLATFORMER = 1;

    public Screen screen;

    public int playId = -1;

    @Override
    public String execute(String key, String value) {
        switch (playId){
            case PLAY_SCROLLER:
                ((ScrollerScreen)screen).setHeroControl(
                        getHeroControl(((ScrollerScreen)screen).isHeroControl()));
                break;
            case PLAY_PLATFORMER:
                ((PlatformerScreen)screen).setHeroControl(
                        getHeroControl(((PlatformerScreen)screen).isHeroControl()));
                break;
        }
        return null;
    }

    public boolean getHeroControl(boolean currentState){
        boolean isHeroControl = currentState == true ? false : true;
        return isHeroControl;
    }

    /**
     * Gets screen and appropriate playId to execute.
     * @param screen
     */
    public void setScreen(ScrollerScreen screen) {
        this.screen = screen;
        if(screen instanceof ScrollerScreen){
            playId = PLAY_SCROLLER;
        }
    }

    public void setScreen(PlatformerScreen screen) {
        this.screen = screen;
        if(screen instanceof PlatformerScreen){
            playId = PLAY_PLATFORMER;
        }
    }
}
