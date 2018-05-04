package com.missionsurvive.map;

/**
 * Created by kuzmin on 03.05.18.
 */

public class MapTer {

    private int row, col;
    private int tileWidth, tileHeight;

    private boolean blocked;
    private boolean isEditing; //переменная, указывающая на то, редактируется ли данный  тайл в данный момент. Нужна для отрисовки editingTileRect.
    private boolean isLadder;

    /**
     * This constrauctor was made for testing. Here we can assign any tile size we want.
     * @param row
     * @param col
     * @param tileSize
     */
    public MapTer(int row, int col, int tileSize){
        this.row = row;
        this.col = col;
        this.tileWidth = tileSize;
        this.tileHeight = tileSize;
    }

    public MapTer(int row, int col){
        this.row = row;
        this.col = col;
        tileWidth = 16;
        tileHeight = 16;
    }

    public void setBlocked(boolean blocked){
        this.blocked = blocked;
    }

    public boolean isBlocked(){
        return blocked;
    }

    public void setLadder(boolean isLadder){
        this.isLadder = isLadder;
    }

    public boolean isLadder(){
        return isLadder;
    }


    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public int getTop(ScrollMap scrollMap){
        return (row * (tileHeight - 1)) - scrollMap.getWorldOffsetY();
    }

    public int getBottom(ScrollMap scrollMap){
        //return ((row * (tileHeight - 1)) + tileHeight) - scrollMap.getWorldOffsetY();
        return ((row * (tileHeight - 1)) + tileHeight - 1) - scrollMap.getWorldOffsetY();
    }

    public int getLeft(ScrollMap scrollMap){
        return (col * (tileWidth - 1)) - scrollMap.getWorldOffsetX();
    }

    public int getRight(ScrollMap scrollMap){
        //return ((col * (tileWidth - 1)) + tileWidth) - scrollMap.getWorldOffsetX();
        return ((col * (tileWidth - 1)) + tileWidth - 1) - scrollMap.getWorldOffsetX();
    }

    public void setEditing(boolean isEditing){
        this.isEditing = isEditing;
    }

    public boolean isEditing(){
        return isEditing;
    }
}
