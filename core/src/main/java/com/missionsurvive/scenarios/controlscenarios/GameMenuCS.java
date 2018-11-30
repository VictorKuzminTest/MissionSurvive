package com.missionsurvive.scenarios.controlscenarios;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.framework.impl.ListButtons;
import com.missionsurvive.utils.Controls;

import java.util.ArrayList;
import java.util.List;

public class GameMenuCS implements ControlScenario{

    public static final int START_MENU = 0;
    public static final int CHOOSE_LEVEL_MENU_BEGINNER = 2;
    public static final int CHOOSE_LEVEL_MENU_EXPERIENCED = 3;

    private List<ControlPanel> listOfPanels = new ArrayList<ControlPanel>();
    private ListingBuilder chooseStageListingBuilderBeginner;
    private ListingBuilder chooseStageListingBuilderExperienced;

    public GameMenuCS(){
        setControlPanels();

        //we create two builders for choosing levels: beginner, experienced.
        chooseStageListingBuilderBeginner = new ChooseStageListingBuilder(
                ChooseStageListingBuilder.DIFFICULTY_BEGINNER);
        chooseStageListingBuilderExperienced = new ChooseStageListingBuilder(
                ChooseStageListingBuilder.DIFFICULTY_EXPERIENCED);

        ListButtons chooseLevelListBeginner = listOfPanels.get(CHOOSE_LEVEL_MENU_BEGINNER)
                .getListButtons("chooseLevelBeginner");
        chooseStageListingBuilderBeginner.addButtons(chooseLevelListBeginner);

        ListButtons chooseLevelListExperienced = listOfPanels.get(CHOOSE_LEVEL_MENU_EXPERIENCED)
                .getListButtons("chooseLevelExperienced");
        chooseStageListingBuilderExperienced.addButtons(chooseLevelListExperienced);
    }

    @Override
    public List<ControlPanel> getControlPanels() {
        return null;
    }

    @Override
    public void setControlPanels() {
        for(int i = 0; i < Controls.controlPanels.length; i++){
            if(Controls.controlPanels[i].getScreen().equalsIgnoreCase("GameMenuScreen")){
                ControlPanel controlPanel = Controls.controlPanels[i];
                listOfPanels.add(controlPanel);
                if(controlPanel.getName().equalsIgnoreCase("StartGameMenu")){
                    controlPanel.setActivated(true);
                }
                else if(controlPanel.getName().equalsIgnoreCase("PurchaseFullVersion")){
                    controlPanel.setActivated(true);
                }
                else{
                    controlPanel.setActivated(false);
                }
            }
        }
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
