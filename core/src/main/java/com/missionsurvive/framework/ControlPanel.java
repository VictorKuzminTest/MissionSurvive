package com.missionsurvive.framework;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    XML xml;

    List<Button> buttons = new ArrayList<Button>();
    List<Icon> icons = new ArrayList<Icon>();
    List<ListButtons> listsButtons = new ArrayList<ListButtons>();

    List<String> listOfTags = new ArrayList<String>();    //в этом листиннге содержится инфа о каждом тэге (имя тэга, и его аттрибуты).

    private boolean isActivated; //активирована ли панель управления.


    public ControlPanel(String screen, String name){
        this.screen = screen;
        this.name = name;
        isActivated = false;
    }

    public void addListButtons(ListButtons listButtons){
        this.listsButtons.add(listButtons);
    }

    public ListButtons getListButtons(String name){
        for(int whichList = 0; whichList < listsButtons.size(); whichList++){
            if(listsButtons.get(whichList).getName().equalsIgnoreCase(name)){
                return listsButtons.get(whichList);
            }
        }
        return null;
    }

    public void addButton(Button button){
        buttons.add(button);
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
    }

    public void drawIcons(SpriteBatch batch){
        int numIcons = icons.size();
        for(int whichIcon = 0; whichIcon < numIcons; whichIcon++){
            icons.get(whichIcon).drawIcon(batch);
        }
    }

    public void drawButtons(SpriteBatch batch){
        int numButtons = buttons.size();
        for(int whichIcon = 0; whichIcon < numButtons; whichIcon++){
            buttons.get(whichIcon).drawButton(batch);
        }
        drawListsButtons(batch);
    }

    public void touchButtons(Game game, ControlScenario controlScenario, TouchControl touchControl, List<TouchEvent> touchEvents, Object object){
        int touching = 0;

        int len = buttons.size();
        for(int whichButton = 0; whichButton < len; whichButton++){

            touching = buttons.get(whichButton).isTouching(touchControl, touchEvents, game, controlScenario, null, object);

            if(touching > 0){ //means, that I just touched it down.
                controlScenario.touchingPanels(true);
            }
            else{
                controlScenario.touchingPanels(false);
            }
        }

        if(touching == 0){
			/*I put "if" here, because, if I touch buttons while putting down my finger,
			* there is no use to check listButtons, because you cannot make any actions anyway with these lists. You can only use listButtons when
			* you put down your finger inside the area of the current list. So if touching == 0, it means you didn't touch any button of the control panel,
			 * so you can check list of buttons of the current panel.*/
            touchListsButtons(touchEvents, touchControl, game, controlScenario, object);
        }
    }

    public void touchListsButtons(List<TouchEvent> touchEvents, TouchControl touchControl, Game game, ControlScenario controlScenario, Object object){
        int touching = 0;

        int numLists = listsButtons.size();
        for(int whichList = 0; whichList < numLists; whichList++){
            ListButtons listButtons = listsButtons.get(whichList);
            touching = listButtons.touchButtons(touchEvents, listButtons, touchControl, game, controlScenario, object);

            if(touching > 0){ //means, that I just touched it down.
                controlScenario.touchingPanels(true);
            }
            else{
                controlScenario.touchingPanels(false);
            }
        }
    }

    public void drawListsButtons(SpriteBatch batch){
        int numLists = listsButtons.size();
        for(int whichList = 0; whichList < numLists; whichList++){
            listsButtons.get(whichList).drawButtons(batch);
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
