package com.missionsurvive.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.TouchControl;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.map.Map;
import com.missionsurvive.map.ParallaxBackground;
import com.missionsurvive.map.ParallaxCamera;
import com.missionsurvive.map.ParallaxLayer;
import com.missionsurvive.map.ScrollMap;
import com.missionsurvive.map.ScrollerMap;
import com.missionsurvive.objs.Obstacle;
import com.missionsurvive.objs.actors.Moto;
import com.missionsurvive.scenarios.PlayScript;
import com.missionsurvive.scenarios.controlscenarios.ControlScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.controlscenarios.GameCS;
import com.missionsurvive.scenarios.ScrollerScenario;
import com.missionsurvive.utils.Assets;

import java.util.ArrayList;

/**
 * Created by kuzmin on 04.05.18.
 */

public class ScrollerScreen extends GameScreen implements Screen {

    private MSGame game;
    private Texture bgTexture;
    private ParallaxCamera gameCam;
    private Viewport gamePort;
    private ParallaxBackground background;
    private Texture texture;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Moto moto;
    private TouchControl touchControl;
    private Map map;
    private ControlScenario controlScenario;
    private ScrollerScenario scrollerScenario;
    private PlayScript playScript;

    private int noStartCol = -1, noEndCol = -1;
    private int screenWidth;

    private float screenTranparency = 0;
    private float darkenTickTime = 0, darkenTick = 0.1f;
    private float blinkTickTime, blinkTick = 0.1f; //floats for obstacle blink.
    private float scaleX, scaleY;

    private boolean blink;
    private boolean onPause;

    public ScrollerScreen(MSGame game, PlayScript playScript) {
        this.game = game;
        this.playScript = playScript;

        scaleX = (float) MSGame.SCREEN_WIDTH / Gdx.graphics.getBackBufferWidth();
        scaleY = (float)MSGame.SCREEN_HEIGHT / Gdx.graphics.getBackBufferHeight();

        scrollerScenario = new ScrollerScenario(this, this.playScript,
                500, 220);
        map = new ScrollerMap();
        touchControl = new TouchControl(scaleX, scaleY);
        controlScenario = new GameCS();
        scrollerScenario.setControlScenario(controlScenario);

        bgTexture = Assets.getTextures()[Assets.getWhichTexture("ocean")];
        texture = Assets.getTextures()[Assets.getWhichTexture("bridge")];

        screenWidth = MSGame.SCREEN_WIDTH;

        gameCam = new ParallaxCamera(480, 320); //extends OrthographicCamera
        gamePort = new StretchViewport(480, 320, gameCam);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        bgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion bg = new TextureRegion(bgTexture, 0, 0, 480, 320);
        background = new ParallaxBackground(new ParallaxLayer[]{
                new ParallaxLayer(bg, new Vector2(1, 1), new Vector2(0, 0)),
        }, 480, 320, new Vector2(50, 0));

        this.playScript.setScreen(this, "gameControls");
        putPlayer(100, 150);
    }

    public void update(float deltaTime) {
        controlScenario.onTouchPanels(deltaTime, scaleX, scaleY);
        if(!onPause){
            scrollerScenario.update(map, touchControl, deltaTime);
        }
    }

    /**
     * the method is used only to set the blinking of an obstacle.
     * @param deltaTime
     */
    public void updateObstacleBlinking(float deltaTime){
        blinkTickTime += deltaTime;
        while(blinkTickTime > blinkTick) {
            blinkTickTime -= blinkTick;

            if(blink) blink = false;
            else blink = true;
        }
    }

    private void drawWorld(float delta){
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(!onPause){
            game.getSpriteBatch().enableBlending();
            game.getSpriteBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            background.render(delta);
        }

        drawRoundedTileset(game.getSpriteBatch(), map.getScrollMap());

        if(moto != null){
            drawTear(scrollerScenario.getTear(), game.getSpriteBatch());
            drawObstacles(scrollerScenario.getObstacles(), game.getSpriteBatch());

            if(moto.isDead()){
                if(moto.getLives() < -1){ //lost continue
                    drawDeathScreen(1);
                }
                else{ //just lost life
                   drawDeathScreen(0);
                }
            }
        }
    }

    /**
     * Filling screen with color and transparency.
     * @param colorRed
     */
    public void drawDeathScreen(float colorRed){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(colorRed, 0, 0, screenTranparency);
        shapeRenderer.rect(0, 0,
                Gdx.graphics.getBackBufferWidth(),
                Gdx.graphics.getBackBufferHeight());
        shapeRenderer.end();
    }

    /**
     * draw obstacles. Draws moto in front of behind osbstacles.
     * @param obstacles
     */
    public void drawObstacles(ArrayList<Obstacle> obstacles, SpriteBatch batch){
        boolean isMotoDrawn = false;

        for(Obstacle obstacle : obstacles) {
            if(obstacle.isPlaced()){
                if(screenWidth < obstacle.getScreenX()){ //if obstacle is beyond the screen.
                    if(blink) { //we draw blinking obstacle on the right side of the screen.
                        obstacle.drawObject(batch, screenWidth - obstacle.getSpriteWidth(), 0);
                    }
                }
                else{ //if obstacle is on the screen
                    if(obstacle.getScreenY() > (moto.getScreenY() + 30)){
                        //then draw moto and set isMotoDrawn to true.
                        // + 30 because sprite height of moto is greater then car's about
                        // 30 pixels (90 - 64 = 26).
                        if(!isMotoDrawn){
                            moto.drawObject(batch, 0, 0, 0, 0);
                            isMotoDrawn = true;
							/*Hitbox motoHitbox = moto.getHitbox();
							g.drawRect(motoHitbox.getLeft(), motoHitbox.getTop(), motoHitbox.getHitboxWidth(),
							motoHitbox.getHitboxHeight(), Color.BLACK, null);*/
                        }
                        obstacle.drawObject(batch, 0, 0, 0, 0);
						/*Hitbox obstacleHitbox = obstacle.getHitbox();
						g.drawRect(obstacleHitbox.getLeft(), obstacleHitbox.getTop(), obstacleHitbox.getHitboxWidth(),
							obstacleHitbox.getHitboxHeight(), Color.MAGENTA, null);*/
                    }
                    else{
                        obstacle.drawObject(batch, 0, 0, 0, 0);
						/*Hitbox obstacleHitbox = obstacle.getHitbox();
						g.drawRect(obstacleHitbox.getLeft(), obstacleHitbox.getTop(), obstacleHitbox.getHitboxWidth(),
							obstacleHitbox.getHitboxHeight(), Color.MAGENTA, null);*/
                    }
                }
            }
        }
        if(!isMotoDrawn){
            moto.drawObject(batch, 0, 0, 0, 0);
			/*Hitbox motoHitbox = moto.getHitbox();
			g.drawRect(motoHitbox.getLeft(), motoHitbox.getTop(), motoHitbox.getHitboxWidth(),
					motoHitbox.getHitboxHeight(),
					Color.BLACK, null);*/
        }
    }

    /**
     * draw tear. No moto is considered.
     * @param obstacles
     */
    public void drawTear(ArrayList<Obstacle> obstacles, SpriteBatch batch){
        for(Obstacle obstacle : obstacles) {
            if(obstacle.isPlaced()){
                if(screenWidth < obstacle.getScreenX()){ //if obstacle is beyond the screen.
                    if(blink) { //we draw blinking obstacle on the right side of the screen.
                        obstacle.drawObject(batch, screenWidth - obstacle.getSpriteWidth(), 0);
                    }
                }
                obstacle.drawObject(batch, 0, 0, 0, 0);
            }
			/*Hitbox obstacleHitbox = obstacle.getHitbox();
			g.drawRect(obstacleHitbox.getLeft(), obstacleHitbox.getTop(), obstacleHitbox.getHitboxWidth(),
							obstacleHitbox.getHitboxHeight(), Color.MAGENTA, null);*/
        }
    }

    public void drawRoundedTileset(SpriteBatch batch, ScrollMap scrollMap){
        int startColOffset = scrollMap.getStartColOffset(); //Колонка, с которой начинается отрисовка тайлов.
        int endColOffset = scrollMap.getEndColOffset();  //Колонка, которой заканчивается отрисовка тайлов.

        for(int row = 0; row < map.getLevel1Ter().length; row++){

            for(int col = startColOffset; col < endColOffset; col++){

                //we check if we can or cannot draw row (col) when tiles are teared:
                if(col >= noStartCol && col <= noEndCol){
                    continue;
                }

                int colToDraw = col;
                if(col >= map.getLevel1Ter()[0].length) colToDraw = col % (map.getLevel1Ter()[0].length - 1);

                batch.begin();
                batch.draw(texture, ((MSGame.SCREEN_OFFSET_X + col * 73) - scrollMap.getWorldOffsetX()),
                        MSGame.SCREEN_OFFSET_Y +
                                GeoHelper.transformCanvasYCoordToGL(row * 128 - scrollMap.getWorldOffsetY(),
                                        MSGame.SCREEN_HEIGHT,
                                        128),
                        map.getLevel1Ter()[row][colToDraw].getSrcX(),
                        map.getLevel1Ter()[row][colToDraw].getSrcY(),
                        73, 128);
                batch.end();
            }
        }
    }

    private void drawInterfaces(){
        controlScenario.drawPanels(game.getSpriteBatch());
    }

    @Override
    public void putPlayer(int x, int y){
        scrollerScenario.placeObject(x, y);
        moto = scrollerScenario.getMoto();
    }

    public boolean onPause(){
        return onPause;
    }

    public void setPause(boolean onPause){
        /*if(isHeroControl()){
            Sounds.theme4.stop();
        }
        else{
            Sounds.theme4.setLooping(true);
            Sounds.theme4.setVolume(1);
            Sounds.theme4.play();
        }*/
        this.onPause = onPause;
    }

    public Map getMap(){
        return map;
    }

    public void setNoStartCol(int noStartCol){
        this.noStartCol = noStartCol;
    }

    public void setNoEndCol(int noEndCol){
        this.noEndCol = noEndCol;
    }

    /**
     * Darken screen when hero is dead.
     * @param deltaTime
     */
    public void darkenScreen(float deltaTime){
        darkenTickTime += deltaTime;
        while(darkenTickTime > darkenTick){
            darkenTickTime -= darkenTick;

            screenTranparency += 0.04f;

            if(screenTranparency >= 1){
                screenTranparency = 0;

                scrollerScenario.newAttempt();

                noStartCol = -1;
                noEndCol = -1;
            }
        }
    }

    @Override
    public void dispose() {
        /*Sounds.theme4.stop();*/
    }

    public Scenario getScenario() {
        return null;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        drawWorld(delta);
        drawInterfaces();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

}
