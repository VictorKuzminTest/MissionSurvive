package com.missionsurvive.scenarios.commands;

import com.missionsurvive.MSGame;
import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.screens.GameScreen;
import com.missionsurvive.screens.ScreenFactory;
import com.missionsurvive.utils.Assets;
import com.missionsurvive.utils.Controls;

/**
 * Created by kuzmin on 07.06.18.
 */

public class ToLevelCommand implements Command{

    private String value;

    public ToLevelCommand(String value){
        this.value = value;
    }

    @Override
    public String execute(String key, String value) {
        hideEndLevelPanel();
        MSGame game = Assets.getGame();
        ScreenFactory screenFactory = game.getScreenFactory();
        game.setScreen(screenFactory
                .newScreen("LoadingLevelScreen", null, this.value));
        return null;
    }

    private void hideEndLevelPanel(){
        for(int i = 0; i < Controls.controlPanels.length; i++){
            ControlPanel cp = Controls.controlPanels[i];
            if(cp.getName().equalsIgnoreCase("EndLevelMenu")){
                if(cp.isActivated()){
                    cp.setActivated(false);
                }
            }
        }
    }

    @Override
    public void setScreen(GameScreen gameScreen) {

    }
}
