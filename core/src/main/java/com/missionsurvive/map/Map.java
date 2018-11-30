package com.missionsurvive.map;

public interface Map {

    public MapTer[][] getLevel1Ter();

    public void addNewTileset(String asset, int tileWidth, int tileHeight);

    public void horizontScroll(float delta, int x);

    public void verticalScroll(int y);

    public ScrollMap getScrollMap();

    public boolean loadMap(String mapTerInString);

    public String saveMap();

    public ParallaxBackground getBackground(String name);
}
