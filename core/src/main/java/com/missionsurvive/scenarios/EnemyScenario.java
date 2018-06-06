package com.missionsurvive.scenarios;

import com.missionsurvive.MSGame;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.actors.Enemy;

/**
 * Created by kuzmin on 04.05.18.
 */

public class EnemyScenario {

    private int screenWidth = MSGame.SCREEN_WIDTH;
    private MapEditor mapEditor;
    private MapTer[][] mapTers;
    private int targetX = -1;
    private int numTileXToCheck;

    public EnemyScenario(MapEditor mapEditor){
        this.mapEditor = mapEditor;
        mapTers = mapEditor.getLevel1Ter();
        numTileXToCheck = screenWidth / mapEditor.getTileWidth();
    }

    /**
     * Sets (resets) a direction to move;
     * @param objX
     * @param objY
     * @param tileSize
     */
    public void setDirection(int objX, int objY, int tileSize, int direction){
        if(direction == Enemy.WEST) checkForEast(objX, objY, tileSize);
        if(direction == Enemy.EAST) checkForWest(objX, objY, tileSize);
    }

    /**
     * Sets targetX (destination) according to blocked tile or (if it cannot find them)
     * end col offset of a scrollMap.
     * @param objX
     * @param objY
     * @param tileSize
     */
    public void checkForEast(int objX, int objY, int tileSize){
        int worldWidth = mapTers[0].length;
        int row = ((objY) + mapEditor.getScrollLevel1Map().getWorldOffsetY())
                / tileSize;
        int startCol = ((objX) + mapEditor.getScrollLevel1Map().getWorldOffsetX())
                / tileSize;
        for(int nextCol = 0; nextCol < numTileXToCheck; nextCol++){
            int col = GeoHelper.checkRowCol(startCol + nextCol, worldWidth);
            if(mapTers[row][col].isBlocked() && !mapTers[row][col].isLadder()){
                targetX = col * tileSize;
                return;
            }
        }
        targetX = mapEditor.getScrollLevel1Map().getEndColOffset() * tileSize;
    }

    /**
     * Sets targetX (destination) according to blocked tile or (if it cannot find them)
     * start col offset of a scrollMap.
     * @param objX
     * @param objY
     * @param tileSize
     */
    public void checkForWest(int objX, int objY, int tileSize){
        int worldWidth = mapTers[0].length;
        int row = ((objY) + mapEditor.getScrollLevel1Map().getWorldOffsetY())
                / tileSize;
        int startCol = ((objX) + mapEditor.getScrollLevel1Map().getWorldOffsetX())
                / tileSize;
        for(int nextCol = 0; nextCol < numTileXToCheck; nextCol++){
            int col = GeoHelper.checkRowCol(startCol - nextCol, worldWidth);
            if(mapTers[row][col].isBlocked() && !mapTers[row][col].isLadder()){
                targetX = col * tileSize;
                return;
            }
        }
        targetX = mapEditor.getScrollLevel1Map().getStartColOffset() * tileSize;
    }

    public int getTargetX(){
        return targetX;
    }
}
