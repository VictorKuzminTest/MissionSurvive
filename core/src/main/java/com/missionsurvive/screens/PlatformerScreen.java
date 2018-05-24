package com.missionsurvive.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.TouchControl;
import com.missionsurvive.framework.impl.DrawerFacade;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.map.Map;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.map.ParallaxCamera;
import com.missionsurvive.map.ScrollMap;
import com.missionsurvive.objs.GameObject;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.Spawn;
import com.missionsurvive.scenarios.SpawnBot;
import com.missionsurvive.scenarios.controlscenarios.ControlScenario;
import com.missionsurvive.scenarios.controlscenarios.MapEditorCS;
import com.missionsurvive.utils.Assets;
import com.missionsurvive.utils.Controls;

/**
 * Created by kuzmin on 14.05.18.
 */

public class PlatformerScreen implements Screen {

    public static final int ALL_LAYERS = 0;
    public static final int FIRST_LAYER = 1;

    /*private Music theme;*/
    private DrawerFacade drawerFacade;
    private Scenario platformerScenario;
    private Map map;
    private ControlScenario controlScenario;
    private TouchControl touchControl;
    private Hero hero;
    private MSGame game;
    private ShapeRenderer foregroundRect;//tile rect for a foreground layer
    private ShapeRenderer editingTileRect;
    private ShapeRenderer terBlockTileRect;
    private ShapeRenderer terLadderTileRect;
    private TiledMapRenderer renderer;
    private ParallaxCamera gameCam;
    private Viewport gamePort;

    //which tileset to draw (only foreground or all backgrounds):
    private int tilesetLayerToDraw;
    private int worldWidth, worldHeight;

    //transforming real coords into logic:
    private float scaleX, scaleY;
    //the multiply parameters opposite to scaleX. It is used for transforming logic
    //coords into real (drawing rects):
    private float scaleToDrawX, scaleToDrawY;

    //control hero (play) or control map (build map):
    private boolean isHeroControl = false;
    private boolean isScrollBlocked;

    public PlatformerScreen(MSGame game, int width, int height) {
        this.game = game;

        scaleX = (float) MSGame.SCREEN_WIDTH / Gdx.graphics.getBackBufferWidth();
        scaleY = (float)MSGame.SCREEN_HEIGHT / Gdx.graphics.getBackBufferHeight();
        scaleToDrawX = (float)Gdx.graphics.getBackBufferWidth() / MSGame.SCREEN_WIDTH;
        scaleToDrawY = (float)Gdx.graphics.getBackBufferHeight() / MSGame.SCREEN_HEIGHT;

        drawerFacade = new DrawerFacade();
        worldWidth = width;
        worldHeight = height;

        gameCam = new ParallaxCamera(MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT); //extends OrthographicCamera
        gamePort = new StretchViewport(MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT, gameCam);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        map = new MapEditor(gameCam, worldWidth, worldHeight);
        renderer = new OrthogonalTiledMapRenderer(((MapEditor)map).getMap());
        platformerScenario = new PlatformerScenario((MapEditor)map);
        controlScenario = new MapEditorCS(this, map, platformerScenario);
        touchControl = new TouchControl(scaleX, scaleY);

        foregroundRect = new ShapeRenderer(); //tile rect for a foreground layer
        editingTileRect = new ShapeRenderer();
        terBlockTileRect = new ShapeRenderer();
        terLadderTileRect = new ShapeRenderer();

        foregroundRect.setColor(0, 1, 0, 1);
        editingTileRect.setColor(0, 0, 0, 0.5f);
        terBlockTileRect.setColor(1, 0, 0, 0.5f);
        terLadderTileRect.setColor(1, 1, 0, 0.5f);

        /*this.whichAsset = Assets.getWhichPixmap("lev3");*/
        /*theme = Sounds.theme2;*/
    }

    public void newMap(int width, int height){
        worldWidth = width;
        worldHeight = height;

        ((MapEditor)map).newMap(worldWidth, worldHeight);
        renderer = new OrthogonalTiledMapRenderer(((MapEditor)map).getMap());
    }

    @Override
    public void show(){

    }

    public void update(float deltaTime) {
        if(!controlScenario.onTouchPanels(scaleX, scaleY)){
            if(!isHeroControl){
                switch(tilesetLayerToDraw){
                    case FIRST_LAYER:
                        if(!isScrollBlocked){
                            touchControl.scrollMapLayer(controlScenario, map.getScrollMap());
                            gameCam.position.x = -MSGame.SCREEN_OFFSET_X + map.getScrollMap().getWorldOffsetX();
                            gameCam.position.y = worldHeight * 16 + MSGame.SCREEN_OFFSET_Y - map.getScrollMap().getWorldOffsetY();
                        }
                        else{
                            controlScenario.action(touchControl.touchMap(map.getLevel1Ter(), map.getScrollMap(),
                                        worldHeight, worldWidth));
                        }
                        break;
                    default://touchControl.scrollMapLayer(controlScenario, scrollLevel1Map);
                        // break;
                }
            }
            else{
                platformerScenario.update(map, touchControl, deltaTime);
            }
        }
    }



    private void drawWorld(){
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(isHeroControl){
            game.getSpriteBatch().enableBlending();
            game.getSpriteBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }

        switch(tilesetLayerToDraw){
            case FIRST_LAYER:
                break;
            default:
                /*int backgroundOffset = (int)mapEditor.getBackgroundOffset();
                g.drawPixmap(Assets.pixmaps[this.whichAsset], 0, 0, backgroundOffset, 0,
                        480 - backgroundOffset, 321, null);
                g.drawPixmap(Assets.pixmaps[this.whichAsset], (480 - 1) - backgroundOffset, 0, 0, 0,
                        (backgroundOffset + 2), 321, null); //(background + 2) because only in this case background is fully drawn on the screen.

                //draw tileset for platformer:
                drawRoundedTileset(level2Ter, scrollLevel2Map, level2TileRect, false);
                drawTileset(level1Ter, scrollLevel1Map, level1TileRect, false);*/
                break;
        }

        gameCam.update();
        renderer.setView(gameCam);
        //game.getSpriteBatch().begin();
        renderer.render();
        //game.getSpriteBatch().end();

        if(tilesetLayerToDraw == FIRST_LAYER){
            drawTileProps(game.getSpriteBatch(), map.getLevel1Ter(), map.getScrollMap(), true);
        }

        drawerFacade.drawWreckages(platformerScenario.getBots(SpawnBot.WRECKAGE), game.getSpriteBatch());

        //draw all other bots:
        drawerFacade.drawBots(platformerScenario.getBots(0), game.getSpriteBatch());

        drawerFacade.drawBots(platformerScenario.getBots(SpawnBot.ZOMBIE), game.getSpriteBatch());

        drawerFacade.drawHero(hero, game.getSpriteBatch());
    }

    public void drawTileset(SpriteBatch batch, MapTer[][] mapTer, ScrollMap scrollMap, boolean isEditing){
        int startRowOffset = scrollMap.getStartRowOffset(); //Строка, с которой начинается отрисовка тайлов.
        int startColOffset = scrollMap.getStartColOffset(); //Колонка, с которой начинается отрисовка тайлов.
        int endRowOffset = scrollMap.getEndRowOffset();  //Строка, которой заканчивается отрисовка тайлов.
        int endColOffset = scrollMap.getEndColOffset();   //Колонка, которой заканчивается отрисовка тайлов.

        for(int row = startRowOffset; row < endRowOffset; row++){      //Отрисовка тайлов карты.
            if (row >= worldHeight) break;  //we don't need to keep drawing, because there is nothing else to draw.
            for(int col = startColOffset; col < endColOffset; col++){
                if(col >= worldWidth) break; // the same: we don't need keep drawing.
                if(isEditing){
                    foregroundRect.begin(ShapeRenderer.ShapeType.Filled);
                    foregroundRect.rect((col * 16 - scrollMap.getWorldOffsetX()) * scaleToDrawX,
                            GeoHelper.transformCanvasYCoordToGL(row * 16 - scrollMap.getWorldOffsetY(),
                                    MSGame.SCREEN_HEIGHT, 16) * scaleToDrawY,
                            16 * scaleToDrawX, 16 * scaleToDrawY);
                    foregroundRect.end();

                    /*if(mapTer[row][col].isEditing()){  //if данный участок карты выделен для редактирования.
                        editingTileRect.begin(ShapeRenderer.ShapeType.Filled);
                        editingTileRect.rect((col * 16 - scrollMap.getWorldOffsetX()) * scaleToDrawX,
                                GeoHelper.transformCanvasYCoordToGL(row * 16 - scrollMap.getWorldOffsetY(),
                                        MSGame.SCREEN_HEIGHT, 16) * scaleToDrawY,
                                16 * scaleToDrawX, 16 * scaleToDrawY);
                        editingTileRect.end();
                    }
                    if(mapTer[row][col].isBlocked()){  //if current tile is blocked.
                        terBlockTileRect.begin(ShapeRenderer.ShapeType.Filled);
                        terBlockTileRect.rect((col * 16 - scrollMap.getWorldOffsetX()) * scaleToDrawX,
                                GeoHelper.transformCanvasYCoordToGL(row * 16 - scrollMap.getWorldOffsetY(),
                                        MSGame.SCREEN_HEIGHT, 16) * scaleToDrawY,
                                16 * scaleToDrawX, 16 * scaleToDrawY);
                        terBlockTileRect.end();
                    }
                    if(mapTer[row][col].isLadder()){   //if current tile is ladder.
                        terLadderTileRect.begin(ShapeRenderer.ShapeType.Filled);
                        terLadderTileRect.rect((col * 16 - scrollMap.getWorldOffsetX()) * scaleToDrawX,
                                GeoHelper.transformCanvasYCoordToGL(row * 16 - scrollMap.getWorldOffsetY(),
                                        MSGame.SCREEN_HEIGHT, 16) * scaleToDrawY,
                                16 * scaleToDrawX, 16 * scaleToDrawY);
                        terLadderTileRect.end();
                    }*/
                }
            }
        }
    }


    /**
     * Draw bots on its spawn position while constructin a map.
     * @param batch
     * @param scrollMap
     */
    private void drawTileProps(SpriteBatch batch, MapTer[][] mapTer, ScrollMap scrollMap, boolean isEditing){
        int startRowOffset = scrollMap.getStartRowOffset(); //Строка, с которой начинается отрисовка тайлов.
        int startColOffset = scrollMap.getStartColOffset(); //Колонка, с которой начинается отрисовка тайлов.
        int endRowOffset = scrollMap.getEndRowOffset();  //Строка, которой заканчивается отрисовка тайлов.
        int endColOffset = scrollMap.getEndColOffset();   //Колонка, которой заканчивается отрисовка тайлов.

        for(int row = startRowOffset; row < endRowOffset; row++){
            if (row >= worldHeight) break;
            for(int col = startColOffset; col < endColOffset; col++){
                if(col >= worldWidth) break;

                if(isEditing){
                    if(mapTer[row][col].isEditing()){  //if данный участок карты выделен для редактирования.
                        editingTileRect.begin(ShapeRenderer.ShapeType.Filled);
                        editingTileRect.rect((col * 16 - scrollMap.getWorldOffsetX()) * scaleToDrawX,
                                GeoHelper.transformCanvasYCoordToGL(row * 16 - scrollMap.getWorldOffsetY(),
                                        MSGame.SCREEN_HEIGHT, 16) * scaleToDrawY,
                                16 * scaleToDrawX, 16 * scaleToDrawY);
                        editingTileRect.end();
                    }
                    if(mapTer[row][col].isBlocked()){  //if current tile is blocked.
                        terBlockTileRect.begin(ShapeRenderer.ShapeType.Filled);
                        terBlockTileRect.rect((col * 16 - scrollMap.getWorldOffsetX()) * scaleToDrawX,
                                GeoHelper.transformCanvasYCoordToGL(row * 16 - scrollMap.getWorldOffsetY(),
                                        MSGame.SCREEN_HEIGHT, 16) * scaleToDrawY,
                                16 * scaleToDrawX, 16 * scaleToDrawY);
                        terBlockTileRect.end();
                    }
                    if(mapTer[row][col].isLadder()){   //if current tile is ladder.
                        terLadderTileRect.begin(ShapeRenderer.ShapeType.Filled);
                        terLadderTileRect.rect((col * 16 - scrollMap.getWorldOffsetX()) * scaleToDrawX,
                                GeoHelper.transformCanvasYCoordToGL(row * 16 - scrollMap.getWorldOffsetY(),
                                        MSGame.SCREEN_HEIGHT, 16) * scaleToDrawY,
                                16 * scaleToDrawX, 16 * scaleToDrawY);
                        terLadderTileRect.end();
                    }
                    if(((MapEditor)map).getSpawns()[row][col] != null){
                        drawerFacade.showBotPos(batch, ((MapEditor)map).getSpawns()[row][col],
                                map, row, col);
                    }
                }
            }
        }
    }

    private void drawInterfaces() {
        controlScenario.drawPanels(game.getSpriteBatch());
    }


    public void putPlayer(int x, int y){
        platformerScenario.placeObject(x, y);
        if(platformerScenario instanceof PlatformerScenario) {
            hero = ((PlatformerScenario) platformerScenario).getHero();

        }
    }

    public boolean isHeroControl(){
        return isHeroControl;
    }


    public void setHeroControl(boolean isHeroControl){
        /*if(isHeroControl()){
            theme.stop();
            if(platformerScenario instanceof PlatformerScenario){
                ((PlatformerScenario)platformerScenario).setLives(23);
            }
        }
        else{
            theme.setLooping(true);
            theme.setVolume(1);
            theme.play();
        }*/
        this.isHeroControl = isHeroControl;
    }

    public void setTilesetLayerToDraw(int tilesetLevelToDraw){
        this.tilesetLayerToDraw = tilesetLevelToDraw;
    }


    public void setScrollBlocked(boolean isScrollBlocked){
        this.isScrollBlocked = isScrollBlocked;
    }

    public boolean isScrollBlocked(){
        return isScrollBlocked;
    }

    public Map getMap(){
        return map;
    }

    @Override
    public void render(float delta) {
        update(delta);
        drawWorld();
        drawInterfaces();
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
}
