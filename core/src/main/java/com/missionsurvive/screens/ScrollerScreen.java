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
import com.missionsurvive.geom.Hitbox;
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
import com.missionsurvive.utils.Sounds;

import java.util.ArrayList;

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
    private ShapeRenderer hitBoxRect;

    private int noStartCol = -1, noEndCol = -1;
    private int screenWidth;

    private float screenTranparency = 0;
    private float darkenTickTime = 0, darkenTick = 0.1f;
    //floats for obstacle blink.
    private float blinkTickTime, blinkTick = 0.1f;
    private float scaleX, scaleY;
    private float scaleToDrawX, scaleToDrawY;

    private boolean blink;
    private boolean onPause;

    public ScrollerScreen(MSGame game, PlayScript playScript, String difficulty) {
        ...
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
            background.render(delta);
        }
        else{
            background.render(0);
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
        ...
    }

    /**
     * draw tear. No moto is considered.
     * @param obstacles
     */
    public void drawTear(ArrayList<Obstacle> obstacles, SpriteBatch batch){
        ...
    }

    public void drawRoundedTileset(SpriteBatch batch, ScrollMap scrollMap){
        ...
    }

    private void drawInterfaces(){
        controlScenario.drawPanels(game.getSpriteBatch());
    }

    @Override
    public void putPlayer(int x, int y){
        scrollerScenario.placeObject(x, y);
        moto = scrollerScenario.getMoto();
    }

    @Override
    public boolean onPause(){
        return onPause;
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
    public void dispose() {}

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
    public void pause(boolean pause) {
        onPause = pause;
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void setScreenPos(int x, int y) {

    }
}
