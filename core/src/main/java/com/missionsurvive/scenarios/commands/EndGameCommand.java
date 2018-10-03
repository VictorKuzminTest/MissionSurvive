package com.missionsurvive.scenarios.commands;

import com.missionsurvive.MSGame;
import com.missionsurvive.screens.GameScreen;
import com.missionsurvive.utils.Assets;

public class EndGameCommand implements Command{

    @Override
    public String execute(String key, String value) {
        MSGame game = Assets.getGame();
        game.setScreen(game.getScreenFactory()
                .newScreen("GameMenuScreen", null, null));
        return null;
    }

    @Override
    public void setScreen(GameScreen gameScreen) {

    }
}
