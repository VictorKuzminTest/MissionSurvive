package com.missionsurvive.objs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.scenarios.PlatformerScenario;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuzmin on 01.05.18.
 */

public class Gun implements Weapon{

    public static final int WEAPON_HANDGUN = 0;
    public static final int WEAPON_HANDGUN_AUTOMATIC = 1;
    public static final int WEAPON_FIREBALL = 2;
    public static final int WEAPON_FIREBALL_SPREAD = 3;

    private List<Weapon> bullets;
    private PlatformerScenario platformerScenario;

    private int weaponOf;

    /**
     * For tests.
     */
    public Gun(){
        bullets = new ArrayList<Weapon>();
    }

    public Gun(PlatformerScenario platformerScenario, int weaponOf){
        this.weaponOf = weaponOf;
        this.platformerScenario = platformerScenario;
        setBulletHolder(15);
    }

    public void setBulletHolder(int numBullets){
        bullets = new ArrayList<Weapon>();
        for(int whichBullet = 0; whichBullet < numBullets; whichBullet++){
            if(weaponOf < WEAPON_FIREBALL){
                bullets.add(new Bullet("bullet", platformerScenario));
            }
            else {
                bullets.add(new Fireball("fireball", platformerScenario));
            }
        }
    }

    @Override
    public void drawObject(SpriteBatch batch, int screenX, int screenY) {

    }

    @Override
    public boolean onTouch() {
        return false;
    }


    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {

    }

    @Override
    public Decorator getDecorator() {
        return null;
    }

    @Override
    public void update(float deltaTime, int worldOffsetX, int worldOffsetY) {

    }

    @Override
    public void update(float deltaTime) {
        switch (weaponOf){
            case WEAPON_HANDGUN:
                weaponOf++;
                break;
            case WEAPON_HANDGUN_AUTOMATIC:
                weaponOf++;
                setBulletHolder(15);
                break;
            case WEAPON_FIREBALL:
                weaponOf++;
                break;
        }
    }

    @Override
    public boolean shoot(int x, int y,
                         int worldOffsetX, int worldOffsetY, int direction) {
        switch(weaponOf){
            case WEAPON_HANDGUN:
                launch(x, y, worldOffsetX, worldOffsetY, direction);
                break;
            case WEAPON_HANDGUN_AUTOMATIC:
                launchAutomatic(x, y, worldOffsetX, worldOffsetY, direction);
                break;
            case WEAPON_FIREBALL:
                launch(x, y, worldOffsetX, worldOffsetY, direction);
                break;
            case WEAPON_FIREBALL_SPREAD:
                launchSpread(x, y, worldOffsetX, worldOffsetY, direction);
                break;
        }
        return false;
    }


    public void launch(int x, int y,
                       int worldOffsetX, int worldOffsetY, int direction){
        for(int whichBullet = 0; whichBullet < bullets.size(); whichBullet++){
            if(bullets.get(whichBullet).shoot(x, y, worldOffsetX, worldOffsetY, direction)){
                return;
            }
        }
    }

    public void launchAutomatic(int x, int y, int worldOffsetX, int worldOffsetY, int direction){
        int numBullets = 0;
        for(int whichBullet = 0; whichBullet < bullets.size(); whichBullet++){
            if(bullets.get(whichBullet).shoot(x, y, worldOffsetX, worldOffsetY,  direction + (numBullets * 10))){
                numBullets++;
                if(numBullets > 1){
                    return;
                }
            }
        }
    }

    public void launchSpread(int x, int y, int worldOffsetX, int worldOffsetY, int direction){
        int numFireballs = 0;
        for(int whichBullet = 0; whichBullet < bullets.size(); whichBullet++){
            if(bullets.get(whichBullet).shoot(x, y, worldOffsetX, worldOffsetY, direction + (numFireballs * 10))){
                numFireballs++;
                if(numFireballs > 2){
                    return;
                }
            }
        }
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
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public boolean hit(boolean isHit) {
        return false;
    }

    @Override
    public List<Weapon> getWeapon() {
        return bullets;
    }

    @Override
    public int getHP() {
        return 0;
    }
}
