package com.missionsurvive.utils;


import com.missionsurvive.scenarios.commands.BlockLayersCommand;
import com.missionsurvive.scenarios.commands.Command;
import com.missionsurvive.scenarios.commands.EndGameCommand;
import com.missionsurvive.scenarios.commands.MuteCommand;
import com.missionsurvive.scenarios.commands.OpenCloseCPCommand;
import com.missionsurvive.scenarios.commands.PlayProjectCommand;
import com.missionsurvive.scenarios.commands.PutPlayerCommand;
import com.missionsurvive.scenarios.commands.QuitGameCommand;
import com.missionsurvive.scenarios.commands.SaveLoadMapCommand;
import com.missionsurvive.scenarios.commands.NewMapCommand;
import com.missionsurvive.scenarios.commands.PutBotCommand;
import com.missionsurvive.scenarios.commands.PutTileCommand;
import com.missionsurvive.scenarios.commands.ScrollDirCommand;
import com.missionsurvive.scenarios.commands.ShowHideBotPosCommand;
import com.missionsurvive.scenarios.commands.PauseMenuCommand;
import com.missionsurvive.scenarios.commands.ShowPopupCommand;
import com.missionsurvive.scenarios.commands.ShowLayerCommand;
import com.missionsurvive.scenarios.commands.ToLevelCommand;
import com.missionsurvive.scenarios.commands.ToPurchaseScreenCommand;
import com.missionsurvive.scenarios.commands.WithTileCommand;

/**
 * Created by kuzmin on 07.05.18.
 */

public class Commands {

    public static Command getCommand(String action){
        if(action != null){
            Command command;
            if(action.equalsIgnoreCase("ToDifficultyGameMenuFromStart")){
                return command = new OpenCloseCPCommand("ToDifficultyGameMenuFromStart", null);
            }
            else if(action.equalsIgnoreCase("ToStartGameMenu")){
                return command = new OpenCloseCPCommand("ToStartGameMenu", null);
            }
            else if(action.equalsIgnoreCase("EndGame")){
                return command = new EndGameCommand();
            }
            else if(action.equalsIgnoreCase("quitGame")){
                return command = new QuitGameCommand();
            }
            else if(action.equalsIgnoreCase("PurchaseFullVersion")) {
                return command = new ToPurchaseScreenCommand();
            }
            else if(action.equalsIgnoreCase("purchase")){
                return command = new ShowPopupCommand(ShowPopupCommand.POPUP_PURCHASE);
            }
            else if(action.equalsIgnoreCase("exit")){
                return command = new PauseMenuCommand("exit");
            }
            else if(action.equalsIgnoreCase("pause")){
                return command = new PauseMenuCommand("pause");
            }
            else if(action.equalsIgnoreCase("sound")){
                return command = new MuteCommand();
            }
            else if(action.equalsIgnoreCase("resume")){
                return command = new PauseMenuCommand("resume");
            }
            else if(action.equalsIgnoreCase("beginner")){
                return command = new OpenCloseCPCommand("ToChooseLevelMenuBeginner", null);
            }
            else if(action.equalsIgnoreCase("experienced")){
                return command = new OpenCloseCPCommand("ToChooseLevelMenuExperienced", null);
            }
            else if(action.equalsIgnoreCase("ToDifficultyGameMenuFromChoose")){
                return command = new OpenCloseCPCommand("ToDifficultyGameMenuFromChoose", null);
            }
            else if(action.equalsIgnoreCase("ToLevel1Beginner")){
                return command = new ToLevelCommand("levs/level11");
            }
            else if(action.equalsIgnoreCase("ToLevel1Experienced")){
                return command = new ToLevelCommand("levs/level12");
            }
            else if(action.equalsIgnoreCase("ToLevel2Beginner")){
                return command = new ToLevelCommand("levs/level21");
            }
            else if(action.equalsIgnoreCase("ToLevel2Experienced")){
                return command = new ToLevelCommand("levs/level22");
            }
            else if(action.equalsIgnoreCase("ToLevel3Beginner")){
                return command = new ToLevelCommand("levs/level31");
            }
            else if(action.equalsIgnoreCase("ToLevel3Experienced")){
                return command = new ToLevelCommand("levs/level32");
            }
            else if(action.equalsIgnoreCase("ToLevel4Beginner")){
                return command = new ToLevelCommand("ToLevel4Beginner");
            }
            else if(action.equalsIgnoreCase("ToLevel4Experienced")){
                return command = new ToLevelCommand("ToLevel4Experienced");
            }
            else if(action.equalsIgnoreCase("ToLevel5Beginner")){
                return command = new ToLevelCommand("levs/level51");
            }
            else if(action.equalsIgnoreCase("ToLevel5Experienced")){
                return command = new ToLevelCommand("levs/level52");
            }
            else if(action.equalsIgnoreCase("ToLevel6Experienced")){
                return command = new ToLevelCommand("levs/level6");
            }
            else if(action.equalsIgnoreCase("NextLevel")){
                return command = new ToLevelCommand("NextLevel");
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
            else if(action.equalsIgnoreCase("showBots")){
                return command = new ShowHideBotPosCommand();
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
