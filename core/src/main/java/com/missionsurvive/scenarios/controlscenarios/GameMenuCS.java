package com.missionsurvive.scenarios.controlscenarios;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.framework.impl.ListButtons;
import com.missionsurvive.screens.GameMenuScreen;
import com.missionsurvive.utils.Controls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuzmin on 31.05.18.
 */

public class GameMenuCS implements ControlScenario{

    public static final int START_MENU = 0;
    public static final int CHOOSE_LEVEL_MENU = 2;

    private List<ControlPanel> listOfPanels = new ArrayList<ControlPanel>();
    private ListingBuilder chooseStageListingBuilder;

    public GameMenuCS(){
        setControlPanels();

        chooseStageListingBuilder = new ChooseStageListingBuilder();
        ListButtons tilesetList = listOfPanels.get(CHOOSE_LEVEL_MENU).getListButtons("chooseLevel");
        chooseStageListingBuilder.addButtons(tilesetList);
    }

    @Override
    public List<ControlPanel> getControlPanels() {
        return null;
    }

    @Override
    public void setControlPanels() {
        for(int i = 0; i < Controls.controlPanels.length; i++){
            if(Controls.controlPanels[i].getScreen().equalsIgnoreCase("GameMenuScreen")){
                listOfPanels.add(Controls.controlPanels[i]);
            }
        }
        listOfPanels.get(START_MENU).setActivated(true);
    }

    @Override
    public void drawPanels(SpriteBatch batch) {
        int numPanels = listOfPanels.size();
        for(int whichPanel = 0; whichPanel < numPanels; whichPanel++){
            ControlPanel controlPanel = listOfPanels.get(whichPanel);
            if(controlPanel.isActivated() == true){
                controlPanel.drawPanel(batch);
            }
        }
    }

    @Override
    public boolean onTouchPanels(float delta, float scaleX, float scaleY) {
        int numPanels = listOfPanels.size();
        for(int whichPanel = 0; whichPanel < numPanels; whichPanel++){
            ControlPanel controlPanel = listOfPanels.get(whichPanel);
            if(controlPanel.isActivated() == true){
                controlPanel.onTouch(delta, scaleX, scaleY);
            }
        }
        return false;
    }

    @Override
    public void action(Object object) {

    }
}
