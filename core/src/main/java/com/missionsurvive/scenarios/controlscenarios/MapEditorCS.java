package com.missionsurvive.scenarios.controlscenarios;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.framework.impl.ListButtons;
import com.missionsurvive.map.Map;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.commands.SaveLoadMapCommand;
import com.missionsurvive.scenarios.commands.NewMapCommand;
import com.missionsurvive.scenarios.commands.PutBotCommand;
import com.missionsurvive.utils.Assets;
import com.missionsurvive.utils.Commands;
import com.missionsurvive.utils.Controls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuzmin on 04.05.18.
 */

public class MapEditorCS implements ControlScenario {

    private List<ControlPanel> listOfPanels;
    private ArrayList<MapTer> mapTerArrayList;
    private ListingBuilder platformerListingBuilder;
    private ListingBuilder tilesetListingBuilder;

    public MapEditorCS(Screen screen, Map map, Scenario scenario){
        listOfPanels = new ArrayList<ControlPanel>();
        mapTerArrayList = new ArrayList<MapTer>();
        setControlPanels();

        platformerListingBuilder = new PlatformerScreenListingBuilder(screen,
                (MapEditor)map, scenario, mapTerArrayList);
        ListButtons controlList = listOfPanels.get(0).getListButtons("editorControl");
        platformerListingBuilder.addButtons(controlList);

        tilesetListingBuilder = new TilesetListingBuilder("lev1", mapTerArrayList, (MapEditor)map);
        ListButtons tilesetList = listOfPanels.get(0).getListButtons("tileset");
        tilesetListingBuilder.addButtons(tilesetList);

        //setting commands to android popups:
        PutBotCommand putBotCommand = (PutBotCommand) Commands.getCommand("putBot");
        putBotCommand.setScenario(scenario, mapTerArrayList);
        Assets.getGame().getActivityCallback().setCommand(putBotCommand, "popup_new_bot");

        SaveLoadMapCommand loadMapCommand = (SaveLoadMapCommand)Commands.getCommand("save_load_map");
        loadMapCommand.setMap(map, SaveLoadMapCommand.ACTION_LOAD);
        Assets.getGame().getActivityCallback().setCommand(loadMapCommand, "load_map");

        NewMapCommand newMapCommand = (NewMapCommand)Commands.getCommand("newMap");
        newMapCommand.setScreen(screen);
        Assets.getGame().getActivityCallback().setCommand(newMapCommand, "new_map");

        SaveLoadMapCommand saveMapCommand = (SaveLoadMapCommand)Commands.getCommand("save_load_map");
        saveMapCommand.setMap(map, SaveLoadMapCommand.ACTION_SAVE);
        Assets.getGame().getActivityCallback().setCommand(saveMapCommand, "save_map");
    }



    @Override
    public boolean onTouchPanels(float delta, float scaleX, float scaleY) {
        boolean onTouch = false;
        int numPanels = listOfPanels.size();
        for(int whichPanel = 0; whichPanel < numPanels; whichPanel++){
            ControlPanel controlPanel = listOfPanels.get(whichPanel);
            if(controlPanel.isActivated() == true){
                onTouch = controlPanel.onTouch(delta, scaleX, scaleY);
            }
        }
        return onTouch;
    }

    @Override
    public void drawPanels(SpriteBatch batch){
        int numPanels = listOfPanels.size();
        for(int whichPanel = 0; whichPanel < numPanels; whichPanel++){
            ControlPanel controlPanel = listOfPanels.get(whichPanel);
            if(controlPanel.isActivated() == true){
                controlPanel.drawPanel(batch);
            }
        }
    }

    @Override
    public void action(Object object) {
        if(object != null){
            if(object instanceof MapTer){
                MapTer mapTer = (MapTer)object;
                mapTer.setEditing(true);
                mapTerArrayList.add(mapTer);
            }
        }
    }


    @Override
    public void setControlPanels() {
        for(int i = 0; i < Controls.controlPanels.length; i++){
            if(Controls.controlPanels[i].getName().equalsIgnoreCase("mapEditorControls")){
                listOfPanels.add(Controls.controlPanels[i]);
            }
            else if(Controls.controlPanels[i].getName().equalsIgnoreCase("EndLevelMenu")){
                listOfPanels.add(Controls.controlPanels[i]);
            }
        }
        listOfPanels.get(0).setActivated(true);
    }

    @Override
    public List<ControlPanel> getControlPanels() {
        return listOfPanels;
    }
}
