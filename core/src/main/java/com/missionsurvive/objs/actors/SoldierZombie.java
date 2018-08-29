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

/**
 * Created by kuzmin on 14.07.18.
 */

public class SoldierZombie implements Bot {

    public static final float ALPHA_STEP = 0.04f;
    public static final float ALPHA_INIT = 1.0f;

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
    private List<EnemyBullet> bullets = new ArrayList<EnemyBullet>();
    private int numBullets;
    private PlatformerScenario platformerScenario;
    private Texture texture;

    private int halfHeroHeight; //"half...", because basically we need to know the half of "real" object width-height to calculate bounding (colliding) points.
    private int halfHeroWidth;
    private int centerX, centerY, top, bottom, left, right;  //ограничивающие точки "тела" героя (top, bootom, left, right). centerX-centerY - центр героя.
    private int vectorX, vectorY;  //Вектор передвижения героя в данный момент времени.
    private int bulletX, bulletY, bulletDirection; //starting point coordinates for bullet to move. They depend on action shooting frames.

    private int[] actions; //массив, в котором содержится инфа о действиях игрока и количестве фреймах в них (номер элемента массива - row, содержание элемента - количество фреймов).
    private int numDirections; //количество направлений для действий (сторон света).
    private int numActions; //количество действий.
    private int currentAction; //currentAction
    private int direction; //direction of action.  0 - right, 1 - left.
    private int fallingSpeed = 7; //falling speed in pixels.
    private int numIdleShootingFrames = 3; //количество фреймов, когда игрок стоит на одном месте, для анимации выстрелов в одну сторону (вверх, прямо-вверх, прямо и т.д.).
    private int startIdleShootingFrame; //какой из фреймов анимации idleShootingFrames является начальным для отрисовки.
    private int hp;

    private float animationTickTime = 0, animationTick = 0.08f;
    private float movingTickTime = 0, movingTick =  0.03f;
    private float zombieDyingTick = 0.5f, zombieDyingTickTime = 0;
    private float shootingTick = 0.2f, shootingTicktime = 0;
    private float betweenActionsTickTime = 1.0f , betweenActionsTick = 1.5f;
    private float alpha = ALPHA_INIT;

    private boolean isNorth, isEast, isSouth, isWest; //переменный, указывающие, какие тайлы мира заблокированы.
    private MapTer northTer, eastTer, southTer, westTer; //переменные, хранящие MapTer смежных с героем тайлов.

    private int isAction; //this variable determines the action our hero is using.
    private int targetX = -1, targetY = -1;

    private final int IDLE_SHOOTING_ACTION = 0;
    private final int DYING = 1;

    public SoldierZombie(String assetName, MapEditor mapEditor, int x, int y, int direction) {
        this.x = x;
        this.y = y;
        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }
        spriteWidth = 54;
        spriteHeight = 70;
        spritesetSpriteWidth = spriteWidth + 2;
        spritesetSpriteHeight = spriteHeight + 2;
        hitboxWidth = 28;
        hitboxHeight = 50;
        halfHeroHeight = hitboxHeight / 2;
        halfHeroWidth = hitboxWidth / 2;
        this.mapEditor = mapEditor;
        hp = 18;
        this.direction = direction;
        startIdleShootingFrame = 6;

        numDirections = 2;
        numActions = 2;
        actions = new int[numDirections * numActions];
        for(int whichAction = 0; whichAction < actions.length; whichAction++){
            if(whichAction >= IDLE_SHOOTING_ACTION * numDirections && whichAction <= IDLE_SHOOTING_ACTION * numDirections + 1) actions[whichAction] = 15;
            else if(whichAction >= DYING * numDirections && whichAction <= DYING * numDirections + 1) actions[whichAction] = 2;
        }
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

    public void setPos(){
        left = (x + 13) - mapEditor.getScrollLevel1Map().getWorldOffsetX(); //+17 means, that I calculated approximately the x coordinate of hitbox: (spriteWidth - hitboxWidth) / 2.
        top = (y + 20) - mapEditor.getScrollLevel1Map().getWorldOffsetY();
        bottom = top + hitboxHeight;
        right = left + hitboxWidth;

        centerX = left + halfHeroWidth;
        centerY = top + halfHeroHeight;
    }

    public void setStartIdleShootingFrame(int left, int top, int right, int bottom){
        int xPos = GeoHelper.inBoundsSpaceX(centerX, left, right);
        int yPos = GeoHelper.inBoundsSpaceY(centerY, top, bottom);

        if(xPos == 1 && yPos == 1){    //up-straight right
            startIdleShootingFrame = 3;

            bulletX = x + 39;
            bulletY = y + 9;
            bulletDirection = EnemyBullet.DIRECTION_UP_RIGHT;
        }
        else if(xPos == 1 && yPos == 0){   //down-straight right
            startIdleShootingFrame = 9;

            bulletX = x + 40;
            bulletY = y + 35;
            bulletDirection = EnemyBullet.DIRECTION_DOWN_RIGHT;
        }
        else if(xPos == 1 && yPos == 2){  //straight right
            startIdleShootingFrame = 6;

            bulletX = x + 45;
            bulletY = y + 22;
            bulletDirection = EnemyBullet.DIRECTION_RIGHT;
        }
        else if(xPos == 2 && yPos == 1){  //up
            startIdleShootingFrame = 0;

            bulletX = x + 31;
            bulletY = y + 5;
            bulletDirection = EnemyBullet.DIRECTION_UP;
        }
        else if(xPos == 2 && yPos == 0){  //down
            startIdleShootingFrame = 12;

            bulletX = x + 28;
            bulletY = y + 48;
            bulletDirection = EnemyBullet.DIRECTION_DOWN;
        }
        else if(xPos == 0 && yPos == 1){   //up-straight left
            startIdleShootingFrame = 3;

            bulletX = x + 15;
            bulletY = y + 9;
            bulletDirection = EnemyBullet.DIRECTION_UP_LEFT;
        }
        else if(xPos == 0 && yPos == 2){  //straight left
            startIdleShootingFrame = 6;

            bulletX = x + 9;
            bulletY = y + 22;
            bulletDirection = EnemyBullet.DIRECTION_LEFT;
        }
        else if(xPos == 0 && yPos == 0){  //down-straight left
            startIdleShootingFrame = 9;

            bulletX = x + 13;
            bulletY = y + 35;
            bulletDirection = EnemyBullet.DIRECTION_DOWN_LEFT;
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
        while(shootingTicktime > shootingTick) {
            shootingTicktime -= shootingTick;

            setActionAnimationFrames(IDLE_SHOOTING_ACTION);
            animation.animate(startIdleShootingFrame, numIdleShootingFrames);

            if(animation.getCurrentFrame() == (startIdleShootingFrame + 1)){
                for(int whichBullet = 0; whichBullet < numBullets; whichBullet++){
                    if(bullets.get(whichBullet).shoot(bulletX, bulletY, bulletDirection)){
                        break;
                    }
                }
            }
            if(animation.getCurrentFrame() == startIdleShootingFrame + 2){
                isAction = 0; //idling
            }
        }
    }


    public void tilemapCollision(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){

        int tileWidth = 16;
        int tileHeight = 16;

        int centerCol = ((centerX) + mapEditor.getScrollLevel1Map().getWorldOffsetX()) / tileWidth;
        int leftCol = ((left) + mapEditor.getScrollLevel1Map().getWorldOffsetX()) / tileWidth;
        int rightCol = ((right) + mapEditor.getScrollLevel1Map().getWorldOffsetX()) / tileWidth;

        int centerRow = ((centerY) + mapEditor.getScrollLevel1Map().getWorldOffsetY()) / tileHeight;
        if(centerRow < 0) centerRow = 0;
        if(centerRow >= worldHeight) centerRow = worldHeight - 1;

        int topRow = ((top) + mapEditor.getScrollLevel1Map().getWorldOffsetY()) / tileHeight;
        if(topRow < 0) topRow = 0;
        if(topRow >= worldHeight) topRow = worldHeight - 1;

        int bottomRow = ((bottom) + mapEditor.getScrollLevel1Map().getWorldOffsetY()) / tileHeight;
        if(bottomRow < 0) bottomRow = 0;
        if(bottomRow >= worldHeight) bottomRow = worldHeight - 1;

        if(mapTer[topRow][centerCol].isBlocked()){  //to the north
            isNorth = true;
            northTer = mapTer[topRow][centerCol];
        }
        else{
            isNorth = false;
            northTer = null;
        }
        if(mapTer[bottomRow][centerCol].isBlocked()){  //south
            isSouth = true;
            southTer = mapTer[bottomRow][centerCol];
        }
        else{
            isSouth = false;
            southTer = null;
        }
        if(mapTer[centerRow][leftCol].isBlocked()){  //west
            isWest = true;
            westTer = mapTer[centerRow][leftCol];
        }
        else{
            isWest = false;
            westTer = null;
        }
        if(mapTer[centerRow][rightCol].isBlocked()){  //east
            isEast = true;
            eastTer = mapTer[centerRow][rightCol];
        }
        else{
            isEast = false;
            eastTer = null;
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
