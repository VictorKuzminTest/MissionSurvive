package com.missionsurvive.scenarios.commands;

import com.badlogic.gdx.Screen;
import com.missionsurvive.screens.ScrollerScreen;

/**
 * Created by kuzmin on 08.05.18.
 */

public class PlayProject implements  Command{

    public static final int PLAY_SCROLLER = 0;
    public static final int PLAY_PLATFORMER = 1;

    public Screen screen;

    public int playId = -1;

    @Override
    public void execute(String data) {
        switch (playId){
            case PLAY_SCROLLER:
                ((ScrollerScreen)screen).setHeroControl(
                        getHeroControl(((ScrollerScreen)screen).isHeroControl()));
                break;
        }
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
}
