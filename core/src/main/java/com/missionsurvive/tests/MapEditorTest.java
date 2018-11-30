package com.missionsurvive.tests;

import com.missionsurvive.scenarios.commands.Command;
import com.missionsurvive.scenarios.commands.LoadFromExternalLibGdx;
import com.missionsurvive.map.MapEditor;

public class MapEditorTest {

    private MapEditor mapEditor;
    private Command loadCommand;

    private int numRows;
    private int numCols;

    public MapEditorTest(MapEditor mapEditor){
        this.mapEditor = mapEditor;
        loadCommand = new LoadFromExternalLibGdx(mapEditor);
    }

    /**
     * Here we generating a "window of a building" from the up left corner of a tilset.
     */
    public void testBasicMapCreation(){
        numRows = 3;
        numCols = 3;
        mapEditor.newMap(numCols, numRows);
        mapEditor.newForeground(numCols, numRows, 16, 16);
        mapEditor.addNewTileset("lev1", 16, 16);
        for(int row = 0; row < numRows; row++){
            for(int col = 0; col < numCols; col++){
                mapEditor.setTile(mapEditor.getTextureRegion(0),
                        col, row, col, row);
            }
        }
    }

    /**
     * loading map from file external storage.
     */
    public void loadMapCommand(){
        loadCommand.execute(null, "level1.txt");
    }
}
