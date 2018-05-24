package com.missionsurvive.framework.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.map.Map;
import com.missionsurvive.objs.Bot;
import com.missionsurvive.objs.Wreckage;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.scenarios.Spawn;
import com.missionsurvive.scenarios.SpawnBot;
import com.missionsurvive.utils.Assets;

import java.util.List;

/**
 * Created by kuzmin on 03.05.18.
 */

public class DrawerFacade {

    private Texture zombieTexture = Assets.getTextures()[Assets.getWhichTexture("zombie")];
    private Texture shotgunZombieTexture = Assets.getTextures()[Assets.getWhichTexture("shotgunzombie")];

    private int tileWidth = 16;
    private int tileHeight = 16;

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

    /**
     * Shows position of a bot in map constructor. The x and y calculation of a position
     * must match the coords in Spawn object. So, if you change one of them, the other has to be changed
     * to match each other.
     * @param batch
     * @param spawn
     * @param row
     * @param col
     */
    public void showBotPos(SpriteBatch batch, Spawn spawn, Map map, int row, int col){
        switch(spawn.getBotId()){
            case SpawnBot.ZOMBIE:{
                drawBot(batch, zombieTexture,
                        col * tileWidth - map.getScrollMap().getWorldOffsetX() - 20,
                        row * tileHeight - map.getScrollMap().getWorldOffsetY() - 70,
                        1, 1,
                        54, 70);
                break;
            }
            case SpawnBot.SHOTGUN_ZOMBIE:
                drawBot(batch, shotgunZombieTexture,
                        col * tileWidth - map.getScrollMap().getWorldOffsetX() - 20,
                        row * tileHeight - map.getScrollMap().getWorldOffsetY() - 70,
                        1, 1,
                        54, 70);
                break;
        }
    }

    public void drawBot(SpriteBatch batch, Texture texture,
                        int x, int y, int srcX, int srcY, int spriteWidth, int spriteHeight){
        batch.begin();
        batch.draw(texture,
                MSGame.SCREEN_OFFSET_X + x,
                MSGame.SCREEN_OFFSET_Y +
                        GeoHelper.transformCanvasYCoordToGL(y, MSGame.SCREEN_HEIGHT, spriteHeight),
                srcX, srcY, spriteWidth, spriteHeight);
        batch.end();
    }
}
