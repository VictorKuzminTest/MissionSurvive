package com.missionsurvive.map;

/**
 * Created by kuzmin on 03.05.18.
 */

public class MapTer {

    private int row, col;
    private int tileWidth, tileHeight;
    private int srcX, srcY;

    private boolean blocked;
    private boolean isEditing; //переменная, указывающая на то, редактируется ли данный  тайл в данный момент. Нужна для отрисовки editingTileRect.
    private boolean isLadder;

    public MapTer(int col, int row, int srcX, int srcY, int tileWidth, int tileHeight){
        this.row = row;
        this.col = col;
        this.srcX = srcX;
        this.srcY = srcY;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public MapTer(int row, int col){
        this.row = row;
        this.col = col;
        tileWidth = 16;
        tileHeight = 16;
        srcX = -1;
        srcY = -1;
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

    public void setSrcX(int srcX){
        this.srcX = srcX;
    }

    public void setSrcY(int srcY){
        this.srcY = srcY;
    }

    public int getSrcX(){
        return srcX;
    }

    public int getSrcY(){
        return srcY;
    }

    public int getTop(ScrollMap scrollMap){
        return row * tileHeight - scrollMap.getWorldOffsetY();
    }

    public int getBottom(ScrollMap scrollMap){
        return row * tileHeight + tileHeight - scrollMap.getWorldOffsetY();
    }

    public int getLeft(ScrollMap scrollMap){
        return col * tileWidth - scrollMap.getWorldOffsetX();
    }

    public int getRight(ScrollMap scrollMap){
        return col * tileWidth + tileWidth - scrollMap.getWorldOffsetX();
    }

    public void setEditing(boolean isEditing){
        this.isEditing = isEditing;
    }

    public boolean isEditing(){
        return isEditing;
    }
}
