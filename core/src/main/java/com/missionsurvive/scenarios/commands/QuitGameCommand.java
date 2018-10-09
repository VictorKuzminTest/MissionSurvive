package com.missionsurvive.scenarios.commands;

import com.badlogic.gdx.Gdx;
import com.missionsurvive.screens.GameScreen;

public class QuitGameCommand implements Command{

    @Override
    public String execute(String key, String value) {
        Gdx.app.exit();
        return null;
    }

    @Override
    public void setScreen(GameScreen gameScreen) {

    }
}
