package com.missionsurvive.scenarios.commands;

import com.missionsurvive.screens.GameScreen;

/**
 * Created by kuzmin on 23.04.18.
 */

public interface Command {

    public String execute(String key, String value);

    public void setScreen(GameScreen gameScreen);
}
