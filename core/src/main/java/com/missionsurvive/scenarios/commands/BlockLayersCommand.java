package com.missionsurvive.scenarios.commands;

import com.badlogic.gdx.Screen;
import com.missionsurvive.screens.EditorScreen;

/**
 * Created by kuzmin on 15.05.18.
 */

public class BlockLayersCommand implements Command{

    private Screen screen;

    @Override
    public String execute(String key, String value) {
        boolean isScrollBlocked = ((EditorScreen)screen).isScrollBlocked() ? false : true;

        ((EditorScreen) screen).setScrollBlocked(isScrollBlocked);
        return null;
    }

    public void setScreen(EditorScreen screen) {
        this.screen = screen;
    }
}
