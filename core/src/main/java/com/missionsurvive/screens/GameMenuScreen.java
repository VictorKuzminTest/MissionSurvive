package com.missionsurvive.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.missionsurvive.MSGame;
import com.missionsurvive.scenarios.PlayScript;
import com.missionsurvive.scenarios.controlscenarios.ControlScenario;
import com.missionsurvive.scenarios.controlscenarios.GameMenuCS;
import com.missionsurvive.utils.Assets;
import com.missionsurvive.utils.Progress;
import com.missionsurvive.utils.Sounds;

public class GameMenuScreen implements Screen {

    private MSGame game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private ControlScenario controlScenario;
    private Texture texture;
    private PlayScript playScript;

    //for transforming real pixel touch coords into logic pixel coords:
    private float scaleX;
    private float scaleY;

    public GameMenuScreen(MSGame game, PlayScript playScript){
        ...
    }

    public void update(float delta){
        controlScenario.onTouchPanels(delta, scaleX, scaleY);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getSpriteBatch().setProjectionMatrix(gameCam.combined);

        drawTexture();

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
