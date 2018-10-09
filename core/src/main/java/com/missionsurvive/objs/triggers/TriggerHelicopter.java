package com.missionsurvive.objs.triggers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.framework.TouchControl;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.Bot;
import com.missionsurvive.objs.Helicopter;
import com.missionsurvive.objs.Weapon;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.utils.Sounds;

public class TriggerHelicopter implements Bot {

    private PlatformerScenario scenario;
    private Hero hero;
    private TouchControl touchControl;
    private Bot bot;

    private int runningPointX = 0;
    private int runningOffsetX = 150;

    public TriggerHelicopter(Bot bot, Scenario scenario, int x, int y){
        this.bot = bot;
        this.scenario = (PlatformerScenario) scenario;
        ((PlatformerScenario) scenario).setPlayerControl(false);
        hero = this.scenario.getHero();
        touchControl = ((PlatformerScenario) scenario).getTouchControl();

        runningPointX = hero.getX() - runningOffsetX;
    }

    @Override
    public void moving(float deltaTime, MapTer[][] mapTer,
                       MapEditor mapEditor, int worldWidth, int worldHeight) {

        touchControl.moveHero(hero, runningPointX, 0);

        if(hero.isAction() == Hero.ACTION_IDLE){
            hero.setDirection(Hero.DIRECTION_RIGHT);
            hero.setActionAndAnimationFrames(Hero.ACTION_IDLE_BACK,
                    Hero.SPRITES_IDLE_BACK, 0);
        }
        if(bot.isAction() == Helicopter.ACTION_SHOTDOWN){
            if(hero.getDirection() != Hero.DIRECTION_LEFT){
                hero.setAction(Hero.ACTION_IDLE);
                hero.setDirection(Hero.DIRECTION_LEFT);
                hero.setActionAndAnimationFrames(Hero.ACTION_IDLE_BACK,
                        Hero.SPRITES_IDLE_BACK, 0);
            }
        }
        else if(bot.isAction() == Helicopter.ACTION_BEYOND_SCREEN){
            scenario.getGameScreen().pause(true);
            Sounds.disposeMusic();
            showEndLevelPanel();
            scenario.removeBot(this, 0);
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
