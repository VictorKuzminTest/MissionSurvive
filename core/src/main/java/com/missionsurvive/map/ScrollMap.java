package com.missionsurvive.map;

import com.missionsurvive.MSGame;

/**
 * Created by kuzmin on 03.05.18.
 */

public class ScrollMap {

    private int tileWidth;
    private int tileHeight;
    private int sizeToDivideWidth;
    private int sizeToDivideHeight;
    private int endRowOffset;  //номер строки, которой заканчивается отрисовка тайлов.
    private int maxRowsToDraw;  //сколько строк тайлов мы должны отрисовать.
    private int startRowOffset;  //сколько строк тайлов мы должны отрисовать.
    private int endColOffset;  //Номер колонки, которой заканчивается отрисовка тайлов.
    private int maxColToDraw;   //сколько колонок тайлов мы должны отрисовать.
    private int startColOffset;   //колонка, с которой начинается отрисовка тайлов.
    private int numCols;
    private int worldOffsetX;
    private int numRows;
    private int worldOffsetY;
    private int screenWidth;
    private int screenHeight;

    private int extremeRightScreen;   //Крайнее правое положение экрана: допустим, 19 - это размер мира, 5 - это размер экрана.
    private int extremeDownScreen;  //Крайнее нижнее положение экрана: допустим, 19 - это размер мира, 7 - это размер экрана.
    private int extremeLeftScreen;  //крайнее левое положение экрана.
    private int extremeUpScreen;  //Крайнее верхнее положение экрана.


    public ScrollMap(int tileWidth, int tileHeight, int numRows, int numCols, boolean isRounded){

        this.numRows = numRows;   //worldHeight.
        this.numCols = numCols;   //worldWidth.

        screenWidth = MSGame.SCREEN_WIDTH;
        screenHeight = MSGame.SCREEN_HEIGHT;

        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;    //Декартова.

        sizeToDivideWidth = tileWidth - 1;
        sizeToDivideHeight = tileHeight - 1;

        if(!isRounded){  //if tile map is not rounded.
            extremeLeftScreen = numCols * tileWidth - numCols * this.tileWidth;  //крайнее левое положение экрана.
            extremeRightScreen = numCols * tileWidth - screenWidth;  //Крайнее правое положение экрана.
            extremeUpScreen  = 0;
            extremeDownScreen = numRows * tileHeight - screenHeight; //Крайнее нижнее положение экрана.
        }
        else { //if the tile map is rounded.
            extremeLeftScreen = -screenWidth;
            extremeRightScreen = numCols * tileWidth;
            extremeUpScreen = -screenHeight;
            extremeDownScreen = numRows * tileHeight;
        }

        maxRowsToDraw = screenHeight / sizeToDivideHeight + 2;
        maxColToDraw = screenWidth / sizeToDivideWidth + 2;
        startRowOffset = 0;
        startColOffset = 0;
        endRowOffset = screenHeight / sizeToDivideHeight + 1;
        if(endRowOffset >= numRows) endRowOffset = numRows;
        endColOffset = screenWidth / sizeToDivideWidth + 1;
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

        if(worldOffsetX > extremeRightScreen){ //Это проверка на крайнее правое положение мира.
            worldOffsetX = extremeRightScreen;
        }
        else if(worldOffsetX < extremeLeftScreen){ //Это проверка на крайнее левое положение экрана.
            worldOffsetX = extremeLeftScreen;
        }
        else if(worldOffsetY > extremeDownScreen){  //Это проверка на крайнее нижнее положение мира.
            worldOffsetY = extremeDownScreen;
        }
        else if(worldOffsetY < extremeUpScreen){ //Это проверка на крайнее верхнее положение экрана.
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
        startColOffset = worldOffsetX / sizeToDivideWidth;
        endColOffset = startColOffset + maxColToDraw;
    }

    public void setRoundRowOffset(){
        startRowOffset = worldOffsetY / sizeToDivideHeight;
        endRowOffset = startRowOffset + maxRowsToDraw;
    }


    public void setColOffset(){ //Оффсет  колонки с тайлами, с которой начнем прорисовку тайлов и которой закончим.
        endColOffset = maxColToDraw + getWorldOffsetX() / sizeToDivideWidth;

        if(endColOffset > numCols){
            endColOffset = numCols;
        }

        startColOffset = endColOffset - maxColToDraw;

        if(startColOffset < 0){ //Проверка на то, чтобы индекс не выходил за границы массива.
            startColOffset = 0;
        }
    }


    public void setRowOffset(){ //Оффсет  строки с тайлами, с которой начнем прорисовку тайлов и которой закончим.
        endRowOffset = maxRowsToDraw + getWorldOffsetY() / sizeToDivideHeight;

        if(endRowOffset > numRows){
            endRowOffset = numRows;
        }

        startRowOffset = endRowOffset - maxRowsToDraw;

        if(startRowOffset < 0){ //Проверка на то, чтобы индекс не выходил за границы массива.
            startRowOffset = 0;
        }
    }

    public void setExtremesForEditing(int extremeLeftScreen, int extremeRightScreen, int extremeUpScreen, int extremeDownScreen){
        /**
         * This method is a compromise between making a new ScrollMap class for offseting the map tileset,
         * where i would have reset the methods calculating "extreme screens", "row and col offset". This would have complicated the whole
         * code (especially of this class). Instead, i just reset "extreme screens" using this method. I call this methds from outside,
         * when i need my tilemap move closer, further or across the screen.
         */
        this.extremeLeftScreen = extremeLeftScreen;
        this.extremeRightScreen = extremeRightScreen;
        this.extremeUpScreen  = extremeUpScreen;
        this.extremeDownScreen = extremeDownScreen;
    }

    public void setExtremesToNormal(boolean isRounded){
        /**
         * This method sets "extremes" back to its initial values (suppose if we changed them before through method setExtremesForEditing(...)).
         */
        if(!isRounded){  //Если ограниченный, т.е. не круговая tilemap.
            extremeLeftScreen = numCols * sizeToDivideWidth - numCols * sizeToDivideWidth;  //крайнее левое положение экрана.
            extremeRightScreen = numCols * sizeToDivideWidth - screenWidth;  //Крайнее правое положение экрана.
            extremeUpScreen  = 0;
            extremeDownScreen = numRows * sizeToDivideHeight - screenHeight; //Крайнее нижнее положение экрана.
        }
        else{ //Если круговая.
            extremeLeftScreen = (numCols * sizeToDivideWidth) * (-1);
            extremeRightScreen = numCols * sizeToDivideWidth;
            extremeUpScreen  = (numRows * sizeToDivideHeight) * (-1);
            extremeDownScreen = numRows * sizeToDivideHeight;
        }
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

    public int getMaxColToDraw(){
        return maxColToDraw;
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
