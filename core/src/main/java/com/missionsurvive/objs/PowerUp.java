package com.missionsurvive.objs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.framework.impl.ObjAnimation;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.geom.Hitbox;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.utils.Assets;

/**
 * Created by kuzmin on 01.05.18.
 */

public class PowerUp implements Bot{

    public static final int DIRECTION_EAST = 0;
    public static final int DIRECTION_NORTH = 1;

    public static final int ACTION_FLY = 0, ACTION_EXPLODE = 1, ACTION_POWER_UP = 2,
            ACTION_PICKED_UP = 3;
    public static final int POWER_LIFE = 0, POWER_GUN = 1;
    public static final int SPRITES_CUADCOPTER = 0, SPRITES_EXPLOSION = 1,
            SPRITES_LIFE = 2, SPRITES_GUN = 3;

    public static final float COLOR_STEP = 0.03f;

    private MapEditor mapEditor;
    private ObjAnimation animation;
    private Hitbox hitbox;
    private MapTer southTer;
    private PlatformerScenario platformerScenario;
    private Texture texture;

    private int screenX, screenY;
    private int spritesetSpriteWidth, spritesetSpriteHeight, spriteWidth, spriteHeight;
    private int flyingSpeed = 3, fallingSpeed = 5;
    private int hits;
    private int isAction;
    private int numExplosionFrames = 18;
    private int powerupId = -1;
    private int direction;

    private float animationTickTime = 0, animationTick = 0.08f;
    private float movingTickTime = 0, movingTick = 0.03f;
    private float removingTickTime = 0, removingTick = 2.0f;
    private float colorGreenBlue;

    private boolean colorUp;
    private boolean isGun;

    public PowerUp(String assetName, MapEditor mapEditor, int x, int y, int powerupId, int direction){
        this.screenX = x;
        this.screenY = y;
        this.powerupId = powerupId;
        this.direction = direction;

        hitbox = new Hitbox(x, y, 64, 26, 0, 0);
        this.mapEditor = mapEditor;
        hitbox.setPos(x - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                y - mapEditor.getScrollLevel1Map().getWorldOffsetY());

        int[] spritesRows = new int[4];
        spritesRows[SPRITES_CUADCOPTER] = 2;
        spritesRows[SPRITES_EXPLOSION] = numExplosionFrames;
        spritesRows[SPRITES_LIFE] = 3;
        spritesRows[SPRITES_GUN] = 1;
        animation = new ObjAnimation(spritesRows, spriteWidth, spriteHeight);
        setSprites(assetName, SPRITES_CUADCOPTER, 64, 26);
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {
        if(isAction != ACTION_PICKED_UP) {
            if(isGun){
                batch.begin();
                Color color = batch.getColor();
                batch.setColor(color.r, colorGreenBlue, colorGreenBlue, color.a);
                drawGun(batch);
                batch.setColor(color.r, color.g, color.b, color.a);
                batch.end();
            }
            else{
                batch.begin();
                draw(batch);
                batch.end();
            }
        }
    }

    private void draw(SpriteBatch batch){
        batch.draw(texture, MSGame.SCREEN_OFFSET_X + screenX - mapEditor.getScrollLevel1Map().getWorldOffsetX() ,
                MSGame.SCREEN_OFFSET_Y +
                        GeoHelper.transformCanvasYCoordToGL(screenY - mapEditor.getScrollLevel1Map().getWorldOffsetY(),
                                MSGame.SCREEN_HEIGHT, spriteHeight),
                1 + animation.getCurrentFrame() * spritesetSpriteWidth,
                1,
                spriteWidth, spriteHeight);
    }

    private void drawGun(SpriteBatch batch){
        batch.draw(texture, MSGame.SCREEN_OFFSET_X + screenX - mapEditor.getScrollLevel1Map().getWorldOffsetX() ,
                MSGame.SCREEN_OFFSET_Y +
                        GeoHelper.transformCanvasYCoordToGL(screenY - mapEditor.getScrollLevel1Map().getWorldOffsetY(),
                                MSGame.SCREEN_HEIGHT, spriteHeight),
                1 + animation.getCurrentFrame() * spritesetSpriteWidth,
                1 + POWER_GUN * spritesetSpriteHeight,
                spriteWidth, spriteHeight);
    }

    @Override
    public void drawObject(SpriteBatch batch, int screenX, int screenY) {

    }

    @Override
    public boolean onTouch() {
        if(isAction == ACTION_FLY){
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public Decorator getDecorator() {
        return null;
    }

    @Override
    public int isAction() {
        return 0;
    }

    @Override
    public void moving(float deltaTime, MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight) {
        move(mapTer, mapEditor, worldWidth, worldHeight, deltaTime);
        animate(mapTer, mapEditor, worldWidth, worldHeight, deltaTime);
    }

    public void animate(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight, float deltaTime){
        animationTickTime += deltaTime;

        switch(isAction){
            case ACTION_FLY:
                animateFlying();
                break;
            case ACTION_EXPLODE:
                animateExploding();
                break;
            case ACTION_POWER_UP:
                animatePowerUp();
                break;
        }
    }

    public void move(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight, float deltaTime){
        switch(isAction){
            case ACTION_FLY:
                movingTickTime += deltaTime;
                flying();
                break;
            case ACTION_POWER_UP:
                movingTickTime += deltaTime;
                falling(mapTer, mapEditor, worldWidth, worldHeight);
                break;
            case ACTION_PICKED_UP:
                removingTickTime += deltaTime;
                removePowerUp();
                break;
        }
    }

    /**
     * We need to remove power after some time passes (removingTickTime).
     * Because if we remove power up at once when we have just taken it,
     * in PlatformerScenario.collideBot() we get ArrayOutOfBoundsException (means: we remove this bot,
     * but continue cycle with already "given" size).
     */
    public void removePowerUp(){
        while(removingTickTime > removingTick){
            removingTickTime = 0;

            platformerScenario.removeBot(this, 0);
        }
    }



    @Override
    public void collide(Hero hero) {
        if(isAction != ACTION_PICKED_UP && isAction != ACTION_FLY){
            switch(powerupId){
                case POWER_LIFE:
                    hero.addLife();
                    isAction = ACTION_PICKED_UP;
                    break;
                case POWER_GUN:
                    hero.addGun(Hero.FROM_POWERUP);
                    isAction = ACTION_PICKED_UP;
                    break;
            }
        }
    }

    public void falling(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){
        while(movingTickTime > movingTick) {
            movingTickTime -= movingTick;

            hitbox.setPos(screenX - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                    screenY - mapEditor.getScrollLevel1Map().getWorldOffsetY());
            tilemapCollision(mapTer, mapEditor, worldWidth, worldHeight);
            if(southTer == null){
                screenY += fallingSpeed;
            }
        }
    }

    public void flying(){
        while(movingTickTime > movingTick) {
            movingTickTime -= movingTick;

            if(direction == DIRECTION_NORTH){
                screenY -= flyingSpeed;
            }
            else{
                screenX += flyingSpeed;
            }
            hitbox.setPos(screenX - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                    screenY - mapEditor.getScrollLevel1Map().getWorldOffsetY());
        }
    }

    public void animateExploding(){
        while(animationTickTime > animationTick){
            animationTickTime -= animationTick;

            if(animation.getCurrentFrame() == (numExplosionFrames - 1)){
                isAction = ACTION_POWER_UP;
                if(powerupId == POWER_LIFE){
                    setSprites("powerup", SPRITES_LIFE, 32, 32);
                }
                else{
                    setSprites("powerup", SPRITES_GUN, 32, 32);
                }
                hitbox = new Hitbox(screenX - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                        screenY - mapEditor.getScrollLevel1Map().getWorldOffsetY(),
                        32, 32, 0, 0);
                return;
            }
            animation.nextFrame();
        }
    }

    public void animatePowerUp(){
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            switch (powerupId){
                case POWER_LIFE:
                    animation.animateBackAndForth(0, 3, 1);
                    break;
                case POWER_GUN:
                    setColorFilter();
                    break;
            }
        }
    }


    /**
     * set color filter for power up could change its color.
     */
    private void setColorFilter(){
        if(colorUp){
            colorGreenBlue = GeoHelper.checkFloat(
                    colorGreenBlue + COLOR_STEP, 1.0f);
            if(colorGreenBlue >= 1.0){
                colorGreenBlue = 1.0f;
                colorUp = false;
            }
        }
        else {
            colorGreenBlue = GeoHelper.checkFloat(
                    colorGreenBlue - COLOR_STEP, 0.5f);
            if(colorGreenBlue <= 0.5f){
                colorUp = true;
            }
        }
    }

    public void animateFlying(){
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            animation.nextFrame();
        }
    }

    @Override
    public void hit(Weapon weapon) {
        hits--;
        if(hits < 0){
            explode(weapon);
            spawnPowerUp();
        }
    }

    public void explode(Weapon weapon){
        if(isAction == ACTION_FLY){
            isAction = ACTION_EXPLODE;
            setSprites("explosion", SPRITES_EXPLOSION, 45, 36);
            weapon.hit(true);
        }
    }

    public void setSprites(String assetName, int setOfFrames, int spriteWidth, int spriteHeight){
        animation.setSetOfFrames(setOfFrames);
        animation.setCurrentFrame(0);
        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        spritesetSpriteWidth = spriteWidth + 2;
        spritesetSpriteHeight = spriteHeight + 2;
    }

    public void spawnPowerUp(){
        if(powerupId == POWER_GUN){
            isGun = true;
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
        return screenX - mapEditor.getScrollLevel1Map().getWorldOffsetX();
    }

    @Override
    public int getY() {
        return screenY - mapEditor.getScrollLevel1Map().getWorldOffsetY();
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

    public void tilemapCollision(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){

        int tileWidth = 16;
        int tileHeight = 16;

        int centerCol = ((hitbox.getCenterX()) + mapEditor.getScrollLevel1Map().getWorldOffsetX()) /
                (tileWidth - 1);

        int bottomRow = ((hitbox.getBottom()) + mapEditor.getScrollLevel1Map().getWorldOffsetY()) /
                (tileHeight - 1);
        if(bottomRow < 0) bottomRow = 0;
        if(bottomRow >= worldHeight) bottomRow = worldHeight - 1;

        if(mapTer[bottomRow][centerCol].isBlocked()){  //south
            southTer = mapTer[bottomRow][centerCol];
        }
        else{
            southTer = null;
        }
    }
}
