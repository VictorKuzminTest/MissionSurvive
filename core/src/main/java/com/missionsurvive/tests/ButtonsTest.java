package com.missionsurvive.tests;

import com.missionsurvive.scenarios.commands.Command;
import com.missionsurvive.framework.Button;
import com.missionsurvive.framework.Listener;
import com.missionsurvive.framework.Observer;
import com.missionsurvive.framework.impl.ActionButton;
import com.missionsurvive.framework.impl.ButtonTouchListener;
import com.missionsurvive.framework.impl.ListButtons;
import com.missionsurvive.framework.impl.ListButtonsTouchListener;
import com.missionsurvive.tests.commands.OnClickButtonLogCommand;

import java.util.ArrayList;

public class ButtonsTest {

    private ArrayList<Button> buttons = new ArrayList<Button>();
    private Listener buttonTouchListener = new ButtonTouchListener();
    private ListButtons rootList;
    private Observer listOne;
    private Observer listTwo;

    private int width, height;
    private int numButtons;
    private int screenX;
    private int spaceYBetweenButtons;
    private int listButtonWidth = 16;
    private int listButtonHeight = 16;
    private int numRowsInList = 5;
    private int numColsInList = 5;
    private int spaceBetweenButtonsInList = 5;

    public ButtonsTest(){
        width = 60;
        height = 30;
        screenX = 10;
        spaceYBetweenButtons = 10;
        numButtons = 5;

        for(int i = 0; i < numButtons; i++){
            Observer button = new ActionButton("clear", screenX, spaceYBetweenButtons + i * height,
                    0, 0, width, height, null);
            buttons.add((Button)button);
            buttonTouchListener.attach(button);
            //shows in sys log the coords of button touched:
            Command command = new OnClickButtonLogCommand((Button)button);
            ((Button)button).setCommand(command);
        }
        initListButtons();
    }

    public void initListButtons(){
        listOne = new ListButtons("list1", null, spaceBetweenButtonsInList,
                100, 100, 0, 0, 50, 50, "grid");

        listTwo = new ListButtons("list2", null, spaceBetweenButtonsInList,
                300, 100, 0, 0, 50, 50, "grid");


        fillListWithButtons((ListButtons) listOne);
        fillListWithButtons((ListButtons) listTwo);

        Listener listener = new ListButtonsTouchListener();
        rootList = new ListButtons(listener);

        listener.attach(listOne);
        listener.attach(listTwo);
        rootList.addList((ListButtons) listOne);
        rootList.addList((ListButtons) listTwo);
    }

    public void fillListWithButtons(ListButtons listButtons){
        for(int row = 0; row < numRowsInList; row++){
            for(int col = 0; col < numColsInList; col++){
                listButtons.addNewButton("lev1", col, row, col * listButtonWidth, row * listButtonHeight,
                        listButtonWidth, listButtonHeight, new OnClickButtonLogCommand(row, col));
            }
        }
    }

    public void touchButtons(float deltaTime, float scaleX, float scaleY){
        buttonTouchListener.trackEvents(deltaTime, scaleX, scaleY);
    }

    public void touchLists(float deltaTime, float scaleX, float scaleY){
        rootList.getListener().trackEvents(deltaTime, scaleX, scaleY);
    }

    public ArrayList<Button> getButtons(){
        return buttons;
    }

    public ListButtons getListsButtons(){
        return rootList;
    }
}
