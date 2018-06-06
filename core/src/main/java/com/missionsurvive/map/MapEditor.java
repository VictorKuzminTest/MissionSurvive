package com.missionsurvive.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.missionsurvive.MSGame;
import com.missionsurvive.scenarios.Spawn;
import com.missionsurvive.scenarios.SpawnBot;
import com.missionsurvive.utils.Assets;

import java.util.ArrayList;

/**
 * Created by kuzmin on 23.04.18.
 */

public class MapEditor implements Map{

    private TiledMap map;
    private ArrayList<TextureRegion[][]> tilesets = new ArrayList<TextureRegion[][]>();
    private MapLayers layers;
    private TiledMapTileLayer foreground;
    private TextureRegion lev3;
    private ScrollMap scrollLevel1Map;
    private MapTer[][] mapTers;
    private Spawn[][] spawns;
    private ParallaxCamera gameCam;
    private Texture layer1Texture;
    private TextureRegion[][] tileset;

    private int tileWidth = 16;
    private int tileHeight = 16;
    private int worldWidth;
    private int worldHeight;

    private boolean isHorizontal = true;

    public MapEditor(int worldWidth, int worldHeight){
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public MapEditor(ParallaxCamera gameCam, int worldWidth, int worldHeight){
        this.gameCam = gameCam;
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

        Texture lev3Tex = Assets.getTextures()[Assets.getWhichTexture("lev3")];
        lev3Tex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        lev3 = new TextureRegion(lev3Tex, 0, 0, 480, 320);
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

    public TextureRegion getlev3(){
        return lev3;
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
    }

    /** Getting 3 map levels from text String.
     * @param mapTerInString
     */
    @Override
    public void loadMap(String mapTerInString){

        if(mapTerInString == null) return;

        //-MapTer[][] mapTer = null;
        String assetName = null;

        String fragmentDelims = "\\:";

        String[] fragmentTokens = mapTerInString.split(fragmentDelims);
        int len = (fragmentTokens.length);
        int row = -1, col = -1;

        for(int whichChar = 0; whichChar < len; whichChar++){
            if(fragmentTokens[whichChar].contains("\n")){
                fragmentTokens[whichChar] = fragmentTokens[whichChar].replaceAll("\n", "");
                row = -1; col = -1;
            }

            //-if(fragmentTokens[whichChar].equalsIgnoreCase("scroll")){
            //-    scrollLevel1Map.setWorldOffset(Integer.parseInt(fragmentTokens[whichChar + 1]),
            //-            Integer.parseInt(fragmentTokens[whichChar + 2]));
            //-}

            //-if(fragmentTokens[whichChar].equalsIgnoreCase("vertical")){
            //-    isHorizontal = false;
            //-}
            //-if(fragmentTokens[whichChar].equalsIgnoreCase("level1MapTer")){
            //-    mapTer = level1Ter;
            //-    assetName = fragmentTokens[whichChar + 1];
            //-}
            //-if(fragmentTokens[whichChar].equalsIgnoreCase("level2MapTer")){
            //-    mapTer = level2Ter;
            //-    assetName = fragmentTokens[whichChar + 1];
            //-}
            //-if(fragmentTokens[whichChar].equalsIgnoreCase("level3MapTer")){
            //-    mapTer = level3Ter;
            //-    assetName = fragmentTokens[whichChar + 1];
            //-}

            if(fragmentTokens[whichChar]. equalsIgnoreCase("level1MapTer")){
                //newForeground(worldWidth, worldHeight, 16, 16);
            }

            //temporary return if to not drawing other layers above layer 1:
            if(fragmentTokens[whichChar].equalsIgnoreCase("level2MapTer")){
                return;
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
                    spawns[row][col] = new SpawnBot(Integer.parseInt(fragmentTokens[whichChar + 7]),
                    Integer.parseInt(fragmentTokens[whichChar + 8]), row, col);
                }

                //-if(Integer.parseInt(fragmentTokens[whichChar + 4]) >=  0){
                //-    int assetY = Integer.parseInt(fragmentTokens[whichChar + 4]);
                //-    int assetX = Integer.parseInt(fragmentTokens[whichChar + 5]);

                //-    MapUtils.addMapObject(mapTer[row][col], new MapObject(assetName, col, row, assetX, assetY, 18, 18, 16, 16, 0, 0, null));
                //-}
                //-if(fragmentTokens[whichChar + 6].equalsIgnoreCase("blocked")){
                //-    mapTer[row][col].setBlocked(true);
                //-}
                //-if(fragmentTokens[whichChar + 6].equalsIgnoreCase("ladder")){
                //-    mapTer[row][col].setBlocked(true);
                //-    mapTer[row][col].setLadder(true);
                //-}
                //-if(fragmentTokens[whichChar + 6].equalsIgnoreCase("enemy")){
                //-    mapTer[row][col].setEditing(true);
                //-    spawns[row][col] = new SpawnBot(Integer.parseInt(fragmentTokens[whichChar + 7]),
                //-            Integer.parseInt(fragmentTokens[whichChar + 8]), row, col);
                //-}
            }
        }
        //-game.getCurrentScreen().setMap(); //set new map to screen.                                                 //+
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
    public void setCamPositionX(){
        gameCam.position.x = -MSGame.SCREEN_OFFSET_X + scrollLevel1Map.getWorldOffsetX();
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
        setCamPositionX();
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
}
