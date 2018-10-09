package com.missionsurvive.scenarios.controlscenarios;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.utils.Controls;

import java.util.ArrayList;
import java.util.List;

public class PurchaseCS implements ControlScenario{

    private List<ControlPanel> listOfPanels = new ArrayList<ControlPanel>();

    public PurchaseCS(){
        setControlPanels();
    }

    @Override
    public List<ControlPanel> getControlPanels() {
        return null;
    }

    @Override
    public void setControlPanels() {
        for(int i = 0; i < Controls.controlPanels.length; i++){
            if(Controls.controlPanels[i].getScreen().equalsIgnoreCase("PurchaseScreen")){
                ControlPanel controlPanel = Controls.controlPanels[i];
                controlPanel.setActivated(true);
                listOfPanels.add(controlPanel);
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
