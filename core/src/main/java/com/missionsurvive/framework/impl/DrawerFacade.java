package com.missionsurvive.framework.impl;

/**
 * Created by kuzmin on 03.05.18.
 */

public class DrawerFacade {

    public void drawHero(HeroNew hero, Graphics g){
        if(hero != null){
            if (hero.isIrresistible()) {
                hero.getDecorator().drawObject(g, Assets.pixmaps);
            }
            else{
                hero.drawObject(g, Assets.pixmaps, 0, 0, 0, 0);
            }
            //Hitbox and vector of legs of a hero:
            /*Hitbox hitbox = hero.getHitbox();
            g.drawRect(hitbox.getLeft(), hitbox.getTop(),
                    hitbox.getHitboxWidth(), hitbox.getHitboxHeight(), 0, hitbox.getPaint()); //draw hitbox.
            g.drawRect(hitbox.getCenterX(), hitbox.getBottom(),  //draw colliding center.
                    2, 2, Color.RED, hitbox.getPaint());*/

            int numBullets = hero.getWeapon().size();
            for(short i = 0; i < numBullets; i++){
                hero.getWeapon().get(i).drawObject(g, Assets.pixmaps, 0, 0, 0, 0);
            }
        }
    }

    public void drawWreckages(List<Bot> wreckages, Graphics g){
        int numWreckages = wreckages.size();
        for(int i = 0; i < numWreckages; i++){
            Bot wreckage = wreckages.get(i);
            if(wreckage.isAction() == Wreckage.ACTION_START){
                wreckage.getDecorator().drawObject(g, Assets.pixmaps);
            }
            else{
                wreckage.drawObject(g, Assets.pixmaps, 0, 0, 0, 0);
            }
            //drawing wreckage on the ground:
            wreckage.drawObject(g, Assets.pixmaps, 0, 0);
            //draw hitbox:
            int left = wreckage.getLeft();
            int top = wreckage.getTop();
            int width = wreckage.getHitboxWidth();
            int height = wreckage.getHitboxHeight();
            g.drawRect(left, top, width, height, Color.BLACK, null);
        }
    }

    public void drawBots(List<Bot> bots, Graphics g){
        int numBots = bots.size();
        for(int i = 0; i < numBots; i++){
            bots.get(i).drawObject(g, Assets.pixmaps, 0, 0, 0, 0);
            //draw hitbox:
            /*int left = bots.get(i).getLeft();
            int top = bots.get(i).getTop();
            int width = bots.get(i).getHitboxWidth();
            int height = bots.get(i).getHitboxHeight();
            g.drawRect(left, top, width, height, Color.BLACK, null);*/
        }
    }
}
