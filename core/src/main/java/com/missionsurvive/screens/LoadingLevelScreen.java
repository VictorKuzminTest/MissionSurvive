package com.missionsurvive.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.missionsurvive.MSGame;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.map.Map;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.scenarios.PlayScript;
import com.missionsurvive.scenarios.commands.Command;
import com.missionsurvive.scenarios.commands.LoadFromAssetsLibGdx;
import com.missionsurvive.scenarios.commands.SaveLoadMapCommand;
import com.missionsurvive.utils.Assets;
import com.missionsurvive.utils.Progress;
import com.missionsurvive.utils.Sounds;

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
    private String assetName;
    private PlayScript playScript;

    private int iconX = 330;
    private int iconY = 290;
    private int assetStartX = 0;
    private int assetStartY = 459;
    private int assetWidth = 148;
    private int assetHeight = 26;

    public LoadingLevelScreen(MSGame game, PlayScript playScript, String assetName){
        this.game = game;
        this.playScript = playScript;

        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT, gameCam);

        texture = Assets.getTextures()[Assets.getWhichTexture("art")];
        loadMapCommand = new LoadFromAssetsLibGdx();
        setAssetName(assetName);

        map = new MapEditor();
    }

    public void setAssetName(String assetName){
        if(assetName.equalsIgnoreCase("NextLevel")){
            if(Assets.getCurrentLevel().equalsIgnoreCase("levs/level11")){
                this.assetName = "levs/level21";
                Progress.setProgress(Progress.BEGINNER, 1);
            }
            else if(Assets.getCurrentLevel().equalsIgnoreCase("levs/level21")){
                this.assetName = "levs/level31";
                Progress.setProgress(Progress.BEGINNER, 2);
            }
            else if(Assets.getCurrentLevel().equalsIgnoreCase("levs/level31")){
                if(Progress.isPurchased()){
                    this.assetName = "ToLevel4Beginner";
                    Progress.setProgress(Progress.BEGINNER, 3);
                }
                else{
                    this.assetName = ":purchase:ToLevel4Beginner:";
                }
            }
            else if(Assets.getCurrentLevel().equalsIgnoreCase("ToLevel4Beginner")){
                this.assetName = "levs/level51";
                Progress.setProgress(Progress.BEGINNER, 4);
            }
            if(Assets.getCurrentLevel().equalsIgnoreCase("levs/level12")){
                this.assetName = "levs/level22";
                Progress.setProgress(Progress.EXPERIENCED, 1);
            }
            else if(Assets.getCurrentLevel().equalsIgnoreCase("levs/level22")){
                this.assetName = "levs/level32";
                Progress.setProgress(Progress.EXPERIENCED, 2);
            }
            else if(Assets.getCurrentLevel().equalsIgnoreCase("levs/level32")){
                if(Progress.isPurchased()){
                    this.assetName = "ToLevel4Experienced";
                    Progress.setProgress(Progress.EXPERIENCED, 3);
                }
                else{
                    this.assetName = ":purchase:ToLevel4Experienced:";
                }
            }
            else if(Assets.getCurrentLevel().equalsIgnoreCase("ToLevel4Experienced")){
                this.assetName = "levs/level52";
                Progress.setProgress(Progress.EXPERIENCED, 4);
            }
            else if(Assets.getCurrentLevel().equalsIgnoreCase("levs/level52")){
                this.assetName = "levs/level6";
                Progress.setProgress(Progress.EXPERIENCED, 5);
            }
            else if(Assets.getCurrentLevel().equalsIgnoreCase("EditorScreen")){
                this.assetName = "EditorScreen";
            }
        }
        else{
            this.assetName = assetName;
        }
        Assets.setCurrentLevel(this.assetName);
    }

    @Override
    public void show() {

    }

    public void update(float delta){
        if(assetName.equalsIgnoreCase("EditorScreen")){
            game.setScreen(game.getScreenFactory().newScreen("EditorScreen", null, null));
        }
        else if(assetName.equalsIgnoreCase("ToLevel4Beginner")){
            game.setScreen(game.getScreenFactory().newScreen("ScrollerScreen", null, Progress.BEGINNER));
        }
        else if(assetName.equalsIgnoreCase("ToLevel4Experienced")){
            game.setScreen(game.getScreenFactory().newScreen("ScrollerScreen", null, Progress.EXPERIENCED));
        }
        else if(assetName.equalsIgnoreCase(":purchase:ToLevel4Beginner:")){
            game.setScreen(game.getScreenFactory().newScreen("PurchaseScreen", null, ":purchase:ToLevel4Beginner:"));
        }
        else if(assetName.equalsIgnoreCase(":purchase:ToLevel4Experienced:")){
            game.setScreen(game.getScreenFactory().newScreen("PurchaseScreen", null, ":purchase:ToLevel4Experienced:"));
        }
        else{
            if(map.loadMap(loadMapCommand.execute(null, assetName))){
                game.setScreen(game.getScreenFactory().newScreen("PlatformerScreen", map, null));
            }
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
