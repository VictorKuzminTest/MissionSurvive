package com.missionsurvive.tests.commands;

import com.missionsurvive.commands.Command;
import com.missionsurvive.framework.Button;

/**
 * Created by kuzmin on 27.04.18.
 */
public class OnClickButtonLogCommand implements Command{

    private Button button;

    public OnClickButtonLogCommand(Button button){
        this.button = button;
    }

    @Override
    public void execute(String data) {
        int endX = button.getStartX() + button.getButtonWidth();
        int endY = button.getStartY() + button.getButtonHeight();
        System.out.println("startX = " + button.getStartX() +
                " startY = " + button.getStartY() +
                " endX = " + endX +
                " endY = " + endY);
    }
}
