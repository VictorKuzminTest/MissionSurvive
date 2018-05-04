package com.missionsurvive.objs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.utils.Sounds;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.geom.Hitbox;

/**
 * Created by kuzmin on 01.05.18.
 */

public class Obstacle implements GameObject{

    public static final int SPRINGBOARD = 0, TEAR = 1, BLOCKAGE = 2, LAND = 3, ROCKET = 4;

    private Hitbox hitbox;
    private boolean isPlaced;
    private int screenX, screenY;
    private float movingTickTime = 0, movingTick =  0.005f;
    private int spriteWidth, spriteHeight;
    private int obstacleId;
    private boolean isExploaded;

    public Obstacle(Hitbox hitbox, String assetName, int screenX, int screenY,
                    int spriteWidth, int spriteHeight){
        this.screenX = screenX;
        this.screenY = screenY;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;

        this.hitbox = hitbox;
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {

    }

    @Override
    public void drawObject(SpriteBatch batch, int screenX, int screenY) {

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

    public void moving(){
        move();
        hitbox.setPos(screenX, screenY);
        isOffTheScreen();
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
        if(GeoHelper.inBoundsSpaceX(screenX + 2 * spriteWidth,
                0, MSGame.SCREEN_WIDTH) == GeoHelper.TO_THE_LEFT){
            isPlaced = false;
        }
    }


    public void placeObstacle(int screenX, int screenY){
        isPlaced = true;
        this.screenX = screenX;
        this.screenY = screenY;

        if(obstacleId == BLOCKAGE) Sounds.carwhish.play(0.65f);
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

    public boolean isExploaded(){
        return isExploaded;
    }

    public void setObstacleId(int obstacleId){
        this.obstacleId = obstacleId;
    }

    public int getSpriteWidth(){
        return spriteWidth;
    }

    public int getSpriteHeight(){
        return spriteHeight;
    }

    /**
     * Which obstacle is this.
     * @return
     */
    public int getObstacleId(){
        return obstacleId;
    }

    public void setAssetX(int assetX){}

    public void setAssetY(int assetY){}
}
