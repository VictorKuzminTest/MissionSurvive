package com.missionsurvive.scenarios.controlscenarios;

import com.badlogic.gdx.Screen;
import com.missionsurvive.framework.impl.ListButtons;
import com.missionsurvive.scenarios.commands.PlayProject;
import com.missionsurvive.scenarios.commands.PutPlayer;
import com.missionsurvive.screens.ScrollerScreen;
import com.missionsurvive.utils.Commands;

/**
 * Created by kuzmin on 08.05.18.
 */

public class ScrollerScreenListingBuilder implements ListingBuilder {

    private ScrollerScreen scrollerScreen;

    public ScrollerScreenListingBuilder(Screen screen){
        scrollerScreen = (ScrollerScreen) screen;
    }

    @Override
    public void addButtons(ListButtons listButtons) {
        PlayProject command = (PlayProject) Commands.getCommand("PlayProject");
        command.setScreen(scrollerScreen);
        listButtons.addNewButton("iconseditor", 0, 1, 1, 1, 32, 32, command);

        PutPlayer putPlayerCommand = (PutPlayer) Commands.getCommand("PutPlayer");
        putPlayerCommand.setScreen(scrollerScreen);
        listButtons.addNewButton("iconseditor", 0, 2, 1, 1 + 6 * 34, 32, 32, putPlayerCommand);
    }
}
