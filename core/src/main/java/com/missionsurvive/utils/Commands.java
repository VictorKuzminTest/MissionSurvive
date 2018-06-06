package com.missionsurvive.utils;


import com.missionsurvive.scenarios.commands.BlockLayersCommand;
import com.missionsurvive.scenarios.commands.Command;
import com.missionsurvive.scenarios.commands.OpenCloseCPCommand;
import com.missionsurvive.scenarios.commands.PlayProjectCommand;
import com.missionsurvive.scenarios.commands.PutPlayerCommand;
import com.missionsurvive.scenarios.commands.SaveLoadMapCommand;
import com.missionsurvive.scenarios.commands.NewMapCommand;
import com.missionsurvive.scenarios.commands.PutBotCommand;
import com.missionsurvive.scenarios.commands.PutTileCommand;
import com.missionsurvive.scenarios.commands.ScrollDirCommand;
import com.missionsurvive.scenarios.commands.ShowPopupCommand;
import com.missionsurvive.scenarios.commands.ShowLayerCommand;
import com.missionsurvive.scenarios.commands.ToScreenCommand;
import com.missionsurvive.scenarios.commands.WithTileCommand;

/**
 * Created by kuzmin on 07.05.18.
 */

public class Commands {

    public static Command getCommand(String action){
        if(action != null){
            Command command;
            if(action.equalsIgnoreCase("EditorScreen")){
                return command = new ToScreenCommand(ToScreenCommand.TO_EDITOR_SCREEN);
            }
            else if(action.equalsIgnoreCase("ScrollerScreen")){
                return command = new ToScreenCommand(ToScreenCommand.TO_SCROLLER_SCREEN);
            }
            else if(action.equalsIgnoreCase("ToDifficultyGameMenuFromStart")){
                return command = new OpenCloseCPCommand("ToDifficultyGameMenuFromStart", null);
            }
            else if(action.equalsIgnoreCase("ToStartGameMenu")){
                return command = new OpenCloseCPCommand("ToStartGameMenu", null);
            }
            else if(action.equalsIgnoreCase("beginner")){
                return command = new OpenCloseCPCommand("ToChooseLevelMenu", null);
            }
            else if(action.equalsIgnoreCase("ToDifficultyGameMenuFromChoose")){
                return command = new OpenCloseCPCommand("ToDifficultyGameMenuFromChoose", null);
            }
            else if(action.equalsIgnoreCase("PlayProjectCommand")){
                return command = new PlayProjectCommand();
            }
            else if(action.equalsIgnoreCase("PutPlayerCommand")){
                return command = new PutPlayerCommand();
            }
            else if(action.equalsIgnoreCase("showLayer")){
                return command = new ShowLayerCommand();
            }
            else if(action.equalsIgnoreCase("blockLayers")){
                return command = new BlockLayersCommand();
            }
            else if(action.equalsIgnoreCase("withTileAction")){
                return command = new WithTileCommand();
            }
            else if(action.equalsIgnoreCase("putTile")){
                return new PutTileCommand();
            }
            else if(action.equalsIgnoreCase("showPopup")){
                return new ShowPopupCommand();
            }
            else if(action.equalsIgnoreCase("putBot")){
                return new PutBotCommand();
            }
            else if(action.equalsIgnoreCase("save_load_map")){
                return new SaveLoadMapCommand();
            }
            else if(action.equalsIgnoreCase("newMap")){
                return new NewMapCommand();
            }
            else if(action.equalsIgnoreCase("scrollDir")){
                return new ScrollDirCommand();
            }
        }
        return null;
    }
}
