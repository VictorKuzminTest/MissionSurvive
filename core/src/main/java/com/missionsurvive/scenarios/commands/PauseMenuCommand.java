package com.missionsurvive.scenarios.commands;

import com.missionsurvive.MSGame;
import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.screens.GameScreen;
import com.missionsurvive.utils.Assets;
import com.missionsurvive.utils.Controls;

public class PauseMenuCommand implements Command{

    public static final int ACTION_PAUSE = 0;
    public static final int ACTION_RESUME = 1;
    public static final int ACTION_EXIT = 2;

    private GameScreen gameScreen;

    private int actionId;

    public PauseMenuCommand(String value){
        if(value.equalsIgnoreCase("pause")){
            actionId = ACTION_PAUSE;
        }
        else if(value.equalsIgnoreCase("resume")){
            actionId = ACTION_RESUME;
        }
        else if(value.equalsIgnoreCase("exit")){
            actionId = ACTION_EXIT;
        }
    }

    @Override
    public String execute(String key, String value) {
        switch (actionId){
            case ACTION_PAUSE:
                gameScreen.pause(true);
                openClosePanel("pause", true);
                break;
            case ACTION_RESUME:
                gameScreen.pause(false);
                openClosePanel("pause" , false);
                break;
            case ACTION_EXIT:
                openClosePanel("pause", false);
                MSGame game = Assets.getGame();
                game.setScreen(game.getScreenFactory()
                        .newScreen("GameMenuScreen", null, null));
                break;
        }
        return null;
    }

    @Override
    public void setScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
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