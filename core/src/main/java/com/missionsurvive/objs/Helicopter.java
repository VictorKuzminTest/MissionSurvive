package com.missionsurvive.objs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Animation;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.framework.impl.Animation2;
import com.missionsurvive.framework.impl.ObjAnimation;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.geom.Hitbox;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.utils.Assets;

import sun.rmi.runtime.Log;

/**
 * Created by kuzmin on 01.05.18.
 */

public class Helicopter implements Bot{
    public static final float SCALE_TICK = 0.2f;
    public static final float CRASHING_TICK = 0.03f;
    public static final float ANIMATION_TICK = 0.08f;

    public static final int FRAMES_FLYING = 0;
    public static final int FRAMES_BURNING = 1;

    public static final int ACTION_FLYING = 0;
    public static final int ACTION_SHOTDOWN = 1;
    public static final int ACTION_BEYOND_SCREEN = 2;

    private MapEditor mapEditor;
    private Hitbox hitbox;
    private Animation animation;
    private Animation flyingAnimation;
    private Animation burningAnimation;
    private Helicopter.RocketLauncher rocketLauncher;
    private PlatformerScenario platformerScenario;
    private Texture texture;

    private int worldX;
    private int worldY;
    private int dstWidth;
    private int dstHeight;
    private int assetId;
    private int spriteWidth;
    private int spriteHeight;
    private int spritesetSpriteWidth;
    private int spritesetSpriteHeight;
    private int frames;
    private int action;

    private float movingTickTime;
    private float animationTickTime;
    private float movingTick;

    public Helicopter(String assetName, MapEditor mapEditor, int x, int y) {
        worldX = x;
        worldY = y;
        spriteWidth = 173;
        spriteHeight = 97;
        spritesetSpriteWidth = spriteWidth + 2;
        spritesetSpriteHeight = spriteHeight + 2;
        //dst rect size to draw
        dstWidth = spriteWidth - 40;
        dstHeight = spriteHeight - 40;

        frames = FRAMES_FLYING;

        this.mapEditor = mapEditor;
        hitbox = new Hitbox(worldX, worldY, 0, 0, 0, 0);
        setAnimation();
        rocketLauncher = new Helicopter.RocketLauncher();
        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
            rocketLauncher.setRocket(false);
        }
        else{
            rocketLauncher.setRocket(true);
        }
        movingTick = SCALE_TICK;
    }

    public void setAnimation(){
        animation = new Animation2(0, 0, spriteWidth, spriteHeight);
        //forming flying animation:
        flyingAnimation = new ObjAnimation(null, spriteWidth, spriteHeight);
        for(int i = 0; i < 3; i++){
            flyingAnimation.addChild(new Animation2(1 + i * spritesetSpriteWidth, 1 + 0,
                    spriteWidth, spriteHeight));
        }
        //forming burning animation:
        burningAnimation = new ObjAnimation(null, spriteWidth, spriteHeight);
        for(int i = 0; i < 3; i++){
            burningAnimation.addChild(new Animation2(1 + i * spritesetSpriteWidth,
                    1 + 1 * spritesetSpriteHeight, spriteWidth, spriteHeight));
        }
        animation.addChild(flyingAnimation);
        animation.addChild(burningAnimation);
    }


    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {

        Animation currentAnimation = animation.getChildren().get(frames);

        batch.begin();
        batch.draw(texture, MSGame.SCREEN_OFFSET_X + worldX - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                MSGame.SCREEN_OFFSET_Y +
                        GeoHelper.transformCanvasYCoordToGL(
                                worldY - mapEditor.getScrollLevel1Map().getWorldOffsetY(),
                                MSGame.SCREEN_HEIGHT, dstHeight),
                dstWidth, dstHeight,
                currentAnimation.getChildren().get(currentAnimation.getCurrentFrame()).getX(),
                currentAnimation.getChildren().get(currentAnimation.getCurrentFrame()).getY(),
                spriteWidth, spriteHeight,
                false, false);
        batch.end();

        rocketLauncher.draw(batch);
    }

    @Override
    public void drawObject(SpriteBatch batch, int screenX, int screenY) {

    }

    @Override
    public void moving(float deltaTime, MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight) {

        if(rocketLauncher.isShot){
            hit(rocketLauncher.getRocket());
        }

        rocketLauncher.moving(deltaTime, mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                mapEditor.getScrollLevel1Map().getWorldOffsetY(), platformerScenario);

        animate(deltaTime);
        move(deltaTime);
    }

    public void animate(float deltaTime){
        animationTickTime += deltaTime;
        while(animationTickTime > ANIMATION_TICK){
            animationTickTime -= ANIMATION_TICK;

            animation.getChildren().get(frames).animate(0, 3);
        }
    }

    public void move(float deltaTime){
        movingTickTime += deltaTime;
        while(movingTickTime > movingTick){
            movingTickTime -= movingTick;

            if(action == ACTION_FLYING){
                scale();
            }
            else{
                worldX += 1;
                if(isBeyondScreen()){
                    action = ACTION_BEYOND_SCREEN;
                }
            }
        }
    }

    public boolean isBeyondScreen(){
        if((worldX - mapEditor.getScrollLevel1Map().getWorldOffsetX()) > MSGame.SCREEN_WIDTH){
            return true;
        }
        return false;
    }

    public void scale(){
        dstWidth += 1;
        dstHeight += 1;
        if(dstWidth >= spriteWidth){
            if(!rocketLauncher.isShot){
                hitbox.setHitbox(worldX, worldY, 45, 30,65, 40);

                hitbox.setPos(worldX - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                        worldY - mapEditor.getScrollLevel1Map().getWorldOffsetY());
                rocketLauncher.shoot(mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                        mapEditor.getScrollLevel1Map().getWorldOffsetY());
            }
        }
    }

    private void hit(Rocket rocket){
        if(GeoHelper.overlapRectangles(rocket.getScreenX(), rocket.getScreenY(),
                rocket.getWidth(),
                rocket.getHeight(),
                getLeft(), getTop(), getHitboxWidth(), getHitboxHeight())){
            if(rocket.getDirection() != 0){
                rocket.gotIt(0);
                action = ACTION_SHOTDOWN;
                frames = FRAMES_BURNING;
                movingTick = CRASHING_TICK;
            }
        }
    }

    @Override
    public boolean onTouch() {
        return false;
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void collide(Hero hero) {

    }

    @Override
    public Decorator getDecorator() {
        return null;
    }

    @Override
    public void hit(Weapon weapon) {

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
    public int isAction() {
        return action;
    }

    private class RocketLauncher{
        private Rocket rocket;

        private boolean isShot = false;

        public void setRocket(boolean isTesting){
            if(isTesting){
                rocket = new Rocket(null, MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT);
            }
            else{
                rocket = new Rocket("rocket", MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT);
            }
        }

        public void draw(SpriteBatch batch){
            rocket.drawObject(batch, 0, 0, 0, 0);
        }

        public void moving(float deltaTime, int worldOffsetX, int worldOffsetY,
                           PlatformerScenario platformerScenario){
            rocket.moving(deltaTime, worldOffsetX, worldOffsetY, platformerScenario);
        }

        public void shoot(int offsetX, int offsetY){
            rocket.shoot(480 + offsetX, hitbox.getCenterY() + offsetY,
                    offsetX, offsetY,
                    EnemyBullet.DIRECTION_LEFT);
            isShot = true;
        }

        public Rocket getRocket(){
            return rocket;
        }
    }
}
