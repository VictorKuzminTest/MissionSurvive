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
import com.missionsurvive.objs.EnemyBullet;
import com.missionsurvive.objs.Weapon;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.utils.Assets;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class L5B implements Bot {

    public static final int ACTION_DEAD = 5;
    public static final int ACTION_DYING = 4;

    private List<EnemyBullet> bullets = new ArrayList<EnemyBullet>();
    private int numBullets;
    ObjAnimation animation;
    MapEditor mapEditor;
    Random random = new Random();
    ActionScenario actionScenario = new ActionScenario();
    private PlatformerScenario platformerScenario;
    private Texture texture;

    private int bulletX, bulletY, bulletDirection;

    private float animationTickTime = 0;
    private float movingTickTime = 0;
    private static float animationTick = 0.08f;
    private static float movingTick =  0.03f;
    private float betweenActionsTickTime = 0 , betweenActionsTick = 0.5f;

    private final int IDLE_ACTION = 0;
    private final int RUN_ACTION = 1;
    private final int SHOOTING_ACTION = 2;
    private final int DYING = 3;

    private int[] actions;
    private int numDirections;
    private int numActions;
    private int currentAction;
    private int direction;
    private int runningSpeed = 2;
    private int fallingSpeed = 7;
    private int hp;

    private float zombieDyingTick = 0.1f, zombieDyingTickTime = 0;

    private boolean isNorth, isEast, isSouth, isWest;
    private MapTer northTer, eastTer, southTer, westTer;

    //this variable determines the action our hero is using.
    private int isAction;
    private boolean isRunning = false;
    private int targetX = -1, targetY = -1;

    public L5B(String assetName, MapEditor mapEditor, int x, int y, int hp) {
        this.x = x;
        this.y = y;
        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }

        this.hp = hp;

        this.mapEditor = mapEditor;

        numDirections = 2;
        numActions = 4;
        actions = new int[numDirections * numActions];

        animation = new ObjAnimation(actions, spriteWidth, spriteHeight);
        numBullets = 10;
        setBulletHolder(numBullets);
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY){
        for(int whichBullet = 0; whichBullet < numBullets; whichBullet++){
            bullets.get(whichBullet).drawObject(batch, col, row,
                    mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                    mapEditor.getScrollLevel1Map().getWorldOffsetY());
        }
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
        if(isAction < 4){
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
        for(int whichBullet = 0; whichBullet < numBullets; whichBullet++){
            bullets.get(whichBullet).moving(deltaTime, mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                    mapEditor.getScrollLevel1Map().getWorldOffsetY(), platformerScenario);
        }

        if(isAction == 5){  //dead
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
                idlingAnimation();
                break;
            case 2: animationTickTime += deltaTime;
                runningAnimation();
                break;
            case 3: animationTickTime += deltaTime;
                shootingAnimation();
                break;
            case 4: zombieDyingTickTime += deltaTime;
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
                case 0: betweenActionsTickTime += deltaTime;
                    idling();
                    break;
                case 1: falling(mapTer, mapEditor, worldWidth, worldHeight);
                    break;
                case 2: running(mapTer, mapEditor, worldWidth, worldHeight);
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
        if(isAction < 4){
            isAction = 4;
        }
    }

    public void dying(){
        while(zombieDyingTickTime > zombieDyingTick){
            zombieDyingTickTime -= zombieDyingTick;

            setActionAnimationFrames(DYING);

            if(animation.getCurrentFrame() == actions[DYING * numDirections] - 1){
                animation.setSetOfFrames(currentAction * numDirections + direction);
                isAction = 5;  //dead;
                return;
            }
            animation.nextFrame();
        }
    }

    public void fall(){
        if(!isSouth){
            isAction = 1;
        }
    }

    public void falling(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){
        setActionAnimationFrames(IDLE_ACTION);

        ...
    }

    public void hit(Weapon weapon){
        hp -= weapon.getHP();
        if(hp < 0){
            die();
        }
        if(isAction < 5){  //if an enemy is not dead...
            weapon.hit(true);
        }
    }

    public void idling(){
        while(betweenActionsTickTime > betweenActionsTick){
            betweenActionsTickTime -= betweenActionsTick;

            if(isRunning){
                isAction = 2; //running
            }
            else{
                actionScenario.setAction(random);
            }
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

            if(isAction != 1){
                currentAction = RUN_ACTION;

                setDirection(targetX);
                setSetOfFrames(currentAction * numDirections + direction);
                isAction = 2;
            }
        }
    }


    public void running(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){
        ...
    }

    public void runningAnimation(){setDirection(targetX);
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

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


    public void setBulletHolder(int numBullets){ //generates the bullet holder.
        for(int whichBullet = 0; whichBullet < numBullets; whichBullet++){
            bullets.add(new EnemyBullet("bullet", MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT));
        }
    }

    public void setDirection(int x){
        if(this.x < x){
            direction = 0;  //right
        }
        if(this.x > x){
            direction = 1;  //left
        }
    }

    public void setSetOfFrames(int setOfFrames){
        animation.setSetOfFrames(setOfFrames);
    }

    public void shoot(){
        if(isAction < ACTION_DYING){ //if zombie is not dying.
            if(!platformerScenario.getHero().equals(null)){
                this.targetX = platformerScenario.getTargetX()
                        + mapEditor.getScrollLevel1Map().getWorldOffsetX();
                setDirection(targetX);
                if(direction == 0){ //right
                    bulletDirection = EnemyBullet.DIRECTION_RIGHT;
                }
                else{ //left
                    bulletDirection = EnemyBullet.DIRECTION_LEFT;
                }
                isAction = 3;
            }
        }
    }

    public void shootingAnimation(){
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            setActionAnimationFrames(SHOOTING_ACTION);
            animation.nextFrame();
            if(animation.getCurrentFrame() == 1){
                for(int whichBullet = 0; whichBullet < numBullets; whichBullet++){
                    if(bullets.get(whichBullet).shoot(bulletX, bulletY,
                            mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                            mapEditor.getScrollLevel1Map().getWorldOffsetY(), bulletDirection)){
                        break;
                    }
                }
            }
            if(animation.getCurrentFrame() == actions[SHOOTING_ACTION * numDirections] - 1){
                isAction = 1;
            }
        }
    }

    public void stopRunning(){
        isRunning = false;
        isAction = 1;
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

        public void setAction(Random random){
            switch(random.nextInt(2)){
                case 0: run();
                    break;
                case 1: shoot();
                    break;
                default:
                    break;
            }
        }
    }
}
