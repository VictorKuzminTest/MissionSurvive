package com.missionsurvive.objs.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.framework.impl.ObjAnimation;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.objs.GameObject;
import com.missionsurvive.utils.Assets;

/**
 * Created by kuzmin on 03.05.18.
 */

public class FlyingHero implements GameObject{

    private Texture texture;

    private int screenX, screenY;
    private float animationTickTime, animationTick = 0.1f;
    private int spriteWidth, spriteHeight;
    private int spritesetSpriteWidth, spritesetSpriteHeight;
    private int[] spritesRows; //each element of this array contains the number of sprites in a row.
    private ObjAnimation animation;


    public FlyingHero(String assetName, int screenX, int screenY){
        this.screenX = screenX;
        this.screenY = screenY;
        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }
        spriteWidth = 88;
        spriteHeight = 50;
        spritesetSpriteWidth = spriteWidth + 2;
        spritesetSpriteHeight = spriteHeight + 2;

        spritesRows = new int[1];
        spritesRows[0] = 4;
        animation = new ObjAnimation(spritesRows, spriteWidth, spriteHeight);
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {

        batch.begin();
        batch.draw(texture, MSGame.SCREEN_OFFSET_X + screenX,
                MSGame.SCREEN_OFFSET_Y +
                        GeoHelper.transformCanvasYCoordToGL(screenY, MSGame.SCREEN_HEIGHT, spriteHeight),
                1 + animation.getCurrentFrame() * spritesetSpriteWidth,
                553,
                spriteWidth, spriteHeight);
        batch.end();
    }

    @Override
    public void drawObject(SpriteBatch batch, int screenX, int screenY){

    }

    @Override
    public boolean onTouch() {
        return false;
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public Decorator getDecorator() {
        return null;
    }

    public void animate(float deltaTime){
        animationTickTime += deltaTime;
        while(animationTickTime > animationTick){
            animationTickTime -= animationTick;

            animation.nextFrame();
        }
    }

    public void setScreen(int screenX, int screenY){
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public void move(int distX, int distY){
        screenX += distX;
        screenY += distY;
    }
}
