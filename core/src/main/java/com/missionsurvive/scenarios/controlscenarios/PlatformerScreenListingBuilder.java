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
        ...
    }
}
