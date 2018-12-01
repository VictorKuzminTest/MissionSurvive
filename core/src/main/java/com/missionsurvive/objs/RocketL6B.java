package com.missionsurvive.objs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Animation;
import com.missionsurvive.framework.impl.Animation2;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.utils.Assets;
import com.missionsurvive.utils.Sounds;

public class RocketL6B extends EnemyBullet {

    private Animation animationRocket;
    private Animation animationExplosion;
    private Texture explosionTexture;

    private float movingTickTime = 0;
    private float movingTick = 0.03f;

    private int width;
    private int height;
    private int explWidth;
    private int explHeight;
    private int spritesetExplWidth;
    private int spritesetExplHeight;
    private int explosionAsset;
    private int spriteWidth, spriteHeight, spritesetSpriteWidth, spritesetSpriteHeight;
    private int screenX = 0, screenY = 0, worldX = 0, worldY = 0;
    private int speed;
    private int direction;

    private boolean exploded;
    private boolean isDrawing;

    public RocketL6B(String assetName, int screenWidth, int screenHeight){
        super(assetName, screenWidth, screenHeight);

        if(assetName != null) {
            explosionTexture = Assets.getTextures()[Assets.getWhichTexture("explosion")];
        }

        ...

        setAnimation();
    }

    public void setAnimation(){
        //forming rocket animation:
        animationRocket = new Animation2(0, 0,
                spriteWidth, spriteHeight);
        for (int i = 0; i < 3; i++){
            animationRocket.addChild(new Animation2(1 + i * spritesetSpriteWidth, 1 + 0,
                    spriteWidth, spriteHeight));
        }
        //forming explosion animation:
        animationExplosion = new Animation2(0, 0,
                spriteWidth, spriteHeight);
        for(int i = 0; i < 18; i++){
            animationExplosion.addChild(new Animation2(1 + i * spritesetExplWidth, 1 + 0,
                    explWidth, explHeight));
        }
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {
        if(isDrawing){
            if(exploded){
                batch.begin();
                batch.draw(explosionTexture, MSGame.SCREEN_OFFSET_X + screenX,
                        MSGame.SCREEN_OFFSET_Y +
                                GeoHelper.transformCanvasYCoordToGL(screenY, MSGame.SCREEN_HEIGHT, explHeight),
                        animationExplosion.getChildren().get(animationExplosion.getCurrentFrame()).getX(),
                        animationExplosion.getChildren().get(animationExplosion.getCurrentFrame()).getY(),
                        explWidth, explHeight);
                batch.end();
            }
            else{
                batch.begin();
                batch.draw(texture, MSGame.SCREEN_OFFSET_X + screenX,
                        MSGame.SCREEN_OFFSET_Y +
                                GeoHelper.transformCanvasYCoordToGL(screenY, MSGame.SCREEN_HEIGHT, spriteHeight),
                        animationRocket.getChildren().get(animationRocket.getCurrentFrame()).getX(),
                        animationRocket.getChildren().get(animationRocket.getCurrentFrame()).getY(),
                        spriteWidth, spriteHeight);
                batch.end();
            }
        }
    }

    public void moving(float deltaTime, int worldOffsetX, int worldOffsetY,
                       PlatformerScenario platformerScenario){
        movingTickTime += deltaTime;

        while(movingTickTime > movingTick) {
            movingTickTime -= movingTick;

            if(!exploded){
                //continuous (infinite) animation:
                animate(animationRocket, 0, 2);

                setXY();
                setScreenXY(worldOffsetX, worldOffsetY);
                goAway(width, height, worldOffsetX, worldOffsetY);

                if(direction != 0){ //if our bullet is going somewhere, we  check it for intersections with the objects.
                    platformerScenario.shotPlayer(this);
                    isDrawing = true;
                }
                else{
                    isDrawing = false;
                }
            }
            else {
                //animate one time, then set current anim frame to zero:
                if (isCurrentFrame(animationExplosion, 17)) {
                    isDrawing = false;
                    exploded = false;
                    animationExplosion.setCurrentFrame(0);
                } else {
                    animate(animationExplosion, 0, 17);
                }
            }
        }
    }

    /**
     * Depending on direction changes the x and y coords.
     */
    public void setXY(){
        ...
    }

    /**
     * This method translates world coordinates into screen coordinates. Because our hero has screen
     * coordinates and also when we check if bullet goes off the screen (again screen coords).
     * @param worldOffsetX
     * @param worldOffsetY
     */
    public void setScreenXY(int worldOffsetX, int worldOffsetY){
        screenX = worldX - worldOffsetX;
        screenY = worldY - worldOffsetY;
    }

    /**
     * If bullet doesn't overlap the screen (it goes off the screen), so this bullet is free to shoot again.
     * World coordinates are comparing here: bullets worldX and Y, and screens worldX and Y.
     * @param width
     * @param height
     * @param worldOffsetX
     * @param worldOffsetY
     */
    public void goAway(int width, int height, int worldOffsetX, int worldOffsetY){
        if(direction > 0){
            if(!GeoHelper.overlapRectangles(worldX, worldY, width, height,
                    0 + worldOffsetX,  //left side of the screen
                    0 + worldOffsetY,  //top
                    worldOffsetX + MSGame.SCREEN_WIDTH,  //right
                    worldOffsetY + MSGame.SCREEN_HEIGHT)){ //down
                direction = 0;
                isDrawing = false;
            }
        }
    }

    public boolean shoot(int x, int y,
                         int worldOffsetX, int worldOffsetY, int direction){
        if(this.direction == 0){
            if(!exploded){
                isDrawing = true;
                worldX = x;
                worldY = y;
                setScreenXY(worldOffsetX, worldOffsetY);
                this.direction = direction;
            }
            return true;
        }
        return false;
    }

    /**
     * When bullet gets an aim. It receives a direction to move (usually 0 - NONE).
     * @param direction
     */
    public void gotIt(int direction){
        this.direction = direction;
        exploded = true;
        Sounds.playSound(Sounds.explosion);
    }

    public void animate(Animation animation, int startFrame, int endFrame){
        if(!isCurrentFrame(animation, endFrame)){
            animation.animate(startFrame, endFrame);
        }
        else {
            animation.setCurrentFrame(startFrame);
        }
    }

    public boolean isCurrentFrame(Animation animation, int frame){
        if(animation.getCurrentFrame() == frame){
            return true;
        }
        return false;
    }

    public int getDirection(){
        return direction;
    }

    public int getScreenX(){
        return screenX;
    }

    public int getScreenY(){
        return screenY;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}
