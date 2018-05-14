package com.missionsurvive.scenarios.commands;

import com.missionsurvive.MSGame;
import com.missionsurvive.screens.ScrollerScreen;
import com.missionsurvive.utils.Assets;

/**
 * Created by kuzmin on 07.05.18.
 */

public class ToScreenCommand implements  Command {

    public static final int TO_PLATFORMER_SCREEN = 1;
    public static final int TO_SCROLLER_SCREEN = 2;

    public int screenId = 0;

    public ToScreenCommand(int screenId){
        this.screenId = screenId;
    }

    @Override
    public void execute(String data) {
        switch (screenId){
            case TO_PLATFORMER_SCREEN:
                break;
            case TO_SCROLLER_SCREEN:
                Assets.getGame().setScreen(new ScrollerScreen((MSGame)Assets.getGame()));
                break;
        }
    }
}