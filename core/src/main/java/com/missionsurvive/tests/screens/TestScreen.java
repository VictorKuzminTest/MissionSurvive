package com.missionsurvive.tests.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.missionsurvive.MSGame;
import com.missionsurvive.commands.Command;
import com.missionsurvive.commands.Load;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.ParallaxBackground;
import com.missionsurvive.map.ParallaxCamera;
import com.missionsurvive.map.ParallaxLayer;
import com.missionsurvive.tests.MapEditorTest;
import com.missionsurvive.utils.Assets;

/**
 * Created by kuzmin on 20.04.18.
 */

public class TestScreen implements Screen {

    private MSGame game;
    Texture texture;
    //private OrthographicCamera gameCam;
    private ParallaxCamera gameCam;
    private Viewport gamePort;
    private MapEditor mapEditor;
    private TiledMapRenderer renderer;
    private BitmapFont font;
    private MapEditorTest mapEditorTest;
    ParallaxBackground background;

    public TestScreen(MSGame game){
        this.game = game;
        //gameCam = new OrthographicCamera(); //default ortho camera
        gameCam = new ParallaxCamera(480, 320); //extends OrthographicCamera
        gamePort = new StretchViewport(480, 320, gameCam);
        //gamePort = new FitViewport(480, 320, gameCam);  don't use this view port
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        texture = Assets.getTextures()[Assets.getWhichTexture("art")];

        font = new BitmapFont();

        mapEditor = new MapEditor(300, 23);
        mapEditorTest = new MapEditorTest(mapEditor);

        //loadbasicTestMap();
        loadMapTest();
        renderer = new OrthogonalTiledMapRenderer(mapEditor.getMap());

        background = new ParallaxBackground(new ParallaxLayer[]{
                new ParallaxLayer(mapEditor.getlev3(), new Vector2(1, 1), new Vector2(0, 0)),
        }, 480, 320, new Vector2(50, 0));
    }

    /**
     * load map from scratch
     */
    public void loadbasicTestMap(){
        mapEditorTest.testBasicMapCreation();
    }

    /**
     * load map from text file.
     */
    public void loadMapTest(){
        mapEditorTest.loadMapCommand();
    }

    @Override
    public void show() {
    }

    public void scroll(float delta){
        gameCam.position.x += 100 * delta;
    }

    @Override
    public void render(float delta) {
        //drawOnlyForeground(delta);
        drawWithParallax(delta);
        //drawTexture();
    }

    public void drawTexture(){
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getSpriteBatch().setProjectionMatrix(gameCam.combined);
        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(texture, -240, -160);
        game.getSpriteBatch().end();
    }

    public void drawWithParallax(float delta){
        scroll(delta);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getSpriteBatch().enableBlending();
        game.getSpriteBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        background.render(delta);

        gameCam.update();
        renderer.setView(gameCam);
        renderer.render();
        game.getSpriteBatch().begin();

        //font.draw(game.getSpriteBatch(), "FPS: " + Gdx.graphics.getFramesPerSecond(), 0, 0); //draw fps/sec
        game.getSpriteBatch().end();
    }

    public void drawOnlyForeground(float delta){
        scroll(delta);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameCam.update();
        renderer.setView(gameCam);
        renderer.render();
        game.getSpriteBatch().begin();
        //font.draw(game.getSpriteBatch(), "FPS: " + Gdx.graphics.getFramesPerSecond(), 0, 0); //draw fps/sec
        game.getSpriteBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
