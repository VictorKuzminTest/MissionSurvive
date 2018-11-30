package com.missionsurvive.objs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.impl.ObjAnimation;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.geom.Hitbox;
import com.missionsurvive.utils.Assets;
import com.missionsurvive.utils.Sounds;

public class RocketL4 extends Obstacle {

    private Hitbox hitbox;
    private Texture rocketTexture;
    private Texture explosionTexture;

    private int rocketHeight;
    private boolean isPlaced, isExploded, isHitGround;
    private int screenX, screenY, screenWidth;
    private float movingTickTime = 0, movingTick = 0.005f;
    private float animationTickTime = 0, animationTick = 0.03f;
    private int spriteWidth, spriteHeight;
    private int spritesetSpriteWidth, spritesetSpriteHeight;
    private Shadow shadow;
    private int[] spritesRows, explosionRows;
    private ObjAnimation animationRocket, animationExplosion, animationShadow;

    public RocketL4(Hitbox hitbox, String assetName, int screenX, int screenY,
                    int spriteWidth, int spriteHeight){

        super(hitbox, assetName, screenX, screenY, spriteWidth, spriteHeight);
        super.setObstacleId(ROCKET);

        screenWidth = MSGame.SCREEN_WIDTH;
        rocketHeight = MSGame.SCREEN_HEIGHT;
        if(assetName != null){
            rocketTexture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
            explosionTexture = Assets.getTextures()[Assets.getWhichTexture("explosion")];
        }
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        spritesetSpriteWidth = spriteWidth + 2;
        spritesetSpriteHeight = spriteHeight + 2;

        this.hitbox = hitbox;
        shadow = new Shadow(screenX, screenY + rocketHeight);

        spritesRows = new int[1]; //we have only one row.
        spritesRows[0] = 3;
        animationRocket = new ObjAnimation(spritesRows, spriteWidth, spriteHeight);
        animationRocket.setSetOfFrames(0);

        explosionRows = new int[1]; //we have only one row.
        explosionRows[0] = 18;
        animationExplosion = new ObjAnimation(explosionRows, spriteWidth, spriteHeight);
        animationExplosion.setSetOfFrames(0);

        int[] shadowRows = new int[1];
        shadowRows[0] = 1;
        animationShadow = new ObjAnimation(shadowRows, shadow.spriteWidth, shadow.spriteHeight);
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {
        if(!isExploded) {
            //draw rocket:
            batch.begin();
            batch.draw(rocketTexture, MSGame.SCREEN_OFFSET_X + screenX,
                    MSGame.SCREEN_OFFSET_Y +
                            GeoHelper.transformCanvasYCoordToGL(screenY, MSGame.SCREEN_HEIGHT, spriteHeight),
                    1 + animationRocket.getCurrentFrame() * spritesetSpriteWidth,
                    15,
                    spriteWidth, spriteHeight);
            batch.end();

            //draw shadow:
            batch.begin();
            batch.setColor(1f, 1f, 1f, 0.5f);
            batch.draw(rocketTexture, MSGame.SCREEN_OFFSET_X + shadow.screenX,
                    MSGame.SCREEN_OFFSET_Y +
                            GeoHelper.transformCanvasYCoordToGL(shadow.screenY, MSGame.SCREEN_HEIGHT, shadow.spriteHeight),
                    1,
                    shadow.assetStartY,
                    shadow.spriteWidth, shadow.spriteHeight);
            batch.setColor(1f, 1f, 1f, 1f);
            batch.end();
        }
        else{
            batch.begin();
            batch.draw(explosionTexture, MSGame.SCREEN_OFFSET_X + screenX,
                    MSGame.SCREEN_OFFSET_Y +
                            GeoHelper.transformCanvasYCoordToGL(screenY, MSGame.SCREEN_HEIGHT, spriteHeight),
                    1 + animationExplosion.getCurrentFrame() * spritesetSpriteWidth,
                    1,
                    spriteWidth, spriteHeight);
            batch.end();
        }
    }

    @Override
    public void drawObject(SpriteBatch batch, int screenX, int screenY) {

    }

    @Override
    public boolean onTouch() {
        return false;
    }

    public void moving(float deltaTime){
        if(isPlaced){
            movingTickTime += deltaTime;

            while(movingTickTime > movingTick) {
                movingTickTime -= movingTick;

                move();
                hitbox.setPos(screenX, screenY);
                isOffTheScreen();
            }
            animate(deltaTime);
        }
    }

    public void move(){
        if(isPlaced){
            if(!isExploded){
                screenX += 2;
                screenY += 2;

                shadow.screenX = screenX;

                if(GeoHelper.overlapRectangles(hitbox.getLeft(), hitbox.getTop(), hitbox.getHitboxWidth(),
                        hitbox.getHitboxHeight(), shadow.screenX, shadow.screenY, shadow.spriteWidth,
                        shadow.spriteHeight)){

                    isExploded = true;
                    isHitGround = true;
                    Sounds.playSound(Sounds.explosion);
                    animationExplosion.setCurrentFrame(0);
                }
            }
            else{
                screenX -= 3;
            }
        }
    }

    /**
     * Check if rocket goes off the screen.
     */
    public void isOffTheScreen(){
        if(GeoHelper.inBoundsSpaceX(screenX + screenWidth, 0, screenWidth) == GeoHelper.TO_THE_LEFT){
            isPlaced = false;
            isExploded = false;
            isHitGround = false;
        }
    }

    public void animate(float deltaTime){
        animationTickTime += deltaTime;
        while(animationTickTime > animationTick){
            animationTickTime -= animationTick;

            if(isExploded){
                if(animationExplosion.getCurrentFrame() > 2){
                    isHitGround = false;
                }
                animationExplosion.nextFrame();
            }
            else animationRocket.nextFrame();
        }
    }

    /**
     * Sets screenX and screenY depending on targetXY coords (Hero x y).
     * @param screenX
     * @param screenY
     */
    public void placeObstacle(int screenX, int screenY){
        isPlaced = true;
        this.screenX = screenX - rocketHeight;
        this.screenY = screenY - rocketHeight;
        setShadow(this.screenX, this.screenY + rocketHeight);

        /*Sounds.rocket.play(1);*/
    }

    public boolean isPlaced(){
        return isPlaced;
    }

    /**
     * we return isHitGround in order moto couldn't smash into fire or smoke.
     * @return
     */
    public boolean isExploaded(){
        return isHitGround;
    }


    public void setShadow(int screenX, int screenY){
        shadow.screenX = screenX;
        shadow.screenY = screenY;
    }

    private class Shadow{

        private int assetStartY = 53, spriteWidth = 45, spriteHeight = 20;
        private int screenX, screenY;

        public Shadow(int screenX, int screenY){
            this.screenX = screenX;
            this.screenY = screenY;
        }
    }
}
