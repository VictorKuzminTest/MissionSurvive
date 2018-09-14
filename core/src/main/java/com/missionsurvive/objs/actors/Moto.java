package com.missionsurvive.objs.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.framework.impl.ObjAnimation;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.geom.Hitbox;
import com.missionsurvive.objs.GameObject;
import com.missionsurvive.objs.Obstacle;
import com.missionsurvive.scenarios.PlayScript;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.ScrollerScenario;
import com.missionsurvive.utils.Assets;

/**
 * Created by kuzmin on 03.05.18.
 */

public class Moto implements GameObject{

    public static final int RIGHT = 0, DOWN = 1, LEFT = 2, TOP = 3;
    public static final int STRAIGHT_SPRITES = 0, GODOWN_SPRITES = 1, GOUP_SPRITES = 2,
            JUMPING_START_SPRITES = 3, JUMPING_PEAK_SPRITES = 4, SMASHING_SPRITES = 5;

    public static final int FALLING_ACTION = 0, RUNNING_ACTION = 1, JUMPING_ACTION = 2,
            SMASHING_ACTION = 3;
    public static final int JUMPING_HEIGHT= 77;

    public static final int MOVING_STRAIGHT = 0, MOVING_TILT = 1, JUMPING_START = 2, SMASHED = 3;

    private int screenX, screenY, limitTop = 120, limitBottom = 300, screenWidth;
    private int hitOffsX;
    private int touchX, touchY;
    private int spriteWidth, spriteHeight;
    private int spritesetSpriteWidth, spritesetSpriteHeight;
    private ObjAnimation animation;
    private Hitbox hitbox;
    private boolean isTouched;
    private int isAction = RUNNING_ACTION;
    private float runningTickTime = 0, runningTick =  0.03f;
    private float jumpingTickTime = 0, jumpingTick = 0.03f;
    private float fallingTickTime = 0, fallingTick = 0.03f;
    private float runningAnimationTickTime = 0, runningAnimationTick = 0.08f;
    private float touchTick = 0.03f, touchTickTime = touchTick;
    private int startJumpingPos, jumpingHeight, startJumping;
    private boolean isJumpingUp, isSmashed, isDead, isFalling;
    private Obstacle obstacle;
    private int movingSpeed = 9;
    private int maneuver;
    private int direction;
    private FlyingHero flyingHero;
    private Texture texture;
    private PlayScript playScript;

    private int[] spritesRows; //each element of this array contains the number of sprites in a row.
    private int numDirections;
    private int numActions;
    private ScrollerScenario scenario;

    public Moto(String assetName, Scenario scenario, PlayScript playScript,
                int screenX, int screenY){
        this.playScript = playScript;
        this.screenX = screenX;
        this.screenY = screenY;
        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }
        spriteWidth = 110;
        spriteHeight = 90;
        spritesetSpriteWidth = spriteWidth + 2;
        spritesetSpriteHeight = spriteHeight + 2;

        //hitbox = new Hitbox(screenX, screenY, 75, 40, 15, 30);
        hitbox = new Hitbox(screenX, screenY, 75, 30, 15, 40);
        if(scenario instanceof ScrollerScenario){
            this.scenario = ((ScrollerScenario)scenario);
        }

        numDirections = 1;
        numActions = 6;
        spritesRows = new int[numDirections * numActions];
        for(int whichAction = 0; whichAction < spritesRows.length; whichAction++){
            if(whichAction == STRAIGHT_SPRITES) spritesRows[whichAction] = 3;
            else if(whichAction == GODOWN_SPRITES) spritesRows[whichAction] = 3;
            else if(whichAction == GOUP_SPRITES) spritesRows[whichAction] = 3;
            else if(whichAction == JUMPING_START_SPRITES) spritesRows[whichAction] = 3;
            else if(whichAction == JUMPING_PEAK_SPRITES) spritesRows[whichAction] = 3;
            else if(whichAction == SMASHING_SPRITES) spritesRows[whichAction] = 1;
        }
        animation = new ObjAnimation(spritesRows, spriteWidth, spriteHeight);

        flyingHero = new FlyingHero("motorcycle", this.screenX, this.screenY + 20);

        screenWidth = MSGame.SCREEN_WIDTH;
    }


    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {
        if(isSmashed){
            flyingHero.drawObject(batch, col, row, offsetX, offsetY);
        }

        batch.begin();
        batch.draw(texture, MSGame.SCREEN_OFFSET_X + screenX,
                MSGame.SCREEN_OFFSET_Y +
                        GeoHelper.transformCanvasYCoordToGL(screenY, MSGame.SCREEN_HEIGHT, spriteHeight),
                1 + animation.getCurrentFrame() * spritesetSpriteWidth,
                1 + animation.getSetOfFrames() * spritesetSpriteHeight,
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

    public void moving(float deltaTime){
        switch (isAction){
            case RUNNING_ACTION: move(deltaTime);
                break;
            case JUMPING_ACTION: jumping(deltaTime, 3);
                break;
            case FALLING_ACTION: falling(deltaTime);
                break;
            case SMASHING_ACTION: smashing();
                break;
        }

        if(isSmashed){
            if(isDead) return;
            flyingHero.animate(deltaTime);
        }

        animate(deltaTime);
    }

    public void animate(float deltaTime){
        animateManeuvers(deltaTime);
    }


    /**
     * Moto is moving toward the assigned touch. Also its position is set while moving.
     * If target point Y of moving is closer than moving speed, than we reset maneuver
     * to MOVING_BACK_STRAIGHT.
     * @param deltaTime
     */
    public void move(float deltaTime){

        runningTickTime += deltaTime;

        while(runningTickTime > runningTick) {
            runningTickTime -= runningTick;

            if(isTouched){
                if(touchX > hitbox.getCenterX()) screenX += calcDestX(movingSpeed, RIGHT);
                else  if(touchX < hitbox.getCenterX()) screenX -= calcDestX(movingSpeed, LEFT);

                if(touchY > hitbox.getCenterY()){
                    int deltaDistY = calcDestY(movingSpeed, DOWN);
                    backStraight(deltaDistY);
                    screenY += deltaDistY;
                }
                else  if(touchY < hitbox.getCenterY()){
                    int deltaDistY = calcDestY(movingSpeed, TOP);
                    backStraight(deltaDistY);
                    screenY -= deltaDistY;
                }
            }
            else {
                maneuver = MOVING_STRAIGHT;
                animation.setSetOfFrames(STRAIGHT_SPRITES);
            }
            setPos();
        }
    }


    public void backStraight(int deltaDistY){
        if(deltaDistY < movingSpeed){
            direction = RIGHT;
            maneuver = MOVING_STRAIGHT;
            animation.setSetOfFrames(STRAIGHT_SPRITES);
        }
    }


    public void animateManeuvers(float deltaTime){
        switch(maneuver){
            case MOVING_STRAIGHT:
                runningAnimationTickTime += deltaTime;
                animateRunning(0, 3, 1);
                break;
            case MOVING_TILT:
                runningAnimationTickTime += deltaTime;
                animateRunning(0, 3, 1);
                break;
            case JUMPING_START:
                runningAnimationTickTime += deltaTime;
                animateRunning(0, 3, 1);
                break;
            case SMASHED:
                animation.setCurrentFrame(0);
                break;
        }
    }


    public void animateRunning(int startFrame, int numFrames, int step){
        while(runningAnimationTickTime > runningAnimationTick){
            runningAnimationTickTime -= runningAnimationTick;

            animation.animateBackAndForth(startFrame, numFrames, step);
        }
    }


    /**
     * Assign falling action.
     */
    public void fall(){
        if(isAction != SMASHING_ACTION){
            isAction = FALLING_ACTION;
            runningTickTime = 0;
            isFalling = true;
            decreaseLives();
        }
    }

    public void falling(float deltaTime){
        fallingTickTime += deltaTime;

        while(fallingTickTime > fallingTick) {
            fallingTickTime -= fallingTick;

            screenY += 5;

            if (screenY > screenWidth){
                isDead = true;
            }
        }
    }

    /**
     * Assign jumping action. Jumping start position and height is assigned.
     */
    public void jump() {
        if(isAction != FALLING_ACTION && isAction != SMASHING_ACTION){
            jumpingTickTime = 0;
            isAction = JUMPING_ACTION;
            startJumpingPos = screenY;
            jumpingHeight = screenY - JUMPING_HEIGHT;
            isJumpingUp = true;
            animation.setSetOfFrames(JUMPING_START_SPRITES);
            startJumping = 0;
        }
    }


    public void jumping(float deltaTime, int jumpingSpeed){
        jumpingTickTime += deltaTime;
        while(jumpingTickTime > jumpingTick) {
            jumpingTickTime -= jumpingTick;

            startJumping ++;

            if(isJumpingUp){
                if(screenY > jumpingHeight){ //go up
                    screenY -= jumpingSpeed;
                    setPos();
                }
                else{
                    isJumpingUp = false;
                }

                if(startJumping > 7){
                    animation.setSetOfFrames(JUMPING_PEAK_SPRITES);
                }
            }
            else{ //go down
                if(screenY < startJumpingPos){
                    screenY += jumpingSpeed;
                    setPos();
                }
                else{
                    isAction = RUNNING_ACTION;
                    setPos();
                    runningTickTime = 0;
                }
            }
        }
    }

    public void setPos(){
        hitbox.setPos(screenX, screenY);
        scenario.collideObject();
    }


    /**
     * Sets screen destination to go. It also resets maneuver if needed. We trace touchings over
     * time ticks , so it could work similar on all devices.
     * @param touchX
     * @param touchY
     */
    public void setDestination(float deltaTime, int touchX, int touchY){
        if(isAction == RUNNING_ACTION){

            touchTickTime += deltaTime;
            while(touchTickTime > touchTick){
                touchTickTime -= touchTick;

                isTouched = true;
                startManeuver(touchY);
                this.touchX = touchX;
                this.touchY = touchY;
                checkBorder();
            }
        }
    }


    /**
     * Here we check for the touch event coordinate (Y), set maneuver to MOVING_TILT_START and
     * reset direction if needed.
     * @param touchY
     */
    public void startManeuver(int touchY){
        if(direction == LEFT || direction == RIGHT){
            setDownDirection(touchY, 5);
            setTopDirection(touchY, 5);
            return;
        }
        if(direction == DOWN){
            setTopDirection(touchY, 5);

        }
        else if(direction == TOP){
            setDownDirection(touchY, 5);
        }
    }

    /**
     * Sets TOP direction and maneuver based on current touch coordinate and touch coordinate already assigned.
     * @param touchY
     */
    public void setTopDirection(int touchY, int betweenTouchDist){
        if(touchY < this.touchY){
            if((this.touchY - touchY) > betweenTouchDist){  //between touch distance when moto has to go tilt.
                direction = TOP;
                maneuver = MOVING_TILT;
                animation.setSetOfFrames(GOUP_SPRITES);
            }
        }
    }

    /**
     * Sets DOWN direction and maneuver based on current touch coordinate and touch coordinate already assigned.
     * @param touchY
     */
    public void setDownDirection(int touchY, int betweenTouchDist){
        if (touchY > this.touchY){
            if((touchY - this.touchY) > betweenTouchDist){ //between touch distance when moto has to go tilt.
                direction = DOWN;
                maneuver = MOVING_TILT;
                animation.setSetOfFrames(GODOWN_SPRITES);
            }
        }
    }

    /**
     * This method is used when users take off the screen his finger.
     * touchTickTime is equal back 0.03f, because moto has to react immediately when user touches screen again.
     */
    public void stop(){
        isTouched = false;
        touchTickTime = touchTick;
    }

    public int calcDestX(int speedInPixels, int direction){
        switch (direction) {
            case RIGHT:
                if ((hitbox.getCenterX() + speedInPixels) >= touchX)
                    speedInPixels = touchX - hitbox.getCenterX();
                break;
            case LEFT:
                if ((hitbox.getCenterX() - speedInPixels) <= touchX)
                    speedInPixels = hitbox.getCenterX() - touchX;
                break;
        }
        return speedInPixels;
    }


    public int calcDestY(int speedInPixels, int direction){
        switch (direction) {
            case DOWN:
                if ((hitbox.getCenterY() + speedInPixels) >= touchY)
                    speedInPixels = touchY - hitbox.getCenterY();
                break;
            case TOP:
                if ((hitbox.getCenterY() - speedInPixels) <= touchY)
                    speedInPixels = hitbox.getCenterY() - touchY;
                break;
        }
        return speedInPixels;
    }

    /**
     * We assign an obstacle for our moto could move back with the speed of this obstacle.
     * We also assign hitOffsetX for we could trace the position of the moto if its already
     * hit an obstacle (blockage).
     * @param obstacle
     */
    public void smash(Obstacle obstacle){
        isAction = SMASHING_ACTION;
        this.obstacle = obstacle;
        animation.setSetOfFrames(SMASHING_SPRITES);
        maneuver = SMASHED;
        animation.setCurrentFrame(0);
        isSmashed = true;
        flyingHero.setScreen(screenX, screenY - 20);
        hitOffsX = obstacle.getHitbox().getRight() - screenX;
        decreaseLives();
    }


    /**
     * Checks whether touchY is upper than limit to go up and reset touchY if needed.
     */
    public void checkBorder(){
        if(touchY < limitTop){
            touchY = limitTop;
        }
        if(touchY > limitBottom){
            touchY = limitBottom;
        }
    }

    /**
     * When we smashed into the blockage we just trace the obstacles right side to move moto synchronously.
     */
    public void smashing(){
        screenX = obstacle.getHitbox().getRight() - hitOffsX;
        flyingHero.move(1, -1);

        if((screenX + spriteWidth) < 0) {
            isDead = true;
        }
    }

    public void decreaseLives(){
        playScript.subtractLife();
    }

    public int getLives(){
        return playScript.getLives();
    }

    public void setScreenX(int screenX){
        this.screenX = screenX;
    }

    public void setScreenY(int screenY){
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

    public boolean isDead(){
        return isDead;
    }

    public void setDead(boolean isDead){
        this.isDead = isDead;
        this.isFalling = isDead;
        this.isSmashed = isDead;
        this.isJumpingUp = isDead;
        isAction = RUNNING_ACTION;
        maneuver = MOVING_STRAIGHT;
    }
}
