package com.missionsurvive.tests;

import com.missionsurvive.commands.Command;
import com.missionsurvive.framework.Button;
import com.missionsurvive.framework.Listener;
import com.missionsurvive.framework.Observer;
import com.missionsurvive.framework.impl.ActionButton;
import com.missionsurvive.framework.impl.ButtonTouchListener;
import com.missionsurvive.tests.commands.OnClickButtonLogCommand;

import java.util.ArrayList;

/**
 * Created by kuzmin on 26.04.18.
 */

public class ButtonsTest {

    private ArrayList<Button> buttons = new ArrayList<Button>();
    private Listener buttonTouchListener = new ButtonTouchListener();

    private int width, height;
    private int numButtons;
    private int screenX;
    private int spaceYBetweenButtons;

    public ButtonsTest(){
        width = 60;
        height = 30;
        screenX = 10;
        spaceYBetweenButtons = 10;
        numButtons = 5;

        for(int i = 0; i < numButtons; i++){
            Observer button = new ActionButton("button", screenX, spaceYBetweenButtons + i * height,
                    0, 0, width, height, null);
            buttons.add((Button)button);
            buttonTouchListener.attach(button);
            //shows in sys log the coords of button touched:
            Command command = new OnClickButtonLogCommand((Button)button);
            ((Button)button).setCommand(command);
        }
    }

    public void touchButtons(float scaleX, float scaleY){
        ((ButtonTouchListener)buttonTouchListener).trackEvents(scaleX, scaleY);
    }

    public ArrayList<Button> getButtons(){
        return buttons;
    }

}
