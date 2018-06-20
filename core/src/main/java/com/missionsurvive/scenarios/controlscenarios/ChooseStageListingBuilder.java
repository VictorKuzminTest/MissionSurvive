package com.missionsurvive.scenarios.controlscenarios;

import com.missionsurvive.framework.impl.ListButtons;
import com.missionsurvive.scenarios.commands.Command;
import com.missionsurvive.scenarios.commands.PlayProjectCommand;
import com.missionsurvive.screens.EditorScreen;
import com.missionsurvive.utils.Commands;

/**
 * Created by kuzmin on 05.06.18.
 */

public class ChooseStageListingBuilder implements ListingBuilder{
    private int buttonWidth = 102;
    private int buttonHeight = 90;

    @Override
    public void addButtons(ListButtons listButtons) {
        //first lev:
        int assetX = 1;
        int assetY = 1;
        Command toLev1Command = Commands.getCommand("ToLevel");
        listButtons.addNewButton("levels", 0, 0, assetX, assetY, buttonWidth, buttonHeight, toLev1Command);

        //sec lev:
        assetX = 1 + (buttonWidth + 2) * 1;
        assetY = 1;
        listButtons.addNewButton("levels", 1, 0, assetX, assetY, buttonWidth, buttonHeight, null);

        //third lev:
        assetX = 1 + (buttonWidth + 2) * 2;
        assetY = 1;
        listButtons.addNewButton("levels", 2, 0, assetX, assetY, buttonWidth, buttonHeight, null);

        //fourth lev:
        assetX = 1;
        assetY = 1 + (buttonHeight + 2) * 1;
        listButtons.addNewButton("levels", 3, 0, assetX, assetY, buttonWidth, buttonHeight, null);

        //fiv lev:
        assetX = 1 + (buttonWidth + 2) * 1;
        assetY = 1 + (buttonHeight + 2) * 1;
        listButtons.addNewButton("levels", 4, 0, assetX, assetY, buttonWidth, buttonHeight, null);

        //six lev:
        assetX = 1 + (buttonWidth + 2) * 2;
        assetY = 1 + (buttonHeight + 2) * 1;
        listButtons.addNewButton("levels", 5, 0, assetX, assetY, buttonWidth, buttonHeight, null);
    }
}
