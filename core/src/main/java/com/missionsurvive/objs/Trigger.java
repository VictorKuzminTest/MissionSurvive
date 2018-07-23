package com.missionsurvive.objs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.commands.Command;

/**
 * Created by kuzmin on 09.06.18.
 * Auto scrolling of the screen to some target.
 */
public class Trigger implements Bot{

    public static final float SCROLLING_TICK = 0.03f;

    private PlatformerScenario scenario;

    private int speedScrollingX = 2;
    private int x;
    private int endScrollX;
    private int numTilesToScroll = 15;

    private float scrollingTickTime;

    public Trigger(Scenario scenario, int x, int y){
        this.x = x;
        endScrollX = x + numTilesToScroll * 16;
        this.scenario = (PlatformerScenario) scenario;
    }

    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {

    }

    @Override
    public void moving(float deltaTime, MapTer[][] mapTer, MapEditor mapEditor,
                       int worldWidth, int worldHeight) {
        scrollingTickTime += deltaTime;
        while(scrollingTickTime > SCROLLING_TICK){
            scrollingTickTime -= SCROLLING_TICK;

            x += getDistX();

            if(speedScrollingX <= 0){
                showEndLevelPanel();
                scenario.removeBot(this, 0);
            }
            else{
                mapEditor.horizontScroll(0, speedScrollingX);
                scenario.getHero().setX(scenario.getHero().getX() - speedScrollingX);
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
    public void collide(Hero hero) {

    }

    @Override
    public void hit(Weapon weapon) {

    }

    @Override
    public Decorator getDecorator() {
        return null;
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
}
