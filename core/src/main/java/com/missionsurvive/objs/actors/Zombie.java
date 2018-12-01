package com.missionsurvive.objs.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.framework.impl.ObjAnimation;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.Bot;
import com.missionsurvive.objs.Weapon;
import com.missionsurvive.scenarios.EnemyScenario;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.SpawnBot;
import com.missionsurvive.utils.Assets;

import java.util.Random;

public class Zombie implements Bot {

    public static final int EAST = 0;
    public static final int WEST = 1;
    public static final float ALPHA_STEP = 0.04f;
    public static final float ALPHA_INIT = 1.0f;

    private ObjAnimation animation;
    private MapEditor mapEditor;
    private EnemyScenario enemyScenario;
    private PlatformerScenario platformerScenario;
    private Texture texture;
    private Random random = new Random();

    private int x;
    private int y;
    private int spritesetSpriteWidth;
    private int spritesetSpriteHeight;
    private int spriteWidth;
    private int spriteHeight;
    private int hitboxWidth;
    private int hitboxHeight;
    private int halfHeroHeight;
    private int halfHeroWidth;
    private int centerX, centerY, top, bottom, left, right;
    private int vectorX, vectorY;

    private float animationTickTime = 0;
    private float movingTickTime = 0;
    private static float animationTick = 0.08f;
    private static float movingTick =  0.03f;
    private float bitingTickTime = 0, bitingTick = 0.6f;

    private final int IDLE_ACTION = 0;
    private final int RUN_ACTION = 1;
    private final int JUMP_ACTION = 2;
    private final int FALLING_ACTION = 3;
    private final int DYING = 4;
    private final int BUILDING_FALLING_ACTION = 5;
    private final int BITING_ACTION = 6;

    private int[] actions;
    private int numDirections;
    private int numActions;
    private int currentAction;
    private int direction;
    private int runningSpeed;
    private int fallingSpeed = 3;
    private int hits;

    private int startFallingFrame = 0, numFallingFrames = 1;

    private float zombieDyingTick = 0.5f, zombieDyingTickTime = 0;
    private float alpha = ALPHA_INIT;

    private boolean isNorth, isEast, isSouth, isWest;
    private boolean isRunning = false;
    private boolean isDead = false;

    private MapTer northTer, eastTer, southTer, westTer;

    //this variable determines the action zombie is using.
    private int isAction;
    private int targetX = -1, targetY = -1;

    public Zombie(String assetName, MapEditor mapEditor,
                  int x, int y, int direction, int speedRunning) {
        this.x = x;
        this.y = y;
        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }

        setPos();
        this.direction = direction;
        enemyScenario = new EnemyScenario(mapEditor);
        enemyScenario.setDirection(centerX, centerY, 16, direction);
        targetX = enemyScenario.getTargetX();
        setDirection(targetX);

        numDirections = 2;
        numActions = 7;
        actions = new int[numDirections * numActions];

        animation = new ObjAnimation(actions, spriteWidth, spriteHeight);
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {
        batch.begin();
        if(alpha < ALPHA_INIT){
            Color color = batch.getColor();
            batch.setColor(color.r, color.g, color.b, alpha);
            drawTexture(batch);
            batch.setColor(color.r, color.g, color.b, 1.0f);
        }
        else{
            drawTexture(batch);
        }
        batch.end();
    }

    public void drawTexture(SpriteBatch batch){
        batch.draw(texture, MSGame.SCREEN_OFFSET_X + x - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                MSGame.SCREEN_OFFSET_Y +
                        GeoHelper.transformCanvasYCoordToGL(y - mapEditor.getScrollLevel1Map().getWorldOffsetY(),
                                MSGame.SCREEN_HEIGHT, spriteHeight),
                1 + animation.getCurrentFrame() * spritesetSpriteWidth,
                1 + animation.getSetOfFrames() * spritesetSpriteHeight,
                spriteWidth, spriteHeight);
    }

    @Override
    public void drawObject(SpriteBatch batch, int screenX, int screenY){

    }

    @Override
    public boolean onTouch() {
        if(isAction < 6){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void moving(float deltaTime, MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){
        if(isAction == 7){  //dead
            setOpacity(deltaTime);
            return;
        }

        move(mapTer, mapEditor, worldWidth, worldHeight, deltaTime);
        animate(mapTer, mapEditor, worldWidth, worldHeight, deltaTime);
    }

    public void animate(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight, float deltaTime){
        switch (isAction){
            case 0: animationTickTime += deltaTime;
                idlingAnimation();
                break;
            case 1: animationTickTime += deltaTime;
                fallingAnimation();
                break;
            case 2: animationTickTime += deltaTime;
                jumping(mapTer, mapEditor, worldWidth, worldHeight);
                break;
            case 3: animationTickTime += deltaTime;
                runningAnimation();
                break;
            case 4: animationTickTime += deltaTime;
                bitingTickTime += deltaTime;
                biting();
                break;
            case 6: zombieDyingTickTime += deltaTime;
                dying();
                break;
            default: break;
        }
    }

    public void move(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight, float deltaTime){
        movingTickTime += deltaTime;

        while(movingTickTime > movingTick) {
            movingTickTime -= movingTick;

            setPos();
            tilemapCollision(mapTer, mapEditor, worldWidth, worldHeight);
            run();
            fall();

            switch(isAction){
                case 0: idling();
                    break;
                case 1: falling(mapTer, mapEditor, worldWidth, worldHeight);
                    break;
                case 3:running(mapTer, mapEditor, worldWidth, worldHeight);
                    break;
                default: break;
            }
        }
    }

    @Override
    public void collide(Hero hero){
        bite(hero);
    }

    /**
     * If enemy is not jumping or falling or dying, it gets the hero position and sets the direction according to it.
     * Then bites hero.
     * @param hero
     */
    public void bite(Hero hero){
        if(isAction < 5){  //falling (jumping) from building. But we refused this action. So here it means if it's not dying.
            if(!platformerScenario.getHero().equals(null)){
                this.targetX = platformerScenario.getTargetX() + mapEditor.getScrollLevel1Map().getWorldOffsetX();
                this.targetY = platformerScenario.getTargetY() + mapEditor.getScrollLevel1Map().getWorldOffsetY();
                setDirection(targetX);
            }
            if(!isDead){
                hero.die();
                isAction = 4;   //biting
            }
        }
    }

    public void biting(){
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            setActionAnimationFrames(BITING_ACTION);
            animation.nextFrame();
        }

        while(bitingTickTime > bitingTick){
            bitingTickTime = 0;
            isAction = 0;
            targetX = enemyScenario.getTargetX();
            setDirection(targetX);
        }
    }

    public void calculateVectorX(int speedInPixels, MapEditor mapEditor){
        ...
    }

    public void calculateVectorY(int direction, int speedInPixels, MapEditor mapEditor){
        ...
    }

    public void die(){
        if(isAction < 6){  //not dying
            isAction = 6;  //dying
            isDead = true;
        }
    }

    public void dying(){
        while(zombieDyingTickTime > zombieDyingTick){
            zombieDyingTickTime -= zombieDyingTick;

            setActionAnimationFrames(DYING);

            if(animation.getCurrentFrame() == actions[DYING * numDirections] - 1){
                animation.setSetOfFrames(currentAction * numDirections + direction);
                isAction = 7;  //dead;
                return;
            }
            animation.nextFrame();
        }
    }

    public void fall(){
        ...
    }

    public void falling(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){
        ...
    }

    public void fallingAnimation(){
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            animation.animate(startFallingFrame, numFallingFrames);
        }
    }

    @Override
    public void hit(Weapon weapon){
        hits--;
        if(hits < 0){
            die();
        }
        if(isAction < 7 ){  //if zombie is not dead...
            if(isAction == 6){
                if(animation.getCurrentFrame() != 1){ //if zombie didn't actually fell while dying...
                    weapon.hit(true);
                }
            }
        }
    }

    public void idling(){
        if(isRunning){
            isAction = 3;  //running
        }
    }

    public void idlingAnimation(){
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            setActionAnimationFrames(IDLE_ACTION);
            animation.nextFrame();
        }
    }

    @Override
    public void jump(int destX, int destY){
        setDirection(destX);
        currentAction = JUMP_ACTION;
        setSetOfFrames(currentAction * numDirections + direction);
        isAction = 2;
    }

    public void jumping(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            setJumpingXY(animation.getCurrentFrame(), mapTer, mapEditor, worldWidth, worldHeight);
            animation.nextFrame();

            if(animation.getCurrentFrame() == actions[JUMP_ACTION * numDirections] - 1){
                if(!isSouth){
                    currentAction = FALLING_ACTION;

                }
                else{
                    currentAction = IDLE_ACTION;
                }
                animation.nextFrame();
                animation.setSetOfFrames(currentAction * numDirections + direction);
                isAction = 1;
            }
        }
    }

    @Override
    public void run(){
        ...
    }

    public void running(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){
        ...
    }

    public void runningAnimation(){
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            setDirection(targetX);
            setActionAnimationFrames(RUN_ACTION);
            animation.nextFrame();
        }
    }

    public void setActionAnimationFrames(int action){
        if(currentAction != action){
            currentAction = action;
            animation.setSetOfFrames(currentAction * numDirections + direction);
        }
    }

    public void setDirection(int x){
        ...
    }


    public void setOpacity(float deltaTime){
        animationTickTime += deltaTime;

        while(animationTickTime > animationTick){
            animationTickTime -= animationTick;
            alpha -= ALPHA_STEP;
            if(alpha < 0){
                alpha = 0;
                platformerScenario.removeBot(this, SpawnBot.ZOMBIE);
                return;
            }
        }
    }

    public void setPos(){
        ...
    }

    public void setSetOfFrames(int setOfFrames){
        animation.setSetOfFrames(setOfFrames);
    }

    public void stopRunning(int direction){
        isRunning = false;
        isAction = 1;
        enemyScenario.setDirection(centerX, centerY, 16, direction);
        targetX = enemyScenario.getTargetX();
        setDirection(targetX);
    }

    @Override
    public int getX(){
        return x - mapEditor.getScrollLevel1Map().getWorldOffsetX();
    }

    @Override
    public int getY(){
        return y - mapEditor.getScrollLevel1Map().getWorldOffsetY();
    }

    @Override
    public int getSpriteWidth(){
        return spriteWidth;
    }

    @Override
    public int getSpriteHeight(){
        return spriteHeight;
    }

    @Override
    public int getLeft(){
        return left;
    }

    public int getRight(){
        return right;
    }

    @Override
    public int getTop(){
        return top;
    }

    public int getBottom(){
        return bottom;
    }

    @Override
    public int getHitboxWidth(){
        return hitboxWidth;
    }

    @Override
    public int getHitboxHeight(){
        return hitboxHeight;
    }

    public int getCenterX(){
        return centerX;
    }

    public int getCenterY() { return centerY; }

    public int getVectorX(){
        return vectorX;
    }

    public int getVectorY(){
        return vectorY;
    }

    @Override
    public void setScenario(Scenario scenario) {
        platformerScenario = (PlatformerScenario) scenario;
    }

    @Override
    public int isAction() {
        return 0;
    }

    @Override
    public Decorator getDecorator() {
        return null;
    }

    /**
     ____________>             _                            _
     |_ |             idleRight  |  currentAction    right    | numDirections
     |_ |             idleLeft  _|                   left    _|
     |_ |_ |_ |_ |_ > runRight   |  currentAction
     |_ |_ |_ |_ |_ >   ...     _|
     |_ |_ |_ |_ |_ > jumpRight  |  currentAction
     |_ |_ |_ |_ |_ >   ...     _|       _
     |_ |_ |_ >       shootStraightRight  | ...
     |_ |_ |_ >         ...               |
     \/

     formula:
     setOfFrames = (currentAction * numDirections) + Direction

     => idleRight = (0 * 2) + 0;
     jumpLeft = (2 * 2) + 1;

     */
}
