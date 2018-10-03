package com.missionsurvive.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.impl.DrawerFacade;
import com.missionsurvive.map.Map;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.ParallaxBackground;
import com.missionsurvive.map.ParallaxCamera;
import com.missionsurvive.scenarios.PlayScript;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.EndGameScenario;
import com.missionsurvive.scenarios.controlscenarios.ControlScenario;
import com.missionsurvive.scenarios.controlscenarios.EndCS;
import com.missionsurvive.scenarios.controlscenarios.GameCS;

public class EndScreen extends GameScreen implements Screen {

    private MSGame game;
    private DrawerFacade drawerFacade;
    private ParallaxCamera gameCam;
    private Viewport gamePort;
    private TiledMapRenderer renderer;
    private Map map;
    private Scenario endGameScenario;
    private PlayScript playScript;
    private ParallaxBackground lev2;
    private ParallaxBackground lev3;
    private ControlScenario controlScenario;

    private int worldHeight, worldWidth;

    //transforming real coords into logic:
    private float scaleX, scaleY;

    public EndScreen(MSGame game, PlayScript playScript, Map map){
        this.game = game;
        this.playScript = playScript;
        drawerFacade = new DrawerFacade();

        scaleX = (float) MSGame.SCREEN_WIDTH / Gdx.graphics.getBackBufferWidth();
        scaleY = (float)MSGame.SCREEN_HEIGHT / Gdx.graphics.getBackBufferHeight();

        this.map = map;
        worldHeight = map.getLevel1Ter().length;
        worldWidth = map.getLevel1Ter()[0].length;
        lev2 = map.getBackground("lev2");
        lev3 = map.getBackground("lev3");

        gameCam = new ParallaxCamera(MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT); //extends OrthographicCamera
        gamePort = new StretchViewport(MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT, gameCam);
        gameCam.position.x = -MSGame.SCREEN_OFFSET_X + map.getScrollMap().getWorldOffsetX();
        gameCam.position.y = worldHeight * 16 + MSGame.SCREEN_OFFSET_Y - map.getScrollMap().getWorldOffsetY();
        gameCam.position.z = 0;
        renderer = new OrthogonalTiledMapRenderer(((MapEditor)map).getMap());
        ((MapEditor)map).setGameCam(gameCam);
        controlScenario = new EndCS();
        endGameScenario = new EndGameScenario((MapEditor) map, controlScenario);
    }

    public void update(float deltaTime) {
        controlScenario.onTouchPanels(deltaTime, scaleX, scaleY);
        endGameScenario.update(map, null, deltaTime);
    }

    private void drawWorld(){
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(lev3 != null){
            lev3.render();
        }
        if(lev2 != null){
            lev2.render();
        }

        gameCam.update();
        renderer.setView(gameCam);
        renderer.render();

        //draw all other bots:
        drawerFacade.drawBots(endGameScenario.getBots(0), game.getSpriteBatch());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        drawWorld();
        drawInterfaces();
    }

    private void drawInterfaces() {
        controlScenario.drawPanels(game.getSpriteBatch());
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void pause(boolean pause) {

    }

    @Override
    public void resume() {

    }

    @Override
    public boolean onPause() {
        return false;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void putPlayer(int x, int y) {

    }

    @Override
    public void setScreenPos(int x, int y) {

    }
}
