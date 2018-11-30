package com.missionsurvive.scenarios.controlscenarios;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.utils.Controls;

import java.util.ArrayList;
import java.util.List;

public class EndCS implements ControlScenario{

    public static final int END_GAME_PANEL = 0;

    private List<ControlPanel> listOfPanels;

    public EndCS(){
        listOfPanels = new ArrayList<ControlPanel>();
        setControlPanels();
    }

    @Override
    public List<ControlPanel> getControlPanels() {
        return listOfPanels;
    }

    @Override
    public void setControlPanels() {
        for(int i = 0; i < Controls.controlPanels.length; i++){
            if(Controls.controlPanels[i].getName().equalsIgnoreCase("EndGame")){
                Controls.controlPanels[i].setActivated(false);
                listOfPanels.add(Controls.controlPanels[i]);
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
    public boolean onTouchPanels(float deltaTime, float scaleX, float scaleY) {
        int numPanels = listOfPanels.size();
        for(int whichPanel = 0; whichPanel < numPanels; whichPanel++){
            ControlPanel controlPanel = listOfPanels.get(whichPanel);
            if(controlPanel.isActivated() == true){
                controlPanel.onTouch(deltaTime, scaleX, scaleY);
            }
        }
        return false;
    }

    @Override
    public void action(Object object) {

    }
}
