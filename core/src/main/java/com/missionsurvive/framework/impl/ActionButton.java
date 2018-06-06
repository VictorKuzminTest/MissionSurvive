package com.missionsurvive.framework.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.missionsurvive.MSGame;
import com.missionsurvive.scenarios.commands.Command;
import com.missionsurvive.framework.Button;
import com.missionsurvive.framework.Observer;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.utils.Assets;

/**
 * Created by kuzmin on 25.04.18.
 */

public class ActionButton implements Button, Observer{

    private Texture texture;
    private Command command;
    private ShapeRenderer coloredBounds;

    private int screenX;
    private int screenY;
    private int srcX, srcY;
    private int buttonWidth;
    private int buttonHeight;

    private float screenXHelper; //helps to accumulate float values (it is used in list of buttons)
    private float screenYHelper;
    private float scaleToDrawX, scaleToDrawY;

    public ActionButton(String assetName, int screenX, int screenY, int srcX, int srcY,
                        int buttonWidth, int buttonHeight, Command command){
        this.screenX = screenX;
        this.screenY = screenY;
        screenXHelper = screenX;
        screenYHelper = screenY;
        this.srcX = srcX;
        this.srcY = srcY;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;

        if(assetName != null){
            if(assetName.equalsIgnoreCase("clear")){
                coloredBounds = new ShapeRenderer();
                coloredBounds.setColor(0.5f, 0.5f, 0, 1);
                scaleToDrawX = (float) Gdx.graphics.getBackBufferWidth() / MSGame.SCREEN_WIDTH;
                scaleToDrawY = (float)Gdx.graphics.getBackBufferHeight() / MSGame.SCREEN_HEIGHT;
            }
            else{
                texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
            }
        }

        this.command = command;
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
                command.execute(null, null);
            }
        }
        return onClick;
    }

    @Override
    public void drawButton(SpriteBatch batch) {
        if(texture != null){
            batch.begin();
            batch.draw(texture, MSGame.SCREEN_OFFSET_X + screenX,
                    MSGame.SCREEN_OFFSET_Y + GeoHelper.transformCanvasYCoordToGL(screenY,
                            MSGame.SCREEN_HEIGHT, buttonHeight),
                    srcX, srcY,
                    buttonWidth, buttonHeight);
            batch.end();
        }
        else if(coloredBounds != null){
            drawColoredBounds();
        }
    }

    public void drawColoredBounds(){
        coloredBounds.begin(ShapeRenderer.ShapeType.Filled);
        coloredBounds.rect(screenX * scaleToDrawX,
                GeoHelper.transformCanvasYCoordToGL(screenY,
                        MSGame.SCREEN_HEIGHT, buttonHeight) * scaleToDrawY,
                buttonWidth * scaleToDrawX, buttonHeight * scaleToDrawY);
        coloredBounds.end();
    }

    @Override
    public void drawButton(SpriteBatch batch, int offsetStartX, int offsetStartY,
                           int offsetWidth, int offsetHeight) {
        batch.begin();
        batch.draw(texture, MSGame.SCREEN_OFFSET_X + screenX + offsetStartX,
                MSGame.SCREEN_OFFSET_Y + GeoHelper.transformCanvasYCoordToGL(screenY + offsetStartY,
                        MSGame.SCREEN_HEIGHT, buttonHeight + offsetHeight),
                srcX + offsetStartX, srcY + offsetStartY,
                buttonWidth + offsetWidth, buttonHeight + offsetHeight);
        batch.end();
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
    public void setStartX(float x) {
        screenXHelper += x;
        screenX = (int)screenXHelper;
    }

    @Override
    public void setStartY(float y) {
        screenYHelper += y;
        screenY = (int)screenYHelper;
    }

}
