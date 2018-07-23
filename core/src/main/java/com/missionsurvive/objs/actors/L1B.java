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

    private int x; //screen coordinates.
    private int y;
    private int spritesetSpriteWidth;
    private int spritesetSpriteHeight;
    private int spriteWidth;
    private int spriteHeight;
    private int hitboxWidth;
    private int hitboxHeight;
    private List<EnemyBullet> bullets = new ArrayList<EnemyBullet>();
    private int numBullets;
    ObjAnimation animation;
    MapEditor mapEditor;
    Random random = new Random();
    L1B.ActionScenario actionScenario = new L1B.ActionScenario();
    private PlatformerScenario platformerScenario;
    private Texture texture;

    private int halfHeroHeight; //"half...", because basically we need to know the half of "real" object width-height to calculate bounding (colliding) points.
    private int halfHeroWidth;
    private int centerX, centerY, top, bottom, left, right;  //ограничивающие точки "тела" героя (top, bootom, left, right). centerX-centerY - центр героя.
    private int vectorX, vectorY;  //Вектор передвижения героя в данный момент времени.
    private int bulletX, bulletY, bulletDirection; //starting point coordinates for bullet to move. They depend on action shooting frames.

    private float animationTickTime = 0;
    private float movingTickTime = 0;
    private static float animationTick = 0.08f;
    private static float movingTick =  0.03f;
    private float betweenActionsTickTime = 0 , betweenActionsTick = 0.5f;

    private final int IDLE_ACTION = 0;
    private final int RUN_ACTION = 1;
    private final int SHOOTING_ACTION = 2;
    private final int DYING = 3;

    private int[] actions; //массив, в котором содержится инфа о действиях игрока и количестве фреймах в них (номер элемента массива - row, содержание элемента - количество фреймов).
    private int numDirections; //количество направлений для действий (сторон света).
    private int numActions; //количество действий.
    private int currentAction; //currentAction
    private int direction; //direction of action.  0 - right, 1 - left.
    private int runningSpeed = 2; //скорость бега в пикселях.
    private int fallingSpeed = 7; //falling speed in pixels.
    private int hits;

    private int startFallingFrame = 0, numFallingFrames = 1;

    private float zombieDyingTick = 0.1f, zombieDyingTickTime = 0;

    private boolean isNorth, isEast, isSouth, isWest; //переменный, указывающие, какие тайлы мира заблокированы.
    private MapTer northTer, eastTer, southTer, westTer; //переменные, хранящие MapTer смежных с героем тайлов.

    private int isAction; //this variable determines the action our hero is using.
    private boolean isRunning = false;
    private int targetX = -1, targetY = -1;

    public L1B(String assetName, MapEditor mapEditor, int x, int y) {
        this.x = x;
        this.y = y;
        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }
        spriteWidth = 60;
        spriteHeight = 70;
        spritesetSpriteWidth = spriteWidth + 2;
        spritesetSpriteHeight = spriteHeight + 2;
        hitboxWidth = 34;
        hitboxHeight = 50;
        halfHeroHeight = hitboxHeight / 2;
        halfHeroWidth = hitboxWidth / 2;
        hits = 10;

        this.mapEditor = mapEditor;

        numDirections = 2;
        numActions = 4;
        actions = new int[numDirections * numActions];
        for(int whichAction = 0; whichAction < actions.length; whichAction++){
            if(whichAction >= IDLE_ACTION * numDirections && whichAction <= IDLE_ACTION * numDirections + 1) actions[whichAction] = 1;
            else if(whichAction >= RUN_ACTION * numDirections && whichAction <= RUN_ACTION * numDirections + 1) actions[whichAction] = 10;
            else if(whichAction >= SHOOTING_ACTION * numDirections && whichAction <= SHOOTING_ACTION * numDirections + 1) actions[whichAction] = 3;
            else if(whichAction >= DYING * numDirections && whichAction <= DYING * numDirections + 1) actions[whichAction] = 4;
        }
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

    ///////////////////////////////THESE ARE MAIN METHODS FOR MOVING AND ANIMATING/////////////////////////////////////////////
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
    ///////////////////////////////THESE ARE MAIN METHODS FOR MOVING AND ANIMATING/////////////////////////////////////////////



    @Override
    public void collide(Hero hero){
        hero.die();
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
        else if( direction == 1){ //west
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

        if(!isSouth){
            calculateVectorY(direction, fallingSpeed, mapEditor);
            y += vectorY;
            isAction = 1;
        }
        else{
            calculateVectorY(direction, fallingSpeed, mapEditor);
            if(vectorY == 0){  //so we stood on the ground.
                animation.setCurrentFrame(0); //current frame = our hero landed on the ground.
                isAction = 0;
            }

            isAction = 0;
        }
    }

    public void hit(Weapon weapon){
        hits--;
        if(hits < 0){
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
        if(isAction < 4){ //if zombie is not dying.
            if(!platformerScenario.getHero().equals(null)){
                if(platformerScenario.getHero().isAction() != 2){  //if our player is not currently jumping.
                    this.targetX = platformerScenario.getTargetX() + mapEditor.getScrollLevel1Map().getWorldOffsetX();
                    this.targetY = platformerScenario.getTargetY() + mapEditor.getScrollLevel1Map().getWorldOffsetY();
                }
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
        if(x < targetX){
            calculateVectorX(0, runningSpeed, mapEditor);
            x += vectorX;
            if(x >= targetX){
                stopRunning();
                return;
            }
            return;
        }
        if(x > targetX){
            calculateVectorX(1, runningSpeed, mapEditor);
            x -= vectorX;
            if(x <= targetX){  //so our hero couldn't move over our touchX.
                stopRunning();
                return;
            }
            return;
        }
        stopRunning();
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

    public void setPos(){
        left = (x + 13) - mapEditor.getScrollLevel1Map().getWorldOffsetX(); //+13 means, that I calculated approximately the x coordinate of hitbox: (spriteWidth - hitboxWidth) / 2.
        top = (y + 10) - mapEditor.getScrollLevel1Map().getWorldOffsetY(); //+10 means, that I calculated approximately the y coordinate of hitbox: (spriteHeight - hitboxHeight) / 2.
        bottom = top + hitboxHeight;
        right = left + hitboxWidth;

        centerX = left + halfHeroWidth;
        centerY = top + halfHeroHeight;
    }

    public void setSetOfFrames(int setOfFrames){
        animation.setSetOfFrames(setOfFrames);
    }

    public void shoot(){
        if(isAction < 4){ //if zombie is not dying.
            if(!platformerScenario.getHero().equals(null)){
                if(platformerScenario.getHero().isAction() != 2){  //if our player is not currently jumping.
                    this.targetX = platformerScenario.getTargetX() + mapEditor.getScrollLevel1Map().getWorldOffsetX();
                    this.targetY = platformerScenario.getTargetY() + mapEditor.getScrollLevel1Map().getWorldOffsetY();
                    setDirection(targetX);
                    if(direction == 0){ //right
                        bulletX = x + 56;
                        bulletY = y + 23;
                        bulletDirection = EnemyBullet.DIRECTION_RIGHT;
                    }
                    else{ //left
                        bulletX = x + 2;
                        bulletY = y + 23;
                        bulletDirection = EnemyBullet.DIRECTION_LEFT;
                    }
                    isAction = 3;
                }
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
                    if(bullets.get(whichBullet).shoot(bulletX, bulletY, bulletDirection)){
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
        return 0;
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
