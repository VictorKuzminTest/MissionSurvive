package com.missionsurvive.map;

/**
 * Created by kuzmin on 08.05.18.
 */

public interface Map {

    public MapTer[][] getLevel1Ter();

    public void addNewTileset(String asset, int tileWidth, int tileHeight);

    public void horizontScroll(float delta, int x);

    public void verticalScroll(int y);

    public ScrollMap getScrollMap();

    public void loadMap(String mapTerInString);

    public String saveMap();
}
