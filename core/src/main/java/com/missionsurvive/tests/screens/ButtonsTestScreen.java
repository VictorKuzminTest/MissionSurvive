package com.missionsurvive.tests.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.impl.ListButtons;
import com.missionsurvive.tests.ButtonsTest;

public class ButtonsTestScreen implements Screen {
    private MSGame game;
    private ButtonsTest buttonsTest;
    private OrthographicCamera gameCam;
    private Viewport gamePort;

    //for transforming real pixel touch coords into logic pixel coords:
    private float scaleX;
    private float scaleY;

    public ButtonsTestScreen(MSGame game){
        this.game = game;

        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(480, 320, gameCam);
        buttonsTest = new ButtonsTest();

        scaleX = (float)480 / Gdx.graphics.getBackBufferWidth();
        scaleY = (float)320 / Gdx.graphics.getBackBufferHeight();
    }

    @Override
    public void show() {

    }

    public void update(float delta){
        buttonsTest.touchButtons(delta, scaleX, scaleY);
        buttonsTest.touchLists(delta, scaleX, scaleY);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getSpriteBatch().setProjectionMatrix(gameCam.combined);
        drawButtons();
        drawListButtons();
    }

    public void drawButtons(){
        int buttonsCount = buttonsTest.getButtons().size();
        for(int i = 0; i < buttonsCount; i++){
            buttonsTest.getButtons().get(i).drawButton(game.getSpriteBatch());
        }
    }

    public void drawListButtons(){
        ListButtons rootList = buttonsTest.getListsButtons();
        int numLists = rootList.getLists().size();
        for(int i = 0; i < numLists; i++){
            rootList.getLists().get(i).drawButtons(game.getSpriteBatch());
        }
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
