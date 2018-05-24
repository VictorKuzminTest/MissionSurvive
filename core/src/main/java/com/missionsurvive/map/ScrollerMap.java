package com.missionsurvive.map;

import com.badlogic.gdx.graphics.Texture;
import com.missionsurvive.scenarios.controlscenarios.ScrollerCS;
import com.missionsurvive.utils.Assets;

import java.util.Random;

/**
 * Created by kuzmin on 08.05.18.
 */

public class ScrollerMap implements Map{

    public static final int UPPER_PART = 0;
    public static final int LOWER_PART = 1;

    public static final float SCROLLING_TICK = 0.005f;

    private MapTer[][] mapTers;
    private ScrollMap scrollMap;
    private Random random = new Random();

    private int numRows = 3;
    private int numCols = 100;
    private int tileWidth = 73;
    private int tileHeight = 128;
    private int numColsInTileset = 7;

    private float scrollingTickTime = 0;

    public ScrollerMap(){
        initMap(numRows, numCols);
        addNewTileset(null, tileWidth, tileHeight);
        scrollMap = new ScrollMap(tileWidth, tileHeight, numRows, numCols, true);
    }

    public void initMap(int numRows, int numCols){
        mapTers = new MapTer[numRows][numCols];
    }

    @Override
    public void addNewTileset(String asset, int tileWidth, int tileHeight) {
        for(int row = 0; row < mapTers.length; row++) {
            for (int col = 0; col < mapTers[row].length; col++) {
                if(row == 0){
                    int railCol = random.nextInt(numColsInTileset);
                    mapTers[row][col] = new MapTer(col, row,
                            railCol * tileWidth, row * tileHeight, tileWidth, tileHeight);
                }
                else if (row == 1){
                    int srcColToDraw = col % (numColsInTileset - 1);
                    switch(srcColToDraw){
                        case 0:
                            mapTers[row][col] = new MapTer(col, row,
                                    0 * tileWidth,  getRandomRow(UPPER_PART) * tileHeight, tileWidth, tileHeight);
                            break;
                        case 2:
                            mapTers[row][col] = new MapTer(col, row,
                                    2 * tileWidth,  getRandomRow(UPPER_PART) * tileHeight, tileWidth, tileHeight);
                            break;
                        case 4:
                            mapTers[row][col] = new MapTer(col, row,
                                    4 * tileWidth,  getRandomRow(UPPER_PART) * tileHeight, tileWidth, tileHeight);
                            break;
                        case 6:
                            mapTers[row][col] = new MapTer(col, row,
                                    6 * tileWidth,  getRandomRow(UPPER_PART) * tileHeight, tileWidth, tileHeight);
                            break;
                        default:
                            mapTers[row][col] = new MapTer(col, row,
                                    srcColToDraw * tileWidth, row * tileHeight, tileWidth, tileHeight);
                            break;
                    }
                }
                else if(row == 2){
                    int srcColToDraw = col % (numColsInTileset - 1);
                    switch(srcColToDraw){
                        case 1:
                            mapTers[row][col] = new MapTer(col, row,
                                    1 * tileWidth,  getRandomRow(LOWER_PART) * tileHeight, tileWidth, tileHeight);
                            break;
                        case 3:
                            mapTers[row][col] = new MapTer(col, row,
                                    3 * tileWidth,  getRandomRow(LOWER_PART) * tileHeight, tileWidth, tileHeight);
                            break;
                        case 5:
                            mapTers[row][col] = new MapTer(col, row,
                                    5 * tileWidth,  getRandomRow(LOWER_PART) * tileHeight, tileWidth, tileHeight);
                            break;
                        default:
                            mapTers[row][col] = new MapTer(col, row,
                                    srcColToDraw * tileWidth, row * tileHeight, tileWidth, tileHeight);
                            break;
                    }
                }
            }
        }
    }

    /**
     * Get random row depending on float value and position of the part of a road.
     * @param roadPart
     * @return
     */
    public int getRandomRow(int roadPart){
        int row = 0;
        switch (roadPart){
            case UPPER_PART:
                row = random.nextFloat() > 0.5f ? 1 : 3;
                break;
            case LOWER_PART:
                row = random.nextFloat() < 0.5f ? 2 : 3;
                break;
        }
        return row;
    }

    @Override
    public MapTer[][] getLevel1Ter(){
        return mapTers;
    }

    @Override
    public void horizontScroll(float delta, int x) {
        scrollingTickTime += delta;
        while(scrollingTickTime > SCROLLING_TICK) {
            scrollingTickTime -= SCROLLING_TICK;

            scrollMap.setRoundWorldOffset(x, 0);
        }
    }

    @Override
    public void loadMap(String mapTerInString) {}

    @Override
    public void verticalScroll(int y) {

    }

    @Override
    public ScrollMap getScrollMap() {
        return scrollMap;
    }

    @Override
    public String saveMap() {
        return null;
    }
}
