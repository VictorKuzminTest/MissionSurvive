package com.missionsurvive.map;

import com.missionsurvive.MSGame;

public class ScrollMap {

    private int tileWidth;
    private int tileHeight;
    private int endRowOffset;
    private int maxRowsToDraw;
    private int startRowOffset;
    private int endColOffset;
    private int maxColsToDraw;
    private int startColOffset;
    private int numCols;
    private int worldOffsetX;
    private int numRows;
    private int worldOffsetY;
    private int screenWidth;
    private int screenHeight;

    private int extremeRightScreen;
    private int extremeDownScreen;
    private int extremeLeftScreen;
    private int extremeUpScreen;

    public ScrollMap(int tileWidth, int tileHeight, int numRows, int numCols, boolean isRounded){

        //world width and height
        this.numRows = numRows;
        this.numCols = numCols;

        screenWidth = MSGame.SCREEN_WIDTH;
        screenHeight = MSGame.SCREEN_HEIGHT;

        //Cartesian
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;

        if(!isRounded){  //if tile map is not rounded.
            extremeLeftScreen = numCols * tileWidth - numCols * this.tileWidth;
            extremeRightScreen = numCols * tileWidth - screenWidth;
            extremeUpScreen  = 0;
            extremeDownScreen = numRows * tileHeight - screenHeight;
        }
        else { //if the tile map is rounded.
            extremeLeftScreen = -screenWidth;
            extremeRightScreen = numCols * tileWidth;
            extremeUpScreen = -screenHeight;
            extremeDownScreen = numRows * tileHeight;
        }

        maxRowsToDraw = screenHeight / tileHeight + 2;
        maxColsToDraw = screenWidth / tileWidth + 2;
        startRowOffset = 0;
        startColOffset = 0;
        endRowOffset = screenHeight / tileHeight + 1;
        if(endRowOffset >= numRows) endRowOffset = numRows;
        endColOffset = screenWidth / tileWidth + 1;
        if(endColOffset >= numCols) endColOffset = numCols;
        worldOffsetX = 0;
        worldOffsetY = 0;
    }

    public int getStartRowOffset(){
        return startRowOffset;
    }

    public int getEndRowOffset(){
        return endRowOffset;
    }

    public int getStartColOffset(){
        return startColOffset;
    }

    public int getEndColOffset(){
        return endColOffset;
    }

    public void setWorldOffset(int distanceX, int distanceY){
        worldOffsetX += distanceX;
        worldOffsetY += distanceY;

        if(worldOffsetX > extremeRightScreen){
            worldOffsetX = extremeRightScreen;
        }
        else if(worldOffsetX < extremeLeftScreen){
            worldOffsetX = extremeLeftScreen;
        }
        else if(worldOffsetY > extremeDownScreen){
            worldOffsetY = extremeDownScreen;
        }
        else if(worldOffsetY < extremeUpScreen){
            worldOffsetY = extremeUpScreen;
        }
        setRowOffset();
        setColOffset();
    }

    public void setRoundWorldOffset(int distanceX, int distanceY){
        worldOffsetX += distanceX;
        worldOffsetY += distanceY;

        setRoundRowOffset();
        setRoundColOffset();
    }

    public void setRoundColOffset(){
        startColOffset = worldOffsetX / tileWidth;
        endColOffset = startColOffset + maxColsToDraw;
    }

    public void setRoundRowOffset(){
        startRowOffset = worldOffsetY / tileHeight;
        endRowOffset = startRowOffset + maxRowsToDraw;
    }

    /**
     * Offset of start column and end column to draw.
     */
    public void setColOffset(){
        endColOffset = maxColsToDraw + getWorldOffsetX() / tileHeight;

        if(endColOffset > numCols){
            endColOffset = numCols;
        }

        startColOffset = endColOffset - maxColsToDraw;

        if(startColOffset < 0){
            startColOffset = 0;
        }
    }

    /**
     * Offset of start row and end row to draw.
     */
    public void setRowOffset(){
        endRowOffset = maxRowsToDraw + getWorldOffsetY() / tileHeight;

        if(endRowOffset > numRows){
            endRowOffset = numRows;
        }

        startRowOffset = endRowOffset - maxRowsToDraw;

        if(startRowOffset < 0){
            startRowOffset = 0;
        }
    }

    /**
     * This method is a compromise between making a new ScrollMap class for offseting the map tileset,
     *  where i would have reset the methods calculating "extreme screens", "row and col offset". This would have complicated the whole
     *  code (especially of this class). Instead, i just reset "extreme screens" using this method. I call this methds from outside,
     *  when i need my tilemap move closer, further or across the screen.
     * @param extremeLeftScreen
     * @param extremeRightScreen
     * @param extremeUpScreen
     * @param extremeDownScreen
     */
    public void setExtremesForEditing(int extremeLeftScreen, int extremeRightScreen,
                                      int extremeUpScreen, int extremeDownScreen){
        this.extremeLeftScreen = extremeLeftScreen;
        this.extremeRightScreen = extremeRightScreen;
        this.extremeUpScreen  = extremeUpScreen;
        this.extremeDownScreen = extremeDownScreen;
    }

    /**
     * This method sets "extremes" back to its initial values
     * (suppose if we changed them before through method setExtremesForEditing(...)).
     * @param isRounded
     */
    public void setExtremesToNormal(boolean isRounded){
        if(!isRounded){
            extremeLeftScreen = numCols * tileWidth - numCols * tileWidth;
            extremeRightScreen = numCols * tileWidth - screenWidth;
            extremeUpScreen  = 0;
            extremeDownScreen = numRows * tileHeight - screenHeight;
        }
        else{ //Если круговая.
            extremeLeftScreen = (numCols * tileWidth) * (-1);
            extremeRightScreen = numCols * tileWidth;
            extremeUpScreen  = (numRows * tileHeight) * (-1);
            extremeDownScreen = numRows * tileHeight;
        }
    }

    public void setWorldOffsetX(int worldOffsetX){
        this.worldOffsetX = worldOffsetX;
    }

    public void setWorldOffsetY(int worldOffsetY){
        this.worldOffsetY = worldOffsetY;
    }

    public int getWorldOffsetX(){
        return worldOffsetX;
    }

    public int getWorldOffsetY(){
        return worldOffsetY;
    }

    public int getMaxRowsTodraw(){
        return maxRowsToDraw;
    }

    public int getMaxColsToDraw(){
        return maxColsToDraw;
    }

    public int getNumRows(){
        return numRows;
    }

    public int getNumCols(){
        return numCols;
    }

    public int getTileWidth(){
        return tileWidth;
    }

    public void setTileWidth(int tileWidth){
        this.tileWidth = tileWidth;
    }

    public int getTileHeight(){
        return tileHeight;
    }

    public void setTileHeight(int tileHeight){
        this.tileHeight = tileHeight;
    }

    public int getExtremeLeftScreen(){
        return extremeLeftScreen;
    }

    public int getExtremeRightScreen(){
        return extremeRightScreen;
    }

    public int getExtremeUpScreen(){
        return extremeUpScreen;
    }

    public int getExtremeDownScreen(){
        return extremeDownScreen;
    }

    public int getScreenWidth(){
        return screenWidth;
    }

    public int getScreenHeight(){
        return screenHeight;
    }
}
