package com.missionsurvive.scenarios.controlscenarios;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.utils.Controls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuzmin on 07.05.18.
 */

public class MenuCS implements ControlScenario {

    private List<ControlPanel> listOfPanels = new ArrayList<ControlPanel>();

    public MenuCS(){
        setControlPanels();
    }

    @Override
    public List<ControlPanel> getControlPanels() {
        return null;
    }

    @Override
    public void setControlPanels() {
        for(int i = 0; i < Controls.controlPanels.length; i++){
            if(Controls.controlPanels[i].getScreen().equalsIgnoreCase("Menu")){
                listOfPanels.add(Controls.controlPanels[i]);
            }
        }
        listOfPanels.get(0).setActivated(true);
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
    public void onTouchPanels(float scaleX, float scaleY) {
        int numPanels = listOfPanels.size();
        for(int whichPanel = 0; whichPanel < numPanels; whichPanel++){
            ControlPanel controlPanel = listOfPanels.get(whichPanel);
            if(controlPanel.isActivated() == true){
                controlPanel.onTouch(scaleX, scaleY);
            }
        }
    }

    @Override
    public void touchingPanels(boolean touch) {

    }

    @Override
    public boolean isTouchingPanels() {
        return false;
    }
}
