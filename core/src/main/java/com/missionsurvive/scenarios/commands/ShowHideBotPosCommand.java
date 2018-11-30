package com.missionsurvive.scenarios.commands;

import com.badlogic.gdx.Screen;
import com.missionsurvive.screens.EditorScreen;
import com.missionsurvive.screens.GameScreen;

public class ShowHideBotPosCommand implements Command{

    private Screen screen;

    @Override
    public String execute(String key, String value) {
        boolean isShowingObjPos = ((EditorScreen)screen).isShowingObjPos() ? false : true;

        ((EditorScreen) screen).setShowingObjPos(isShowingObjPos);
        return null;
    }

    public void setScreen(EditorScreen screen) {
        this.screen = screen;
    }

    @Override
    public void setScreen(GameScreen gameScreen) {

    }
}
