package com.missionsurvive.framework.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.missionsurvive.MSGame;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.map.Map;
import com.missionsurvive.objs.Bot;
import com.missionsurvive.objs.Wreckage;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.scenarios.Spawn;
import com.missionsurvive.scenarios.SpawnBot;
import com.missionsurvive.scenarios.SpawnScenario;
import com.missionsurvive.utils.Assets;

import java.util.List;

/**
 * Created by kuzmin on 03.05.18.
 */

public class DrawerFacade {

    private Texture zombieTexture = Assets.getTextures()[Assets.getWhichTexture("zombie")];
    private Texture shotgunZombieTexture = Assets.getTextures()[Assets.getWhichTexture("shotgunzombie")];
    private Texture soldierZombieTexture = Assets.getTextures()[Assets.getWhichTexture("soldierzombie")];
    private Texture l1bTexture = Assets.getTextures()[Assets.getWhichTexture("l1b")];
    private Texture l3bTexture = Assets.getTextures()[Assets.getWhichTexture("l3b")];
    private Texture l5bTexture = Assets.getTextures()[Assets.getWhichTexture("l5b")];
    private Texture l6bTexture = Assets.getTextures()[Assets.getWhichTexture("l6b")];

    private ShapeRenderer hitBox;

    private float scaleToDrawX, scaleToDrawY;
    private int tileWidth = 16;
    private int tileHeight = 16;

    public DrawerFacade(){
        scaleToDrawX = (float)Gdx.graphics.getBackBufferWidth()
                / MSGame.SCREEN_WIDTH;
        scaleToDrawY = (float)Gdx.graphics.getBackBufferHeight()
                / MSGame.SCREEN_HEIGHT;
        hitBox = new ShapeRenderer();
        hitBox.setColor(0, 0, 0, 0.5f);
    }

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
            drawHitBox(wreckage);
        }
    }

    public void drawBots(List<Bot> bots, SpriteBatch batch){
        int numBots = bots.size();
        for(int i = 0; i < numBots; i++){
            bots.get(i).drawObject(batch, 0, 0, 0, 0);
            //draw hitbox:
            drawHitBox(bots.get(i));
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
            case SpawnBot.SOLDIER_ZOMBIE:
                drawBot(batch, soldierZombieTexture,
                        col * tileWidth - map.getScrollMap().getWorldOffsetX() - 20,
                        row * tileHeight - map.getScrollMap().getWorldOffsetY() - 70,
                        1, 1,
                        54, 70);
                break;
            case SpawnScenario.LEVEL_1_SCENE:
                drawBot(batch, l1bTexture,
                        col * tileWidth - map.getScrollMap().getWorldOffsetX(),
                        row * tileHeight - map.getScrollMap().getWorldOffsetY(),
                        1, 1,
                        60, 70);
                break;
            case SpawnBot.LEVEL_3_BOSS:
                drawBot(batch, l3bTexture,
                        col * tileWidth - map.getScrollMap().getWorldOffsetX(),
                        row * tileHeight - map.getScrollMap().getWorldOffsetY(),
                        1, 1,
                        140, 110);
                break;
            case SpawnBot.LEVEL_5_BOSS:
                drawBot(batch, l5bTexture,
                        col * tileWidth - map.getScrollMap().getWorldOffsetX(),
                        row * tileHeight - map.getScrollMap().getWorldOffsetY(),
                        1, 1,
                        60, 70);
                break;
            case SpawnBot.LEVEL_6_BOSS:
                drawBot(batch, l6bTexture,
                        col * tileWidth - map.getScrollMap().getWorldOffsetX(),
                        row * tileHeight - map.getScrollMap().getWorldOffsetY(),
                        1, 1,
                        100, 110);
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

    public void drawHitBox(Bot bot){
        int left = bot.getLeft();
        int top = bot.getTop();
        int width = bot.getHitboxWidth();
        int height = bot.getHitboxHeight();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        hitBox.begin(ShapeRenderer.ShapeType.Filled);
        hitBox.rect(left * scaleToDrawX,
                GeoHelper.transformCanvasYCoordToGL(top,
                        MSGame.SCREEN_HEIGHT, height) * scaleToDrawY,
                width * scaleToDrawX, height * scaleToDrawY);
        hitBox.end();
    }
}
