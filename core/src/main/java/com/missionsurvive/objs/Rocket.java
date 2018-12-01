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

public class Rocket extends EnemyBullet {

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

    private boolean exploded;
    private boolean isDrawing;

    private int spriteWidth, spriteHeight, spritesetSpriteWidth, spritesetSpriteHeight;

    public Rocket(String assetName, int screenWidth, int screenHeight){
        super(assetName, screenWidth, screenHeight);

        if(assetName != null){
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
                batch.draw(explosionTexture, MSGame.SCREEN_OFFSET_X + super.getScreenX(),
                        MSGame.SCREEN_OFFSET_Y +
                                GeoHelper.transformCanvasYCoordToGL(super.getScreenY(), MSGame.SCREEN_HEIGHT, explHeight),
                        animationExplosion.getChildren().get(animationExplosion.getCurrentFrame()).getX(),
                        animationExplosion.getChildren().get(animationExplosion.getCurrentFrame()).getY(),
                        explWidth, explHeight);
                batch.end();
            }
            else{
                batch.begin();
                batch.draw(texture, MSGame.SCREEN_OFFSET_X + super.getScreenX(),
                        MSGame.SCREEN_OFFSET_Y +
                                GeoHelper.transformCanvasYCoordToGL(super.getScreenY(), MSGame.SCREEN_HEIGHT, spriteHeight),
                        animationRocket.getChildren().get(animationRocket.getCurrentFrame()).getX(),
                        animationRocket.getChildren().get(animationRocket.getCurrentFrame()).getY(),
                        spriteWidth, spriteHeight);
                batch.end();
            }
        }
    }

    @Override
    public boolean onTouch() {
        return false;
    }

    public void moving(float deltaTime, int worldOffsetX, int worldOffsetY,
                       PlatformerScenario platformerScenario){
        movingTickTime += deltaTime;

        while(movingTickTime > movingTick) {
            movingTickTime -= movingTick;

            if(!exploded){
                //continuous (infinite) animation:
                animate(animationRocket, 0, 2);

                super.setXY();
                super.setScreenXY(worldOffsetX, worldOffsetY);
                super.goAway(width, height, worldOffsetX, worldOffsetY);

                if(super.getDirection() != 0){ //if our bullet is going somewhere, we  check it for intersections with the objects.
                    platformerScenario.shotPlayer(this);
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

    public boolean shoot(int x, int y,
                         int worldOffsetX, int worldOffsetY, int direction){
        isDrawing = true;
        return super.shoot(x, y, worldOffsetX, worldOffsetY, direction);
    }

    public int getScreenX(){
        return super.getScreenX();
    }

    @Override
    public void gotIt(int direction) {
        super.gotIt(direction);
        exploded = true;
        Sounds.playSound(Sounds.explosion);
    }

    @Override
    public int getDirection() {
        return super.getDirection();
    }

    public int getScreenY(){
        return super.getScreenY();
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}
