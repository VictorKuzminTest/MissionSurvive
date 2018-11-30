package com.missionsurvive.scenarios.commands;

import com.missionsurvive.screens.GameScreen;

public interface Command {

    public String execute(String key, String value);

    public void setScreen(GameScreen gameScreen);
}
