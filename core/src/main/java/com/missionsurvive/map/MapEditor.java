package com.missionsurvive.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.missionsurvive.utils.Assets;

import java.util.ArrayList;

/**
 * Created by kuzmin on 23.04.18.
 */

public class MapEditor {

    private TiledMap map;
    private ArrayList<TextureRegion[][]> tilesets = new ArrayList<TextureRegion[][]>();
    private MapLayers layers;
    private TiledMapTileLayer foreground;
    private TextureRegion lev3;
    private ScrollMap scrollLevel1Map;
    private MapTer[][] mapTers;

    private int tileWidth = 16;
    private int tileHeight = 16;
    private int worldWidth;
    private int worldHeight;

    public MapEditor(int worldWidth, int worldHeight){
        newMap(worldWidth, worldHeight);
    }

    public void newMap(int numCols, int numRows){
        initMap(numCols, numRows);
    }

    public void initMap(int worldWidth, int worldHeight){
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        tilesets.clear();
        map = new TiledMap();
        layers = map.getLayers();

        Texture lev3Tex = Assets.getTextures()[Assets.getWhichTexture("lev3")];
        lev3Tex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        lev3 = new TextureRegion(lev3Tex, 0, 0, 480, 320);
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

    public void addNewTileset(String asset, int tileWidth, int tileHeight){
        Texture texture = Assets.getTextures()[Assets.getWhichTexture(asset)];
        TextureRegion[][] tileset;
        tileset = TextureRegion.split(texture, tileWidth, tileHeight);
        tilesets.add(tileset);
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
    }


    /** Getting 3 map levels from text String.
     * @param mapTerInString
     */
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
                newForeground(worldWidth, worldHeight, 16, 16);
                addNewTileset("lev1", 16, 16);
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

                    setTile(getTextureRegion(0),
                            col, row, assetX, assetY);
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

    public void horizontScroll(int x){}

    public void verticalScroll(int y){}

    public ScrollMap getScrollLevel1Map(){
        return scrollLevel1Map;
    }

    public void autoScrollX(float delta){}

    public TiledMap getMap(){
        return map;
    }

    public MapTer[][] getLevel1Ter(){
        return mapTers;
    }

    public int getTileWidth(){
        return tileWidth;
    }

    public int getTileHeight(){
        return tileHeight;
    }

}
