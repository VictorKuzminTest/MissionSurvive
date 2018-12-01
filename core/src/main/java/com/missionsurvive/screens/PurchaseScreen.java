package com.missionsurvive.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Button;
import com.missionsurvive.framework.impl.ActionButton;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.scenarios.PlayScript;
import com.missionsurvive.scenarios.commands.Command;
import com.missionsurvive.scenarios.controlscenarios.ControlScenario;
import com.missionsurvive.scenarios.controlscenarios.PurchaseCS;
import com.missionsurvive.utils.Assets;
import com.missionsurvive.utils.Progress;

import java.util.ArrayList;

public class PurchaseScreen implements Screen {

    private static final float SCROLLING_TICK = 0.015f;
    private static final float GET_PURCHASE_TICK = 1.0f;

    private MSGame game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private ControlScenario controlScenario;
    private Texture texture;
    private PlayScript playScript;
    private ListAds adsPics;
    //which screen to go (Platformer or main menu).
    private String action;

    //for transforming real pixel touch coords into logic pixel coords:
    private float scaleX;
    private float scaleY;
    private float scrollingTickTime;
    private float getPurchaseTickTime;

    public PurchaseScreen(MSGame game, PlayScript playScript, String action){
       ...
    }

    /**
     * If we receive String like ":purchase:ToLevel4Beginner:", we have to
     * separate "ToLevel4Beginner" from "purchase" and assign the new value to action.
     */
    private void parseAction(){
        ...
    }

    public void populateAdsPics(){
       ...
    }

    @Override
    public void show() {

    }

    public void update(float delta){
        controlScenario.onTouchPanels(delta, scaleX, scaleY);
        scroll(delta);
        getPurchase(delta);
    }

    private void getPurchase(float delta){
        ...
    }

    private void scroll(float delta){
        scrollingTickTime += delta;
        while(scrollingTickTime > SCROLLING_TICK){
            scrollingTickTime -= SCROLLING_TICK;

            adsPics.scroll(-1, 0);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getSpriteBatch().setProjectionMatrix(gameCam.combined);

        drawTexture();

        adsPics.drawAds(game.getSpriteBatch());

        controlScenario.drawPanels(game.getSpriteBatch());
    }

    public void drawTexture(){
        game.getSpriteBatch().setProjectionMatrix(gameCam.combined);
        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(texture, MSGame.SCREEN_OFFSET_X, MSGame.SCREEN_OFFSET_Y,
                0, 0, MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT);
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
