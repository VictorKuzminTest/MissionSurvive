package com.missionsurvive.scenarios;

import com.missionsurvive.framework.TouchControl;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.Bot;
import com.missionsurvive.objs.EnemyBullet;
import com.missionsurvive.objs.Weapon;
import com.missionsurvive.objs.actors.Hero;

import java.util.List;

/**
 * Created by kuzmin on 03.05.18.
 */

public class PlatformerScenario implements Scenario {

    private Hero hero;

    public void shotEnemy(Weapon weapon){

    }

    public void shotPlayer(EnemyBullet bullet){

    }

    public void removeBot(Bot bot, int criteria) {

    }

    public Hero getHero(){
        return hero;
    }

    public int getTargetX(){
        if(hero !=  null){
            return hero.getCenterX();
        }
        else{
            return -1;
        }
    }

    public int getTargetY(){
        if(hero !=  null){
            return hero.getCenterY();
        }
        else{
            return -1;
        }
    }

    public void collideObject() {

    }

    public void setHorizontal(boolean isHorizontal){}

    public void setLives(int lives){
    }

    public int getLives(){
        return 0;
    }

    public void shootEnemy(Hero hero, int lastDownX, int lastDownY){}

    @Override
    public void placeObject(int x, int y) {

    }

    @Override
    public void update(MapTer[][] level1Ter, MapEditor mapEditor, int worldWidth, int worldHeight, TouchControl touchControl, float deltaTime) {

    }

    @Override
    public void addBot(Bot bot, int criteria) {

    }

    @Override
    public List<Bot> getBots(int criteria) {
        return null;
    }
}
