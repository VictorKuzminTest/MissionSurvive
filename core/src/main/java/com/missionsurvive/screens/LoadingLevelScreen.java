package com.missionsurvive.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.missionsurvive.MSGame;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.map.Map;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.scenarios.commands.Command;
import com.missionsurvive.scenarios.commands.LoadFromAssetsLibGdx;
import com.missionsurvive.scenarios.commands.SaveLoadMapCommand;
import com.missionsurvive.utils.Assets;

/**
 * Created by kuzmin on 07.06.18.
 */

public class LoadingLevelScreen implements Screen {

    private MSGame game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Texture texture;
    private Map map;
    private Command loadMapCommand;

    private int iconX = 330;
    private int iconY = 290;
    private int assetStartX = 0;
    private int assetStartY = 459;
    private int assetWidth = 148;
    private int assetHeight = 26;

    public LoadingLevelScreen(MSGame game){
        this.game = game;

        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT, gameCam);

        texture = Assets.getTextures()[Assets.getWhichTexture("art")];
        loadMapCommand = new LoadFromAssetsLibGdx();

        map = new MapEditor();
    }

    @Override
    public void show() {

    }

    public void update(float delta){
        if(map.loadMap(loadMapCommand.execute(null, "levs/lev1"))){
            game.setScreen(game.getScreenFactory().newScreen("PlatformerScreen", map));
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getSpriteBatch().setProjectionMatrix(gameCam.combined);

        drawBackground();
        drawLoadingIcon();

        update(delta);
    }

    public void drawBackground(){
        game.getSpriteBatch().setProjectionMatrix(gameCam.combined);
        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(texture, MSGame.SCREEN_OFFSET_X, MSGame.SCREEN_OFFSET_Y,
                0, 0, MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT);
        game.getSpriteBatch().end();
    }

    public void drawLoadingIcon(){
        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(texture, MSGame.SCREEN_OFFSET_X + iconX,
                MSGame.SCREEN_OFFSET_Y + GeoHelper.transformCanvasYCoordToGL(iconY,
                        MSGame.SCREEN_HEIGHT, assetHeight),
                assetStartX, assetStartY,
                assetWidth, assetHeight);
        game.getSpriteBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
}
