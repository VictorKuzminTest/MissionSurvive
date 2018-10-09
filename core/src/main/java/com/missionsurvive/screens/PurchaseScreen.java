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
import com.missionsurvive.framework.Listener;
import com.missionsurvive.framework.impl.ActionButton;
import com.missionsurvive.framework.impl.ListButtons;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.scenarios.PlayScript;
import com.missionsurvive.scenarios.commands.Command;
import com.missionsurvive.scenarios.controlscenarios.ControlScenario;
import com.missionsurvive.scenarios.controlscenarios.GameMenuCS;
import com.missionsurvive.scenarios.controlscenarios.PurchaseCS;
import com.missionsurvive.utils.Assets;
import com.missionsurvive.utils.Progress;
import com.missionsurvive.utils.Sounds;

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
    private String action; //which screen to go (Platformer or main menu).

    private int adsWidth = 102;
    private int adsHeight = 90;

    //for transforming real pixel touch coords into logic pixel coords:
    private float scaleX;
    private float scaleY;
    private float scrollingTickTime;
    private float getPurchaseTickTime;

    public PurchaseScreen(MSGame game, PlayScript playScript, String action){
        this.game = game;
        this.playScript = playScript;
        this.action = action;
        parseAction();

        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(MSGame.SCREEN_WIDTH, MSGame.SCREEN_HEIGHT, gameCam);

        texture = Assets.getTextures()[Assets.getWhichTexture("art")];

        scaleX = (float)480 / Gdx.graphics.getBackBufferWidth();
        scaleY = (float)320 / Gdx.graphics.getBackBufferHeight();

        adsPics = new ListAds(10, 80, 115, 320, 100);
        populateAdsPics();
        controlScenario = new PurchaseCS();
    }

    /**
     * If we receive String like ":purchase:ToLevel4Beginner:", we have to
     * separate "ToLevel4Beginner" from "purchase" and assign the new value to action.
     */
    private void parseAction(){
        if(action != null){
            if(!action.equalsIgnoreCase("")){
                String fragmentDelims = "\\:";

                String[] fragmentTokens = action.split(fragmentDelims);
                int len = (fragmentTokens.length);

                for(int whichChar = 0; whichChar < len; whichChar++) {
                    if (fragmentTokens[whichChar].contains("\n")) {
                        fragmentTokens[whichChar] = fragmentTokens[whichChar].replaceAll("\n", "");
                    }
                    if(fragmentTokens[whichChar].equalsIgnoreCase("purchase")){
                        action = fragmentTokens[whichChar + 1];
                    }
                }
            }
        }
    }

    public void populateAdsPics(){
        adsPics.addNewButton("ads", 0, 0,
                1, 1, adsWidth, adsHeight, null);
        adsPics.addNewButton("ads", 1, 0,
                1 + (adsWidth + 2) * 1, 1, adsWidth, adsHeight, null);
        adsPics.addNewButton("ads", 2, 0,
                1, 1 + (adsHeight + 2) * 1, adsWidth, adsHeight, null);
        adsPics.addNewButton("ads", 3, 0,
                1 + (adsWidth + 2) * 1, 1 + (adsHeight + 2) * 1, adsWidth, adsHeight, null);
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
        getPurchaseTickTime += delta;
        while(getPurchaseTickTime > GET_PURCHASE_TICK){
            getPurchaseTickTime -= GET_PURCHASE_TICK;

            if(Progress.isPurchased()){
                if(action == null){
                    game.getScreenFactory().newScreen("GameMenuScreen", null, null);
                }
                else{
                    Assets.setCurrentLevel(this.action);
                    if(action.equalsIgnoreCase("ToLevel4Beginner")){
                        game.setScreen(game.getScreenFactory().newScreen("ScrollerScreen",
                                null, Progress.BEGINNER));
                    }
                    else if(action.equalsIgnoreCase("ToLevel4Experienced")){
                        game.setScreen(game.getScreenFactory().newScreen("ScrollerScreen",
                                null, Progress.EXPERIENCED));
                    }
                }
            }
        }
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

    private class ListAds{
        private ArrayList<Button> buttons;

        private int startX;  //listing startX
        private int startY;   //listing startY
        private int listingWidth;
        private int listingHeight;
        private int spaceBetweenButtons; //space between buttons in pixels
        private int endOfListX;

        public ListAds(int spaceBetweenButtons,
                           int startX, int startY,
                           int listingWidth, int listingHeight){
            buttons = new ArrayList<Button>();

            this.startX = startX;
            this.startY = startY;
            this.listingWidth = listingWidth;
            this.listingHeight = listingHeight;

            this.spaceBetweenButtons = spaceBetweenButtons;
        }

        public void addNewButton(String assetName,
                                 int col, int row, int assetStartX, int assetStartY,
                                 int buttonWidth, int buttonHeight, Command command){
            int startX;
            int startY;

            startX = this.startX + this.buttons.size() * (buttonWidth + spaceBetweenButtons);
            startY = this.startY;

            Button button = new ActionButton(assetName,
                    startX, startY, assetStartX, assetStartY, buttonWidth, buttonHeight, command);
            buttons.add(button);

            endOfListX  = startX + button.getButtonWidth();
        }

        public void drawAds(SpriteBatch batch){
            int numbuttons = buttons.size();
            for(int whichButton = 0; whichButton < numbuttons; whichButton++){
                Button button = buttons.get(whichButton);

                int buttonStartX = button.getStartX();
                int buttonAssetWidth = button.getButtonWidth();
                int buttonStartY = button.getStartY();
                int buttonAssetHeight = button.getButtonHeight();

                if(!GeoHelper.overlapRectangles(buttonStartX, buttonStartY, buttonAssetWidth, buttonAssetHeight,
                        this.startX, this.startY, this.listingWidth, this.listingHeight)){
                    //button is absolutely beyond listing
                    continue;
                }
                else{
                    int offsetX = 0;
                    int offsetY = 0;
                    int offsetWidth = 0;
                    int offsetHeight = 0;

                    if(buttonStartX <= this.startX){ //часть иконки за пределами листинга левой части листинга.
                        offsetX = this.startX - buttonStartX;
                        offsetWidth = -offsetX;
                    }
                    if(buttonStartX + buttonAssetWidth > this.startX + listingWidth){
                        offsetWidth = ((buttonStartX + buttonAssetWidth) - (this.startX  + listingWidth)) * -1;
                    }
                    if(buttonStartY <= this.startY){
                        offsetY = this.startY - buttonStartY;
                        offsetHeight = -offsetY;
                    }
                    if(buttonStartY + buttonAssetHeight > this.startY + listingHeight){
                        offsetHeight = ((buttonStartY + buttonAssetHeight) - (this.startY  + listingHeight)) * -1;
                    }
                    //drawing button with offset
                    button.drawButton(batch, offsetX, offsetY, offsetWidth, offsetHeight);
                }
            }
        }

        public void scroll(float distanceX, float distanceY){
            endOfListX += distanceX;

            int len = buttons.size();
            for(int whichButton = 0; whichButton < len; whichButton++){
                Button button = buttons.get(whichButton);

                button.setStartX(distanceX);
                button.setStartY(distanceY);

                if((button.getStartX() + button.getButtonWidth() + spaceBetweenButtons) <= startX){
                    button.setStartX((endOfListX + spaceBetweenButtons) - button.getStartX());

                    endOfListX = button.getStartX() + button.getButtonWidth();
                }
            }
        }
    }
}
