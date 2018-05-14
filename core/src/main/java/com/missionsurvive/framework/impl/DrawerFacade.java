package com.missionsurvive.framework.impl;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.objs.Bot;
import com.missionsurvive.objs.Wreckage;
import com.missionsurvive.objs.actors.Hero;

import java.util.List;

/**
 * Created by kuzmin on 03.05.18.
 */

public class DrawerFacade {

    public void drawHero(Hero hero, SpriteBatch batch){
        if(hero != null){
            if (hero.isIrresistible()) {
                hero.getDecorator().drawObject(batch);
            }
            else{
                hero.drawObject(batch, 0, 0, 0, 0);
            }
            //Hitbox and vector of legs of a hero:
            /*Hitbox hitbox = hero.getHitbox();
            g.drawRect(hitbox.getLeft(), hitbox.getTop(),
                    hitbox.getHitboxWidth(), hitbox.getHitboxHeight(), 0, hitbox.getPaint()); //draw hitbox.
            g.drawRect(hitbox.getCenterX(), hitbox.getBottom(),  //draw colliding center.
                    2, 2, Color.RED, hitbox.getPaint());*/

            int numBullets = hero.getWeapon().size();
            for(short i = 0; i < numBullets; i++){
                hero.getWeapon().get(i).drawObject(batch, 0, 0, 0, 0);
            }
        }
    }

    public void drawWreckages(List<Bot> wreckages, SpriteBatch batch){
        int numWreckages = wreckages.size();
        for(int i = 0; i < numWreckages; i++){
            Bot wreckage = wreckages.get(i);
            if(wreckage.isAction() == Wreckage.ACTION_START){
                wreckage.getDecorator().drawObject(batch);
            }
            else{
                wreckage.drawObject(batch, 0, 0, 0, 0);
            }
            //drawing wreckage on the ground:
            wreckage.drawObject(batch, 0, 0);
            //draw hitbox:
            int left = wreckage.getLeft();
            int top = wreckage.getTop();
            int width = wreckage.getHitboxWidth();
            int height = wreckage.getHitboxHeight();
            /*g.drawRect(left, top, width, height, Color.BLACK, null);*/
        }
    }

    public void drawBots(List<Bot> bots, SpriteBatch batch){
        int numBots = bots.size();
        for(int i = 0; i < numBots; i++){
            bots.get(i).drawObject(batch, 0, 0, 0, 0);
            //draw hitbox:
            /*int left = bots.get(i).getLeft();
            int top = bots.get(i).getTop();
            int width = bots.get(i).getHitboxWidth();
            int height = bots.get(i).getHitboxHeight();
            g.drawRect(left, top, width, height, Color.BLACK, null);*/
        }
    }
}
