package com.missionsurvive.framework;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.impl.ButtonTouchListener;
import com.missionsurvive.framework.impl.Icon;
import com.missionsurvive.framework.impl.ListButtons;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuzmin on 04.05.18.
 */

public class ControlPanel {

    private String screen; //какому экрану принадлежит панель управления.
    private String name; //название панели управления.
    private Listener buttonTouchListener = new ButtonTouchListener();
    List<Button> buttons = new ArrayList<Button>();
    List<Icon> icons = new ArrayList<Icon>();
    private ListButtons rootList;

    private boolean isActivated; //активирована ли панель управления.

    public ControlPanel(String screen, String name){
        this.screen = screen;
        this.name = name;
        isActivated = false;
    }

    public void addListButtons(ListButtons listButtons){
        rootList = listButtons;
    }

    public ListButtons getListButtons(String name){
        if(rootList != null){
            int listsCount = rootList.getLists().size();
            for(int i = 0; i < listsCount; i++){
                ListButtons listButtons = rootList.getLists().get(i);
                if(listButtons.getName().equalsIgnoreCase(name)){
                    return listButtons;
                }
            }
        }
        return null;
    }

    public void addButton(Button button){
        buttons.add(button);
        buttonTouchListener.attach((Observer)button);
    }

    public void addIcon(Icon icon){
        icons.add(icon);
    }

    public Icon getIcon(int whichIcon){
        return icons.get(whichIcon);
    }

    public void drawPanel(SpriteBatch batch){
        drawIcons(batch);
        drawButtons(batch);
        drawListsButtons(batch);
    }

    public void drawIcons(SpriteBatch batch){
        int numIcons = icons.size();
        for(int whichIcon = 0; whichIcon < numIcons; whichIcon++){
            icons.get(whichIcon).drawIcon(batch);
        }
    }

    public void drawButtons(SpriteBatch batch){
        int numButtons = buttons.size();
        for(int i = 0; i < numButtons; i++){
            buttons.get(i).drawButton(batch);
        }
    }

    public void onTouch(float scaleX, float scaleY){
        buttonTouchListener.trackEvents(scaleX, scaleY);
        if(rootList != null){
            rootList.getListener().trackEvents(scaleX, scaleY);
        }
    }

    public void drawListsButtons(SpriteBatch batch){
        if(rootList != null){
            rootList.drawLists(batch);
        }
    }

    public String getScreen(){
        return screen;
    }

    public String getName(){
        return name;
    }

    public void setActivated(boolean isActivated){
        this.isActivated = isActivated;
    }

    public boolean isActivated(){
        return isActivated;
    }
}
