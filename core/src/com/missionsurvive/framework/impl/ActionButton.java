package com.missionsurvive.framework.impl;

import com.badlogic.gdx.graphics.Texture;
import com.missionsurvive.framework.Button;
import com.missionsurvive.framework.TouchControl;
import com.missionsurvive.utils.Assets;

/**
 * Created by kuzmin on 25.04.18.
 */

public class ActionButton implements Button{

    private int screenX;
    private int screenY;
    private int srcX, srcY;
    private int buttonWidth;
    private int buttonHeight;
    private int actionParameter;

    private Texture texture;

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
        else{
            texture = Assets.getTextures()[Assets.getWhichTexture("button")];
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
    public int isTouching(TouchControl touchControl) {
        return 0;
    }

    @Override
    public void touch() {

    }

    @Override
    public void drawButton() {

    }

    @Override
    public void drawButton(int offsetStartX, int offsetStartY, int offsetWidth, int offsetHeight) {

    }

    @Override
    public void setObject(Object object) {

    }

    @Override
    public int getStartX() {
        return 0;
    }

    @Override
    public int getStartY() {
        return 0;
    }

    @Override
    public int getButtonWidth() {
        return 0;
    }

    @Override
    public int getButtonHeight() {
        return 0;
    }

    @Override
    public void setStartX(int x) {

    }

    @Override
    public void setStartY(int y) {

    }
}
