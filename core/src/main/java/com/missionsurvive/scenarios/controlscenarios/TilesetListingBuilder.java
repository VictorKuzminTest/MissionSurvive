package com.missionsurvive.scenarios.controlscenarios;

import com.missionsurvive.framework.impl.ListButtons;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.scenarios.commands.PutTileCommand;
import com.missionsurvive.scenarios.commands.ShowLayerCommand;
import com.missionsurvive.screens.PlatformerScreen;
import com.missionsurvive.utils.Assets;
import com.missionsurvive.utils.Commands;

import java.util.ArrayList;

/**
 * Created by kuzmin on 16.05.18.
 */

public class TilesetListingBuilder implements ListingBuilder{

    private String assetName;
    private ArrayList<MapTer> mapTerArrayList;
    private MapEditor mapEditor;

    public TilesetListingBuilder(String assetName, ArrayList<MapTer> mapTerArrayList,
                                 MapEditor mapEditor){
        this.assetName = assetName;
        this.mapEditor = mapEditor;
        this.mapTerArrayList = mapTerArrayList;
    }

    @Override
    public void addButtons(ListButtons listButtons) {
        addTileset(listButtons);
    }

    /**
     * Непосредственное добавление тайлсета.
     * @param listButtons
     */
    public void addTileset(ListButtons listButtons){

        int width = Assets.getTextures()[Assets.getWhichTexture(assetName)].getWidth();
        int height = Assets.getTextures()[Assets.getWhichTexture(assetName)].getHeight();

        listButtons.clearList(); //сначала очищаем список кнопок, чтобы потом добавлять целый новый tileset.

        for(int row = 0; row < height / 16; row++){ //18 is tileset tile height.
            for(int col = 0; col < width / 16; col++){ //...tileset tile width.

                PutTileCommand putTile = (PutTileCommand) Commands.getCommand("putTile");
                putTile.setAsset(mapEditor, mapTerArrayList, col, row);
                listButtons.addNewButton(assetName, col, row, col * 16, row * 16, 16, 16, putTile);
            }
        }
    }
}
