package com.missionsurvive.scenarios.commands;

import com.badlogic.gdx.Screen;
import com.missionsurvive.screens.EditorScreen;
import com.missionsurvive.screens.GameScreen;
import com.missionsurvive.screens.ScrollerScreen;

/**
 * Created by kuzmin on 08.05.18.
 */

public class PutPlayerCommand implements Command {

    public static final int PLAY_SCROLLER = 0;
    public static final int PLAY_PLATFORMER = 1;

    public Screen screen;

    public int playId = -1;

    @Override
    public String execute(String key, String value) {
        switch (playId){
            case PLAY_SCROLLER:
                ((ScrollerScreen)screen).putPlayer(100, 150);
                break;
            case PLAY_PLATFORMER:
                ((EditorScreen)screen).putPlayer(100, 150);
                break;
        }
        return null;
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

    /**
     * Gets screen and appropriate playId to execute.
     * @param screen
     */
    public void setScreen(EditorScreen screen) {
        this.screen = screen;
        if(screen instanceof EditorScreen){
            playId = PLAY_PLATFORMER;
        }
    }

    @Override
    public void setScreen(GameScreen gameScreen) {

    }
}
