package com.missionsurvive.objs.triggers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.Bot;
import com.missionsurvive.objs.Weapon;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.objs.actors.L3B;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.utils.Sounds;

public class TriggerL3B implements Bot {

    public static final float SCROLLING_TICK = 0.03f;

    private PlatformerScenario scenario;
    private Bot bot;

    private int speedScrollingX = 2;
    private int x;
    private int y;
    private int endScrollX;
    private int numTilesToScroll = 15;
    //offset to spawn the boss.
    private int offsetToSpawn;
    private int hp;

    private float scrollingTickTime;

    private boolean isSpawned;

    public TriggerL3B(Scenario scenario, int x, int y, int hp){
        Sounds.playBossMusic();
        this.x = x;
        this.y = y;
        offsetToSpawn = x + (9 * 16);
        endScrollX = x + numTilesToScroll * 16;
        this.scenario = (PlatformerScenario) scenario;
        this.hp = hp;
    }

    @Override
    public void moving(float deltaTime, MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight) {
        scrollingTickTime += deltaTime;
        while(scrollingTickTime > SCROLLING_TICK){
            scrollingTickTime -= SCROLLING_TICK;

            if(speedScrollingX > 0){
                x += getDistX();

                mapEditor.horizontScroll(0, speedScrollingX);
                scenario.getHero().setX(scenario.getHero().getX() - speedScrollingX);

                if(x >= offsetToSpawn ){
                    if(!isSpawned){
                        bot = new L3B("l3b", mapEditor, offsetToSpawn, y, hp);
                        scenario.addBot(bot, 0);
                        isSpawned = true;
                    }
                }
            }

            if(bot != null){
                if(bot.isAction() == L3B.DEAD){
                    scenario.getGameScreen().pause(true);
                    Sounds.stopBossMusic();
                    showEndLevelPanel();
                    scenario.removeBot(this, 0);
                }
            }
        }
    }

    public void showEndLevelPanel(){
        int numPanels = scenario.getControlScenario().getControlPanels().size();
        for(int i  = 0; i < numPanels; i++){
            ControlPanel cp = scenario.getControlScenario().getControlPanels().get(i);
            if(cp.getName().equalsIgnoreCase("EndLevelMenu")){
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
