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

/**
 * Created by kuzmin on 14.07.18.
 */
public class L1B implements Bot{

    public static final int ACTION_IDLE = 0;
    public static final int ACTION_FALLING = 1;
    public static final int ACTION_RUNNING = 2;
    public static final int ACTION_DYING = 4;
    public static final int ACTION_DEAD = 5;

    private final int SPRITES_IDLE = 0;
    private final int SPRITES_RUN = 1;
    private final int SPRITES_DYING = 2;

    private int x; //screen coordinates.
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

    private int halfHeroHeight; //"half...", because basically we need to know the half of "real" object width-height to calculate bounding (colliding) points.
    private int halfHeroWidth;
    private int centerX, centerY, top, bottom, left, right;  //ограничивающие точки "тела" героя (top, bootom, left, right). centerX-centerY - центр героя.
    private int vectorX, vectorY;  //Вектор передвижения героя в данный момент времени.

    private float animationTickTime = 0;
    private float movingTickTime = 0;
    private float animationTick = 0.08f;
    private float movingTick =  0.03f;
    private float betweenActionsTickTime = 0 , betweenActionsTick = 1.2f;

    private int[] actions; //массив, в котором содержится инфа о действиях игрока и количестве фреймах в них (номер элемента массива - row, содержание элемента - количество фреймов).
    private int numDirections; //количество направлений для действий (сторон света).
    private int numActions; //количество действий.
    private int currentAction; //currentAction
    private int direction; //direction of action.  0 - right, 1 - left.
    private int runningSpeed = 4; //скорость бега в пикселях.
    private int fallingSpeed = 1; //falling speed in pixels.
    private int hp;

    private float zombieDyingTick = 0.1f, zombieDyingTickTime = 0;

    private boolean isNorth, isEast, isSouth, isWest; //переменный, указывающие, какие тайлы мира заблокированы.
    private MapTer northTer, eastTer, southTer, westTer; //переменные, хранящие MapTer смежных с героем тайлов.

    private int isAction; //this variable determines the action our hero is using.
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
        for(int whichAction = 0; whichAction < actions.length; whichAction++){
            if(whichAction >= SPRITES_IDLE * numDirections && whichAction <= SPRITES_IDLE * numDirections + 1) actions[whichAction] = 1;
            else if(whichAction >= SPRITES_RUN * numDirections && whichAction <= SPRITES_RUN * numDirections + 1) actions[whichAction] = 10;
            else if(whichAction >= SPRITES_DYING * numDirections && whichAction <= SPRITES_DYING * numDirections + 1) actions[whichAction] = 4;
        }
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

    ///////////////////////////////THESE ARE MAIN METHODS FOR MOVING AND ANIMATING/////////////////////////////////////////////
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
    ///////////////////////////////THESE ARE MAIN METHODS FOR MOVING AND ANIMATING/////////////////////////////////////////////


    @Override
    public void collide(Hero hero){
        if(isAction < ACTION_DYING){
            hero.die();
        }
    }

    public void calculateVectorX(int direction, int speedInPixels, MapEditor mapEditor){
        if(direction == 0){  //east
            if(!isEast){
                vectorX = speedInPixels;
                return;
            }
            else{
                if(eastTer != null){
                    int eastTerLeft = eastTer.getLeft(mapEditor.getScrollLevel1Map());
                    if((right + speedInPixels) > eastTerLeft){  //если при runningSpeed герой окажется внутри тайла... В данном случае - это (right + runningSpeed) > eastTerLeft.
                        vectorX = eastTerLeft - right;  //vectorX изменит свое значение ровно до позиции столкновения с тайлом.
                        return;
                    }
                    else{  //в ином случае: пусть герой ещо пробежит со своей "крейсерской" скоростью.
                        vectorX = speedInPixels;
                        return;
                    }
                }
            }
        }
        else if(direction == 1){ //west
            if(!isWest){
                vectorX = speedInPixels;
                return;
            }
            else{
                if(westTer != null){ //на всякий случай сначала проверяем на null.
                    int westTerRight = westTer.getRight(mapEditor.getScrollLevel1Map());
                    if((left - speedInPixels) <= westTerRight){  //если при runningSpeed герой окажется внутри тайла... В данном случае - это (left - runningSpeed) > westTerRight.
                        vectorX = left - westTerRight;  //vectorX изменит свое значение ровно до позиции столкновения с тайлом.
                        return;
                    }
                    else{  //в ином случае: пусть герой ещо пробежит со своей "крейсерской" скоростью.
                        vectorX = speedInPixels;
                        return;
                    }
                }
            }
        }
    }


    public void calculateVectorY(int direction, int speedInPixels, MapEditor mapEditor){
        if(direction == 0){  //north
            if(!isNorth){
                vectorY = speedInPixels;
                return;
            }
            else{
                if(northTer != null){
                    int northTerBottom = northTer.getBottom(mapEditor.getScrollLevel1Map());
                    if((top - speedInPixels) < northTerBottom){  //если при runningSpeed герой окажется внутри тайла... В данном случае - это (top - speedInPixels) < northTerBottom).
                        vectorY = top - northTerBottom;  //vectorY изменит свое значение ровно до позиции столкновения с тайлом.
                        return;
                    }
                    else{  //в ином случае: пусть герой ещо пробежит со своей "крейсерской" скоростью.
                        vectorY = speedInPixels;
                        return;
                    }
                }
            }
        }
        if(direction == 1){  //south
            if(!isSouth){
                vectorY = speedInPixels;
                return;
            }
            else{
                if(southTer != null){
                    int southTerTop = southTer.getTop(mapEditor.getScrollLevel1Map());
                    if((bottom + speedInPixels) > southTerTop){  //если при runningSpeed герой окажется внутри тайла... В данном случае - это (bottom + speedInPixels) > southTerTop).
                        vectorY = southTerTop - bottom;  //vectorX изменит свое значение ровно до позиции столкновения с тайлом.
                        return;
                    }
                    else{  //в ином случае: пусть герой ещо пробежит со своей "крейсерской" скоростью.
                        vectorY = speedInPixels;
                        return;
                    }
                }
            }
        }
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
        if((x + 31) < targetX){
            calculateVectorX(0, runningSpeed, mapEditor);
            x += vectorX;
            if((x + 31) >= targetX){
                stopRunning();
                return;
            }
            return;
        }
        if((x + 31) > targetX){
            calculateVectorX(1, runningSpeed, mapEditor);
            x -= vectorX;
            if((x + 31) <= targetX){
                stopRunning();
                return;
            }
            return;
        }
        stopRunning();
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

    public void setPos(){
        left = (x + 23) - mapEditor.getScrollLevel1Map().getWorldOffsetX(); //+23 means, that I calculated approximately the x coordinate of hitbox: (spriteWidth - hitboxWidth) / 2.
        top = (y + 10) - mapEditor.getScrollLevel1Map().getWorldOffsetY(); //+10 means, that I calculated approximately the y coordinate of hitbox: (spriteHeight - hitboxHeight) / 2.
        bottom = top + hitboxHeight;
        right = left + hitboxWidth;

        centerX = left + halfHeroWidth;
        centerY = top + halfHeroHeight;
    }

    public void setSetOfFrames(int setOfFrames){
        animation.setSetOfFrames(setOfFrames);
    }

    public void stopRunning(){
        isRunning = false;
        isAction = ACTION_FALLING;
    }

    public  void tilemapCollision(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){
        int tileWidth = 16;
        int tileHeight = 16;

        int centerCol = ((centerX) + mapEditor.getScrollLevel1Map().getWorldOffsetX()) / tileWidth;
        int leftCol = ((left) + mapEditor.getScrollLevel1Map().getWorldOffsetX()) / tileWidth;
        int rightCol = ((right) + mapEditor.getScrollLevel1Map().getWorldOffsetX()) / tileWidth;

        int centerRow = ((centerY) + mapEditor.getScrollLevel1Map().getWorldOffsetY()) / tileHeight;
        int topRow = ((top) + mapEditor.getScrollLevel1Map().getWorldOffsetY()) / tileHeight;
        int bottomRow = ((bottom) + mapEditor.getScrollLevel1Map().getWorldOffsetY()) / tileHeight;

        if(topRow >= 0) {  //to the north
            if(mapTer[topRow][centerCol].isBlocked()){
                isNorth = true;
                northTer = mapTer[topRow][centerCol];
            }
            else{
                isNorth = false;
                northTer = null;
            }
        }
        if(bottomRow < worldHeight){  //south
            if(mapTer[bottomRow][centerCol].isBlocked()){
                isSouth = true;
                southTer = mapTer[bottomRow][centerCol];
            }
            else{
                isSouth = false;
                southTer = null;
            }
        }
        if(leftCol >= 0 ){   //west
            if(mapTer[centerRow][leftCol].isBlocked()){
                isWest = true;
                westTer = mapTer[centerRow][leftCol];
            }
            else{
                isWest = false;
                westTer = null;
            }
        }
        if(rightCol >= 0 ){   //east
            if(mapTer[centerRow][rightCol].isBlocked()){
                isEast = true;
                eastTer = mapTer[centerRow][rightCol];
            }
            else{
                isEast = false;
                eastTer = null;
            }
        }
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
