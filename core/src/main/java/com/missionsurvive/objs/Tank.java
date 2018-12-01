package com.missionsurvive.objs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Animation;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.framework.impl.Animation2;
import com.missionsurvive.framework.impl.ObjAnimation;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.utils.Assets;

public class Tank implements Bot{

    public static final int FRAMES_MOVING = 0;

    public static final int ACTION_AWAY = 1;

    public static final float ANIMATION_TICK = 0.04f;
    public static final float SCALE_TICK = 0.1f;
    public static final float CORRECTION_TICK = 0.1f;

    private Animation animation;
    private Animation movingAnimation;
    private Texture texture;
    private MapEditor mapEditor;

    private float worldX;
    private float worldY;
    private int assetStartX;
    private int spriteWidth;
    private int spriteHeight;
    private int spritesetSpriteWidth;
    private int spritesetSpriteHeight;
    private int action;

    private float dstWidth;
    private float dstHeight;
    private float scaleIncrementX;
    private float scaleIncrementY;
    private float correctionX = 0.55f;
    private float animationTickTime;
    private float movingTickTime;
    private float correctionTickTime;

    public Tank(String assetName, MapEditor mapEditor, int x, int y){
        ...
        this.mapEditor = mapEditor;

        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }
        setAnimation();
    }

    public void setAnimation(){
        animation = new Animation2(0, 0, spriteWidth, spriteHeight);
        //forming moving animation:
        movingAnimation = new ObjAnimation(null, spriteWidth, spriteHeight);
        for(int i = 0; i < 3; i++){
            movingAnimation.addChild(new Animation2(1 + i * spritesetSpriteWidth, assetStartX,
                    spriteWidth, spriteHeight));
        }
        animation.addChild(movingAnimation);
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row,
                           int offsetX, int offsetY) {
        if(action != ACTION_AWAY) {
            Animation currentAnimation = animation.getChildren().get(FRAMES_MOVING);

            batch.begin();
            batch.draw(texture, MSGame.SCREEN_OFFSET_X + worldX - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                    MSGame.SCREEN_OFFSET_Y +
                            GeoHelper.transformCanvasYCoordToGL(
                                    (int)worldY - mapEditor.getScrollLevel1Map().getWorldOffsetY(),
                                    MSGame.SCREEN_HEIGHT, (int)dstHeight),
                    dstWidth, dstHeight,
                    currentAnimation.getChildren().get(currentAnimation.getCurrentFrame()).getX(),
                    currentAnimation.getChildren().get(currentAnimation.getCurrentFrame()).getY(),
                    spriteWidth, spriteHeight,
                    false, false);
            batch.end();
        }
    }

    @Override
    public void drawObject(SpriteBatch batch, int screenX, int screenY) {

    }

    @Override
    public void moving(float deltaTime, MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight) {
        if(action != ACTION_AWAY){
            setAction();
            animate(deltaTime);
            move(deltaTime);
        }
    }

    public void animate(float deltaTime){
        animationTickTime += deltaTime;
        while(animationTickTime > ANIMATION_TICK){
            animationTickTime -= ANIMATION_TICK;

            animation.getChildren().get(FRAMES_MOVING).animate(0, 3);
        }
    }

    public void move(float deltaTime){
        movingTickTime += deltaTime;
        while(movingTickTime > SCALE_TICK){
            movingTickTime -= SCALE_TICK;

            dstWidth -= scaleIncrementX;
            dstHeight -= scaleIncrementY;
        }

        correctionTickTime += deltaTime;
        while(correctionTickTime > CORRECTION_TICK){
            correctionTickTime -= CORRECTION_TICK;

            worldX += correctionX;
        }
    }

    public void setAction(){
        if(dstWidth < 2){
            action = ACTION_AWAY;
        }
    }

    @Override
    public void collide(Hero hero) {

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
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getSpriteWidth() {
        return 0;
    }

    @Override
    public int getSpriteHeight() {
        return 0;
    }

    @Override
    public int getLeft() {
        return 0;
    }

    @Override
    public int getRight() {
        return 0;
    }

    @Override
    public int getTop() {
        return 0;
    }

    @Override
    public int getBottom() {
        return 0;
    }

    @Override
    public int getHitboxWidth() {
        return 0;
    }

    @Override
    public int getHitboxHeight() {
        return 0;
    }

    @Override
    public void setScenario(Scenario platformerScenario) {

    }

    @Override
    public int isAction() {
        return action;
    }

    @Override
    public boolean onTouch() {
        return false;
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public Decorator getDecorator() {
        return null;
    }
}
