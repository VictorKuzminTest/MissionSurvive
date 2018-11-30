package com.missionsurvive.objs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.geom.Hitbox;
import com.missionsurvive.utils.Assets;

public class RightSide extends Obstacle {

    private Hitbox hitbox;
    private Texture texture;

    private boolean isPlaced;
    private int screenX, screenY;
    private float movingTickTime = 0, movingTick =  0.005f;
    private int screenWidth, screenHeight;
    private int spriteWidth, spriteHeight;
    private int spritesetSpriteWidth, spritesetSpriteHeight;
    private int rightSideY;

    public RightSide(Hitbox hitbox, String assetName, int screenX, int screenY,
                     int spriteWidth, int spriteHeight, int rightSideY){
        super(hitbox, assetName, screenX, screenY, spriteWidth, spriteHeight);
        super.setObstacleId(LAND);

        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }
        screenWidth = MSGame.SCREEN_WIDTH;
        screenHeight = MSGame.SCREEN_HEIGHT;

        this.screenX = screenX;
        this.screenY = screenY;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        spritesetSpriteWidth = spriteWidth + 2;
        spritesetSpriteHeight = spriteHeight + 2;

        this.rightSideY = rightSideY;
        this.hitbox = hitbox;
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {

        batch.begin();
        batch.draw(texture, MSGame.SCREEN_OFFSET_X + screenX,
                MSGame.SCREEN_OFFSET_Y +
                        GeoHelper.transformCanvasYCoordToGL(screenY, MSGame.SCREEN_HEIGHT, spriteHeight),
                1 + 1 * spritesetSpriteWidth,
                rightSideY,
                spriteWidth, spriteHeight);
        batch.end();
    }


    @Override
    public boolean onTouch() {
        return false;
    }

    /**
     * movingTick has got to be same as auto scrolling in MapEditor class.
     * @param deltaTime
     */
    public void moving(float deltaTime){
        movingTickTime += deltaTime;

        while(movingTickTime > movingTick) {
            movingTickTime -= movingTick;

            move();
            hitbox.setPos(screenX, screenY);
            isOffTheScreen();
        }
    }

    public void move(){
        if(isPlaced){
            screenX -= 3;
        }
    }

    /**
     * Check if obstacle goes off the screen.
     */
    public void isOffTheScreen(){
        if(GeoHelper.inBoundsSpaceX(screenX + spriteWidth, 0, screenWidth) == 1){
            isPlaced = false;
        }
    }

    public void placeObstacle(int screenX, int screenY){
        isPlaced = true;
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public int getScreenX(){
        return screenX;
    }

    public int getScreenY(){
        return screenY;
    }


    public Hitbox getHitbox(){
        return hitbox;
    }

    public boolean isPlaced(){
        return isPlaced;
    }
}
