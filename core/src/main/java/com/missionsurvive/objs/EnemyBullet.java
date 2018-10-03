package com.missionsurvive.objs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.utils.Assets;

/**
 * Created by kuzmin on 01.05.18.
 */

public class EnemyBullet implements GameObject{

    public static final int DIRECTION_NONE = 0, DIRECTION_DOWN_RIGHT = 1, DIRECTION_RIGHT = 2,
            DIRECTION_UP_RIGHT = 3, DIRECTION_UP = 4, DIRECTION_UP_LEFT = 5, DIRECTION_LEFT = 6,
            DIRECTION_DOWN_LEFT = 7, DIRECTION_DOWN = 8;

    public Texture texture;

    private int screenX = 0, screenY = 0, worldX = 0, worldY = 0;
    private int absoluteValueX , absoluteValueY; //these are absolute values determining the place of drawing frame inside asset, because size of bullets for different guns are different.
    private int width;
    private int height;
    private int speed;
    private int direction;
    private float movingTickTime = 0;
    private float movingTick =  0.03f;
    private int screenWidth, screenHeight;
    private float animationTickTime = 0, animationTick = 0.08f;
    private int burstFirstFrame = 6, burstSecondFrame = 24;
    private int burstDrawingWidth = 16, burstDrawingHeight = 16;
    private int drawingWidth, drawingHeight;
    private int frameCount = 0;
    private boolean isDrawing = false;
    private boolean isBurning = false;

    public EnemyBullet(String assetName, int screenWidth, int screenHeight){
        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }
        width = 4;
        height = 4;
        absoluteValueX = 0;
        absoluteValueY = 0;
        speed = 3;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }


    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {
        if(isDrawing){
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
    public void update(float deltaTime) {

    }

    @Override
    public Decorator getDecorator() {
        return null;
    }

    public void moving(float deltaTime, int worldOffsetX, int worldOffsetY,
                       PlatformerScenario platformerScenario){

        movingTickTime += deltaTime;

        while(movingTickTime > movingTick) {
            movingTickTime -= movingTick;

            setXY();
            setScreenXY(worldOffsetX, worldOffsetY);
            goAway(width, height, worldOffsetX, worldOffsetY);
            if(direction != 0){ //if our bullet is going somewhere, we  check it for intersections with the objects.
                platformerScenario.shotPlayer(this);
            }
        }
        if(isBurning){
            burst(deltaTime);
        }
    }

    /**
     * Depending on direction changes the x and y coords.
     */
    public void setXY(){
        switch(direction){
            case DIRECTION_NONE: break;  //goes no where.
            case DIRECTION_RIGHT: worldX += speed;
                break;
            case DIRECTION_UP_RIGHT: worldX += speed;
                worldY -= speed;
                break;
            case DIRECTION_UP: worldY -= speed;
                break;
            case DIRECTION_UP_LEFT: worldX -= speed;
                worldY -= speed;
                break;
            case DIRECTION_LEFT: worldX -= speed;
                break;
            case DIRECTION_DOWN_LEFT: worldX -= speed;
                worldY += speed;
                break;
            case DIRECTION_DOWN: worldY += speed;
                break;
            case DIRECTION_DOWN_RIGHT: worldX += speed;
                worldY += speed;
                break;
            default: break;
        }
    }

    /**
     * The burst of a bullet when it gets an aim.
     * @param deltaTime
     */
    public void burst(float deltaTime){
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
                isDrawing = false;
                isBurning = false;
                frameCount = 0;
            }
        }
    }


    /**
     * When bullet gets an aim. It receives a direction to move (usually 0 - NONE).
     * @param direction
     */
    public void gotIt(int direction){
        this.direction = direction;
        isBurning = true;
        drawingWidth = burstDrawingWidth;
        drawingHeight = burstDrawingHeight;
    }


    /**
     * If bullet doesn't overlap the screen (it goes off the screen), so this bullet is free to shoot again.
     * World coordinates are comparing here: bullets worldX and Y, and screens worldX and Y.
     * @param width
     * @param height
     * @param worldOffsetX
     * @param worldOffsetY
     */
    public void goAway(int width, int height, int worldOffsetX, int worldOffsetY){
        if(direction > 0){
            if(!GeoHelper.overlapRectangles(worldX, worldY, width, height,
                    0 + worldOffsetX,  //left side of the screen
                    0 + worldOffsetY,  //top
                    worldOffsetX + screenWidth,  //right
                    worldOffsetY + screenHeight)){ //down
                direction = 0;
                isDrawing = false;
            }
        }
    }


    /**
     * The method receives world x and y coords (the position bullet is launched from)
     * and also the direction to move.
     * @param x
     * @param y
     * @param direction
     * @return
     */
    public boolean shoot(int x, int y,
                         int worldOffsetX, int worldOffsetY, int direction){
        if(this.direction == 0){
            isDrawing = true;
            worldX = x;
            worldY = y;
            setScreenXY(worldOffsetX, worldOffsetY);
            this.direction = direction;
            setDrawingParams();
            return true;
        }
        return false;
    }

    /**
     * This method sets drawing specific params. For Rocket, for example, this method is useless,
     * because its drawing algorithm differs, but it is called inside boolean shoot(int x, int y, int direction)
     * method (Rocket doesn't have its own implementation).
     */
    public void setDrawingParams(){
        drawingWidth = 4;
        drawingHeight = 4;
        absoluteValueX = 0;
    }


    /**
     * This method translates world coordinates into screen coordinates. Because our hero has screen
     * coordinates and also when we check if bullet goes off the screen (again screen coords).
     * @param worldOffsetX
     * @param worldOffsetY
     */
    public void setScreenXY(int worldOffsetX, int worldOffsetY){
        screenX = worldX - worldOffsetX;
        screenY = worldY - worldOffsetY;
    }

    public int getDirection(){
        return direction;
    }

    public int getScreenX(){
        return screenX;
    }

    public int getScreenY(){
        return screenY;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public boolean isDrawing(){
        return isDrawing;
    }

    /** The cases for bullet moving direction:
     *
     * [4]  [3]  [2]
     * [5]  [0]  [1]
     * [6]  [7]  [8]
     *
     * case 0 means that bullet goes nowhere.
     * case 1: to the right.
     * case 2: up-right...
     * and so on.
     */
}
