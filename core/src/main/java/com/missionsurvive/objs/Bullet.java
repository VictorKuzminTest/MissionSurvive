package com.missionsurvive.objs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.utils.Assets;
import com.missionsurvive.utils.Sounds;

import java.util.List;

public class Bullet implements Weapon{

    public static final int ACTION_NONE = 0;
    public static final int ACTION_MOVE = 1;
    public static final int ACTION_WAIT = 2;
    public static final int ACTION_BURST = 3;

    public static final float AUTOMATIC_TICK = 0.05f;

    private PlatformerScenario platformerScenario;
    private Texture texture;

    private int x = 0, y = 0, screenX, screenY;
    //these are absolute values determining the place of drawing frame inside asset,
    //because size of bullets for different guns are different.
    private int absoluteValueX , absoluteValueY;
    private int width;
    private int height;
    private int speed;
    private int direction;
    private float movingTickTime = 0;
    private float movingTick =  0.03f;
    private int screenWidth, screenHeight;
    private float animationTickTime = 0, animationTick = 0.08f;
    private float automaticTickTime = 0;
    private int burstFirstFrame = 6, burstSecondFrame = 24;
    private int burstDrawingWidth = 16, burstDrawingHeight = 16;
    private int drawingWidth, drawingHeight;
    private int frameCount = 0;
    private int action;
    private int hp;

    public Bullet(String assetName, PlatformerScenario platformerScenario){
        this.platformerScenario = platformerScenario;
        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }
        width = 4;
        height = 4;
        absoluteValueX = 0;
        absoluteValueY = 0;
        speed = 8;
        hp = 1;

        screenWidth = 480;
        screenHeight = 320;
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {
        if(action != ACTION_NONE){
            batch.begin();
            batch.draw(texture, MSGame.SCREEN_OFFSET_X + screenX,
                    MSGame.SCREEN_OFFSET_Y +
                            GeoHelper.transformCanvasYCoordToGL(screenY, MSGame.SCREEN_HEIGHT, drawingHeight),
                    1 + absoluteValueX,
                    1 + absoluteValueY,
                    drawingWidth, drawingHeight);
            batch.end();
        }
    }

    @Override
    public void drawObject(SpriteBatch batch, int screenX, int screenY){

    }

    @Override
    public boolean onTouch() {
        return false;
    }

    @Override
    public Decorator getDecorator() {
        return null;
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void update(float deltaTime, int worldOffsetX, int worldOffsetY){
        switch (action){
            case ACTION_NONE:
                break;
            case ACTION_MOVE:
                move(deltaTime, worldOffsetX, worldOffsetY);
                break;
            case ACTION_WAIT:
                wait(deltaTime);
                break;
            case ACTION_BURST:
                burst(deltaTime);
                break;
        }
    }

    public void move(float deltaTime, int worldOffsetX, int worldOffsetY){
        movingTickTime += deltaTime;

        while(movingTickTime > movingTick) {
            movingTickTime -= movingTick;

            switch(direction){
                case 0: break;  //goes nowhere.
                case 1: x += speed;
                    break;
                case 2: x += speed;
                    y -= speed;
                    break;
                case 3: y -= speed;
                    break;
                case 4: x -= speed;
                    y -= speed;
                    break;
                case 5: x -= speed;
                    break;
                case 6: x -= speed;
                    y += speed;
                    break;
                case 7: y += speed;
                    break;
                case 8: x += speed;
                    y += speed;
                    break;
                default: break;
            }
            setScreenXY(worldOffsetX, worldOffsetY);
            goAway();
            if(direction != 0){ //if our bullet is going somewhere, we  check it for intersections with the objects.
                platformerScenario.shotEnemy(this);
            }
        }
    }

    public void burst(float deltaTime){  //the burst of a bullet when it gets an aim.
        animationTickTime += deltaTime;

        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            frameCount++;
            if(frameCount == 1){
                absoluteValueX = burstFirstFrame;
            }
            else if(frameCount == 2){
                absoluteValueX = burstSecondFrame;
            }
            else if(frameCount == 3){
                action = ACTION_NONE;
                animationTickTime = 0;
                movingTickTime = 0;
                direction = 0;
                frameCount = 0;
            }
        }
    }

    @Override
    public boolean hit(boolean isHit) {
        if(isHit){
            direction = 0;
            action = ACTION_BURST;
            drawingWidth = burstDrawingWidth;
            drawingHeight = burstDrawingHeight;
            return true;
        }
        return false;
    }

    public void goAway(){
        if(direction > 0){
            if(!GeoHelper.overlapRectangles(screenX, screenY, width, height, 0, 0, screenWidth, screenHeight)){ //if the bullet goes away from the screen, so this bullet is free to shoot again.
                direction = 0;
                action = ACTION_NONE;
                movingTickTime = 0;
                animationTickTime = 0;
            }
        }
    }

    public void setDirection(int direction){
        this.direction = direction;
    }

    @Override
    public boolean shoot(int x, int y,
                         int worldOffsetX, int worldOffsetY, int direction){
        if(action == ACTION_NONE){
            Sounds.playSound(Sounds.gun);
            this.x = x;
            this.y = y;
            this.direction = direction % 10;
            drawingWidth = 4;
            drawingHeight = 4;
            absoluteValueX = 0;
            setAction(direction);
            setScreenXY(worldOffsetX, worldOffsetY);
            return true;
        }
        return false;
    }

    public void setAction(int direction){
        switch ((direction / 10)){
            case 0:
                action = ACTION_MOVE;
                break;
            case 1:
                action = ACTION_WAIT;
                break;
        }
    }

    public void wait(float deltaTime){
        automaticTickTime += deltaTime;
        while(automaticTickTime > AUTOMATIC_TICK){
            automaticTickTime = 0;

            action = ACTION_MOVE;
        }
    }

    /**
     * This method translates world coordinates into screen coordinates. Because our hero has screen
     * coordinates and also when we check if bullet goes off the screen (again screen coords).
     * @param worldOffsetX
     * @param worldOffsetY
     */
    public void setScreenXY(int worldOffsetX, int worldOffsetY){
        screenX = x - worldOffsetX;
        screenY = y - worldOffsetY;
    }

    public int getDirection(){
        return direction;
    }

    @Override
    public int getX(){
        return screenX;
    }

    @Override
    public int getY(){
        return screenY;
    }

    @Override
    public int getWidth(){
        return width;
    }

    @Override
    public int getHeight(){
        return height;
    }

    @Override
    public List<Weapon> getWeapon() {
        return null;
    }

    @Override
    public int getHP() {
        return hp;
    }

    /** The cases for bullet moving direction:
     *
     * [4]  [3]  [2]
     * [5]  [0]  [1]
     * [6]  [7]  [8]
     *
     * case 0 means that bullet goes nowhere.
     * case 1: to the right.
     * case 2: up-right....
     * and so on.
     */
}
