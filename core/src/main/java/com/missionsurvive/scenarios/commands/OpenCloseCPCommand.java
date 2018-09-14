package com.missionsurvive.scenarios.commands;

import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.utils.Controls;

/**
 * Created by kuzmin on 01.06.18.
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
        if(this.key != null){
            if(this.key.equalsIgnoreCase("ToDifficultyGameMenuFromStart")){
                openClosePanel("StartGameMenu", false);
                openClosePanel("DifficultyGameMenu", true);
            }
            else if(this.key.equalsIgnoreCase("ToChooseLevelMenuBeginner")){
                openClosePanel("DifficultyGameMenu", false);
                openClosePanel("ChooseLevelMenuBeginner", true);
            }
            else if(this.key.equalsIgnoreCase("ToChooseLevelMenuExperienced")){
                openClosePanel("DifficultyGameMenu", false);
                openClosePanel("ChooseLevelMenuExperienced", true);
            }
            else if(this.key.equalsIgnoreCase("ToStartGameMenu")){
                openClosePanel("DifficultyGameMenu", false);
                openClosePanel("StartGameMenu", true);
            }
            else if(this.key.equalsIgnoreCase("ToDifficultyGameMenuFromChoose")){
                openClosePanel("ChooseLevelMenuBeginner", false);
                openClosePanel("ChooseLevelMenuExperienced", false);
                openClosePanel("DifficultyGameMenu", true);
            }
        }
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

}
