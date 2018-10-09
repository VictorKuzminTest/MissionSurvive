package com.missionsurvive.scenarios.commands;

import com.missionsurvive.MSGame;
import com.missionsurvive.screens.GameScreen;
import com.missionsurvive.screens.ScreenFactory;
import com.missionsurvive.utils.Assets;

public class ToPurchaseScreenCommand implements Command{

    @Override
    public String execute(String key, String value) {
        MSGame game = Assets.getGame();
        ScreenFactory screenFactory = game.getScreenFactory();
        game.setScreen(screenFactory
                .newScreen("PurchaseScreen", null, null));
        return null;
    }

    @Override
    public void setScreen(GameScreen gameScreen) {

    }
}
