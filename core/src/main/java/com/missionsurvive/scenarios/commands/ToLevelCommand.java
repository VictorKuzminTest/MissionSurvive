package com.missionsurvive.scenarios.commands;

import com.missionsurvive.MSGame;
import com.missionsurvive.screens.ScreenFactory;
import com.missionsurvive.utils.Assets;

/**
 * Created by kuzmin on 07.06.18.
 */

public class ToLevelCommand implements Command{

    @Override
    public String execute(String key, String value) {
        MSGame game = Assets.getGame();
        ScreenFactory screenFactory = game.getScreenFactory();
        game.setScreen(screenFactory.newScreen("LoadingLevelScreen", null));
        return null;
    }
}
