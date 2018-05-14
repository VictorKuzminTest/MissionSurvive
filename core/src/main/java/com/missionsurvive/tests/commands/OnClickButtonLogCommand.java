package com.missionsurvive.tests.commands;

import com.missionsurvive.scenarios.commands.Command;
import com.missionsurvive.framework.Button;

/**
 * Created by kuzmin on 27.04.18.
 */
public class OnClickButtonLogCommand implements Command{

    private Button button;

    //for logging rows and cols int listButtons:
    private int row, col;

    public OnClickButtonLogCommand(Button button){
        this.button = button;
    }

    public OnClickButtonLogCommand(int row, int col){
        this.row = row;
        this.col = col;
    }

    /**
     * If there is no button object attached, showing only row - col message through log.
     * @param data
     */
    @Override
    public void execute(String data) {
        if(button != null){
            int endX = button.getStartX() + button.getButtonWidth();
            int endY = button.getStartY() + button.getButtonHeight();
            System.out.println("startX = " + button.getStartX() +
                    " startY = " + button.getStartY() +
                    " endX = " + endX +
                    " endY = " + endY);
        }
        else{
            System.out.println("row = " + row + " col = " + col);
        }
    }
}
