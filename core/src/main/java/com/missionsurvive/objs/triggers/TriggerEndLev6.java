package com.missionsurvive.objs.triggers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.map.Map;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.Bot;
import com.missionsurvive.objs.Weapon;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.commands.Command;
import com.missionsurvive.scenarios.commands.LoadFromAssetsLibGdx;
import com.missionsurvive.utils.Assets;

public class TriggerEndLev6 implements Bot {

    private Map map;

    public TriggerEndLev6(){
        MSGame game = Assets.getGame();
        map = new MapEditor();
        Command loadMapCommand = new LoadFromAssetsLibGdx();
        if(map.loadMap(loadMapCommand.execute(null, "levs/end"))){
            game.setScreen(game.getScreenFactory()
                    .newScreen("EndScreen", map, null));
        }
    }

    @Override
    public void moving(float deltaTime, MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight) {

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
