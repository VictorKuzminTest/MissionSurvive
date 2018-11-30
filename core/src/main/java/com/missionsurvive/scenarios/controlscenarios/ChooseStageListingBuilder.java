package com.missionsurvive.scenarios.controlscenarios;

import com.missionsurvive.framework.impl.ListButtons;
import com.missionsurvive.scenarios.commands.Command;
import com.missionsurvive.utils.Commands;
import com.missionsurvive.utils.Progress;

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
        listButtons.clearList();
        switch (difficulty){
            case DIFFICULTY_BEGINNER:
                int levelCountBeginner = Progress.getProgress(Progress.BEGINNER);
                addNewButton(listButtons, 1, 1, 0, getCommand(0, levelCountBeginner, Progress.BEGINNER), 0, levelCountBeginner);
                addNewButton(listButtons, 1 + (buttonWidth + 2) * 1, 1, 1, getCommand(1, levelCountBeginner, Progress.BEGINNER), 1, levelCountBeginner);
                addNewButton(listButtons, 1 + (buttonWidth + 2) * 2, 1, 2, getCommand(2, levelCountBeginner, Progress.BEGINNER), 2, levelCountBeginner);
                addNewButton(listButtons, 1, 1 + (buttonHeight + 2) * 1, 3, getCommand(3, levelCountBeginner, Progress.BEGINNER), 3, levelCountBeginner);
                addNewButton(listButtons, 1 + (buttonWidth + 2) * 1,
                            1 + (buttonHeight + 2) * 1, 4, getCommand(4, levelCountBeginner, Progress.BEGINNER), 4, levelCountBeginner);
            break;
            case DIFFICULTY_EXPERIENCED:
                int levelCountExperienced = Progress.getProgress(Progress.EXPERIENCED);
                addNewButton(listButtons, 1, 1, 0, getCommand(0, levelCountExperienced, Progress.EXPERIENCED),0, levelCountExperienced);
                addNewButton(listButtons, 1 + (buttonWidth + 2) * 1, 1, 1, getCommand(1, levelCountExperienced, Progress.EXPERIENCED), 1, levelCountExperienced);
                addNewButton(listButtons, 1 + (buttonWidth + 2) * 2, 1, 2, getCommand(2, levelCountExperienced, Progress.EXPERIENCED), 2, levelCountExperienced);
                addNewButton(listButtons, 1, 1 + (buttonHeight + 2) * 1, 3, getCommand(3, levelCountExperienced, Progress.EXPERIENCED), 3, levelCountExperienced);
                addNewButton(listButtons, 1 + (buttonWidth + 2) * 1,
                            1 + (buttonHeight + 2) * 1, 4, getCommand(4, levelCountExperienced, Progress.EXPERIENCED), 4, levelCountExperienced);
                addNewButton(listButtons, 1 + (buttonWidth + 2) * 2,
                            1 + (buttonHeight + 2) * 1, 5, getCommand(5, levelCountExperienced, Progress.EXPERIENCED), 5, levelCountExperienced);
            break;
        }
    }

    private Command getCommand(int currentLevel, int levelCount, String difficulty){
        if(currentLevel <= levelCount){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ToLevel");
            //+ 1 is because we begin level count with 0, but in Commands.get(...) with 1:
            stringBuilder.append(currentLevel + 1);
            stringBuilder.append(difficulty);
            return Commands.getCommand(stringBuilder.toString());
        }
        return null;
    }

    public void addNewButton(ListButtons listButtons,
                             int assetX, int assetY, int col,
                             Command command, int currentLevel, int levelCount){
        if(currentLevel <= levelCount){
            listButtons.addNewButton("levels", col, 0,
                    assetX, assetY, buttonWidth, buttonHeight, command);
        }
        //put question picture:
        else{
            listButtons.addNewButton("levels", col, 0,
                    1 + (buttonWidth + 2) * 3, 1,
                    buttonWidth, buttonHeight, command);
        }
    }
}
