package com.missionsurvive.framework.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.commands.Command;
import com.missionsurvive.framework.Button;
import com.missionsurvive.framework.Observer;
import com.missionsurvive.framework.TouchControl;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.utils.Assets;

/**
 * Created by kuzmin on 25.04.18.
 */

public class ActionButton implements Button, Observer{

    private Texture texture;
    private Command command;

    private int screenX;
    private int screenY;
    private int srcX, srcY;
    private int buttonWidth;
    private int buttonHeight;
    private int actionParameter;

    public ActionButton(String assetName, int screenX, int screenY, int srcX, int srcY, int buttonWidth, int buttonHeight, String action){
        this.screenX = screenX;
        this.screenY = screenY;
        this.srcX = srcX;
        this.srcY = srcY;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;

        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }

        if(action != null){
            if(action.equalsIgnoreCase("MapScreen")) actionParameter = 1;
            if(action.equalsIgnoreCase("MapEditorScreen")) actionParameter = 2;

            if(action.equalsIgnoreCase("lev1")) actionParameter = 3;
            if(action.equalsIgnoreCase("lev2")) actionParameter = 4;
            if(action.equalsIgnoreCase("lev3")) actionParameter = 5;
            if(action.equalsIgnoreCase("putPlayer")) actionParameter = 6;
            if(action.equalsIgnoreCase("putBot")) actionParameter = 7;
            if(action.equalsIgnoreCase("PlayProject")) actionParameter = 8;
            if(action.equalsIgnoreCase("allLevelsTileset")) actionParameter = 9;
            if(action.equalsIgnoreCase("level1Tileset")) actionParameter = 10;
            if(action.equalsIgnoreCase("level2Tileset")) actionParameter = 11;
            if(action.equalsIgnoreCase("level3Tileset")) actionParameter = 12;
            if(action.equalsIgnoreCase("blockTile")) actionParameter = 13;
            if(action.equalsIgnoreCase("unblockTile")) actionParameter = 14;
            if(action.equalsIgnoreCase("save")) actionParameter = 15;
            if(action.equalsIgnoreCase("load")) actionParameter = 16;
            if(action.equalsIgnoreCase("putBoss")) actionParameter = 17;
            if(action.equalsIgnoreCase("putShotgun")) actionParameter = 18;
            if(action.equalsIgnoreCase("removeTile")) actionParameter = 19;
            if(action.equalsIgnoreCase("blockMap")) actionParameter = 20;
            if(action.equalsIgnoreCase("putLadder")) actionParameter = 21;
            if(action.equalsIgnoreCase("sloMo")) actionParameter = 22;
            if(action.equalsIgnoreCase("newMap")) actionParameter = 23;
            if(action.equalsIgnoreCase("scrollMap")) actionParameter = 24;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void setCommand(Command command) {
        this.command = command;
    }

    @Override
    public boolean onClick(boolean onClick) {
        if(onClick){
            if(command != null){
                command.execute(null);
            }
        }
        return onClick;
    }

    @Override
    public void drawButton(SpriteBatch batch) {
        batch.begin();
        batch.draw(texture, -240 + screenX,
                -160 + GeoHelper.transformCanvasYCoordToGL(screenY, 320, buttonHeight),
                buttonWidth, buttonHeight);
        batch.end();
    }

    @Override
    public void drawButton(SpriteBatch batch, int offsetStartX, int offsetStartY,
                           int offsetWidth, int offsetHeight) {

    }

    @Override
    public int getStartX() {
        return screenX;
    }

    @Override
    public int getStartY() {
        return screenY;
    }

    @Override
    public int getButtonWidth() {
        return buttonWidth;
    }

    @Override
    public int getButtonHeight() {
        return buttonHeight;
    }

    @Override
    public void setStartX(int x) {

    }

    @Override
    public void setStartY(int y) {

    }
}
