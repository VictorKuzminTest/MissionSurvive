package com.missionsurvive.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.TouchControl;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.map.ParallaxBackground;
import com.missionsurvive.map.ParallaxCamera;
import com.missionsurvive.map.ParallaxLayer;
import com.missionsurvive.map.ScrollMap;
import com.missionsurvive.objs.GameObject;
import com.missionsurvive.objs.Obstacle;
import com.missionsurvive.objs.actors.Moto;
import com.missionsurvive.scenarios.ControlScenario;
import com.missionsurvive.scenarios.MapEditorCS;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.ScrollerScenario;
import com.missionsurvive.tests.MapEditorTest;
import com.missionsurvive.utils.Assets;

import java.util.ArrayList;

/**
 * Created by kuzmin on 04.05.18.
 */

public class ScrollerScreen implements Screen {

    private MSGame game;
    private Texture bgTexture;
    private SpriteBatch batch;
    private ParallaxCamera gameCam;
    private Viewport gamePort;
    private TiledMapRenderer renderer;
    private ParallaxBackground background;

    private int noStartCol = -1, noEndCol = -1;
    private int screenWidth;
    private int screenTranparency;
    private float darkenTickTime = 0, darkenTick = 0.1f;

    private MapTer[][] level1Ter;
    private MapTer[][] level2Ter;
    private MapTer[][] level3Ter;
    ScrollMap scrollLevel1Map;
    ScrollMap scrollLevel2Map;
    ScrollMap scrollLevel3Map;

    private float blinkTickTime, blinkTick = 0.1f; //floats for obstacle blink.
    private boolean blink;

    private int tilesetLevelToDraw; //переменная отвечает, какой тайлсет рисуется. 1 - level1, 2 - level2, 3 - level3, default - все.

    /*private GameObject level1TileRect = new TileRect(Color.GREEN, 255);
    private GameObject level2TileRect = new TileRect(Color.BLUE , 255);
    private GameObject level3TileRect = new TileRect(Color.YELLOW, 255);
    private GameObject editingTileRect = new TileRect(Color.BLACK, 127);
    private GameObject terBlockTileRect = new TileRect(Color.RED, 127);
    private GameObject terLadderTileRect = new TileRect(Color.YELLOW, 127);*/

    private MapEditor mapEditor;
    private ControlScenario controlScenario;
    TouchControl touchControl;
    private int worldWidth, worldHeight, level2Width, level2Height, level3Width, level3Height;
    Moto moto;
    private ScrollerScenario scrollerScenario;
    private boolean isScrollMap;

    private boolean isHeroControl; //переменная указывает на то, что управляем ли мы героем на карте или строим собственно карту.

    public ScrollerScreen(MSGame game) {
        this.game = game;

        batch = game.getSpriteBatch();
        scrollerScenario = new ScrollerScenario(this, 500, 220);
        mapEditor = new MapEditor(50, 23);
        touchControl = new TouchControl();
        controlScenario = new MapEditorCS();
        getMapThroughMapEditor();

        bgTexture = Assets.getTextures()[Assets.getWhichTexture("ocean")];

        /*Assets.controlScenario = controlScenario;*/
        screenWidth = MSGame.SCREEN_WIDTH;

        gameCam = new ParallaxCamera(480, 320); //extends OrthographicCamera
        gamePort = new StretchViewport(480, 320, gameCam);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        renderer = new OrthogonalTiledMapRenderer(mapEditor.getMap());

        background = new ParallaxBackground(new ParallaxLayer[]{
                new ParallaxLayer(mapEditor.getlev3(), new Vector2(1, 1), new Vector2(0, 0)),
        }, 480, 320, new Vector2(50, 0));
    }

    /**
     * First, we check if buttons are touched. If not, then inside each method of touchControl we set
     * isMapTouchedDown = true (when TouchEvent.TOUCH_DOWN).
     * While TouchEvent.TOUCH_DRAGGED and TouchEvent.TOUCH_UP we check and do something if only
     * isMapTouchedDown(not buttons). When TouchEvent.TOUCH_UP, we set isMapTouchedDown = false.
     * @param deltaTime
     */
    public void update(float deltaTime) {
        controlScenario.touchPanels(touchControl, this);
        if(!controlScenario.isTouchingPanels()){
            if(!isHeroControl){
                switch(tilesetLevelToDraw){
                    case 1:
                        if(isScrollMap) touchControl.scrollMapLayers(controlScenario,
                                scrollLevel1Map, null, null);
                        else controlScenario.action(touchControl.touchMap(level1Ter, scrollLevel1Map,
                                worldHeight, worldWidth), 0);
                        break;
                    case 2:
                        if(isScrollMap)touchControl.scrollMapLayers(controlScenario,
                                null, scrollLevel2Map, null);
                        else controlScenario.action(touchControl.touchMap(level2Ter, scrollLevel2Map,
                                level2Height, level2Width), 0);
                        break;
                    case 3:
                        if(isScrollMap)touchControl.scrollMapLayers(controlScenario,
                                null, null, scrollLevel3Map);
                        else controlScenario.action(touchControl.touchMap(level3Ter, scrollLevel3Map,
                                level3Height, level3Width), 0);
                        break;
                    default:touchControl.scrollMapLayers(controlScenario,
                            scrollLevel1Map, scrollLevel2Map, scrollLevel3Map);
                        break;
                }
            }
            else{
                scrollerScenario.update(level1Ter, mapEditor, worldWidth, worldHeight,
                        touchControl, deltaTime);
            }
        }
    }

    /**
     * the method is used only to set the blinking of the obstacle.
     * @param deltaTime
     */
    public void updateObstacleBlinking(float deltaTime){
        blinkTickTime += deltaTime;
        while(blinkTickTime > blinkTick) {
            blinkTickTime -= blinkTick;

            if(blink) blink = false;
            else blink = true;
        }
    }

    private void drawWorld(float delta){
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch(tilesetLevelToDraw){
            case 1: /*drawTileset(level1Ter, scrollLevel1Map, level1TileRect, true);*/
                break;
            case 2: /*drawTileset(level2Ter, scrollLevel2Map, level2TileRect, true);*/
                break;
            case 3: /*drawTileset(level3Ter, scrollLevel3Map, level3TileRect, true);*/
                break;
            default:
                background.render(delta);
                //draw tileset for scroller:
                /*drawRoundedTileset(level1Ter, scrollLevel1Map, level1TileRect, false);*/
                break;
        }

        if(moto != null){

            drawTear(scrollerScenario.getTear());
            drawObstacles(scrollerScenario.getObstacles());

            if(moto.isDead()){
                if(moto.getLives() < 0){ //lost continue
                    /*g.clear(screenTranparency, 255, 0, 0);*/
                }
                else{ //just lost life
                    /*g.clear(screenTranparency, 0, 0, 0);*/
                }
            }
        }
    }

    /**
     * draw obstacles. Draws moto in front of behind osbstacles.
     * @param obstacles
     */
    public void drawObstacles(ArrayList<Obstacle> obstacles){
        boolean isMotoDrawn = false;

        for(Obstacle obstacle : obstacles) {
            if(obstacle.isPlaced()){
                if(screenWidth < obstacle.getScreenX()){ //if obstacle is beyond the screen.
                    if(blink) { //we draw blinking obstacle on the right side of the screen.
                        obstacle.drawObject(batch, screenWidth - obstacle.getSpriteWidth(), 0);
                    }
                }
                else{ //if obstacle is on the screen
                    if(obstacle.getScreenY() > (moto.getScreenY() + 30)){
                        //then draw moto and set isMotoDrawn to true.
                        // + 30 because sprite height of moto is greater then car's about
                        // 30 pixels (90 - 64 = 26).
                        if(!isMotoDrawn){
                            moto.drawObject(batch, 0, 0, 0, 0);
                            isMotoDrawn = true;
							/*Hitbox motoHitbox = moto.getHitbox();
							g.drawRect(motoHitbox.getLeft(), motoHitbox.getTop(), motoHitbox.getHitboxWidth(),
							motoHitbox.getHitboxHeight(), Color.BLACK, null);*/
                        }
                        obstacle.drawObject(batch, 0, 0, 0, 0);
						/*Hitbox obstacleHitbox = obstacle.getHitbox();
						g.drawRect(obstacleHitbox.getLeft(), obstacleHitbox.getTop(), obstacleHitbox.getHitboxWidth(),
							obstacleHitbox.getHitboxHeight(), Color.MAGENTA, null);*/
                    }
                    else{
                        obstacle.drawObject(batch, 0, 0, 0, 0);
						/*Hitbox obstacleHitbox = obstacle.getHitbox();
						g.drawRect(obstacleHitbox.getLeft(), obstacleHitbox.getTop(), obstacleHitbox.getHitboxWidth(),
							obstacleHitbox.getHitboxHeight(), Color.MAGENTA, null);*/
                    }
                }
            }
        }
        if(!isMotoDrawn){
            moto.drawObject(batch, 0, 0, 0, 0);
			/*Hitbox motoHitbox = moto.getHitbox();
			g.drawRect(motoHitbox.getLeft(), motoHitbox.getTop(), motoHitbox.getHitboxWidth(),
					motoHitbox.getHitboxHeight(),
					Color.BLACK, null);*/
        }
    }


    /**
     * draw tear. No moto is considered.
     * @param obstacles
     */
    public void drawTear(ArrayList<Obstacle> obstacles){
        for(Obstacle obstacle : obstacles) {
            if(obstacle.isPlaced()){
                if(screenWidth < obstacle.getScreenX()){ //if obstacle is beyond the screen.
                    if(blink) { //we draw blinking obstacle on the right side of the screen.
                        obstacle.drawObject(batch, screenWidth - obstacle.getSpriteWidth(), 0);
                    }
                }
                obstacle.drawObject(batch, 0, 0, 0, 0);
            }
			/*Hitbox obstacleHitbox = obstacle.getHitbox();
			g.drawRect(obstacleHitbox.getLeft(), obstacleHitbox.getTop(), obstacleHitbox.getHitboxWidth(),
							obstacleHitbox.getHitboxHeight(), Color.MAGENTA, null);*/
        }
    }

    public void drawTileset(MapTer[][] mapTer, ScrollMap scrollMap, GameObject tileRect, boolean isEditing){
        /*int startRowOffset = scrollMap.getStartRowOffset(); //Строка, с которой начинается отрисовка тайлов.
        int startColOffset = scrollMap.getStartColOffset(); //Колонка, с которой начинается отрисовка тайлов.
        int endRowOffset = scrollMap.getEndRowOffset();  //Строка, которой заканчивается отрисовка тайлов.
        int endColOffset = scrollMap.getEndColOffset();   //Колонка, которой заканчивается отрисовка тайлов.

        for(int row = startRowOffset; row < endRowOffset; row++){      //Отрисовка тайлов карты.
            if (row >= worldHeight) break;  //we don't need to keep drawing, becouse there is nothing else to draw.
            for(int col = startColOffset; col < endColOffset; col++){
                if(col >= worldWidth) break; // the same: we don't need keep drawing.//
                //ОСНОВНОЙ ЦИКЛ...
                if(mapTer[row][col].getMapObject() != null){ //Если на этом участке карты есть объект карты для отрисовки.
                    mapTer[row][col].drawTerrain(g, Assets.pixmaps, col, row, scrollMap.getWorldOffsetX(), scrollMap.getWorldOffsetY());

                    if(mapTer[row][col].isEditing()){  //if данный участок карты выделен для редактирования.
                        editingTileRect.drawObject(g, Assets.pixmaps, col, row, scrollMap.getWorldOffsetX(), scrollMap.getWorldOffsetY());
                    }
                    if(mapTer[row][col].isBlocked()){   //if current tile is blocked.
                        terBlockTileRect.drawObject(g, Assets.pixmaps, col, row, scrollMap.getWorldOffsetX(), scrollMap.getWorldOffsetY());
                    }
                    if(mapTer[row][col].isLadder()){   //if current tile is ladder.
                        terLadderTileRect.drawObject(g, Assets.pixmaps, col, row, scrollMap.getWorldOffsetX(), scrollMap.getWorldOffsetY());
                    }
                }
                else if(isEditing){ //в ином случае: отрисовываем tileRect определенного цвета.
                    tileRect.drawObject(g, null, col, row, scrollMap.getWorldOffsetX(), scrollMap.getWorldOffsetY());

                    if(mapTer[row][col].isEditing()){  //if данный участок карты выделен для редактирования.
                        editingTileRect.drawObject(g, Assets.pixmaps, col, row, scrollMap.getWorldOffsetX(), scrollMap.getWorldOffsetY());
                    }
                    if(mapTer[row][col].isBlocked()){  //if current tile is blocked.
                        terBlockTileRect.drawObject(g, Assets.pixmaps, col, row, scrollMap.getWorldOffsetX(), scrollMap.getWorldOffsetY());
                    }
                    if(mapTer[row][col].isLadder()){   //if current tile is ladder.
                        terLadderTileRect.drawObject(g, Assets.pixmaps, col, row, scrollMap.getWorldOffsetX(), scrollMap.getWorldOffsetY());
                    }
                }
            }
        }*/
    }

    public void drawRoundedTileset(MapTer[][] mapTer, ScrollMap scrollMap, GameObject tileRect, boolean isEditing){
        /*int startRowOffset = scrollMap.getStartRowOffset(); //Строка, с которой начинается отрисовка тайлов.
        int startColOffset = scrollMap.getStartColOffset(); //Колонка, с которой начинается отрисовка тайлов.
        int endRowOffset = scrollMap.getEndRowOffset();  //Строка, которой заканчивается отрисовка тайлов.
        int endColOffset = scrollMap.getEndColOffset();  //Колонка, которой заканчивается отрисовка тайлов.

        for(int row = startRowOffset; row < endRowOffset; row++){      //Отрисовка тайлов карты.
            int rowToDraw = row;
            if(row >= worldHeight) rowToDraw = row % (worldHeight - 1);

            for(int col = startColOffset; col < endColOffset; col++){

                if(col >= noStartCol && col <= noEndCol){ //we check if we can or cannot draw row (col) when tiles are teared.
                    continue;
                }

                int colToDraw = col;
                if(col >= worldWidth) colToDraw = col % (worldWidth - 1);

                if(mapTer[rowToDraw][colToDraw].getMapObject() != null){ //Если на этом участке карты есть объект карты для отрисовки.
                    mapTer[rowToDraw][colToDraw].drawTerrain(g, Assets.pixmaps, col, row, scrollMap.getWorldOffsetX(), scrollMap.getWorldOffsetY());
                }
            }
        }*/
    }

    private void drawInterfaces(float deltaTime){
        controlScenario.drawPanels(batch);
    }

    public void putPlayer(int x, int y){
        scrollerScenario.placeObject(x, y);
        moto = scrollerScenario.getMoto();
    }


    public boolean isHeroControl(){
        return isHeroControl;
    }


    public void setHeroControl(boolean isHeroControl){
        /*if(isHeroControl()){
            Sounds.theme4.stop();
        }
        else{
            Sounds.theme4.setLooping(true);
            Sounds.theme4.setVolume(1);
            Sounds.theme4.play();
        }*/
        this.isHeroControl = isHeroControl;
        //КОСТЫЛЬ, который работает, чтобы не было разрыва тайлсета, когда отрисовывается
        //карта круговая. В самом начале кругового цикла между "склееными" тайлсетами происходит разрыв,
        //который потом исчезает. Поэтому при первом запуске мы сразу делаем оффсет на ширину мира:
        scrollLevel1Map.setRoundWorldOffset(worldWidth * (16 - 1), 0);
    }


    public void setTilesetLevelToDraw(int tilesetLevelToDraw){
        this.tilesetLevelToDraw = tilesetLevelToDraw;
    }


    public MapEditor getMapEditor(){
        return mapEditor;
    }


    public void setScrollMap(boolean isScrollMap){
        this.isScrollMap = isScrollMap;
    }

    public boolean isScrollMap(){
        return isScrollMap;
    }

    public void setNoStartCol(int noStartCol){
        this.noStartCol = noStartCol;
    }

    public void setNoEndCol(int noEndCol){
        this.noEndCol = noEndCol;
    }

    public void setMap(){
        getMapThroughMapEditor();
    }

    public void getMapThroughMapEditor(){
        level1Ter = mapEditor.getLevel1Ter();
        /*level2Ter = mapEditor.getLevel2Ter();
        level3Ter = mapEditor.getLevel3Ter();*/

        scrollLevel1Map = mapEditor.getScrollLevel1Map();
        /*scrollLevel2Map = mapEditor.getScrollLevel2Map();
        scrollLevel3Map = mapEditor.getScrollLevel3Map();*/

        worldWidth = level1Ter[0].length;
        worldHeight = level1Ter.length;
        level2Width = level2Ter[0].length;
        level2Height = level2Ter.length;
        level3Width = level3Ter[0].length;
        level3Height = level3Ter.length;
    }

    public void newMap(int width, int height){
        /*mapEditor.createMap(width, height, true);*/
        setMap();
    }

    /**
     * Darken screen when hero is dead.
     * @param deltaTime
     */
    public void darkenScreen(float deltaTime){
        darkenTickTime += deltaTime;
        while(darkenTickTime > darkenTick){
            darkenTickTime -= darkenTick;

            screenTranparency += 10;

            if(screenTranparency >= 255){
                screenTranparency = 0;

                scrollerScenario.newAttempt();

                noStartCol = -1;
                noEndCol = -1;
            }
        }
    }


    @Override
    public void dispose() {
        /*Sounds.theme4.stop();*/
    }


    public Scenario getScenario() {
        return null;
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        drawWorld(delta);
        drawInterfaces(delta);
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

}
