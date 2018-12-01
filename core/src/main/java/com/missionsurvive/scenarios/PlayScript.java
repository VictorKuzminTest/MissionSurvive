package com.missionsurvive.scenarios;

import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.Gun;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.screens.GameScreen;
import com.missionsurvive.utils.Controls;

public class PlayScript {

    private GameScreen gameScreen;
    private ControlPanel controlPanel;

    private int lives = 1;
    private int weaponOf;

    public void setScreen(GameScreen gameScreen, String controlPanelName){
        this.gameScreen = gameScreen;
        controlPanel = getControlPanel(controlPanelName);
        setLivesIcon();
    }

    public void setWeapon(Hero hero){
        for(int i = 0; i < weaponOf; i++){
            hero.addGun(Hero.FROM_PLAYSCRIPT);
        }
    }

    public void newLives(){
        lives = 1;
        weaponOf = Gun.WEAPON_HANDGUN;
        controlPanel = getControlPanel("gameControls");
        setLivesIcon();
    }

    public int getLives(){
        return lives;
    }

    public void addLife() {
        lives = lives + 1;
        setLivesIcon();
    }

    public void addGun(){
        switch (weaponOf){
            case Gun.WEAPON_HANDGUN:
                weaponOf++;
                break;
            case Gun.WEAPON_HANDGUN_AUTOMATIC:
                weaponOf++;
                break;
            case Gun.WEAPON_FIREBALL:
                weaponOf++;
                break;
        }
    }

    public void subtractLife(){
        lives = lives - 1;
        weaponOf = Gun.WEAPON_HANDGUN;
        setLivesIcon();
    }

    /**
     * We resurrect hero on the last fixed MapTer.
     * @param currentMapTer
     * @param mapEditor
     */
    public void resurrectHero(MapTer currentMapTer, MapEditor mapEditor, Hero hero) {
        ...
    }

    /**
     * Calculates the x or y position basing on row-col, object value and world offset coordinates.
     * @param rowCol
     * @param objOffsetValue
     * @param worldOffsetXY
     * @return
     */
    public int getXYToResurrect(int rowCol, int objOffsetValue, int worldOffsetXY){
        return rowCol * 16 - objOffsetValue - worldOffsetXY;
    }

    /**
     * tileWidth - 16. spaceBetweenTiles - (16 + 2).
     * When we calculate the second digit: if numLives >= 9 we set digit width and height
     * to tiles width and height. If not - we set width and height to 0, for it couldn't be visible.
     */
    public void setLivesIcon(){
        ...
    }

    /**
     * Setting control panel for further use.
     * @param name
     */
    public ControlPanel getControlPanel(String name){
        for(int i = 0; i < Controls.controlPanels.length; i++){
            if(Controls.controlPanels[i].getName().equalsIgnoreCase(name)){
                this.controlPanel = Controls.controlPanels[i];
            }
        }
        return controlPanel;
    }
}
