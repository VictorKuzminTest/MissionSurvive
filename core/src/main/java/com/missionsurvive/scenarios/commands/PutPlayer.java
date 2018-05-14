package com.missionsurvive.scenarios.commands;

import com.badlogic.gdx.Screen;
import com.missionsurvive.screens.ScrollerScreen;

/**
 * Created by kuzmin on 08.05.18.
 */

public class PutPlayer implements Command {

    public static final int PLAY_SCROLLER = 0;
    public static final int PLAY_PLATFORMER = 1;

    public Screen screen;

    public int playId = -1;

    @Override
    public void execute(String data) {
        switch (playId){
            case PLAY_SCROLLER:
                ((ScrollerScreen)screen).putPlayer(100, 150);
                break;
        }
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
