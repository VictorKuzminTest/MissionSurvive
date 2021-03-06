package com.missionsurvive.objs.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Animation;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.framework.Vector;
import com.missionsurvive.framework.impl.Animation2;
import com.missionsurvive.framework.impl.Vector2;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.geom.Hitbox;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.Bot;
import com.missionsurvive.objs.EnemyBullet;
import com.missionsurvive.objs.RocketL6B;
import com.missionsurvive.objs.Weapon;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.utils.Assets;

public class L6B implements Bot {

    public static final int WAIT_FOR_ATTACK = 22;
    public static final int WAIT_AFTER_DYING = 100;

    public static final int SPEED_MOVING = 2;
    public static final int SPEED_ATTACKING = 6;

    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_LEFT = -1;
    public static final int DIRECTION_ABOVE = 0;
    public static final int DIRECTION_UP = -1;
    public static final int DIRECTION_DOWN = 1;

    public static final int SPRITES_DIRECTION = 0;
    public static final int SPRITES_ATTACK = 1;
    public static final int SPRITES_DYING = 2;

    public static final int FRAME_ABOVE = 3;
    public static final int FRAME_ATTACK = 11;

    public static final int ACTION_PERSUE = 0;
    public static final int ACTION_WAIT = 1;
    public static final int ACTION_ATTACK = 2;
    public static final int ACTION_GOBACK = 3;
    public static final int ACTION_DYING = 4;
    public static final int ACTION_WAIT_AFTER_DYING = 5;
    public static final int ACTION_DEAD = 6;

    private Hitbox hitbox;
    private Vector movingVector;
    private PlatformerScenario platformerScenario;
    private MapEditor mapEditor;
    private Animation animation;
    private Animation directionAnimation;
    private Animation attackAnimation;
    private Animation dyingAnimation;
    private Animation burstAnimation;
    private RocketLauncher rocketLauncher;
    private Texture texture;

    private int worldX, worldY;
    private int targetX;
    private int directionX;
    private int frames;
    private int spriteWidth = 100;
    private int spriteHeight = 110;
    private int spritesetSpriteWidth = spriteWidth + 2;
    private int spritesetSpriteHeight = spriteHeight + 2;
    private int action;
    private int hitpoints;
    private int numBursts;
    private int waitAfterDying;
    private int waitForAttack;

    private float movingTickTime = 0;
    private float movingTick = 0.03f;

    private boolean isBurning;

    public L6B(String assetName, MapEditor mapEditor, int worldX, int worldY){
        this.worldX = worldX;
        this.worldY = worldY;
        movingVector = new Vector2(SPEED_MOVING, SPEED_ATTACKING);
        hitbox = new Hitbox(worldX, worldY, 26, 48, 31, 47);
        this.mapEditor = mapEditor;
        directionX = DIRECTION_LEFT;
        hitpoints = 10;
        setAnimation();
        rocketLauncher = new RocketLauncher();

        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
            rocketLauncher.setRocket(false);
        }
        else{
            rocketLauncher.setRocket(true);
        }
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {
        ...
    }

    public void drawTale(SpriteBatch batch){
        ...
    }

    @Override
    public void drawObject(SpriteBatch batch, int screenX, int screenY) {

    }

    @Override
    public boolean onTouch() {
        return true;
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void moving(float deltaTime, MapTer[][] mapTer, MapEditor mapEditor,
                       int worldWidth, int worldHeight) {

        if(action < ACTION_DYING){
            rocketLauncher.shoot(deltaTime,
                    mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                    mapEditor.getScrollLevel1Map().getWorldOffsetY());
            hit(rocketLauncher.getRocket());
        }

        rocketLauncher.moving(deltaTime, mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                mapEditor.getScrollLevel1Map().getWorldOffsetY(), platformerScenario);

        movingTickTime += deltaTime;

        while(movingTickTime > movingTick) {
            movingTickTime -= movingTick;

            switch(action){
                case ACTION_PERSUE:
                    setTargetX(platformerScenario.getHero());
                    persue();
                    break;
                case ACTION_WAIT:
                    setTargetX(platformerScenario.getHero());
                    if(directionX == DIRECTION_ABOVE){
                        gettingReadyToAttack();
                    }
                    else{
                        waitForAttack = 0;
                        action = ACTION_PERSUE;
                    }
                    break;
                case ACTION_ATTACK:
                    attacking();
                    break;
                case ACTION_GOBACK:
                    goBack();
                    break;
                case ACTION_DYING:
                    dying();
                    break;
                case ACTION_WAIT_AFTER_DYING:
                    waitAfterDying();
                    break;
            }
        }
    }

    private void dying() {
        if(numBursts < 20){
            if(burstAnimation.getCurrentFrame() == 4){
                burstAnimation.setCurrentFrame(0);
                numBursts++;
            }
            else{
                burstAnimation.animate(0, 4);
            }
        }
        else{
            isBurning = false;
            dyingAnimation.animate(0, 6);
            if(dyingAnimation.getCurrentFrame() == 6){
                action = ACTION_WAIT_AFTER_DYING;
            }
        }
    }

    public void waitAfterDying(){
        waitAfterDying++;
        if(waitAfterDying > WAIT_AFTER_DYING){
            action = ACTION_DEAD;
        }
    }

    @Override
    public void collide(Hero hero) {
        if(action < ACTION_DYING){
            hero.die();
        }
    }

    @Override
    public void hit(Weapon weapon) {

    }

    private void hit(RocketL6B rocket){
        if(GeoHelper.overlapRectangles(rocket.getScreenX(), rocket.getScreenY(),
                rocket.getWidth(),
                rocket.getHeight(),
                getLeft(), getTop(), getHitboxWidth(), getHitboxHeight())){
            if(rocket.getDirection() != 0){
                rocket.gotIt(0);
                hitpoints--;
            }
        }
        if(hitpoints < 0){
            isBurning = true;
            action = ACTION_DYING;
            setAnimationFrames(SPRITES_DYING);
        }
    }

    @Override
    public void jump(int destX, int destY) {

    }

    @Override
    public void run() {

    }

    @Override
    public int getX() {
        return worldX - mapEditor.getScrollLevel1Map().getWorldOffsetX();
    }

    @Override
    public int getY() {
        return worldY - mapEditor.getScrollLevel1Map().getWorldOffsetY();
    }

    @Override
    public int getSpriteWidth() {
        return spriteWidth;
    }

    @Override
    public int getSpriteHeight() {
        return spriteHeight;
    }

    @Override
    public int getLeft() {
        return hitbox.getLeft();
    }

    @Override
    public int getRight() {
        return hitbox.getRight();
    }

    @Override
    public int getTop() {
        return hitbox.getTop();
    }

    @Override
    public int getBottom() {
        return hitbox.getBottom();
    }

    @Override
    public int getHitboxWidth() {
        return hitbox.getHitboxWidth();
    }

    @Override
    public int getHitboxHeight() {
        return hitbox.getHitboxHeight();
    }

    @Override
    public void setScenario(Scenario scenario) {
        platformerScenario = (PlatformerScenario) scenario;
    }

    @Override
    public Decorator getDecorator() {
        return null;
    }

    @Override
    public int isAction() {
        return action;
    }

    public void setDirectionX(int directionX){
        this.directionX = directionX;
    }

    public void falling(MapTer[][] map, int tileSize) {
        int row = GeoHelper.checkRowCol(hitbox.getBottom() / tileSize, map.length);
        int col = GeoHelper.checkRowCol(hitbox.getCenterX() / tileSize, map[0].length);
        if(!map[row][col].isBlocked()){
            attackMovement(DIRECTION_DOWN);
        }
        else{
            makeBackAttackingSprites();
            action = ACTION_GOBACK;
        }
    }

    public Hitbox getHitbox(){
        return hitbox;
    }

    public int getTargetX() {
        return targetX;
    }

    public void setTargetX(Hero hero) {
        if(GeoHelper.inBoundsSpaceX(
                hitbox.getCenterX(),
                hero.getHitbox().getLeft(), hero.getHitbox().getRight()) == GeoHelper.INSIDE){

            directionX = DIRECTION_ABOVE;
        }
        else{
            targetX = hero.getCenterX();

            if(targetX > hitbox.getCenterX()){
                directionX = DIRECTION_RIGHT;
            }
            else{
                directionX = DIRECTION_LEFT;
            }
        }
    }

    public void move() {
        worldX += movingVector.getX() * directionX;

        hitbox.setPos(worldX - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                worldY - mapEditor.getScrollLevel1Map().getWorldOffsetY());
    }

    public void attackMovement(int directionY) {
        worldY += movingVector.getY() * directionY;

        hitbox.setPos(worldX - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                worldY - mapEditor.getScrollLevel1Map().getWorldOffsetY());
    }

    public void setAnimation() {
        animation = new Animation2(0, 0, spriteWidth, spriteHeight);
        //forming direction animation:
        directionAnimation = new Animation2(0, 0, spriteWidth, spriteHeight);
        for(int i = 0; i < 7; i++){
            directionAnimation.addChild(new Animation2(1 + i * spritesetSpriteWidth, 1 + 0,
                    spriteWidth, spriteHeight));
        }
        //forming attacking animation:
        attackAnimation = new Animation2(0, 0, spriteWidth, spriteHeight);
        for(int i = 0; i <= 5; i++){
            attackAnimation.addChild(new Animation2(1 + i * spritesetSpriteWidth, 1 + 1 * spritesetSpriteHeight,
                    spriteWidth, spriteHeight));
        }
        for(int i = 4; i >= 0; i--){
            attackAnimation.addChild(new Animation2(1 + i * spritesetSpriteWidth, 1 + 1 * spritesetSpriteHeight,
                    spriteWidth, spriteHeight));
        }
        attackAnimation.addChild(new Animation2(1 + 0, 1 + 2 * spritesetSpriteHeight,
                spriteWidth, spriteHeight));
        //forming dying animation:
        dyingAnimation = new Animation2(0, 0, spriteWidth, spriteHeight);
        for(int i = 0; i < 7; i++){
            dyingAnimation.addChild(new Animation2(1 + i * spritesetSpriteWidth, 1 + 3 * spritesetSpriteHeight,
                    spriteWidth, spriteHeight));
        }
        //forming burst animation:
        burstAnimation = new Animation2(0, 0, spriteWidth, spriteHeight);
        for(int i = 2; i < 7; i++){
            burstAnimation.addChild(new Animation2(1 + i * spritesetSpriteWidth, 1 + 2 * spritesetSpriteHeight,
                    spriteWidth, spriteHeight));
        }
        //add children to main animation:
        animation.addChild(directionAnimation);
        animation.addChild(attackAnimation);
        animation.addChild(dyingAnimation);
    }

    public Animation getAnimation() {
        return animation;
    }


    public void turnRight() {
        directionAnimation.animate(directionAnimation.getCurrentFrame(),
                directionAnimation.getChildren().size() - 1);
    }

    public void turnLeft() {
        directionAnimation.animate(directionAnimation.getCurrentFrame(), 0);
    }

    public void turnCenter() {
        directionAnimation.animate(directionAnimation.getCurrentFrame(), FRAME_ABOVE);
    }

    public void animateStartingAttack() {
        attackAnimation.animate(attackAnimation.getCurrentFrame(), FRAME_ATTACK);
    }


    public boolean isChangingDirection() {
        switch(directionX){
            case DIRECTION_RIGHT:
                if(directionAnimation.getCurrentFrame() != directionAnimation.getChildren().size() - 1){
                    return true;
                }
                break;
            case DIRECTION_LEFT:
                if(directionAnimation.getCurrentFrame() != 0){
                    return true;
                }
                break;
            case DIRECTION_ABOVE:
                if(directionAnimation.getCurrentFrame() != FRAME_ABOVE){
                    return true;
                }
                break;
        }
        return false;
    }

    public void persue() {
        if(!isChangingDirection()){
            move();
        }
        else{
            switch (directionX){
                case DIRECTION_RIGHT:
                    turnRight();
                    break;
                case DIRECTION_LEFT:
                    turnLeft();
                    break;
                case DIRECTION_ABOVE:
                    turnCenter();
                    checkForCenterSpritePosition();
                    break;
            }
        }
    }

    public void checkForCenterSpritePosition(){
        if(directionAnimation.getCurrentFrame() == FRAME_ABOVE){
            gettingReadyToAttack();
        }
    }

    public void setAnimationFrames(int frames) {
        this.frames = frames;
    }

    public int getFrames() {
        return frames;
    }

    public void attacking() {
        if(attackAnimation.getCurrentFrame() != FRAME_ATTACK){
            animateStartingAttack();
        }
        else{
            falling(mapEditor.getLevel1Ter(), 16);
        }
    }

    public void makeBackAttackingSprites() {
        attackAnimation.setCurrentFrame(0);
    }


    public void goBack() {
        attackMovement(DIRECTION_UP);
        if(worldY - mapEditor.getScrollLevel1Map().getWorldOffsetY() < 0){
            worldY = DIRECTION_ABOVE;
            action = ACTION_PERSUE;
            setAnimationFrames(SPRITES_DIRECTION);
        }
    }

    public void gettingReadyToAttack(){
        switch (action){
            case ACTION_PERSUE:
                action = ACTION_WAIT;
                break;
            case ACTION_WAIT:
                waitForAttack++;
                if(waitForAttack > WAIT_FOR_ATTACK){
                    waitForAttack = 0;
                    action = ACTION_ATTACK;
                    setAnimationFrames(SPRITES_ATTACK);
                }
                break;
        }
    }


    private class RocketLauncher{

        private RocketL6B rocket;

        private float shootingTickTime;
        private float shootingTick = 2.0f;

        public void setRocket(boolean isTesting){
            if(!isTesting){
                rocket = new RocketL6B("rocket", MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT);
            }
            else{
                rocket = new RocketL6B(null, 480, 320);
            }
        }

        public void draw(SpriteBatch batch){
            rocket.drawObject(batch, 0, 0, 0, 0);
        }

        public void moving(float deltaTime, int worldOffsetX, int worldOffsetY,
                           PlatformerScenario platformerScenario){
            rocket.moving(deltaTime, worldOffsetX, worldOffsetY, platformerScenario);
        }

        public void shoot(float deltaTime, int offsetX, int offsetY){
            shootingTickTime += deltaTime;

            while(shootingTickTime > shootingTick){
                shootingTickTime -= shootingTick;

                rocket.shoot(480 + offsetX, 240, offsetX, offsetY, EnemyBullet.DIRECTION_LEFT);
            }
        }

        public RocketL6B getRocket(){
            return rocket;
        }
    }
}
