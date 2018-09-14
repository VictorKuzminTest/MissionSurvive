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
import com.missionsurvive.map.ParallaxCamera;
import com.missionsurvive.scenarios.PlayScript;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.EndGameScenario;

public class EndScreen extends GameScreen implements Screen {

    private MSGame game;
    private DrawerFacade drawerFacade;
    private ParallaxCamera gameCam;
    private Viewport gamePort;
    private TiledMapRenderer renderer;
    private Map map;
    private Scenario endGameScenario;
    private PlayScript playScript;

    private int worldHeight;

    public EndScreen(MSGame game, PlayScript playScript, Map map){
        this.game = game;
        this.playScript = playScript;
        drawerFacade = new DrawerFacade();
        this.map = map;

        worldHeight = map.getLevel1Ter().length;

        gameCam = new ParallaxCamera(MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT); //extends OrthographicCamera
        gamePort = new StretchViewport(MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT, gameCam);
        gameCam.position.x = -MSGame.SCREEN_OFFSET_X + map.getScrollMap().getWorldOffsetX();
        gameCam.position.y = worldHeight * 16 + MSGame.SCREEN_OFFSET_Y - map.getScrollMap().getWorldOffsetY();
        gameCam.position.z = 0;
        renderer = new OrthogonalTiledMapRenderer(((MapEditor)map).getMap());
        ((MapEditor)map).setGameCam(gameCam);
        endGameScenario = new EndGameScenario((MapEditor) map);
    }

    public void update(float deltaTime) {
        endGameScenario.update(map, null, deltaTime);
    }

    private void drawWorld(){
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //CHECK IF IT IS REALLY NEEDED EVERY TIME THE WORLD RENDERS!!!!!!!!!
        game.getSpriteBatch().enableBlending();
        game.getSpriteBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

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

    @Override
    public void dispose() {

    }

    @Override
    public void putPlayer(int x, int y) {

    }
}
