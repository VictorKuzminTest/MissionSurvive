package com.missionsurvive.scenarios.commands;

import com.missionsurvive.map.Map;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.screens.GameScreen;

import java.util.ArrayList;

/**
 * Created by kuzmin on 16.05.18.
 */

public class PutTileCommand implements Command{

    private MapEditor mapEditor;
    private ArrayList<MapTer> mapTerArrayList;
    private int assetX, assetY;

    @Override
    public String execute(String key, String value) {
        putTile();
        return null;
    }

    public void setAsset(MapEditor mapEditor, ArrayList<MapTer> mapTerArrayList, int assetX, int assetY){
        this.assetX = assetX;
        this.assetY = assetY;
        this.mapEditor = mapEditor;
        this.mapTerArrayList = mapTerArrayList;
    }

    public void putTile(){
        int len = mapTerArrayList.size();
        for(int mapTerNum = 0; mapTerNum < len; mapTerNum++) {
            mapEditor.setTile(mapEditor.getTextureRegion(0),
                    mapTerArrayList.get(mapTerNum).getCol(),
                    mapTerArrayList.get(mapTerNum).getRow(),
                    assetX, assetY);
            mapTerArrayList.get(mapTerNum).setEditing(false);
        }
        mapTerArrayList.clear();
    }

    @Override
    public void setScreen(GameScreen gameScreen) {

    }
}
