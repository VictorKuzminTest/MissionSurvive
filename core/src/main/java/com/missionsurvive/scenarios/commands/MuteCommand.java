package com.missionsurvive.scenarios.commands;

import com.missionsurvive.screens.GameScreen;
import com.missionsurvive.utils.Sounds;

public class MuteCommand implements Command{

    @Override
    public String execute(String key, String value) {
        String asset = null;
        if(!Sounds.mute){
            Sounds.mute = true;
            Sounds.pauseAllMusic();
            asset = ":srcX:33:srcY:33:";
        }
        else{
            Sounds.mute = false;
            Sounds.unpauseAllMusic();
            asset = ":srcX:1:srcY:33:";
        }
        return asset;
    }

    @Override
    public void setScreen(GameScreen gameScreen) {

    }
}
