package com.missionsurvive.scenarios.commands;

import com.badlogic.gdx.Screen;
import com.missionsurvive.screens.PlatformerScreen;

/**
 * Created by kuzmin on 15.05.18.
 */

public class BlockLayersCommand implements Command{

    private Screen screen;

    @Override
    public String execute(String key, String value) {
        boolean isScrollBlocked = ((PlatformerScreen)screen).isScrollBlocked() ? false : true;

        ((PlatformerScreen) screen).setScrollBlocked(isScrollBlocked);
        return null;
    }

    public void setScreen(PlatformerScreen screen) {
        this.screen = screen;
    }
}
