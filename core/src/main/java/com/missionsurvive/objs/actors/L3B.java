package com.missionsurvive.objs.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.framework.impl.ObjAnimation;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.geom.Hitbox;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.Bot;
import com.missionsurvive.objs.EnemyBullet;
import com.missionsurvive.objs.Rocket;
import com.missionsurvive.objs.Weapon;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.utils.Assets;

import java.util.ArrayList;
import java.util.List;

public class L3B implements Bot {

    private final int SHOOTING_SPRITES = 0, DYING_SPRITES = 1;
    public static final int IDLE_ACTION = 0;
    public static final int FALLING_ACTION = 1;
    public static final int SHOOTING_ACTION = 2;
    public static final int START_DYING_ACTION = 3;
    public static final int BETWEEN_DYING_ACTION = 4;
    public static final int END_DYING_ACTION = 5;
    public static final int DEAD = 6;


    private Hitbox hitbox;
    private List<EnemyBullet> bullets = new ArrayList<EnemyBullet>();
    private int numBullets;
    ObjAnimation animation;
    MapEditor mapEditor;
    private PlatformerScenario platformerScenario;
    private Texture texture;

    private int vectorX, vectorY;  //Вектор передвижения героя в данный момент времени.
    private int bulletX, bulletY, bulletDirection; //starting point coordinates for bullet to move. They depend on action shooting frames.

    private float animationTickTime = 0;
    private float movingTickTime = 0;
    private static float animationTick = 0.08f;
    private static float movingTick =  0.03f;
    private float betweenShotsTickTime = 0 , betweenShotsTick = 2.0f;

    //each element of this array contains the number of sprites in a row.
    private int[] spritesRows;
    private int numDirections;
    private int numActions;
    private int currentAction;
    //direction of action.  0 - right, 1 - left.
    private int direction;
    private int fallingSpeed = 7;
    private int hp;

    private float StartDyingTick = 0.1f, StartDyingTickTime = 0,
            betweenDyingTick = 1.5f, betweenDyingTickTime = 0, EndDyingTick = 0.1f,
            EndDyingTickTime = 0;

    private boolean isNorth, isEast, isSouth, isWest;
    private MapTer northTer, eastTer, southTer, westTer;

    //this variable determines the action our hero is using.
    private int isAction;

    public L3B(String assetName, MapEditor mapEditor, int x, int y, int hp) {
        this.x = x;
        this.y = y;
        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }

        this.hp = hp;

        hitbox = new Hitbox(x, y, 40, 80, 20, 17);

        this.mapEditor = mapEditor;

        numDirections = 1;
        numActions = 2;
        spritesRows = new int[numDirections * numActions];
        for(int whichAction = 0; whichAction < spritesRows.length; whichAction++){
            if(whichAction >= SHOOTING_SPRITES * numDirections && whichAction <= SHOOTING_SPRITES * numDirections + 1) spritesRows[whichAction] = 10;
            else if(whichAction >= DYING_SPRITES * numDirections && whichAction <= DYING_SPRITES * numDirections + 1) spritesRows[whichAction] = 9;
        }
        animation = new ObjAnimation(spritesRows, spriteWidth, spriteHeight);
        numBullets = 5;
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
        if(isAction < START_DYING_ACTION){
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

        if(isAction == DEAD){  //dead
            return;
        }

        move(mapTer, mapEditor, worldWidth, worldHeight, deltaTime);
        animate(mapTer, mapEditor, worldWidth, worldHeight, deltaTime);
    }

    public void animate(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight, float deltaTime){

        switch (isAction){
            case IDLE_ACTION: animationTickTime += deltaTime;
                idlingAnimation();
                break;
            case FALLING_ACTION: animationTickTime += deltaTime;
                idlingAnimation();
                break;
            case SHOOTING_ACTION: animationTickTime += deltaTime;
                shootingAnimation();
                break;
            case START_DYING_ACTION: StartDyingTickTime += deltaTime;
                dying();
                break;
            case BETWEEN_DYING_ACTION: betweenDyingTickTime += deltaTime;
                dying();
                break;
            case END_DYING_ACTION: EndDyingTickTime += deltaTime;
                dying();
                break;
            default: break;
        }
    }

    public void move(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight, float deltaTime){
        movingTickTime += deltaTime;

        while(movingTickTime > movingTick) {
            movingTickTime -= movingTick;

            hitbox.setPos(x - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                    y - mapEditor.getScrollLevel1Map().getWorldOffsetY());
            tilemapCollision(mapTer, mapEditor, worldWidth, worldHeight);
            fall();

            switch(isAction){
                case IDLE_ACTION: betweenShotsTickTime += deltaTime;
                    idling();
                    break;
                case FALLING_ACTION: falling(mapTer, mapEditor, worldWidth, worldHeight);
                    break;
                default: break;
            }
        }
    }

    @Override
    public void collide(Hero hero){
        if(isAction < START_DYING_ACTION){
            hero.die();
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
                    if((hitbox.getTop() - speedInPixels) < northTerBottom){  //если при runningSpeed герой окажется внутри тайла... В данном случае - это (top - speedInPixels) < northTerBottom).
                        vectorY = hitbox.getTop() - northTerBottom;  //vectorY изменит свое значение ровно до позиции столкновения с тайлом.
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
                    if((hitbox.getBottom() + speedInPixels) > southTerTop){  //если при runningSpeed герой окажется внутри тайла... В данном случае - это (bottom + speedInPixels) > southTerTop).
                        vectorY = southTerTop - hitbox.getBottom();  //vectorX изменит свое значение ровно до позиции столкновения с тайлом.
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
        if(isAction < START_DYING_ACTION){
            isAction = START_DYING_ACTION;
        }
    }

    public void dying(){
        if(isAction == START_DYING_ACTION){
            while(StartDyingTickTime > StartDyingTick){
                StartDyingTickTime -= StartDyingTick;

                setActionAnimationFrames(DYING_SPRITES);

                if(animation.getCurrentFrame() == spritesRows[DYING_SPRITES * numDirections] - 5){
                    animation.setSetOfFrames(currentAction * numDirections + direction);
                    isAction = BETWEEN_DYING_ACTION;
                    return;
                }
                animation.nextFrame();
            }
        }
        else if(isAction == BETWEEN_DYING_ACTION){
            while(betweenDyingTickTime > betweenDyingTick){
                betweenDyingTickTime -= betweenDyingTick;

                isAction = END_DYING_ACTION;
            }
        }
        else if(isAction == END_DYING_ACTION){
            while(EndDyingTickTime > EndDyingTick){
                EndDyingTickTime -= EndDyingTick;

                setActionAnimationFrames(DYING_SPRITES);

                if(animation.getCurrentFrame() == spritesRows[DYING_SPRITES * numDirections] - 2){
                    animation.setSetOfFrames(currentAction * numDirections + direction);
                    isAction = DEAD;
                    return;
                }
                animation.nextFrame();
            }
        }
    }

    public void fall(){
        if(!isSouth){
            isAction = FALLING_ACTION;
        }
    }


    public void falling(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){
        setActionAnimationFrames(SHOOTING_SPRITES);

        if(!isSouth){
            calculateVectorY(direction, fallingSpeed, mapEditor);
            y += vectorY;
            isAction = FALLING_ACTION;
        }
        else{
            calculateVectorY(direction, fallingSpeed, mapEditor);
            if(vectorY == 0){  //so we stood on the ground.
                animation.setCurrentFrame(0); //current frame = our hero landed on the ground.
            }
            isAction = IDLE_ACTION;
        }
    }

    @Override
    public void hit(Weapon weapon){
        hp -= weapon.getHP();
        if(hp < 0){
            die();
        }
        if(isAction < DEAD){  //if an enemy is not dead...
            weapon.hit(true);
        }
    }

    public void idling(){
        while(betweenShotsTickTime > betweenShotsTick){
            betweenShotsTickTime -= betweenShotsTick;

            shoot();
        }
    }

    public void idlingAnimation(){
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            setActionAnimationFrames(SHOOTING_SPRITES);
            animation.animate(0, 1);
        }
    }

    @Override
    public void jump(int destX, int destY){

    }

    @Override
    public void run(){

    }

    public void setActionAnimationFrames(int action){
        if(currentAction != action){
            currentAction = action;
            animation.setSetOfFrames(currentAction * numDirections + direction);
        }
    }

    public void setBulletHolder(int numBullets){ //generates the bullet holder.
        for(int whichBullet = 0; whichBullet < numBullets; whichBullet++){
            bullets.add(new Rocket("rocket", MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT));
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
        if(isAction == IDLE_ACTION){

            isAction = SHOOTING_ACTION;

            bulletX = x + 2;
            bulletY = y + 56;
            bulletDirection = EnemyBullet.DIRECTION_LEFT;
        }
    }


    public void shootingAnimation(){
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            setActionAnimationFrames(SHOOTING_SPRITES);
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
            if(animation.getCurrentFrame() == spritesRows[SHOOTING_SPRITES * numDirections] - 1){
                isAction = IDLE_ACTION;
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
        return spriteWidth - 70;
    }

    @Override
    public int getSpriteHeight(){
        return spriteHeight;
    }

    @Override
    public int getLeft(){
        return hitbox.getLeft();
    }

    @Override
    public int getRight(){
        return hitbox.getRight();
    }

    @Override
    public int getTop(){
        return hitbox.getTop();
    }

    @Override
    public int getBottom(){
        return hitbox.getBottom();
    }

    @Override
    public int getHitboxWidth(){
        return hitbox.getHitboxWidth();
    }

    @Override
    public int getHitboxHeight(){
        return hitbox.getHitboxHeight();
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
}
