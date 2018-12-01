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
import com.missionsurvive.objs.EnemyBullet;
import com.missionsurvive.objs.Weapon;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.SpawnBot;
import com.missionsurvive.utils.Assets;

import java.util.ArrayList;
import java.util.List;

public class SoldierZombie implements Bot {

    public static final float ALPHA_STEP = 0.04f;
    public static final float ALPHA_INIT = 1.0f;

    ObjAnimation animation;
    MapEditor mapEditor;
    private List<EnemyBullet> bullets = new ArrayList<EnemyBullet>();
    private int numBullets;
    private PlatformerScenario platformerScenario;
    private Texture texture;

    private int halfHeroHeight;
    private int halfHeroWidth;
    private int centerX, centerY, top, bottom, left, right;
    //moving vector in current moment
    private int vectorX, vectorY;
    private int bulletX, bulletY, bulletDirection;

    private int[] actions;
    private int numDirections;
    private int numActions;
    private int currentAction;
    //direction of action.  0 - right, 1 - left.
    private int direction;
    private int fallingSpeed = 7;
    private int numIdleShootingFrames = 3;
    private int startIdleShootingFrame;
    private int hp;

    private float animationTickTime = 0, animationTick = 0.08f;
    private float movingTickTime = 0, movingTick =  0.03f;
    private float zombieDyingTick = 0.5f, zombieDyingTickTime = 0;
    private float shootingTick = 0.2f, shootingTicktime = 0;
    private float betweenActionsTickTime = 1.0f , betweenActionsTick = 1.5f;
    private float alpha = ALPHA_INIT;

    private boolean isNorth, isEast, isSouth, isWest;
    private MapTer northTer, eastTer, southTer, westTer;

    //this variable determines the action our hero is using.
    private int isAction;
    private int targetX = -1, targetY = -1;

    private final int IDLE_SHOOTING_ACTION = 0;
    private final int DYING = 1;

    public SoldierZombie(String assetName, MapEditor mapEditor, int x, int y, int direction) {
        this.x = x;
        this.y = y;
        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }
        this.mapEditor = mapEditor;
        hp = 18;
        this.direction = direction;
        startIdleShootingFrame = 6;

        numDirections = 2;
        numActions = 2;
        actions = new int[numDirections * numActions];

        animation = new ObjAnimation(actions, spriteWidth, spriteHeight);
        numBullets = 5;
        setBulletHolder(numBullets);
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {
        for(int whichBullet = 0; whichBullet < numBullets; whichBullet++){
            bullets.get(whichBullet).drawObject(batch, col, row, mapEditor.getScrollLevel1Map().getWorldOffsetX(), mapEditor.getScrollLevel1Map().getWorldOffsetY());
        }

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

    private void drawTexture(SpriteBatch batch){
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
        if(isAction < 3){
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

        if(isAction == 4){  //dead
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
                animateFalling();
                break;
            case 2: shootingTicktime += deltaTime;
                shootingAnimation();
                break;
            case 3: zombieDyingTickTime += deltaTime;
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
                default: break;
            }
        }
    }

    @Override
    public void collide(Hero hero){
        if(isAction < 3){
            hero.die();
        }
    }


    public void animateFalling(){

        setActionAnimationFrames(IDLE_SHOOTING_ACTION);
        animation.setCurrentFrame(0);

        if(isSouth){
            animation.setCurrentFrame(1); //current frame = hero landed on the ground.
            isAction = 0;
        }
    }

    public void calculateVectorY(int direction, int speedInPixels, MapEditor mapEditor){
        if(!isSouth){
            vectorY = speedInPixels;
            return;
        }
        else{
            if(southTer != null){
                int southTerTop = southTer.getTop(mapEditor.getScrollLevel1Map());
                if((bottom + speedInPixels) >= southTerTop){  //если при fallingSpeed герой окажется внутри тайла... В данном случае - это (bottom + speedInPixels) > southTerTop).
                    vectorY = southTerTop - bottom;
                    return;
                }
                else{  //в ином случае: пусть герой еще пролетит со своей "крейсерской" скоростью.
                    vectorY = speedInPixels;
                    return;
                }
            }
        }
    }

    public void die(){
        if(isAction < 3){
            isAction = 3;
        }
    }

    public void dying(){
        while(zombieDyingTickTime > zombieDyingTick){
            zombieDyingTickTime -= zombieDyingTick;

            setActionAnimationFrames(DYING);

            if(animation.getCurrentFrame() == actions[DYING * numDirections] - 1){
                animation.setSetOfFrames(currentAction * numDirections + direction);
                isAction = 4;  //dead;
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
        calculateVectorY(direction, fallingSpeed, mapEditor);
        if(vectorY <= 0){
            isAction = 0; //idling
        }
        else{
            y += vectorY;
            isAction = 1; //falling
        }
    }

    @Override
    public void hit(Weapon weapon){
        hp -= weapon.getHP();
        if(hp < 0){
            die();
        }
        if(isAction < 4){  //if an enemy is not dead...
            if(isAction == 3){
                if(animation.getCurrentFrame() != 1){ //if zombie didn't actually fell while dying...
                    weapon.hit(true);
                }
            }
        }
    }

    public void idling(){
        if(isAction != 2) { //if it's not shooting.
            while(betweenActionsTickTime > betweenActionsTick){
                betweenActionsTickTime -= betweenActionsTick;
                shoot();
            }
        }
    }

    public void idlingAnimation(){
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            setActionAnimationFrames(IDLE_SHOOTING_ACTION);
            animation.animate(startIdleShootingFrame, 0);
        }
    }

    @Override
    public void jump(int destX, int destY){

    }

    @Override
    public void run(){

    }

    /**
     * this method is without parameter, because idling action and shooting action is the same for this zombie. That's why we need to
     * set setOfFrames directly (without "if" clause if(currentAction != action)).
     */
    public void setActionAnimationFrames(){
        animation.setSetOfFrames(currentAction * numDirections + direction);
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

    public void setOpacity(float deltaTime){
        animationTickTime += deltaTime;

        while(animationTickTime > animationTick){
            animationTickTime -= animationTick;
            alpha -= ALPHA_STEP;
            if(alpha < 0){
                alpha = 0;
                platformerScenario.removeBot(this, SpawnBot.SHOTGUN_ZOMBIE);
                return;
            }
        }
    }

    public void shoot(){
        if(isAction < 3){ //if zombie is not dying.
            if(!platformerScenario.getHero().equals(null)){
                this.targetX = platformerScenario.getTargetX() +
                        mapEditor.getScrollLevel1Map().getWorldOffsetX();

                isAction = 2;
                setDirection(targetX);
                setActionAnimationFrames();

                int left = platformerScenario.getHero().getX();
                int top = platformerScenario.getHero().getY();
                int right = left + platformerScenario.getHero().getSpriteWidth();
                int bottom = top + platformerScenario.getHero().getSpriteHeight();
                setStartIdleShootingFrame(left, top, right, bottom);
                animation.setCurrentFrame(startIdleShootingFrame);
            }
        }
    }

    public void shootingAnimation(){
        ...
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

    @Override
    public int getRight(){
        return right;
    }

    @Override
    public int getTop(){
        return top;
    }

    @Override
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
}
