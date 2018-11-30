package com.missionsurvive.scenarios.commands;

import com.badlogic.gdx.Screen;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.screens.EditorScreen;
import com.missionsurvive.screens.GameScreen;
import com.missionsurvive.screens.ScrollerScreen;

public class PlayProjectCommand implements  Command{

    public static final int PLAY_SCROLLER = 0;
    public static final int PLAY_PLATFORMER = 1;

    public Screen screen;

    public int playId = -1;

    @Override
    public String execute(String key, String value) {
        switch (playId){
            case PLAY_SCROLLER:
                ((ScrollerScreen)screen).setPause(
                        getPause(((ScrollerScreen)screen).onPause()));
                break;
            case PLAY_PLATFORMER:
                EditorScreen editorScreen = (EditorScreen)screen;
                editorScreen.setPause(
                        getPause(editorScreen.onPause()));

                PlatformerScenario platformerScenario =
                        (PlatformerScenario)editorScreen.getPlatformerScenario();
                platformerScenario.setPlayerControl(true);
                platformerScenario.setFirstTimeSpawned();
                platformerScenario.removeAllBots();
                break;
        }
        return null;
    }

    public boolean getPause(boolean currentState){
        boolean onPause = currentState == true ? false : true;
        return onPause;
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
