package com.missionsurvive.utils;


import com.missionsurvive.scenarios.commands.Command;
import com.missionsurvive.scenarios.commands.PlayProject;
import com.missionsurvive.scenarios.commands.PutPlayer;
import com.missionsurvive.scenarios.commands.ToScreenCommand;

/**
 * Created by kuzmin on 07.05.18.
 */

public class Commands {

    public static Command getCommand(String action){
        if(action != null){
            Command command;
            if(action.equalsIgnoreCase("PlatformerScreen")){
                return command = new ToScreenCommand(ToScreenCommand.TO_PLATFORMER_SCREEN);
            }
            if(action.equalsIgnoreCase("ScrollerScreen")){
                return command = new ToScreenCommand(ToScreenCommand.TO_SCROLLER_SCREEN);
            }
            if(action.equalsIgnoreCase("PlayProject")){
                return command = new PlayProject();
            }
            if(action.equalsIgnoreCase("PutPlayer")){
                return command = new PutPlayer();
            }
        }
        return null;
    }
}
