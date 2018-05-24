package com.missionsurvive.scenarios.commands;

import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.scenarios.PlatformerScenario;

import java.util.ArrayList;

/**
 * Created by kuzmin on 16.05.18.
 */

public class WithTileCommand implements Command{

    public static final int REMOVE_TILE = 0;
    public static final int PUT_TILE = 1;
    public static final int BLOCK_TILE = 2;
    public static final int UNBLOCK_TILE = 3;
    public static final int PUT_LADDER = 4;

    private ArrayList<MapTer> mapTerArrayList;
    private MapEditor mapEditor;

    private int action;

    @Override
    public String execute(String key, String value) {
        editMapTerList();
        return null;
    }

    public void setAction(MapEditor mapEditor, ArrayList<MapTer> mapTerArrayList, int action){
        this.action = action;
        this.mapTerArrayList = mapTerArrayList;
        this.mapEditor = mapEditor;
    }

    /**
     * Edits tiles that array list of tiles contain depending on action we want to do with the tiles.
     */
    public void editMapTerList(){
        int len = mapTerArrayList.size();
        switch (action){
            case REMOVE_TILE:
                for(int mapTerNum = 0; mapTerNum < len; mapTerNum++){
                    mapEditor.removeTile(mapTerArrayList.get(mapTerNum).getCol(),
                            mapTerArrayList.get(mapTerNum).getRow());
                    mapTerArrayList.get(mapTerNum).setEditing(false);
                }
                break;
            case PUT_TILE:
                for(int mapTerNum = 0; mapTerNum < len; mapTerNum++) {
                    mapTerArrayList.get(mapTerNum).setEditing(false);
                }
                break;
            case BLOCK_TILE:
                for(int mapTerNum = 0; mapTerNum < len; mapTerNum++) {
                    mapTerArrayList.get(mapTerNum).setBlocked(true);
                    mapTerArrayList.get(mapTerNum).setEditing(false);
                }
                break;
            case UNBLOCK_TILE:
                for(int mapTerNum = 0; mapTerNum < len; mapTerNum++) {
                    mapTerArrayList.get(mapTerNum).setBlocked(false);
                    mapTerArrayList.get(mapTerNum).setLadder(false);
                    mapTerArrayList.get(mapTerNum).setEditing(false);
                    //cancel spawn enemies:
                    /*Scenario scenario = Assets.getGame().getCurrentScreen().getScenario();
                    if(scenario instanceof PlatformerScenario){
                        ((PlatformerScenario)scenario).getSpawnEnemies()[mapTerArrayList.get(mapTerNum).getRow()]
                                [mapTerArrayList.get(mapTerNum).getCol()] = null;
                    }*/
                }
                break;
            case PUT_LADDER:
                for(int mapTerNum = 0; mapTerNum < len; mapTerNum++) {
                    mapTerArrayList.get(mapTerNum).setBlocked(true);
                    mapTerArrayList.get(mapTerNum).setLadder(true);
                    mapTerArrayList.get(mapTerNum).setEditing(false);
                }
                break;
        }
        mapTerArrayList.clear();
    }
}
