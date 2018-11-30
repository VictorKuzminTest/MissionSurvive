package com.missionsurvive.geom;

import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;

public class Movement {
    /**
     * In this method we check for blocked tile existence to the east of an object.
     * If there is no tile, then we just return the speed (or some distance) object moves with.
     * If blocked tile exists, then we calculate exact distance to the left of that tile.
     * @param mapEditor
     * @param mapTer potential tile to check
     * @param speedInPixels
     * @param objRight  the right side of an object
     * @return
     */
    public static int checkForEastCollision(MapEditor mapEditor, MapTer mapTer, int speedInPixels, int objRight){
        if(mapTer == null){ //no obstacle to the east...
            return speedInPixels;
        }
        else{ //if there is obstacle to the east...
            int eastTerLeft = mapTer.getLeft(mapEditor.getScrollLevel1Map());
            if((objRight + speedInPixels) > eastTerLeft){
                return eastTerLeft - objRight;
            }
        }
        return speedInPixels;
    }

    /**
     * In this method we check for blocked tile existence to the west of an object.
     * If there is no tile, then we just return the speed (or some distance) object moves with.
     * If blocked tile exists, then we calculate exact distance to the left of that tile.
     * @param mapEditor
     * @param mapTer potential tile to check
     * @param speedInPixels
     * @param objLeft  the right side of an object
     * @return
     */
    public static int checkForWestCollision(MapEditor mapEditor, MapTer mapTer, int speedInPixels, int objLeft){
        if(mapTer == null){ //no obstacle to the west...
            return speedInPixels;
        }
        else{ //if there is obstacle to the west...
            int westTerRight = mapTer.getRight(mapEditor.getScrollLevel1Map());
            if((objLeft - speedInPixels) <= westTerRight) {
                return objLeft - westTerRight;
            }
        }
        return speedInPixels;
    }
}
