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
import com.missionsurvive.map.ParallaxBackground;
import com.missionsurvive.map.ParallaxCamera;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.PlayScript;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.SpawnBot;
import com.missionsurvive.scenarios.controlscenarios.ControlScenario;
import com.missionsurvive.scenarios.controlscenarios.GameCS;
import com.missionsurvive.scenarios.controlscenarios.MapEditorCS;
import com.missionsurvive.utils.Sounds;

public class PlatformerScreen extends GameScreen implements Screen {

    private MSGame game;
    private DrawerFacade drawerFacade;
    private ParallaxCamera gameCam;
    private ParallaxBackground lev2;
    private ParallaxBackground lev3;
    private Viewport gamePort;
    private TiledMapRenderer renderer;
    private Map map;
    private PlayScript playScript;
    private Scenario platformerScenario;
    private ControlScenario controlScenario;
    private TouchControl touchControl;
    private Hero hero;

    private int worldWidth, worldHeight;

    //transforming real coords into logic:
    private float scaleX, scaleY;

    private boolean onPause;

    public PlatformerScreen(MSGame game, PlayScript playScript, Map map){
        ...
    }

    public void update(float deltaTime) {
        controlScenario.onTouchPanels(deltaTime, scaleX, scaleY);
        if(!onPause){
            platformerScenario.update(map, touchControl, deltaTime);
        }
    }

    @Override
    public void show() {

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
    public boolean onPause() {
        return onPause;
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
    public void dispose() {

    }

    @Override
    public void putPlayer(int x, int y) {
        platformerScenario.placeObject(x, y);
        if(platformerScenario instanceof PlatformerScenario) {
            hero = ((PlatformerScenario) platformerScenario).getHero();
        }
    }

    @Override
    public void setScreenPos(int x, int y) {
       ...
    }
}
