package com.missionsurvive.scenarios.controlscenarios;

import com.badlogic.gdx.Screen;
import com.missionsurvive.framework.impl.ListButtons;
import com.missionsurvive.scenarios.commands.PlayProjectCommand;
import com.missionsurvive.scenarios.commands.PutPlayerCommand;
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
        PlayProjectCommand command = (PlayProjectCommand) Commands.getCommand("PlayProjectCommand");
        command.setScreen(scrollerScreen);
        listButtons.addNewButton("iconseditor", 0, 1, 0, 0, 32, 32, command);

        PutPlayerCommand putPlayerCommandCommand = (PutPlayerCommand) Commands.getCommand("PutPlayerCommand");
        putPlayerCommandCommand.setScreen(scrollerScreen);
        listButtons.addNewButton("iconseditor", 0, 2, 0, 32, 32, 32, putPlayerCommandCommand);
    }
}
