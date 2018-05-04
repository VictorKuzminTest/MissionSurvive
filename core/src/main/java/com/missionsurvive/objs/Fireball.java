package com.missionsurvive.objs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.framework.impl.ObjAnimation;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.geom.Hitbox;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.utils.Assets;

import java.util.List;

/**
 * Created by kuzmin on 01.05.18.
 */

public class Fireball implements Weapon{

    public static final int ACTION_NONE = 0;
    public static final int ACTION_MOVE = 1;
    public static final int ACTION_HIT = 2;

    public static final int DIRECTION_NONE = 0;
    public static final int DIRECTION_R = 1;
    public static final int DIRECTION_UR = 2;
    public static final int DIRECTION_U = 3;
    public static final int DIRECTION_UL = 4;
    public static final int DIRECTION_L = 5;
    public static final int DIRECTION_DL = 6;
    public static final int DIRECTION_D = 7;
    public static final int DIRECTION_DR = 8;

    public static final float MOVING_TICK = 0.03f;
    public static final float ANIMATION_TICK =  0.08f;

    public static final int SPREAD_ABOVE = 1;
    public static final int SPREAD_NONE = 0;
    public static final int SPREAD_BELOW = 2;

    public static final int FRAMES_MOVING = 0;
    public static final int FRAMES_HIT = 1;

    private Hitbox hitbox;
    private ObjAnimation animation;
    private PlatformerScenario platformerScenario;
    private Texture texture;

    private int speed;
    private int deltaSpread;
    private int movingPos;
    private int direction;
    private int x, y, screenX, screenY;
    private int spriteWidth;
    private int spriteHeight;
    private int spritesetSpriteWidth;
    private int spritesetSpriteHeight;
    private int screenWidth = 480;
    private int screenHeight = 320;
    private int numMovingFrames = 5, numHitFrames = 5;
    private int action;
    private int whichAsset;

    private float movingTickTime = 0;
    private float animationTickTime = 0;

    public Fireball(String assetName, PlatformerScenario platformerScenario){
        this.platformerScenario = platformerScenario;
        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }
        spriteWidth = 16;
        spriteHeight = 16;
        spritesetSpriteWidth = spriteWidth + 2;
        spritesetSpriteHeight = spriteHeight + 2;

        speed = 8;
        deltaSpread = 4;
        hitbox = new Hitbox(null, x, y, 9, 9, 4, 4);

        int actions[] = new int[2];
        actions[Fireball.FRAMES_MOVING] = 5;
        actions[Fireball.FRAMES_HIT] = 5;
        setAnimation(new ObjAnimation(actions, spriteWidth, spriteHeight));
    }

    @Override
    public void drawObject(SpriteBatch batch, int screenX, int screenY) {

    }

    @Override
    public boolean onTouch() {
        return false;
    }


    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {
        if(action != ACTION_NONE){
            batch.begin();
            batch.draw(texture, MSGame.SCREEN_OFFSET_X + screenX,
                    MSGame.SCREEN_OFFSET_Y +
                            GeoHelper.transformCanvasYCoordToGL(screenY, MSGame.SCREEN_HEIGHT, spriteHeight),
                    1 + animation.getCurrentFrame() * spritesetSpriteWidth,
                    1 + animation.getSetOfFrames() * spritesetSpriteHeight,
                    spriteWidth, spriteHeight);
            batch.end();
        }
    }

    @Override
    public Decorator getDecorator() {
        return null;
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void update(float deltaTime, int worldOffsetX, int worldOffsetY) {
        if(action != ACTION_NONE){
            animate(deltaTime);
            move(deltaTime, worldOffsetX, worldOffsetY);
        }
    }

    public void animate(float deltaTime){
        animationTickTime += deltaTime;

        while(animationTickTime > ANIMATION_TICK){
            animationTickTime -= ANIMATION_TICK;

            switch (action){
                case ACTION_MOVE:
                    animateMoving();
                    break;
                case ACTION_HIT:
                    animateHit();
                    break;
            }
        }
    }

    public void animateMoving() {
        animation.animateOneTime(0, numMovingFrames);
    }

    public void animateHit() {
        if(animation.getCurrentFrame() == (numHitFrames - 1)){
            stopActions();
            return;
        }
        animation.animateOneTime(0, numHitFrames);
    }

    public void move(float deltaTime, int worldOffsetX, int worldOffsetY) {
        movingTickTime += deltaTime;

        while(movingTickTime > MOVING_TICK){
            movingTickTime -= MOVING_TICK;

            switch(direction){
                case DIRECTION_NONE: break;  //goes nowhere.
                case DIRECTION_R:
                    x += speed;
                    spread(true);
                    break;
                case DIRECTION_UR:
                    x += speed;
                    y -= speed;
                    spread(true);
                    break;
                case DIRECTION_U:
                    y -= speed;
                    spread(false);
                    break;
                case DIRECTION_UL:
                    x -= speed;
                    y -= speed;
                    spread(true);
                    break;
                case DIRECTION_L:
                    x -= speed;
                    spread(true);
                    break;
                case DIRECTION_DL:
                    x -= speed;
                    y += speed;
                    spread(true);
                    break;
                case DIRECTION_D:
                    y += speed;
                    spread(false);
                    break;
                case DIRECTION_DR:
                    x += speed;
                    y += speed;
                    spread(true);
                    break;
                default: break;
            }
            setScreenXY(worldOffsetX, worldOffsetY);
            hitbox.setPos(screenX, screenY);
            goAway();
            platformerScenario.shotEnemy(this);
        }
    }


    /**
     * If the fireball goes away from the screen, so this fireball is free to shoot again.
     */
    public void goAway(){
        if(direction > 0){
            if(!GeoHelper.overlapRectangles(screenX, screenY, spriteWidth, spriteHeight,
                    0, 0, screenWidth, screenHeight)){
                stopActions();
            }
        }
    }


    /**
     * This method reflects the situation when object (fireball) deviates from standard direction.
     * It changes the x or y coord depending on Moving "in spread position" and whether it is
     * moving along x axis from the moment of previous step.
     * @param isMovingX
     */
    public void spread(boolean isMovingX){
        if(movingPos != SPREAD_NONE){
            if(movingPos == SPREAD_ABOVE){
                if(isMovingX){
                    y -= deltaSpread;
                }
                else{
                    x -= deltaSpread;
                }
            }
            else if(movingPos == SPREAD_BELOW){
                if(isMovingX){
                    y += deltaSpread;
                }
                else{
                    x += deltaSpread;
                }
            }
        }
    }

    @Override
    public boolean shoot(int x, int y, int direction) {
        if(action == ACTION_NONE){

            setMovingPos(direction);

            action = ACTION_MOVE;
            this.x = x;
            this.y = y;
            this.direction = direction % 10;
            return true;
        }
        return false;
    }

    @Override
    public int getX() {
        return hitbox.getLeft();
    }

    @Override
    public int getY() {
        return hitbox.getTop();
    }

    @Override
    public int getWidth() {
        return hitbox.getHitboxWidth();
    }

    @Override
    public int getHeight() {
        return hitbox.getHitboxHeight();
    }

    @Override
    public boolean hit(boolean isHit) {
        if(isHit){
            direction = DIRECTION_NONE;
            action = ACTION_HIT;
            setSetOfFrames(FRAMES_HIT);
            return true;
        }
        return false;
    }

    public void setSetOfFrames(int setOfFrames){
        if(animation.getSetOfFrames() != setOfFrames){
            animation.setSetOfFrames(setOfFrames);
            animation.setCurrentFrame(0);
        }
    }

    public void stopActions(){
        action = ACTION_NONE;
        animation.setSetOfFrames(FRAMES_MOVING);
        animation.setCurrentFrame(0);
        direction = DIRECTION_NONE;
    }

    public void setHitbox(Hitbox hitbox){
        this.hitbox = hitbox;
    }

    public void setAnimation(ObjAnimation animation) {
        this.animation = animation;
    }

    public ObjAnimation getAnimation() {
        return animation;
    }

    public int getMovingPos() {
        return movingPos;
    }

    public void setMovingPos(int direction) {
        switch ((direction / 10)){
            case SPREAD_NONE:
                movingPos = SPREAD_NONE;
                break;
            case SPREAD_ABOVE:
                movingPos = SPREAD_ABOVE;
                break;
            case SPREAD_BELOW:
                movingPos = SPREAD_BELOW;
                break;
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

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDeltaSpread(int deltaSpread) {
        this.deltaSpread = deltaSpread;
    }

    public int getScreenX(){
        return screenX;
    }

    public int getScreenY(){
        return screenY;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int isAction() {
        return action;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public List<Weapon> getWeapon() {
        return null;
    }

    /** The cases for fireball moving direction:
     *
     * [4]  [3]  [2]
     * [5]  [0]  [1]
     * [6]  [7]  [8]
     *
     * case 0 means that fireball goes nowhere.
     * case 1: to the right.
     * case 2: up-right....
     * and so on.
     */
}
