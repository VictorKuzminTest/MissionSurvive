package com.missionsurvive.framework.impl;

import com.missionsurvive.framework.Physics;
import com.missionsurvive.framework.Vector;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.map.ScrollMap;
import com.missionsurvive.objs.actors.Hero;

import sun.rmi.runtime.Log;

/**
 * Created by kuzmin on 03.05.18.
 */

public class HeroPhysics implements Physics {

    public HeroPhysics(){
    }

    @Override
    public Vector calculateVector(MapTer[][] map, ScrollMap scrollMap,
                                  int vectStartX, int vectStartY, Vector vector, int tileSize, int isAction) {
        MapTer mapTer = getMapTer(map, scrollMap,
                vectStartX, vectStartY,
                vector.getX(), vector.getY(), tileSize);
        switch (isAction){
            case Hero.ACTION_JUMPING:
                calculateWhenJumpingOrFalling(mapTer, scrollMap, vectStartX, vectStartY, vector, tileSize);
                break;
            case Hero.ACTION_RUNNING:
                calculateWhenRunning(mapTer, scrollMap, vectStartX, vectStartY,  vector, tileSize);
                break;
        }
        return vector;
    }


    public Vector calculateWhenRunning(MapTer mapTer, ScrollMap scrollMap,
                                       int vectStartX, int vectStartY,
                                       Vector vector, int tileSize){
        if(mapTer != null){
            if(mapTer.isLadder()){
                short offsetX = 4;
                if(vector.getX() < 0){
                    offsetX = -4;
                }
                cutVector(vectStartX, vectStartY, vector,
                        getBorderX(mapTer, scrollMap, vector.getX()),
                        getBorderY(mapTer, scrollMap, vector.getY()));

                vector.set(vector.getX() + offsetX, -tileSize);
            }
            else{
                cutVector(vectStartX, vectStartY, vector,
                        getBorderX(mapTer, scrollMap, vector.getX()),
                        getBorderY(mapTer, scrollMap, vector.getY()));
            }
        }
        return vector;
    }


    public Vector calculateWhenJumpingOrFalling(MapTer mapTer, ScrollMap scrollMap,
                                                int vectStartX, int vectStartY, Vector vector, int tileSize){
        if(mapTer != null){
            //calculate vector for a ladder:
            if(mapTer.isLadder()){
                //if object moves for straight forward or down:
                if(vector.getY() >= 0){
                    cutVector(vectStartX, vectStartY, vector,
                            getBorderX(mapTer, scrollMap, vector.getX()),
                            getBorderY(mapTer, scrollMap, vector.getY()));
                }
            }
            else{
                cutVector(vectStartX, vectStartY, vector,
                        getBorderX(mapTer, scrollMap, vector.getX()),
                        getBorderY(mapTer, scrollMap, vector.getY()));
            }
        }
        return vector;
    }



    /**
     * Cuts vector depending on x and y coordinates of a rectangle (tile).
     * @param startX
     * @param startY
     * @param vector
     * @param borderX  left or right border of a rectangle.
     * @param borderY  top or bottom border of a rectangle.
     */
    public void cutVector(int startX, int startY, Vector vector,
                          int borderX, int borderY){
        float initX = vector.getX();
        float initY = vector.getY();
        boolean isVertical = false;

        short correctionX = 1;
        short correctionY = 1;

        if(initY < 0){
            correctionY = -1;
        }
        if(initX < 0){
            correctionX = -1;
        }

        if(initY == 0){  //if vertical side.
            isVertical = true;
        }
        else if (initX == 0){  //if horizontal side.
            isVertical = false;
        }
        else{
            //coefficients that make vector's  x and y constituents equal to each other.
            //It is important for proper calculation of x and y border of a rect to collide if
            //x and y constituents of a vector have different sizes.
            int coefficientX = Math.round(initY / initX);
            int coefficientY = Math.round(initX / initY);

            if(Math.abs((startX + initX + coefficientX) - borderX) <
                    Math.abs((startY + initY + coefficientY) - borderY)){
                isVertical = true;
            }
        }

        if(isVertical){
            vector.setX(correctionX * (Math.abs(startX - borderX)) - correctionX);
            vector.setY(Math.round(recalculateVectorSide(initY, initX, vector.getX())));
        }
        else{
            vector.setY(correctionY * (Math.abs(startY - borderY)) - correctionY);
            vector.setX(Math.round(recalculateVectorSide(initX, initY, vector.getY())));
        }
    }

    /**
     * recalculates X or Y constituent of a vector.
     * @param numerator
     * @param denominator
     * @param step
     * @return
     */
    public float recalculateVectorSide(float numerator, float denominator, float step){
        return numerator / denominator * step;
    }


    /**
     * Search for tiles to collide:
     *  |_|_|^|_|_|
     *  |_|_|||_|_|
     *  |<|-|0|-|>|
     *  |_|_|||_|_|
     *  |_|_|v|_|_|
     * @param map
     * @param scrollMap
     * @param startVectorX
     * @param startVectorY
     * @param lengthX
     * @param lengthY
     * @param tileSize
     * @return
     */
    public MapTer getMapTer(MapTer[][] map, ScrollMap scrollMap, int startVectorX, int startVectorY,
                            int lengthX, int lengthY, int tileSize){
        int startCol = getCol(map, scrollMap, startVectorX , tileSize);
        int endCol = getCol(map, scrollMap, startVectorX + lengthX, tileSize);
        int numColsToCheck = endCol - startCol;
        short colMultiplier = 1; //multiplies col offset (-1 negative dir, +1 positive dir).
        if(numColsToCheck < 0){
            numColsToCheck = Math.abs(numColsToCheck);
            colMultiplier = -1;
        }

        int startRow = getRow(map, scrollMap, startVectorY , tileSize);
        int endRow = getRow(map, scrollMap, startVectorY + lengthY, tileSize);
        int numRowsToCheck = endRow - startRow;
        short rowMultiplier = 1; //multiplies row offset (-1 negative dir, +1 positive dir).
        if(numRowsToCheck < 0){
            numRowsToCheck = Math.abs(numRowsToCheck);
            rowMultiplier = -1;
        }

        for(int rowOffset = 0; rowOffset <= numRowsToCheck; rowOffset++){
            for(int colOffset = 0; colOffset <= numColsToCheck; colOffset++){
                if(map[startRow + rowOffset * rowMultiplier][startCol + colOffset * colMultiplier].isBlocked()){
                    return map[startRow + rowOffset * rowMultiplier][startCol + colOffset * colMultiplier];
                }
            }
        }
        return null;
    }


    public int getRow(MapTer[][] map, ScrollMap scrollMap, int coordY, int tileHeight){
        return GeoHelper.checkRowCol((coordY + scrollMap.getWorldOffsetY())
                / tileHeight, map.length);
    }

    public int getCol(MapTer[][] map, ScrollMap scrollMap, int coordX, int tileWidth){
        return GeoHelper.checkRowCol((coordX + scrollMap.getWorldOffsetX())
                        / tileWidth, map[0].length);
    }


    /**
     * Gets the X border of a tile (left or right) depending on x direction (positive or negative).
     * @param mapTer
     * @param scrollMap
     * @param x
     * @return
     */
    public int getBorderX(MapTer mapTer, ScrollMap scrollMap, int x){
        int borderX = x < 0 ? mapTer.getRight(scrollMap) : mapTer.getLeft(scrollMap);
        return borderX;
    }


    /**
     * Gets the Y border of a tile (bottom or top) depending on y direction (positive or negative).
     * @param mapTer
     * @param scrollMap
     * @param y
     * @return
     */
    public int getBorderY(MapTer mapTer, ScrollMap scrollMap, int y){
        int borderY = y < 0 ? mapTer.getBottom(scrollMap) : mapTer.getTop(scrollMap);
        return borderY;
    }
}
