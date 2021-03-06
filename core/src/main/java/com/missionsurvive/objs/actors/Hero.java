package com.missionsurvive.objs.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.framework.Physics;
import com.missionsurvive.framework.Vector;
import com.missionsurvive.framework.impl.BlinkingDecorator;
import com.missionsurvive.framework.impl.HeroPhysics;
import com.missionsurvive.framework.impl.ObjAnimation;
import com.missionsurvive.framework.impl.Vector2;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.geom.Hitbox;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.map.ScrollMap;
import com.missionsurvive.objs.EnemyBullet;
import com.missionsurvive.objs.GameObject;
import com.missionsurvive.objs.Gun;
import com.missionsurvive.objs.Weapon;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.utils.Assets;

import java.util.List;

public class Hero implements GameObject {

    public static final int FROM_POWERUP = 0;
    public static final int FROM_PLAYSCRIPT = 1;

    public static final int DIRECTION_RIGHT = 0;
    public static final int DIRECTION_LEFT = 1;

    public static final int SPRITES_IDLE = 0;
    public static final int SPRITES_RUN = 1;
    public static final int SPRITES_JUMPING = 2;
    public static final int SPRITES_IDLE_SHOOTING = 3;
    public static final int SPRITES_IDLE_BACK = 4;
    public static final int SPRITES_FALLING = 5;
    public static final int SPRITES_DYING = 6;

    public static final int ACTION_IDLE = 0;
    public static final int ACTION_FALLING = 1;
    public static final int ACTION_RUNNING = 2;
    public static final int ACTION_JUMPING = 3;
    public static final int ACTION_SHOOTING = 4;
    public static final int ACTION_DYING = 5;
    public static final int ACTION_DEAD = 6;
    public static final int ACTION_BEYOND_SCREEN = 7;
    public static final int ACTION_IDLE_BACK = 8;

    //correction for shooting relatively y axis:
    public static final int SHOOT_CORRECTION_Y = 13;

    public static final float IRRESISTIBLE_TICK = 3.0f;

    public static final int NUM_JUMPING_ITERATIONS = 5;

    private Physics physics = new HeroPhysics();
    private PlatformerScenario platformerScenario;
    private Hitbox hitbox;
    private ObjAnimation heroAnimation;
    private Weapon weapon;
    private Vector fallingVector;
    private Vector movingVector;
    private Vector runningVector;
    private Vector jumpingVector;
    private MapTer mapterToResurrect;
    private Decorator decorator;
    private Texture texture;

    //screen coordinates
    private int x;
    private int y;
    private int spritesetSpriteWidth;
    private int spritesetSpriteHeight;
    private int spriteWidth;
    private int spriteHeight;
    private int direction;
    private int numDirections;
    private int numActions;
    //The array contains info about hero's actions and number of frames in these actions.
    //Number of arrays element - it is row in a spritesheet (action), value of an element - number of frames:
    private int[] actions;
    private int numBullets;
    //this variable determines the action our hero is using
    private int isAction;
    private int tileSize = 16;
    private int speedRunning = 2;
    private int speedJumpingY = 7;
    private int speedJumpingX = 3;
    private int speedFallingY = 5;
    private int speedFallingX = 0;
    private int numShootingFrames = 4;
    private int startShootingFrame;
    //starting point coordinates for weapon to move. They depend on action shooting frames.
    private int bulletX, bulletY, bulletDirection;
    private int jumpingIterCount;

    private float animationTick = 0.08f;
    private float animationTickTime = 0;
    private float movingTick = 0.03f;
    private float movingTickTime = 0;
    private float shootingTick = 0.3f;
    private float shootingTickTime = shootingTick;
    private float irresistibleTickTime = 0;

    private boolean isRunning;
    private boolean isShooting;
    private boolean isVerticalScroll;
    private boolean isHorizontalScroll;
    private boolean irresistible = true;

    public Hero(String assetName, PlatformerScenario scenario, int x, int y, int direction,
                   boolean horizontal, boolean vertical) {
        setHero(x, y, direction);
        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }

        this.platformerScenario = scenario;
        numBullets = 10;
        setWeapon(new Gun(platformerScenario, Gun.WEAPON_HANDGUN));
        isHorizontalScroll = horizontal;
        isVerticalScroll = vertical;
    }

    /**for testing*/
    public Hero(String assetName, int x, int y, int direction){
        hitbox = new Hitbox(x, y, 10, 48, 20, 17);
        setHero(x, y, direction);
    }

    public void setHero(int x, int y, int direction){
        this.x = x;
        this.y = y;
        spritesetSpriteWidth = 56;
        spriteWidth = 54;
        spritesetSpriteHeight = 72;
        spriteHeight = 70;

        movingVector = new Vector2(0, 5);
        fallingVector = new Vector2(0, 5);
        runningVector = new Vector2(2, 0);
        jumpingVector = new Vector2(speedJumpingX, speedJumpingY);

        this.direction = direction;
        numDirections = 2;
        numActions = 7;
        actions = new int[numDirections * numActions];

        heroAnimation = new ObjAnimation(actions, spriteWidth, spriteHeight);

        decorator = new BlinkingDecorator(this);
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {
        batch.begin();
        batch.draw(texture, MSGame.SCREEN_OFFSET_X + x,
                MSGame.SCREEN_OFFSET_Y +
                        GeoHelper.transformCanvasYCoordToGL(y, MSGame.SCREEN_HEIGHT, spriteHeight),
                1 + heroAnimation.getCurrentFrame() * spritesetSpriteWidth,
                1 + heroAnimation.getSetOfFrames() * spritesetSpriteHeight,
                spriteWidth, spriteHeight);
        batch.end();
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

    public void moving(float deltaTime, MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){
        for(int whichBullet = 0; whichBullet < numBullets; whichBullet++){
            weapon.getWeapon().get(whichBullet).update(deltaTime,
                    mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                    mapEditor.getScrollLevel1Map().getWorldOffsetY());
        }

        if(irresistible){
            updateResistence(deltaTime);
            decorator.update(deltaTime);
        }

        move(mapTer, mapEditor, worldWidth, worldHeight, deltaTime);
        animate(mapTer, mapEditor, worldWidth, worldHeight, deltaTime);
    }

    public void setWeapon(Weapon weapon){
        this.weapon = weapon;
    }

    public void animate(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight, float deltaTime){
        switch (isAction){
            case ACTION_FALLING:
                animationTickTime += deltaTime;
                animateFalling();
                break;
            case ACTION_IDLE:
                animationTickTime += deltaTime;
                animateIdling();
                break;
            case ACTION_RUNNING:
                animationTickTime += deltaTime;
                animateRunning();
                break;
            case ACTION_SHOOTING:
                shootingTickTime += deltaTime;
                animateShooting(mapEditor);
                break;
            case ACTION_DYING:
                animationTickTime += deltaTime;
                animateDying();
                break;
            case ACTION_IDLE_BACK:
                animationTickTime += deltaTime;
                animateIdlingBack();
                break;
        }
    }

    public void move(MapTer[][] map, MapEditor mapEditor,
                     int worldWidth, int worldHeight, float deltaTime) {

        movingTickTime += deltaTime;

        while(movingTickTime > movingTick) {
            movingTickTime -= movingTick;

            fall(map, mapEditor.getScrollLevel1Map(), tileSize);
            if(isAction < ACTION_DYING){
                platformerScenario.collideObject();
            }

            switch(isAction){
                case ACTION_FALLING:
                    falling(map, mapEditor, tileSize);
                    break;
                case ACTION_DEAD:
                    speedFallingX = 0;
                    movingVector.set(fallingVector);
                    falling(map, mapEditor, tileSize);
                    break;
                case ACTION_RUNNING:
                    running(map, mapEditor, tileSize);
                    break;
                case ACTION_JUMPING:
                    jumping(map, mapEditor, tileSize);
                    break;
            }
            hitbox.setPos(x, y);
        }
    }

    public void animateFalling(){
        while(animationTickTime > animationTick){
            animationTickTime -= animationTick;
            heroAnimation.animateBackAndForth(0, 2, 1);
        }
    }

    public void animateIdling(){
        while(animationTickTime > animationTick){
            animationTickTime -= animationTick;
            heroAnimation.nextFrame();
        }
    }

    public void animateIdlingBack(){
        while(animationTickTime > animationTick){
            animationTickTime -= animationTick;
            heroAnimation.animateBackAndForth(0, 4, 1);
        }
    }

    public void animateRunning() {
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            heroAnimation.nextFrame();
        }
    }

    public void animateShooting(MapEditor mapEditor) {
        while(shootingTickTime > shootingTick) {
            shootingTickTime -= shootingTick;

            if(heroAnimation.getCurrentFrame() % 2 == 0){
                int worldOffsetX = mapEditor.getScrollLevel1Map().getWorldOffsetX();
                int worldOffsetY = mapEditor.getScrollLevel1Map().getWorldOffsetY();
                weapon.shoot(bulletX + worldOffsetX,
                        bulletY + worldOffsetY, worldOffsetX, worldOffsetY, bulletDirection);
            }
            if(!isShooting){
                if(checkFrameToStopShooting()){
                    shootingTickTime = 0.2f;
                    setActionAndAnimationFrames(ACTION_IDLE, SPRITES_IDLE, 0);
                }
            }
            heroAnimation.animate(startShootingFrame, numShootingFrames);
        }
    }

    public void calculateMapTerToResurrect(MapTer[][] map, ScrollMap scrollMap, int tileSize){
        int row = GeoHelper.checkRowCol((getBottom() + scrollMap.getWorldOffsetY()) / tileSize,
                map.length);
        int col = GeoHelper.checkRowCol((getCenterX() + scrollMap.getWorldOffsetX()) / tileSize,
                map[0].length);
        mapterToResurrect = map[row][col];
    }


    public void calculateMovingVector(MapTer[][] map, ScrollMap scrollMap, int tileSize, int isAction){
        physics.calculateVector(map, scrollMap, hitbox.getCenterX(), hitbox.getBottom(),
                movingVector, tileSize, isAction);
    }

    public boolean checkFrameToStopShooting() {
        if((heroAnimation.getCurrentFrame() + 1) % 2 == 0){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * hero couldn't go beyond left of the screen:
     */
    public void checkLeftScreen(){
        if((x + movingVector.getX()) <= 0){
            x = 0;
        }
        else{
            x += movingVector.getX();
        }
    }

    public void collideBullet(EnemyBullet bullet){
        //if a hero is not dying or dead:
        ...
    }


    public void fall(MapTer[][] map, ScrollMap scrollMap, int tileSize){
        if(isAction != ACTION_JUMPING){
            //with this "if" we check whether hero is still on the ground (if not
            // we can assign speed falling x),
            //otherwise it could fall into between tiles gap:
            ...
        }
    }

    public void falling(MapTer[][] map, MapEditor mapEditor, int tileSize){
        moveOrScroll(mapEditor);
    }


    public void jump() {
        if(isAction != ACTION_FALLING && isAction != ACTION_JUMPING){
            setActionAndAnimationFrames(ACTION_JUMPING, SPRITES_JUMPING, 0);
        }
    }

    public void jumping(MapTer[][] map, MapEditor mapEditor, int tileSize) {

        int directionX = direction == DIRECTION_RIGHT ? 1 : -1;

        iterateJumping();

        if(heroAnimation.getCurrentFrame() >= 0 && heroAnimation.getCurrentFrame() <= 2){
            jumpingToMovingVector(map, mapEditor, tileSize, directionX, -1);
            moveOrScroll(mapEditor);
        }
        else if(heroAnimation.getCurrentFrame() == 3){
            jumpingToMovingVector(map, mapEditor, tileSize, directionX, 0);
            moveOrScroll(mapEditor);
        }
        else if(heroAnimation.getCurrentFrame() >= 4 && heroAnimation.getCurrentFrame() <= 6){
            jumpingToMovingVector(map, mapEditor, tileSize, directionX, 1);
            moveOrScroll(mapEditor);
        }
        else if(heroAnimation.getCurrentFrame() == 7){
            setActionAndAnimationFrames(ACTION_IDLE, SPRITES_IDLE, 0);
            movingVector.set(0, 0);
            jumpingIterCount = 0;
        }
        hitbox.setPos(x, y);
    }

    /**
     * Counts moving progress while jumping. If it is greater then particular value, animation frame is
     * switched.
     */
    public void iterateJumping(){
        jumpingIterCount++;
        if(jumpingIterCount >= NUM_JUMPING_ITERATIONS){
            jumpingIterCount = 0;
            heroAnimation.nextFrame();
        }
    }

    public void jumpingToMovingVector(MapTer[][] map, MapEditor mapEditor, int tileSize,
                                      int directionX, int directionY){
        int jumpingX = directionX * jumpingVector.getX();
        int jumpingY = directionY * jumpingVector.getY();
        movingVector.set(jumpingX, jumpingY);
        calculateMovingVector(map, mapEditor.getScrollLevel1Map(),
                tileSize, ACTION_JUMPING);

        //when collides tile while going down, sets action to ACTION_FALLING (also reset jumpIterCount):
        if(directionY > 0){
            if(movingVector.getY() < jumpingVector.getY()){
                setActionAndAnimationFrames(ACTION_FALLING, SPRITES_FALLING, 0);
                jumpingIterCount = 0;
            }
        }

        //for hero could move only up, even when collides tile:
        if(directionY < 0){
            if(movingVector.getY() > jumpingY){
                movingVector.setY(jumpingY);
            }
        }
    }

    public void moveOrScroll(MapEditor mapEditor){
        scrollVerticalMap(mapEditor);
        scrollHorizontMap(mapEditor);
    }

    public boolean notJumpingFallingShooting(){
        if(isAction != ACTION_JUMPING && isAction != ACTION_FALLING && isAction != ACTION_SHOOTING){
            return true;
        }
        else{
            return false;
        }
    }

    public void run() {
        isRunning = true;
        if(notJumpingFallingShooting()){

            setDirectionAndActionToRun();
        }
    }

    public void running(MapTer[][] map, MapEditor mapEditor, int tileSize) {
        movingVector.set(runningVector);
        calculateMovingVector(map, mapEditor.getScrollLevel1Map(), tileSize, ACTION_RUNNING);
        calculateMapTerToResurrect(map, mapEditor.getScrollLevel1Map(), tileSize);
        moveOrScroll(mapEditor);
    }

    public void setActionAndAnimationFrames(int action, int spritesRow, int currentFrame){
        if(isAction >= ACTION_DYING){
            //if hero is still dying, go out of the method:
            if(action < ACTION_DEAD){
                return;
            }
        }
        if (isAction != action) {
            isAction = action;
            heroAnimation.setSetOfFrames(spritesRow * numDirections + direction);
            heroAnimation.setCurrentFrame(currentFrame);
        }
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setDirectionAndActionToRun(){
        if(direction == DIRECTION_RIGHT){
            runningVector.set(speedRunning, 0);
        }
        else{
            runningVector.set(-speedRunning, 0);
        }
        setActionAndAnimationFrames(ACTION_RUNNING, SPRITES_RUN, 0);
    }

    /**
     * Scrolling of a tile map depending on hero movements (horizontal to the right).
     * @param mapEditor
     */
    public void scrollHorizontMap(MapEditor mapEditor){
        if(isHorizontalScroll){
            if(direction == DIRECTION_RIGHT){
                if(hitbox.getCenterX() > mapEditor.getScrollLevel1Map().getScreenWidth() - (mapEditor.getScrollLevel1Map().getScreenWidth() / 2)){
                    mapEditor.horizontScroll(0, movingVector.getX());
                }
                else{
                    x += movingVector.getX();
                }
            }
            else{
                checkLeftScreen();
            }
        }
    }

    /**
     * Scrolling of a tile map depending on hero movements (vertical up).
     * @param mapEditor
     */
    public void scrollVerticalMap(MapEditor mapEditor){
        if(isVerticalScroll) {
            if (movingVector.getY() < 0) { //moves up...
                if (hitbox.getCenterY() < mapEditor.getScrollLevel1Map().getScreenHeight() - (mapEditor.getScrollLevel1Map().getScreenHeight() / 2)) {
                    mapEditor.verticalScroll(movingVector.getY());
                } else {
                    y += movingVector.getY();
                }
            } else {
                y += movingVector.getY();
            }
        }
    }

    public void shoot(int left, int top, int right, int bottom, boolean shoot) {
        if(shoot){
            if(notJumpingFallingShooting()){
                setStartShootingFrame(left, top, right, bottom);
                setActionAndAnimationFrames(ACTION_SHOOTING, SPRITES_IDLE_SHOOTING, startShootingFrame);
                isShooting = true;
            }
        }
        else{
            isShooting = false;
        }
    }

    public void stopActions() {
        speedFallingX = 0;
        isRunning = false;
        isShooting = false;
        if(notJumpingFallingShooting()){
            setActionAndAnimationFrames(ACTION_IDLE, SPRITES_IDLE, 0);
        }
    }

    public int getX(){return x;}

    public int getY() {
        return y;
    }

    public Hitbox getHitbox(){
        return hitbox;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public Vector getMovingVector(){
        return movingVector;
    }

    public Vector getFallingVector(){
        return fallingVector;
    }

    public Vector getRunningVector(){
        return runningVector;
    }

    public ObjAnimation getAnimation(){return heroAnimation;}

    public int isAction(){
        return isAction;
    }

    public void setAction(int action) {
        this.isAction = action;
    }

    public int getDirection() {
        return direction;
    }

    public MapTer getPreviosMapTer() {
        return mapterToResurrect;
    }

    public void setPreviousMapTer(MapTer mapterToResurrect){
        this.mapterToResurrect = mapterToResurrect;
    }

    public int getSpriteWidth(){
        return spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    /**
     * The method is used when hero goes beyond the bottom of the screen.
     * @param deltatime
     */
    public void fallingBeyondScreen(float deltatime){
        movingTickTime += deltatime;
        while (movingTickTime > movingTick){
            movingTickTime -= movingTick;
            y += fallingVector.getY();
        }
    }

    public int getCenterX() {
        return hitbox.getCenterX();
    }

    public int getCenterY() {
        return hitbox.getCenterY();
    }

    public int getLeft() {
        return hitbox.getLeft();
    }

    public int getTop() {
        return hitbox.getTop();
    }

    public int getHitboxWidth() {
        return hitbox.getHitboxWidth();
    }

    public int getHitboxHeight() {
        return hitbox.getHitboxHeight();
    }

    public int getBottom() {
        return hitbox.getBottom();
    }

    public void setRunning(boolean isRunning){
        this.isRunning = isRunning;
    }

    public int getSpeedJumpingX(){
        return speedJumpingX;
    }

    public int getSpeedFallingY(){
        return speedFallingY;
    }

    public void setSpeedFallingX(int speedFallingX){
        this.speedFallingX = speedFallingX;
    }

    public void setFallingVector(int x, int y){
        fallingVector.set(x, y);
    }

    public int getSpeedJumpingY(){
        return speedJumpingY;
    }

    public int getSpeedRunning(){
        return speedRunning;
    }

    public void setAnimationTickTime(float deltaTime){
        this.animationTickTime = deltaTime;
    }

    public void setVerticalScroll(boolean isVerticalScroll){
        this.isVerticalScroll = isVerticalScroll;
    }

    public void setHorizontalScroll(boolean isHorizontalScroll){
        this.isHorizontalScroll = isHorizontalScroll;
    }

    public boolean isHorizontalScroll(){
        return isHorizontalScroll;
    }

    public void setMovingVector(int x, int y) {
        movingVector.set(x, y);
    }

    public void setJumpingVector(int x, int y) {
        jumpingVector.set(x, y);
    }



    public void die() {
        setActionAndAnimationFrames(ACTION_DYING, SPRITES_DYING, 0);
    }

    public void animateDying(){
        while(animationTickTime > animationTick){
            animationTickTime -= animationTick;
            heroAnimation.nextFrame();
            if(heroAnimation.getCurrentFrame() == heroAnimation.
                    getActionFrames()[SPRITES_DYING * numDirections + direction] - 1){

                setActionAndAnimationFrames(ACTION_DEAD, SPRITES_DYING,
                        heroAnimation.getActionFrames()[SPRITES_DYING * numDirections + direction] - 1);
            }
        }
    }

    public void addLife(){
        platformerScenario.addLife();
    }

    public void addGun(int from){
        switch (from){
            case FROM_POWERUP:
                weapon.update(0);
                platformerScenario.addGun();
                break;
            case FROM_PLAYSCRIPT:
                weapon.update(0);
                break;
        }
    }

    public void setPlatformerScenario(PlatformerScenario platformerScenario) {
        this.platformerScenario = platformerScenario;
    }

    public boolean isIrresistible() {
        return irresistible;
    }


    public Decorator getDecorator() {
        return decorator;
    }

    public void updateResistence(float deltaTime) {
        irresistibleTickTime += deltaTime;
        while(irresistibleTickTime > IRRESISTIBLE_TICK){
            irresistibleTickTime = 0;
            irresistible = false;
        }
    }

    public List<Weapon> getWeapon(){
        return weapon.getWeapon();
    }
}
