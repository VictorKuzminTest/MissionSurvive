package com.missionsurvive.scenarios.controlscenarios;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.map.MapTer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuzmin on 04.05.18.
 */

public class MapEditorCS implements ControlScenario {

    public static final int REMOVE_TILE = -1;
    public static final int PUT_TILE = 0;
    public static final int BLOCK_TILE = 1;
    public static final int UNBLOCK_TILE = 2;
    public static final int PUT_LADDER = 3;

    private List<ControlPanel> listOfPanels = new ArrayList<ControlPanel>();
    private ArrayList<MapTer> mapTerArrayList = new ArrayList<MapTer>();
    private  boolean isTouchingPanels;

    public MapEditorCS(){
        /*setControlPanels(object);
        ListingScenario.addButtonsToList("lev1", 0, null);
        ListingScenario.addButtonsToList("iconseditor", 1, object);*/
    }



    /*public void setControlPanels(){
        for(int whichPanel = 0; whichPanel < Controls.controlPanels.length; whichPanel++){
            if(Controls.controlPanels[whichPanel].getScreen().equalsIgnoreCase("MapEditorScreen")) listOfPanels.add(Controls.controlPanels[whichPanel]);
        }
        listOfPanels.get(0).setActivated(true);
    }*/

    @Override
    public void onTouchPanels(float scaleX, float scaleY) {

    }

    public void drawPanels(SpriteBatch batch){
        /*int numPanels = listOfPanels.size();
        for(int whichPanel = 0; whichPanel < numPanels; whichPanel++){
            ControlPanel controlPanel = listOfPanels.get(whichPanel);
            if(controlPanel.isActivated() == true) controlPanel.drawPanel(g);
        }*/
    }

    /*public void onTouchPanels(float scaleX, float scaleY){
        int numPanels = listOfPanels.size();
        for(int whichPanel = 0; whichPanel < numPanels; whichPanel++){
            ControlPanel controlPanel = listOfPanels.get(whichPanel);
            if(controlPanel.isActivated() == true) controlPanel.touchButtons(game, this, touchControl, touchEvents, object);
        }
    }*/

    @Override
    public boolean isTouchingPanels(){
        return isTouchingPanels;
    }

    @Override
    public void touchingPanels(boolean touch){
        isTouchingPanels = touch;
    }


    /*@Override
    public void action(Game game, Player player, Object object, int actionParameter) {
        if(actionParameter == 0){ //просто касаемся участков карты.
            if(object != null){
                if(object instanceof MapTer){
                    MapTer mapTer = (MapTer)object;
                    mapTer.setEditing(true);
                    mapTerArrayList.add(mapTer);
                }
            }
        }
        if(actionParameter == 8){ //PlayProject
            if(object instanceof MapEditorScreen){
                MapEditorScreen mapEditorScreen = (MapEditorScreen)object;

                if(mapEditorScreen.isHeroControl()){
                    mapEditorScreen.setHeroControl(false);
                }
                else{
                    mapEditorScreen.setHeroControl(true);
                    if(mapEditorScreen.getScenario() instanceof PlatformerScenario){
                        ((PlatformerScenario)mapEditorScreen.getScenario()).setFirstTimeSpawned();
                    }
                }
            }
        }
        if(actionParameter == 3){  //Here we put tiles level 1.
            if(object != null){
                if(object instanceof MapObject){
                    MapObject mapObject = (MapObject)object;
                    editMapTerList(PUT_TILE, mapObject);
                }
            }
        }
        if(actionParameter == 4){  //Here we put background level 2.
            if(object != null){
                if(object instanceof MapObject){
                    MapObject mapObject = (MapObject)object;
                    editMapTerList(PUT_TILE, mapObject);
                }
            }
        }
        if(actionParameter == 5){  //Here we put background level 3.
            if(object != null){
                if(object instanceof MapObject){
                    MapObject mapObject = (MapObject)object;
                    editMapTerList(PUT_TILE, mapObject);
                }
            }
        }
        if(actionParameter == 6){  //put player.
            if(object != null){
                if(object instanceof MapEditorScreen){
                    MapEditorScreen mapEditorScreen = (MapEditorScreen)object;
                    mapEditorScreen.putPlayer(100, 150);
                }
            }
        }
        if(actionParameter == 7){ //put zombie.
            game.showPopup(AndroidLook.POPUP_NEW_BOT); //show dialog for setting new bots.
        }
        if(actionParameter == 300){
            putBot(SpawnBot.ZOMBIE, Enemy.WEST);
        }
        if(actionParameter == 301){
            putBot(SpawnBot.ZOMBIE, Enemy.EAST);
        }
        if(actionParameter == 310){
            putBot(SpawnBot.SHOTGUN_ZOMBIE, Enemy.EAST);
        }
        if(actionParameter == 311){
            putBot(SpawnBot.SHOTGUN_ZOMBIE, Enemy.WEST);
        }
        if(actionParameter == 32){
            putBot(SpawnBot.LEVEL_1_BOSS, Enemy.EAST);
        }
        if(actionParameter == 33){
            putBot(SpawnBot.LEVEL_3_BOSS, Enemy.EAST);
        }
        if(actionParameter == 66){
            putBot(SpawnBot.LEVEL_6_BOSS, 0);
        }
        if(actionParameter == 1001){
            putBot(SpawnBot.POWER_UP_LIFE, PowerUp.DIRECTION_EAST);
        }
        if(actionParameter == 1002){
            putBot(SpawnBot.POWER_UP_GUN, PowerUp.DIRECTION_EAST);
        }
        if(actionParameter == 1003){
            putBot(SpawnBot.POWER_UP_LIFE, PowerUp.DIRECTION_NORTH);
        }
        if(actionParameter == 1004){
            putBot(SpawnBot.POWER_UP_GUN, PowerUp.DIRECTION_NORTH);
        }
        if(actionParameter == 2000){
            putBot(SpawnBot.WRECKAGE, 0);
        }
        if(actionParameter == 3000){
            putBot(SpawnScenario.LEVEL_2_SCENE, 0);
        }

        if(actionParameter == 9){  //showAllLevels of tileset.
            if(object instanceof MapEditorScreen){
                MapEditorScreen mapEditorScreen = (MapEditorScreen)object;
                mapEditorScreen.setTilesetLevelToDraw(0);
                mapEditorScreen.getMapEditor().getScrollLevel1Map().setExtremesToNormal(false);
                mapEditorScreen.getMapEditor().getScrollLevel2Map().setExtremesToNormal(true);
                mapEditorScreen.getMapEditor().getScrollLevel3Map().setExtremesToNormal(true);
            }
        }
        if(actionParameter == 10){ //showLevel1Tileset
            if(object instanceof MapEditorScreen){
                MapEditorScreen mapEditorScreen = (MapEditorScreen)object;
                mapEditorScreen.setTilesetLevelToDraw(1);  //draw only first level of tileset.

                //we also reset extremes in ScrollMap Class, so user could edit map easily:
                mapEditorScreen.getMapEditor().getScrollLevel1Map().setExtremesForEditing(-(mapEditorScreen.getGame().getGraphics().getWidth() / 2),
                        mapEditorScreen.getMapEditor().getLevel1Ter()[0].length * 16,              //16 - tileWidth
                        -(mapEditorScreen.getGame().getGraphics().getHeight() / 2),
                        mapEditorScreen.getMapEditor().getLevel1Ter().length * 16);        //tileHeight.
            }
            ListingScenario.addButtonsToList("lev1", 0, null);
        }
        if(actionParameter == 11){ //showLevel2Tileset
            if(object instanceof MapEditorScreen){
                MapEditorScreen mapEditorScreen = (MapEditorScreen)object;
                mapEditorScreen.setTilesetLevelToDraw(2);  //draw only first level of tileset.

                mapEditorScreen.getMapEditor().getScrollLevel2Map().setExtremesForEditing(-(mapEditorScreen.getGame().getGraphics().getWidth() / 2),
                        mapEditorScreen.getMapEditor().getLevel2Ter()[0].length * 16,              //16 - tileWidth
                        -(mapEditorScreen.getGame().getGraphics().getHeight() / 2),
                        mapEditorScreen.getMapEditor().getLevel2Ter().length * 16);        //tileHeight.
            }
            ListingScenario.addButtonsToList("lev2", 0, null);
        }
        if(actionParameter == 12){  //showLevel3Tileset
            if(object instanceof MapEditorScreen){
                MapEditorScreen mapEditorScreen = (MapEditorScreen)object;
                mapEditorScreen.setTilesetLevelToDraw(3);  //draw only first level of tileset.

                mapEditorScreen.getMapEditor().getScrollLevel3Map().setExtremesForEditing(-(mapEditorScreen.getGame().getGraphics().getWidth() / 2),
                        mapEditorScreen.getMapEditor().getLevel3Ter()[0].length * 16,              //16 - tileWidth
                        -(mapEditorScreen.getGame().getGraphics().getHeight() / 2),
                        mapEditorScreen.getMapEditor().getLevel3Ter().length * 16);        //tileHeight.
            }
            ListingScenario.addButtonsToList("lev3", 0, null);
        }
        if(actionParameter == 13){  //blockTile
            editMapTerList(BLOCK_TILE, null);
        }
        if(actionParameter == 14){  //unblockTile
            editMapTerList(UNBLOCK_TILE, null);
        }
        if(actionParameter == 15){  //save
            game.showPopup(AndroidLook.POPUP_SAVE); //show dialog for saving levels.
        }
        if(actionParameter == 16){ //load
            game.showPopup(AndroidLook.POPUP_LOAD); //show dialog for loading levels.
        }
        if(actionParameter == 19){ //removeTile
            editMapTerList(REMOVE_TILE, null);
        }
        if(actionParameter == 20){ //blockMap
            if(object instanceof MapEditorScreen){
                MapEditorScreen mapEditorScreen = (MapEditorScreen)object;

                boolean isScrollMap;
                isScrollMap = (mapEditorScreen.isScrollMap()) ? false : true;
                mapEditorScreen.setScrollMap(isScrollMap);
            }
        }
        if(actionParameter == 21){ //putLadder
            editMapTerList(PUT_LADDER, null);
        }
        if(actionParameter == 22){ //sloMo: change skipping sprites time.
            if(object != null){
                if(object instanceof PlatformerScreen){
                    PlatformerScreen platformerScreen = (PlatformerScreen)object;
                    platformerScreen.getHero().setAnimationTick();
                }
            }
        }
        if(actionParameter == 23){ //newMap: calls a dialog for new map building.
            game.showPopup(AndroidLook.POPUP_NEW_MAP); //show dialog for saving levels.
        }
        if(actionParameter == 24){ //scrollMap: changes a scroll of a map to horizontal, vertical etc.
            if(object != null){
                if(object instanceof PlatformerScreen){
                    PlatformerScreen platformerScreen = (PlatformerScreen)object;
                    if(platformerScreen.getScenario() instanceof PlatformerScenario){
                        PlatformerScenario platformerScenario =
                                (PlatformerScenario) platformerScreen.getScenario();

                        if(platformerScenario.isHorizontal()){
                            platformerScenario.setHorizontal(false);
                            platformerScenario.setVertical(true);
                        }
                        else{
                            platformerScenario.setHorizontal(true);
                            platformerScenario.setVertical(false);
                        }
                    }
                }
            }
        }
    }*/


    /**
     * Edits tiles that array list of tiles contain depending on action we want to do with the tiles.
     * @param action
     * @param mapObject
     */
    /*public void editMapTerList(int action, MapObject mapObject){
        int len = mapTerArrayList.size();
        switch (action){
            case REMOVE_TILE:
                for(int mapTerNum = 0; mapTerNum < len; mapTerNum++){

                    MapUtils.removeMapObject(mapTerArrayList.get(mapTerNum));
                    mapTerArrayList.get(mapTerNum).setEditing(false);
                }
                break;
            case PUT_TILE:
                for(int mapTerNum = 0; mapTerNum < len; mapTerNum++) {
                    MapUtils.addMapObject(mapTerArrayList.get(mapTerNum), mapObject);
                    mapTerArrayList.get(mapTerNum).setEditing(false);
                }
                break;
            case BLOCK_TILE:
                for(int mapTerNum = 0; mapTerNum < len; mapTerNum++) {
                    mapTerArrayList.get(mapTerNum).setBlocked(true);
                    mapTerArrayList.get(mapTerNum).setEditing(false);
                }
                break;
            case UNBLOCK_TILE:
                for(int mapTerNum = 0; mapTerNum < len; mapTerNum++) {
                    mapTerArrayList.get(mapTerNum).setBlocked(false);
                    mapTerArrayList.get(mapTerNum).setLadder(false);
                    mapTerArrayList.get(mapTerNum).setEditing(false);
                    Scenario scenario = Assets.getGame().getCurrentScreen().getScenario();
                    if(scenario instanceof PlatformerScenario){
                        ((PlatformerScenario)scenario).getSpawnEnemies()[mapTerArrayList.get(mapTerNum).getRow()]
                                [mapTerArrayList.get(mapTerNum).getCol()] = null;
                    }

                }
                break;
            case PUT_LADDER:
                for(int mapTerNum = 0; mapTerNum < len; mapTerNum++) {
                    mapTerArrayList.get(mapTerNum).setBlocked(true);
                    mapTerArrayList.get(mapTerNum).setLadder(true);
                    mapTerArrayList.get(mapTerNum).setEditing(false);
                }
                break;
        }
        mapTerArrayList.clear();
    }*/


    /**
     * Puts bot to chosen MapTers.
     */
    /*public void putBot(int bot, int direction){
        Scenario scenario = Assets.getGame().getCurrentScreen().getScenario();
        if(scenario instanceof PlatformerScenario){
            int len = mapTerArrayList.size();
            for(int mapTerNum = 0; mapTerNum < len; mapTerNum++){
                mapTerArrayList.get(mapTerNum).setEditing(true);
                ((PlatformerScenario)scenario).setSpawn(mapTerArrayList.get(mapTerNum).getCol(),
                        mapTerArrayList.get(mapTerNum).getRow(), bot, direction);
            }
            mapTerArrayList.clear();
        }
    }*/


    @Override
    public void setControlPanels() {
        // TODO Auto-generated method stub
    }

    @Override
    public List<ControlPanel> getControlPanels() {
        return listOfPanels;
    }
}
