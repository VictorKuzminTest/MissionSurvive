package com.missionsurvive.scenarios.controlscenarios;

import com.badlogic.gdx.Screen;
import com.missionsurvive.framework.impl.ListButtons;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.commands.BlockLayersCommand;
import com.missionsurvive.scenarios.commands.PlayProjectCommand;
import com.missionsurvive.scenarios.commands.PutPlayerCommand;
import com.missionsurvive.scenarios.commands.ShowPopupCommand;
import com.missionsurvive.scenarios.commands.ScrollDirCommand;
import com.missionsurvive.scenarios.commands.ShowHideBotPosCommand;
import com.missionsurvive.scenarios.commands.ShowLayerCommand;
import com.missionsurvive.scenarios.commands.WithTileCommand;
import com.missionsurvive.screens.EditorScreen;
import com.missionsurvive.utils.Assets;
import com.missionsurvive.utils.Commands;

import java.util.ArrayList;

/**
 * Created by kuzmin on 14.05.18.
 */

public class PlatformerScreenListingBuilder implements ListingBuilder{

    private ArrayList<MapTer> mapTerArrayList;
    private MapEditor mapEditor;
    private Screen screen;
    private Scenario scenario;

    public PlatformerScreenListingBuilder(Screen screen, MapEditor mapEditor, Scenario scenario,
                                          ArrayList<MapTer> mapTerArrayList){
        this.mapTerArrayList = mapTerArrayList;
        this.mapEditor = mapEditor;
        this.screen = screen;
        this.scenario = scenario;
    }

    @Override
    public void addButtons(ListButtons listButtons) {
        PlayProjectCommand playProjectCommandCommand = (PlayProjectCommand) Commands.getCommand("PlayProjectCommand");
        playProjectCommandCommand.setScreen((EditorScreen) screen);
        listButtons.addNewButton("iconseditor", 0, 1, 0, 0, 32, 32, playProjectCommandCommand);

        PutPlayerCommand putPlayerCommandCommand = (PutPlayerCommand) Commands.getCommand("PutPlayerCommand");
        putPlayerCommandCommand.setScreen((EditorScreen) screen);
        listButtons.addNewButton("iconseditor", 0, 2, 0, 32, 32, 32, putPlayerCommandCommand);

        ShowLayerCommand showLayer1 = (ShowLayerCommand)Commands.getCommand("showLayer");
        showLayer1.setScreenAndLayer((EditorScreen) screen, EditorScreen.FIRST_LAYER);
        listButtons.addNewButton("iconseditor", 0, 3, 0, 64, 32, 32, showLayer1);

        ShowLayerCommand showAllLayers = (ShowLayerCommand)Commands.getCommand("showLayer");
        showAllLayers.setScreenAndLayer((EditorScreen) screen, EditorScreen.ALL_LAYERS);
        listButtons.addNewButton("iconseditor", 0, 4, 0, 96, 32, 32, showAllLayers);

        BlockLayersCommand blockLayers = (BlockLayersCommand)Commands.getCommand("blockLayers");
        blockLayers.setScreen((EditorScreen) screen);
        listButtons.addNewButton("iconseditor", 0, 5, 64, 96, 32, 32, blockLayers);

        //block tile:
        WithTileCommand blockTile = (WithTileCommand)Commands.getCommand("withTileAction");
        blockTile.setAction(mapEditor, mapTerArrayList, WithTileCommand.BLOCK_TILE);
        listButtons.addNewButton("iconseditor", 0, 6, 32, 32, 32, 32, blockTile);

        //put ladder:
        WithTileCommand putLadder = (WithTileCommand)Commands.getCommand("withTileAction");
        putLadder.setAction(mapEditor, mapTerArrayList, WithTileCommand.PUT_LADDER);
        listButtons.addNewButton("iconseditor", 0, 7, 96, 0, 32, 32, putLadder);

        //unblock tile:
        WithTileCommand unblockTile = (WithTileCommand)Commands.getCommand("withTileAction");
        unblockTile.setAction(mapEditor, mapTerArrayList, WithTileCommand.UNBLOCK_TILE);
        listButtons.addNewButton("iconseditor", 0, 8, 32, 64, 32, 32, unblockTile);

        //remove tile:
        WithTileCommand removeTile = (WithTileCommand)Commands.getCommand("withTileAction");
        removeTile.setAction(mapEditor, mapTerArrayList, WithTileCommand.REMOVE_TILE);
        listButtons.addNewButton("iconseditor", 0, 9, 64, 64, 32, 32, removeTile);

        //put bot:
        ShowPopupCommand popupNewBot = (ShowPopupCommand)Commands.getCommand("showPopup");
        popupNewBot.setActivity(Assets.getGame().getActivityCallback(), ShowPopupCommand.POPUP_NEW_BOT);
        listButtons.addNewButton("iconseditor", 0, 10, 32, 0, 32, 32, popupNewBot);

        //show position of enemies:
        ShowHideBotPosCommand showEnemyPos = (ShowHideBotPosCommand)Commands.getCommand("showBots");
        showEnemyPos.setScreen((EditorScreen) screen);
        listButtons.addNewButton("iconsEditor", 0, 11, 96, 96, 32, 32, showEnemyPos);

        //scrolling direction of a map: horizontal or vertical:
        ScrollDirCommand scrollDirCommand = (ScrollDirCommand)Commands.getCommand("scrollDir");
        scrollDirCommand.setScenario(scenario);
        listButtons.addNewButton("iconseditor", 0, 12, 96, 32, 32, 32, scrollDirCommand);

        //new map:
        ShowPopupCommand popupNewMap = (ShowPopupCommand)Commands.getCommand("showPopup");
        popupNewMap.setActivity(Assets.getGame().getActivityCallback(), ShowPopupCommand.POPUP_NEW_MAP);
        listButtons.addNewButton("iconseditor", 0, 13, 64, 32, 32, 32, popupNewMap);

        //save map:
        ShowPopupCommand popupSave = (ShowPopupCommand)Commands.getCommand("showPopup");
        popupSave.setActivity(Assets.getGame().getActivityCallback(), ShowPopupCommand.POPUP_SAVE);
        listButtons.addNewButton("iconseditor", 0, 14, 32, 96, 32, 32, popupSave);

        //load map:
        ShowPopupCommand popupLoad = (ShowPopupCommand)Commands.getCommand("showPopup");
        popupLoad.setActivity(Assets.getGame().getActivityCallback(), ShowPopupCommand.POPUP_LOAD);
        listButtons.addNewButton("iconseditor", 0, 15, 64, 0, 32, 32, popupLoad);
    }
}
