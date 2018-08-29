package com.missionsurvive.scenarios;

import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.screens.GameScreen;
import com.missionsurvive.utils.Controls;

/**
 * Created by kuzmin on 29.05.18.
 */

public class PlayScript {

    private int lives;
    private GameScreen gameScreen;
    private ControlPanel controlPanel;

    public PlayScript(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        controlPanel = getControlPanel("mapEditorControls");
        setLives(3);
    }

    public void setLives(int lives){
        this.lives = lives;
        setLivesIcon();
    }

    public int getLives(){
        return lives;
    }

    public void addLife() {
        lives = lives + 1;
        setLivesIcon();
    }

    public void subtractLife(){
        lives = lives - 1;
        setLivesIcon();
    }

    /**
     * We resurrect hero on the last fixed MapTer.
     * @param currentMapTer
     * @param mapEditor
     */
    public void resurrectHero(MapTer currentMapTer, MapEditor mapEditor, Hero hero) {
        if(currentMapTer != null){
            gameScreen.putPlayer(getXYToResurrect(currentMapTer.getCol(), 20,
                                mapEditor.getScrollLevel1Map().getWorldOffsetX()),
                                getXYToResurrect(currentMapTer.getRow(), hero.getSpriteHeight(),
                                        mapEditor.getScrollLevel1Map().getWorldOffsetY()));
        }
    }

    /**
     * Calculates the x or y position basing on row-col, object value and world offset coordinates.
     * @param rowCol
     * @param objOffsetValue
     * @param worldOffsetXY
     * @return
     */
    public int getXYToResurrect(int rowCol, int objOffsetValue, int worldOffsetXY){
        return rowCol * (16 - 1) - objOffsetValue - worldOffsetXY;
    }

    /**
     * tileWidth - 16. spaceBetweenTiles - (16 + 2).
     * When we calculate the second digit: if numLives >= 9 we set digit width and height
     * to tiles width and height. If not - we set width and height to 0, for it couldn't be visible.
     */
    public void setLivesIcon(){
        if(lives >= 9){
            int assetCol = (lives + 1) / 10;
            controlPanel.getIcon(2).setAssetStartX(1 + assetCol * (16 + 2));
            controlPanel.getIcon(2).setAssetStartY(1);

            assetCol = (lives + 1) % 10;
            controlPanel.getIcon(3).setAssetStartX(1 + assetCol * (16 + 2));
            controlPanel.getIcon(3).setAssetStartY(1);
            controlPanel.getIcon(3).setAssetWidth(16);
            controlPanel.getIcon(3).setAssetHeight(16);
        }
        else{
            int assetCol = lives + 1;
            controlPanel.getIcon(2).setAssetStartX(1 + assetCol * (16 + 2));
            controlPanel.getIcon(2).setAssetStartY(1);

            controlPanel.getIcon(3).setAssetWidth(0);
            controlPanel.getIcon(3).setAssetHeight(0);
        }
    }

    /**
     * Setting control panel for further use.
     * @param name
     */
    public ControlPanel getControlPanel(String name){
        for(int i = 0; i < Controls.controlPanels.length; i++){
            if(Controls.controlPanels[i].getName().equalsIgnoreCase("mapEditorControls")){
                this.controlPanel = Controls.controlPanels[i];
            }
        }
        return controlPanel;
    }

}