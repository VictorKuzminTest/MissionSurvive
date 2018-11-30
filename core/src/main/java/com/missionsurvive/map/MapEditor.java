package com.missionsurvive.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.missionsurvive.MSGame;
import com.missionsurvive.scenarios.Spawn;
import com.missionsurvive.scenarios.SpawnBot;
import com.missionsurvive.scenarios.SpawnScenario;
import com.missionsurvive.utils.Assets;
import com.missionsurvive.utils.Progress;
import com.missionsurvive.utils.Sounds;

import java.util.ArrayList;

public class MapEditor implements Map{

    private TiledMap map;
    private ArrayList<TextureRegion[][]> tilesets = new ArrayList<TextureRegion[][]>();
    private MapLayers layers;
    private TiledMapTileLayer foreground;
    private ScrollMap scrollLevel1Map;
    private MapTer[][] mapTers;
    private Spawn[][] spawns;
    private ParallaxCamera gameCam;
    private Texture layer1Texture;
    private TextureRegion[][] tileset;
    private ParallaxBackground lev3;
    private ParallaxBackground lev2;

    private int tileWidth = 16;
    private int tileHeight = 16;
    private int worldWidth;
    private int worldHeight;

    private boolean isHorizontal = true;
    private boolean isLoaded;

    public MapEditor(){}

    public MapEditor(int worldWidth, int worldHeight){
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public MapEditor(ParallaxBackground lev3, ParallaxBackground lev2,
                     ParallaxCamera gameCam, int worldWidth, int worldHeight){
        this.gameCam = gameCam;
        this.lev3 = lev3;
        this.lev2 = lev2;
        newMap(worldWidth, worldHeight);
    }

    public void newMap(int numCols, int numRows){
        addNewTileset("lev1", tileWidth, tileHeight);
        initMap(numCols, numRows);
        newForeground(numCols, numRows, tileWidth, tileHeight);
    }

    public void initMap(int worldWidth, int worldHeight){
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        scrollLevel1Map = new ScrollMap(tileWidth, tileHeight, worldHeight, worldWidth, false);
        newMapTers(worldWidth, worldHeight);

        map = new TiledMap();
        layers = map.getLayers();
    }

    public void newMapTers(int width, int height){
        mapTers = new MapTer[worldHeight][worldWidth];
        spawns = new Spawn[worldHeight][worldWidth];
        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                mapTers[row][col] = new MapTer(row, col);
            }
        }
    }

    public TiledMapTileLayer getForeground(){
        return foreground;
    }

    public void newForeground(int width, int height, int tileWidth, int tileHeight){
        foreground = new TiledMapTileLayer(width, height, tileWidth, tileHeight);
        layers.add(foreground);
    }

    @Override
    public void addNewTileset(String asset, int tileWidth, int tileHeight){
        if(tileset == null) {
            tilesets.clear();
            layer1Texture = Assets.getTextures()[Assets.getWhichTexture(asset)];
            layer1Texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            int numRows = layer1Texture.getHeight() / (tileHeight + 2);
            int numCols = layer1Texture.getWidth() / (tileWidth + 2);
            tileset = new TextureRegion[numRows][numCols];
            for(int row = 0; row < numRows; row++){
                for(int col = 0; col < numCols; col++){
                    tileset[row][col] = new TextureRegion(layer1Texture,
                            1 + col * (tileWidth + 2), 1 + row * (tileHeight + 2),
                            tileWidth, tileHeight);
                }
            }
            tilesets.add(tileset);
        }
    }

    public TextureRegion[][] getTextureRegion(int i){
        return tilesets.get(i);
    }

    /**
     * default generation was from top-left to bottom-right. With (worldHeight - 1) we
     * adjust default map generation to libGdx coords of a tilemap.
     * @param col
     * @param row
     * @param assetX
     * @param assetY
     */
    public void setTile(TextureRegion[][] textureRegion,
                        int col, int row, int assetX, int assetY){
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(new StaticTiledMapTile(textureRegion[assetY][assetX]));
        foreground.setCell(col, (worldHeight - 1) - row, cell);
        mapTers[row][col].setSrcX(assetX);
        mapTers[row][col].setSrcY(assetY);
    }

    public void removeTile(int col, int row){
        foreground.setCell(col, (worldHeight - 1) - row, null);
        mapTers[row][col].setSrcX(-1);
        mapTers[row][col].setSrcY(-1);
    }

    private void setLev2(String assetName, float  startX, float startY ){
        Texture lev2Texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        lev2Texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion bg2 = new TextureRegion(lev2Texture, 0, 0, 256, 256);
        lev2 = new ParallaxBackground(new ParallaxLayer[]{
                new ParallaxLayer(bg2, new Vector2(1, 1), new Vector2(startX, startY), new Vector2(0, 0))
        }, 480, 320, new Vector2(0.5f, 0));
    }

    private void setLev3(String assetName){
        Texture lev3Texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        lev3Texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion bg3 = new TextureRegion(lev3Texture, 1, 1, 480, 320);
        lev3 = new ParallaxBackground(new ParallaxLayer[]{
                new ParallaxLayer(bg3, new Vector2(1, 1), new Vector2(0, 0)),
        }, 480, 320, new Vector2(0.25f, 0));
    }

    /** Getting 3 map levels from text String.
     * @param mapTerInString
     */
    @Override
    public boolean loadMap(String mapTerInString){
        isLoaded = false;
        String difficulty = Progress.BEGINNER;

        if(mapTerInString == null){
            return isLoaded = false;
        }

        String fragmentDelims = "\\:";

        String[] fragmentTokens = mapTerInString.split(fragmentDelims);
        int len = (fragmentTokens.length);
        int row = -1, col = -1;

        for(int whichChar = 0; whichChar < len; whichChar++){
            if(fragmentTokens[whichChar].contains("\n")){
                fragmentTokens[whichChar] = fragmentTokens[whichChar].replaceAll("\n", "");
            }

            if(fragmentTokens[whichChar]. equalsIgnoreCase("new")){
                worldWidth = Integer.parseInt(fragmentTokens[whichChar + 1]);
                worldHeight = Integer.parseInt(fragmentTokens[whichChar + 2]);
                newMap(worldWidth, worldHeight);

                isLoaded = true;
            }
            if(fragmentTokens[whichChar].equalsIgnoreCase("scroll")){
                scrollLevel1Map.setWorldOffset(Integer.parseInt(fragmentTokens[whichChar + 1]),
                        Integer.parseInt(fragmentTokens[whichChar + 2]));
            }
            if(fragmentTokens[whichChar].equalsIgnoreCase("difficulty")){
                difficulty = fragmentTokens[whichChar + 1];
            }
            if(fragmentTokens[whichChar].equalsIgnoreCase("vertical")){
                isHorizontal = false;
            }
            if(fragmentTokens[whichChar].equalsIgnoreCase("level2MapTer")){
                String assetName = fragmentTokens[whichChar + 1];
                int startX = Integer.parseInt(fragmentTokens[whichChar + 2]);
                int startY = Integer.parseInt(fragmentTokens[whichChar + 3]);
                setLev2(assetName, startX, startY);
            }
            if(fragmentTokens[whichChar].equalsIgnoreCase("level3MapTer")){
                setLev3(fragmentTokens[whichChar + 1]);
            }
            if(fragmentTokens[whichChar].equalsIgnoreCase("music")){
                Sounds.loadMusic(fragmentTokens[whichChar + 1]);
            }

            if(fragmentTokens[whichChar].equalsIgnoreCase("row")){
                row = Integer.parseInt(fragmentTokens[whichChar + 1]); //+1, because we have integer after String "row" saved in our txt file.
                col = Integer.parseInt(fragmentTokens[whichChar + 3]); //+3 - whichCol.

                if(row < 0 || row >= worldHeight){
                    continue;
                }
                if(col < 0 || col >= worldWidth){
                    continue;
                }

                if(Integer.parseInt(fragmentTokens[whichChar + 4]) >=  0) {
                    int assetY = Integer.parseInt(fragmentTokens[whichChar + 4]);
                    int assetX = Integer.parseInt(fragmentTokens[whichChar + 5]);

                    if(assetY != -1){
                        if(assetX != -1){
                            setTile(getTextureRegion(0),
                                    col, row, assetX, assetY);
                        }
                    }
                }
                if(fragmentTokens[whichChar + 6].equalsIgnoreCase("blocked")){
                    mapTers[row][col].setBlocked(true);
                }
                if(fragmentTokens[whichChar + 6].equalsIgnoreCase("ladder")){
                    mapTers[row][col].setBlocked(true);
                    mapTers[row][col].setLadder(true);
                }
                if(fragmentTokens[whichChar + 6].equalsIgnoreCase("enemy")){
                    int botId = Integer.parseInt(fragmentTokens[whichChar + 7]);
                    if(botId < 1000){
                        spawns[row][col] = new SpawnBot(botId,
                                Integer.parseInt(fragmentTokens[whichChar + 8]), row, col);
                    }
                    else{
                        spawns[row][col] = new SpawnScenario(botId,
                                Integer.parseInt(fragmentTokens[whichChar + 8]), difficulty, row, col);
                    }
                }
            }
        }
        return isLoaded;
    }

    @Override
    public String saveMap() {

        String map = null;
        StringBuilder stringBuilder = new StringBuilder();
        if(mapTers == null) return map;

        if(!isHorizontal){
            stringBuilder.append(":");
            stringBuilder.append("vertical");
            stringBuilder.append("\n");
        }

        if(mapTers != null){
            stringBuilder.append(":");
            stringBuilder.append("level1MapTer");
            stringBuilder.append(":");
            stringBuilder.append("lev1"); //name of the asset with tileset.
            stringBuilder.append(":");
            stringBuilder.append("\n");
            setLevelMap(stringBuilder, mapTers, spawns);
        }

        map = stringBuilder.toString();
        return map;
    }

    private void setLevelMap(StringBuilder stringBuilder, MapTer[][] mapTer, Spawn[][] spawns){
        for(int row = 0; row < mapTer.length; row++){
            for(int col = 0; col < mapTer[0].length; col++){
                stringBuilder.append("row");
                stringBuilder.append(":");
                stringBuilder.append(row);
                stringBuilder.append(":");
                stringBuilder.append("col");
                stringBuilder.append(":");
                stringBuilder.append(col);
                stringBuilder.append(":");
                if(mapTer[row][col] != null){
                    stringBuilder.append(mapTer[row][col].getSrcY());
                    stringBuilder.append(":");
                    stringBuilder.append(mapTer[row][col].getSrcX());
                }
                else{
                    stringBuilder.append(-1);
                    stringBuilder.append(":");
                    stringBuilder.append(-1);
                }
                if(mapTer[row][col].isLadder()){
                    stringBuilder.append(":");
                    stringBuilder.append("ladder");
                    stringBuilder.append(":");
                    stringBuilder.append("\n");
                    continue;
                }
                if(spawns != null){
                    if(spawns[row][col] != null){
                        stringBuilder.append(":");
                        stringBuilder.append("enemy");
                        stringBuilder.append(":");
                        stringBuilder.append(spawns[row][col].getBotId());
                        stringBuilder.append(":");
                        stringBuilder.append(spawns[row][col].getDirection());
                        stringBuilder.append(":");
                        stringBuilder.append("\n");
                        continue;
                    }
                }
                if(mapTer[row][col].isBlocked()){
                    stringBuilder.append(":");
                    stringBuilder.append("blocked");
                }
                stringBuilder.append(":");
                stringBuilder.append("\n");
            }
        }
    }

    /**
     * sets position of a foreground to see.
     */
    public void setCamPositionX(float x){
        gameCam.position.x = -MSGame.SCREEN_OFFSET_X + scrollLevel1Map.getWorldOffsetX();
        lev3.updateCamera(x);
        lev2.updateCamera(x);
    }

    public void setCamPositionY(){
        gameCam.position.y = worldHeight * 16 + MSGame.SCREEN_OFFSET_Y - scrollLevel1Map.getWorldOffsetY();
    }

    public boolean isHorizontal(){
        return isHorizontal;
    }

    public void setHorizontal(boolean horizontal){this.isHorizontal = horizontal;}

    @Override
    public void horizontScroll(float delta, int x){
        scrollLevel1Map.setWorldOffset(x, 0);
        setCamPositionX(x);
    }

    @Override
    public void verticalScroll(int y){
        scrollLevel1Map.setWorldOffset(0, y);
        setCamPositionY();
    }

    public ScrollMap getScrollLevel1Map(){
        return scrollLevel1Map;
    }

    public void autoScrollX(float delta){}

    public TiledMap getMap(){
        return map;
    }

    @Override
    public MapTer[][] getLevel1Ter(){
        return mapTers;
    }

    public int getTileWidth(){
        return tileWidth;
    }

    public int getTileHeight(){
        return tileHeight;
    }

    @Override
    public ScrollMap getScrollMap() {
        return scrollLevel1Map;
    }

    public Spawn[][] getSpawns(){
        return spawns;
    }

    public void setScrollMap(ScrollMap scrollMap){
        scrollLevel1Map = scrollMap;
    }

    public void setGameCam(ParallaxCamera gameCam) {
        this.gameCam = gameCam;
    }

    @Override
    public ParallaxBackground getBackground(String name) {
        if(name != null){
            if(name.equalsIgnoreCase("lev2")){
                return lev2;
            }
            else if(name.equalsIgnoreCase("lev3")){
                return lev3;
            }
        }
        return null;
    }
}
