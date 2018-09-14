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

    public static final int DIFFICULTY_BEGINNER = 0;
    public static final int DIFFICULTY_EXPERIENCED = 1;

    private int buttonWidth = 102;
    private int buttonHeight = 90;
    private int difficulty;

    public ChooseStageListingBuilder(int difficulty){
        this.difficulty = difficulty;
    }

    @Override
    public void addButtons(ListButtons listButtons) {
        switch (difficulty){
            case DIFFICULTY_BEGINNER:
                addNewButton(listButtons, 1, 1, 0, "ToLevel1Beginner");
                addNewButton(listButtons, 1 + (buttonWidth + 2) * 1, 1, 1, "ToLevel2Beginner");
                addNewButton(listButtons, 1 + (buttonWidth + 2) * 2, 1, 2, "ToLevel3Beginner");
                addNewButton(listButtons, 1, 1 + (buttonHeight + 2) * 1, 3, "ToLevel4Beginner");
                addNewButton(listButtons, 1 + (buttonWidth + 2) * 1,
                        1 + (buttonHeight + 2) * 1, 4, "ToLevel5Beginner");
                break;
            case DIFFICULTY_EXPERIENCED:
                addNewButton(listButtons, 1, 1, 0, "ToLevel1Experienced");
                addNewButton(listButtons, 1 + (buttonWidth + 2) * 1, 1, 1, "ToLevel2Experienced");
                addNewButton(listButtons, 1 + (buttonWidth + 2) * 2, 1, 2, "ToLevel3Experienced");
                addNewButton(listButtons, 1, 1 + (buttonHeight + 2) * 1, 3, "ToLevel4Experienced");
                addNewButton(listButtons, 1 + (buttonWidth + 2) * 1,
                        1 + (buttonHeight + 2) * 1, 4, "ToLevel5Experienced");
                addNewButton(listButtons, 1 + (buttonWidth + 2) * 2,
                        1 + (buttonHeight + 2) * 1, 5, "ToLevel6");
                break;
        }
    }

    public void addNewButton(ListButtons listButtons,
                             int assetX, int assetY, int col, String commandName){
        Command command = Commands.getCommand(commandName);
        listButtons.addNewButton("levels", col, 0,
                assetX, assetY, buttonWidth, buttonHeight, command);
    }
}
