package com.missionsurvive.scenarios.commands;

import com.missionsurvive.screens.EditorScreen;
import com.missionsurvive.screens.ScrollerScreen;
import com.missionsurvive.utils.Assets;

/**
 * Created by kuzmin on 07.05.18.
 */

public class ToScreenCommand implements  Command {

    public static final int TO_EDITOR_SCREEN = 1;
    public static final int TO_SCROLLER_SCREEN = 2;

    public int screenId = 0;

    public ToScreenCommand(int screenId){
        this.screenId = screenId;
    }

    @Override
    public String execute(String key, String value) {
        switch (screenId){
            case TO_EDITOR_SCREEN:
                Assets.getGame().setScreen(
                        new EditorScreen(Assets.getGame(), 400, 23));
                break;
            case TO_SCROLLER_SCREEN:
                Assets.getGame().setScreen(new ScrollerScreen(Assets.getGame()));
                break;
        }
        return null;
    }
}
