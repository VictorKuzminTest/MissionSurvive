package com.missionsurvive.objs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.framework.impl.BlinkingDecorator;
import com.missionsurvive.framework.impl.ObjAnimation;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.geom.Hitbox;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.utils.Assets;

public class Wreckage implements Bot {

    public static final int ACTION_START = 0;
    public static final int ACTION_FALLING = 1;
    public static final int ACTION_ENCOUNTERED = 2;

    public static final float MOVING_TICK = 0.03f;
    public static final float BLINKING_TICK = 0.8f;
    public static final float ENCOUNTER_TICK = 0.08f;

    private Hitbox hitbox;
    private ObjAnimation animation;
    private MapEditor mapEditor;
    private Decorator decorator;
    private Texture texture;

    private int x;
    private int y;
    private int spriteWidth;
    private int spriteHeight;
    private int spritesetSpriteWidth;
    private int spritesetSpriteHeight;
    private int speedFalling = 9;
    private int tileSize = 16;
    private int action;
    private int encounterFrame = 0;
    private int numEncounterFrames = 5;
    private int initialY;
    private int onTheGroundY;
    private int hitboxWidth = 25;
    private int hitboxHeight = 35;

    private float movingTickTime = 0;
    private float blinkingTickTime = 0;
    private float encounterTickTime = 0;

    private boolean onTheGround = false;

    public Wreckage(){}

    public Wreckage(String assetName, MapEditor mapEditor, int x, int y){
        hitbox = new Hitbox(x, y, hitboxWidth, hitboxHeight, 20, 9);
        this.mapEditor = mapEditor;

        int[] actions = new int[1];
        actions[0] = numEncounterFrames;
        newAnimation(actions);

        decorator = new BlinkingDecorator(this);
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {
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
    public void drawObject(SpriteBatch batch, int screenX, int screenY) {

        if(onTheGround){
            batch.begin();
            batch.draw(texture, MSGame.SCREEN_OFFSET_X + x - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                    MSGame.SCREEN_OFFSET_Y +
                            GeoHelper.transformCanvasYCoordToGL(onTheGroundY - mapEditor.getScrollLevel1Map().getWorldOffsetY(),
                                    MSGame.SCREEN_HEIGHT, spriteHeight),
                    1 + (numEncounterFrames - 1) * spritesetSpriteWidth,
                    1 + 0,
                    spriteWidth, spriteHeight);
            batch.end();
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
    public void moving(float deltaTime, MapTer[][] map, MapEditor mapEditor, int worldWidth, int worldHeight) {

        switch (action){
            case ACTION_START:
                updateBlinking(deltaTime);
                decorator.update(deltaTime);
                break;
            case ACTION_FALLING:
                falling(map, mapEditor, tileSize, deltaTime);
                break;
            case ACTION_ENCOUNTERED:
                encountered(deltaTime);
                break;
        }
    }

    @Override
    public void collide(Hero hero) {
        if(action == ACTION_FALLING){
            hero.die();
        }
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
        return x - mapEditor.getScrollLevel1Map().getWorldOffsetX();
    }

    @Override
    public int getY() {
        return y - mapEditor.getScrollLevel1Map().getWorldOffsetY();
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

    public Hitbox getHitbox(){
        return hitbox;
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

    }

    public void falling(MapTer[][] map, MapEditor mapEditor, int tileSize, float deltaTime) {
        movingTickTime += deltaTime;

        while(movingTickTime > MOVING_TICK) {
            movingTickTime -= MOVING_TICK;

            y += speedFalling;
            hitbox.setPos(x - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                    y - mapEditor.getScrollLevel1Map().getWorldOffsetY());
            int row = GeoHelper.checkRowCol(hitbox.getBottom() / (tileSize - 1), map.length);
            int col = GeoHelper.checkRowCol(hitbox.getCenterX() / (tileSize - 1), map[0].length);
            if(map[row][col].isBlocked()){
                setActionAndAnimationFrames(ACTION_ENCOUNTERED);
                movingTickTime = 0;
            }
        }
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setHitbox(Hitbox hitbox) {
        this.hitbox = hitbox;
    }

    public int getSpeedFalling() {
        return speedFalling;
    }

    public void setAction(int action) {
        this.action = action;
    }

    @Override
    public int isAction() {
        return action;
    }

    public void updateBlinking(float deltaTime) {
        blinkingTickTime += deltaTime;
        while(blinkingTickTime > BLINKING_TICK){
            blinkingTickTime = 0;
            hitbox.setPos(x - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                    y - mapEditor.getScrollLevel1Map().getWorldOffsetY());
            setActionAndAnimationFrames(ACTION_FALLING);
        }
    }

    @Override
    public Decorator getDecorator(){
        return decorator;
    }

    public void setNumEncounterFrames(int numEncounteredFrames){
        this.numEncounterFrames = numEncounteredFrames;
    }

    public void encountered(float deltaTime) {
        encounterTickTime += deltaTime;

        while(encounterTickTime > ENCOUNTER_TICK){
            encounterTickTime -= ENCOUNTER_TICK;

            if(encounterFrame >= (numEncounterFrames - 1)){
                if(!onTheGround){
                    onTheGroundY = y;
                    onTheGround = true;
                }
                setActionAndAnimationFrames(ACTION_START);
                encounterFrame = 0;
                y = initialY;
                encounterTickTime = 0;
                hitbox.setPos(x - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                        y - mapEditor.getScrollLevel1Map().getWorldOffsetY());
                break;
            }
            hitbox.setPos(x - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                    y - mapEditor.getScrollLevel1Map().getWorldOffsetY());
            encounterFrame ++;
            animation.nextFrame();
        }
    }

    public void newAnimation(int[] actions) {
        animation = new ObjAnimation(actions, spriteWidth, spriteHeight);
    }

    public void setMapEditor(MapEditor mapEditor) {
        this.mapEditor = mapEditor;
    }

    public void setActionAndAnimationFrames(int action){
        if(this.action != action){
            this.action = action;
            animation.setCurrentFrame(0);
        }
    }

    public ObjAnimation getAnimation() {
        return animation;
    }
}
