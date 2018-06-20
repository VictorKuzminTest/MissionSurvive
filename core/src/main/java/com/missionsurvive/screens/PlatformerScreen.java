package com.missionsurvive.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.TouchControl;
import com.missionsurvive.framework.impl.DrawerFacade;
import com.missionsurvive.map.Map;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.ParallaxCamera;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.PlayScript;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.SpawnBot;

/**
 * Created by kuzmin on 31.05.18.
 */

public class PlatformerScreen extends GameScreen implements Screen {

    private MSGame game;
    private DrawerFacade drawerFacade;
    private ParallaxCamera gameCam;
    private Viewport gamePort;
    private TiledMapRenderer renderer;
    private Map map;
    private PlayScript playScript;
    private Scenario platformerScenario;
    private TouchControl touchControl;
    private Hero hero;

    private int worldWidth, worldHeight;

    //transforming real coords into logic:
    private float scaleX, scaleY;

    public PlatformerScreen(MSGame game, Map map){
        this.game = game;

        scaleX = (float) MSGame.SCREEN_WIDTH / Gdx.graphics.getBackBufferWidth();
        scaleY = (float)MSGame.SCREEN_HEIGHT / Gdx.graphics.getBackBufferHeight();

        drawerFacade = new DrawerFacade();

        this.map = map;
        worldHeight = map.getLevel1Ter().length;
        worldWidth = map.getLevel1Ter()[0].length;

        gameCam = new ParallaxCamera(MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT); //extends OrthographicCamera
        gamePort = new StretchViewport(MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT, gameCam);
        gameCam.position.x = -MSGame.SCREEN_OFFSET_X + map.getScrollMap().getWorldOffsetX();
        gameCam.position.y = worldHeight * 16 + MSGame.SCREEN_OFFSET_Y - map.getScrollMap().getWorldOffsetY();
        gameCam.position.z = 0;
        renderer = new OrthogonalTiledMapRenderer(((MapEditor)map).getMap());
        ((MapEditor)map).setGameCam(gameCam);

        playScript = new PlayScript(this);
        platformerScenario = new PlatformerScenario((MapEditor)map, playScript);
        touchControl = new TouchControl(scaleX, scaleY);

        putPlayer(100, 150);
    }

    public void update(float deltaTime) {
        platformerScenario.update(map, touchControl, deltaTime);
    }

    @Override
    public void show() {

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

        drawerFacade.drawWreckages(platformerScenario.getBots(SpawnBot.WRECKAGE), game.getSpriteBatch());

        //draw all other bots:
        drawerFacade.drawBots(platformerScenario.getBots(0), game.getSpriteBatch());

        drawerFacade.drawBots(platformerScenario.getBots(SpawnBot.ZOMBIE), game.getSpriteBatch());

        drawerFacade.drawHero(hero, game.getSpriteBatch());
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
        platformerScenario.placeObject(x, y);
        if(platformerScenario instanceof PlatformerScenario) {
            hero = ((PlatformerScenario) platformerScenario).getHero();
        }
    }
}
