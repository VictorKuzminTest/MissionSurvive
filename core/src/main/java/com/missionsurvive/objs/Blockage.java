package com.missionsurvive.objs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.geom.Hitbox;
import com.missionsurvive.utils.Assets;

/**
 * Created by kuzmin on 01.05.18.
 */

public class Blockage extends Obstacle{

    private Texture texture;

    private int spriteWidth, spriteHeight;
    private int spritesetSpriteWidth, spritesetSpriteHeight;
    private int whichAsset;
    private int carY;

    public Blockage(Hitbox hitbox, String assetName, int screenX, int screenY,
                    int spriteWidth, int spriteHeight){
        super(hitbox, assetName, screenX, screenY, spriteWidth, spriteHeight);
        super.setObstacleId(BLOCKAGE);

        this.whichAsset = Assets.getWhichTexture(assetName);
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        spritesetSpriteWidth = spriteWidth + 2;
        spritesetSpriteHeight = spriteHeight + 2;

        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {
        batch.begin();
        batch.draw(texture, MSGame.SCREEN_OFFSET_X + super.getScreenX(),
                MSGame.SCREEN_OFFSET_Y +
                        GeoHelper.transformCanvasYCoordToGL(super.getScreenY(), MSGame.SCREEN_HEIGHT, spriteHeight),
                1 + 0 * spritesetSpriteWidth,
                1 + carY * spritesetSpriteHeight,
                spriteWidth, spriteHeight);
        batch.end();
    }

    @Override
    public void drawObject(SpriteBatch batch, int screenX, int screenY){
        batch.begin();
        batch.draw(texture, MSGame.SCREEN_OFFSET_X + super.getScreenX(),
                MSGame.SCREEN_OFFSET_Y +
                        GeoHelper.transformCanvasYCoordToGL(super.getScreenY(), MSGame.SCREEN_HEIGHT, spriteHeight),
                1 + 0 * spritesetSpriteWidth,
                1 + carY * spritesetSpriteHeight,
                spriteWidth, spriteHeight);
        batch.end();
    }

    @Override
    public boolean onTouch() {
        return false;
    }

    @Override
    public void update(float deltaTime) {

    }

    public void setAssetY(int carY){
        this.carY = carY;
    }
}
