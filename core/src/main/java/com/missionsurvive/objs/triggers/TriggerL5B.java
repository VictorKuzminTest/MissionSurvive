package com.missionsurvive.objs.triggers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.Bot;
import com.missionsurvive.objs.Weapon;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.objs.actors.L1B;
import com.missionsurvive.objs.actors.L5B;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.SpawnScenario;
import com.missionsurvive.utils.Sounds;

public class TriggerL5B implements Bot {

    public static final float SCROLLING_TICK = 0.03f;

    private PlatformerScenario scenario;
    private Bot bot;

    private int speedScrollingX = 2;
    private int x;
    private int endScrollX;
    private int numTilesToScroll = 15;
    private int difficulty;

    private float scrollingTickTime;

    public TriggerL5B(Bot bot, Scenario scenario, int x, int y, int difficulty){
        Sounds.playBossMusic();
        this.x = x;
        endScrollX = x + numTilesToScroll * 16;
        this.scenario = (PlatformerScenario) scenario;
        this.bot = bot;
        this.difficulty = difficulty;
    }

    @Override
    public void moving(float deltaTime, MapTer[][] mapTer, MapEditor mapEditor,
                       int worldWidth, int worldHeight) {
        scrollingTickTime += deltaTime;
        while(scrollingTickTime > SCROLLING_TICK){
            scrollingTickTime -= SCROLLING_TICK;

            if(speedScrollingX > 0){
                x += getDistX();

                mapEditor.horizontScroll(0, speedScrollingX);
                scenario.getHero().setX(scenario.getHero().getX() - speedScrollingX);
            }

            if(bot.isAction() == L5B.ACTION_DEAD){
                scenario.getGameScreen().pause(true);
                Sounds.stopBossMusic();
                showEndLevelPanel();
                scenario.removeBot(this, 0);
            }
        }
    }

    public void showEndLevelPanel(){
        switch (difficulty){
            case SpawnScenario.DIFFICULTY_BEGINNER:
                activateControlPanel("EndBeginner");
                break;
            case SpawnScenario.DIFFICULTY_EXPERIENCED:
                activateControlPanel("EndLevelMenu");
                break;
        }
    }

    private void activateControlPanel(String panelName){
        int numPanels = scenario.getControlScenario().getControlPanels().size();
        for(int i = 0; i < numPanels; i++){
            ControlPanel cp = scenario.getControlScenario().getControlPanels().get(i);
            if(cp.getName().equalsIgnoreCase(panelName)){
                cp.setActivated(true);
            }
        }
    }

    public int getDistX(){
        if((x + speedScrollingX) > endScrollX){
            speedScrollingX = endScrollX - x;
        }
        return speedScrollingX;
    }

    @Override
    public void collide(Hero hero) {

    }

    @Override
    public void hit(Weapon weapon) {

    }

    @Override
    public void jump(int destX, int destY) {

    }

    @Override
    public void run() {

    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getSpriteWidth() {
        return 0;
    }

    @Override
    public int getSpriteHeight() {
        return 0;
    }

    @Override
    public int getLeft() {
        return 0;
    }

    @Override
    public int getRight() {
        return 0;
    }

    @Override
    public int getTop() {
        return 0;
    }

    @Override
    public int getBottom() {
        return 0;
    }

    @Override
    public int getHitboxWidth() {
        return 0;
    }

    @Override
    public int getHitboxHeight() {
        return 0;
    }

    @Override
    public void setScenario(Scenario platformerScenario) {

    }

    @Override
    public int isAction() {
        return 0;
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {

    }

    @Override
    public void drawObject(SpriteBatch batch, int screenX, int screenY) {

    }

    @Override
    public boolean onTouch() {
        return false;
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public Decorator getDecorator() {
        return null;
    }
}
