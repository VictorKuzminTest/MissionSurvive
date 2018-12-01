package com.missionsurvive.scenarios.commands;

import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.screens.GameScreen;
import com.missionsurvive.utils.Controls;

/**
 * The command opens or closes control panel needed.
 */
public class OpenCloseCPCommand implements Command{

    private String key;
    private String value;

    public OpenCloseCPCommand(String key, String value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String execute(String key, String value) {
        ...
        return null;
    }

    private void openClosePanel(String name, boolean isActivate){
        int cpCount = Controls.controlPanels.length;
        for(int i = 0; i < cpCount; i++){
            ControlPanel controlPanel = Controls.controlPanels[i];
            if(controlPanel.getName().equalsIgnoreCase(name)){
                controlPanel.setActivated(isActivate);
            }
        }
    }

    @Override
    public void setScreen(GameScreen gameScreen) {

    }
}
