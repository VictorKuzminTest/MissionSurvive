package com.missionsurvive.scenarios.controlscenarios;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.screens.GameScreen;
import com.missionsurvive.utils.Controls;

import java.util.ArrayList;
import java.util.List;

public class GameCS implements ControlScenario{
    private static final int HUD = 0;

    private List<ControlPanel> listOfPanels;
    private GameScreen gameScreen;

    public GameCS(GameScreen gameScreen){
        listOfPanels = new ArrayList<ControlPanel>();
        this.gameScreen = gameScreen;
        setControlPanels();
    }

    @Override
    public List<ControlPanel> getControlPanels() {
        return listOfPanels;
    }

    @Override
    public void setControlPanels() {
        for(int i = 0; i < Controls.controlPanels.length; i++){
            if(Controls.controlPanels[i].getName().equalsIgnoreCase("gameControls")){
                listOfPanels.add(Controls.controlPanels[i]);
                Controls.controlPanels[i].getButton(1).getCommand().setScreen(gameScreen);
            }
            else if(Controls.controlPanels[i].getName().equalsIgnoreCase("EndLevelMenu")){
                listOfPanels.add(Controls.controlPanels[i]);
            }
            else if(Controls.controlPanels[i].getName().equalsIgnoreCase("pause")){
                listOfPanels.add(Controls.controlPanels[i]);
                Controls.controlPanels[i].getButton(1).getCommand().setScreen(gameScreen);
            }
            else if(Controls.controlPanels[i].getName().equalsIgnoreCase("EndBeginner")){
                listOfPanels.add(Controls.controlPanels[i]);
                Controls.controlPanels[i].setActivated(false);
            }
        }
        listOfPanels.get(HUD).setActivated(true);
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
    public boolean onTouchPanels(float deltaTime, float scaleX, float scaleY) {
        boolean onTouch = false;
        int numPanels = listOfPanels.size();
        for(int whichPanel = 0; whichPanel < numPanels; whichPanel++){
            ControlPanel controlPanel = listOfPanels.get(whichPanel);
            if(controlPanel.isActivated() == true){
                onTouch = controlPanel.onTouch(deltaTime, scaleX, scaleY);
            }
        }
        return onTouch;
    }

    @Override
    public void action(Object object) {

    }
}
