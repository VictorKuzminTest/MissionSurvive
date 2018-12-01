package com.missionsurvive.objs.actors;

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
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.utils.Assets;

public class L1B implements Bot{

    public static final int ACTION_IDLE = 0;
    public static final int ACTION_FALLING = 1;
    public static final int ACTION_RUNNING = 2;
    public static final int ACTION_DYING = 4;
    public static final int ACTION_DEAD = 5;

    private final int SPRITES_IDLE = 0;
    private final int SPRITES_RUN = 1;
    private final int SPRITES_DYING = 2;

    //screen coordinates
    private int x;
    private int y;
    private int spritesetSpriteWidth;
    private int spritesetSpriteHeight;
    private int spriteWidth;
    private int spriteHeight;
    private int hitboxWidth;
    private int hitboxHeight;
    ObjAnimation animation;
    MapEditor mapEditor;
    L1B.ActionScenario actionScenario = new L1B.ActionScenario();
    private PlatformerScenario platformerScenario;
    private Texture texture;

    private int halfHeroHeight;
    private int halfHeroWidth;
    private int centerX, centerY, top, bottom, left, right;
    private int vectorX, vectorY;

    private float animationTickTime = 0;
    private float movingTickTime = 0;
    private float animationTick = 0.08f;
    private float movingTick =  0.03f;
    private float betweenActionsTickTime = 0 , betweenActionsTick = 1.2f;

    //the array contains info of actors events and number of frames in em.
    private int[] actions;
    private int numDirections;
    private int numActions;
    private int currentAction;
    //direction 0 - right, 1 - left
    private int direction;
    private int runningSpeed = 4;
    private int fallingSpeed = 1;
    private int hp;

    private float zombieDyingTick = 0.1f, zombieDyingTickTime = 0;

    private boolean isNorth, isEast, isSouth, isWest;
    private MapTer northTer, eastTer, southTer, westTer;

    //this variable determines the action our hero is using
    private int isAction;
    private boolean isRunning = false;
    private int targetX = -1;

    public L1B(String assetName, MapEditor mapEditor, int x, int y, int hp) {
        this.x = x;
        this.y = y;
        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }
        spriteWidth = 60;
        spriteHeight = 70;
        spritesetSpriteWidth = spriteWidth + 2;
        spritesetSpriteHeight = spriteHeight + 2;
        hitboxWidth = 16;
        hitboxHeight = 60;
        halfHeroHeight = hitboxHeight / 2;
        halfHeroWidth = hitboxWidth / 2;
        this.hp = hp;

        this.mapEditor = mapEditor;

        numDirections = 2;
        numActions = 3;
        actions = new int[numDirections * numActions];

        animation = new ObjAnimation(actions, spriteWidth, spriteHeight);
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY){
        batch.begin();
        batch.draw(texture, MSGame.SCREEN_OFFSET_X + x - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                MSGame.SCREEN_OFFSET_Y +
                        GeoHelper.transformCanvasYCoordToGL(y - mapEditor.getScrollLevel1Map().getWorldOffsetY(),
                                MSGame.SCREEN_HEIGHT, spriteHeight),
                1 + animation.getCurrentFrame() * spritesetSpriteWidth,
                1 + animation.getSetOfFrames() * spritesetSpriteHeight,
                spriteWidth, spriteHeight);
        batch.end();
    }

    @Override
    public void drawObject(SpriteBatch batch, int screenX, int screenY){

    }

    @Override
    public boolean onTouch(){
        if(isAction < ACTION_DYING){
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
        if(isAction == ACTION_DEAD){
            return;
        }

        move(mapTer, mapEditor, worldWidth, worldHeight, deltaTime);
        animate(mapTer, mapEditor, worldWidth, worldHeight, deltaTime);
    }

    public void animate(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight, float deltaTime){
        switch (isAction){
            case ACTION_IDLE:
                animationTickTime += deltaTime;
                idlingAnimation();
                break;
            case ACTION_FALLING:
                animationTickTime += deltaTime;
                idlingAnimation();
                break;
            case ACTION_RUNNING:
                animationTickTime += deltaTime;
                runningAnimation();
                break;
            case ACTION_DYING:
                zombieDyingTickTime += deltaTime;
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
            fall();

            switch(isAction){
                case ACTION_IDLE:
                    betweenActionsTickTime += deltaTime;
                    idling();
                    break;
                case ACTION_FALLING:
                    falling(mapTer, mapEditor, worldWidth, worldHeight);
                    break;
                case ACTION_RUNNING:
                    running(mapTer, mapEditor, worldWidth, worldHeight);
                    break;
                default: break;
            }
        }
    }

    @Override
    public void collide(Hero hero){
        if(isAction < ACTION_DYING){
            hero.die();
        }
    }

    public void calculateVectorX(int direction, int speedInPixels, MapEditor mapEditor){
        ...
    }


    public void calculateVectorY(int direction, int speedInPixels, MapEditor mapEditor){
        ...
    }

    public void die(){
        if(isAction < ACTION_DYING){
            isAction = ACTION_DYING;
        }
    }

    public void dying(){
        while(zombieDyingTickTime > zombieDyingTick){
            zombieDyingTickTime -= zombieDyingTick;

            setActionAnimationFrames(SPRITES_DYING);

            if(animation.getCurrentFrame() == actions[SPRITES_DYING * numDirections] - 1){
                animation.setSetOfFrames(currentAction * numDirections + direction);
                isAction = ACTION_DEAD;  //dead;
                return;
            }
            animation.nextFrame();
        }
    }

    public void fall(){
        if(!isSouth){
            isAction = ACTION_FALLING;
        }
    }

    public void falling(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){
        setActionAnimationFrames(SPRITES_IDLE);

        if(!isSouth){
            calculateVectorY(direction, fallingSpeed, mapEditor);
            y += vectorY;
            isAction = ACTION_FALLING;
        }
        else{
            calculateVectorY(direction, fallingSpeed, mapEditor);
            if(vectorY == 0){  //so we stood on the ground.
                animation.setCurrentFrame(0); //current frame = our hero landed on the ground.
                isAction = ACTION_IDLE;
            }

            isAction = ACTION_IDLE;
        }
    }

    public void hit(Weapon weapon){
        hp -= weapon.getHP();
        if(hp < 0){
            die();
        }
        if(isAction < ACTION_DEAD){  //if an enemy is not dead...
            weapon.hit(true);
        }
    }

    public void idling(){
        while(betweenActionsTickTime > betweenActionsTick){
            betweenActionsTickTime -= betweenActionsTick;

            if(isRunning){
                isAction = ACTION_RUNNING;
            }
            else{
                actionScenario.setAction();
            }
        }
    }

    public void idlingAnimation(){
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            setActionAnimationFrames(SPRITES_IDLE);
            animation.nextFrame();
        }
    }

    @Override
    public void jump(int destX, int destY){

    }

    @Override
    public void run(){
        if(isAction < ACTION_DYING){ //if zombie is not dying.
            if(!platformerScenario.getHero().equals(null)){
                this.targetX = platformerScenario.getTargetX()
                        + mapEditor.getScrollLevel1Map().getWorldOffsetX();
            }
            if(targetX > 0){
                isRunning = true;
            }

            if(isAction != ACTION_FALLING){
                currentAction = SPRITES_RUN;

                setDirection(targetX);
                setSetOfFrames(currentAction * numDirections + direction);
                isAction = ACTION_RUNNING;
            }
        }
    }

    public void running(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){
        ...
    }

    public void runningAnimation(){
        setDirection(targetX);
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            setActionAnimationFrames(SPRITES_RUN);
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
        if((this.x + 31) < x){
            direction = 0;  //right
        }
        if((this.x + 31) > x){
            direction = 1;  //left
        }
    }

    public void setSetOfFrames(int setOfFrames){
        animation.setSetOfFrames(setOfFrames);
    }

    public void stopRunning(){
        isRunning = false;
        isAction = ACTION_FALLING;
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

    public int getLeft(){
        return left;
    }

    public int getRight(){
        return right;
    }

    public int getTop(){
        return top;
    }

    public int getBottom(){
        return bottom;
    }

    public int getHitboxWidth(){
        return hitboxWidth;
    }

    public int getHitboxHeight(){
        return hitboxHeight;
    }

    @Override
    public void setScenario(Scenario scenario) {
        platformerScenario = (PlatformerScenario) scenario;
    }

    @Override
    public int isAction() {
        return isAction;
    }

    @Override
    public Decorator getDecorator() {
        return null;
    }

    public class ActionScenario{

        public void setAction(){
            run();
        }
    }
}
