package com.missionsurvive.objs;

import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.scenarios.Scenario;

/**
 * Created by kuzmin on 01.05.18.
 */

public interface Bot extends GameObject{

    public void moving(float deltaTime, MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight);

    public void collide(Hero hero);

    public void hit(Weapon weapon);

    public void jump(int destX, int destY);

    public void run();

    public int getX();

    public int getY();

    public int getSpriteWidth();

    public int getSpriteHeight();

    public int getLeft();

    public int getRight();

    public int getTop();

    public int getBottom();

    public int getHitboxWidth();

    public int getHitboxHeight();

    public void setScenario(Scenario platformerScenario);

    public int isAction();
}
