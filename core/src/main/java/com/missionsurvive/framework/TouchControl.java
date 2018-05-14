package com.missionsurvive.framework;

import com.badlogic.gdx.Gdx;
import com.missionsurvive.MSGame;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.map.ScrollMap;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.objs.actors.Moto;
import com.missionsurvive.scenarios.controlscenarios.ControlScenario;
import com.missionsurvive.scenarios.PlatformerScenario;

/**
 * Created by kuzmin on 25.04.18.
 */

public class TouchControl {

    private int lastDownX;  //Последнее зафиксированная координата X пальца на экране в состоянии  down.
    private int lastDownY;   //Последнее зафиксированная координата Y пальца на экране в состоянии  down.
    private int distanceX;   //Вычисляемая дистанция по X на экране в зависимости от событий.
    private int distanceY;   //Вычисляемая дистанция по Y на экране в зависимости от событий.

    private int initEventToJumpX, initEventToJumpY;

    private static int downEventX;
    private static int downEventY;
    private static int dragEventX;
    private static int dragEventY;
    private static int startScrollX; //these 4 variable are use to control "Button List scrolling" and "pressing" button in that list.
    private static int startScrollY;
    private static boolean scrolling = false;  //this boolean is used to control whether wwe can "press" button int th ListButtons or not.

    private final int MIN_THRASHOLD_Y = 32;
    private final int MIN_THRASHOLD_X = 24;

    private int touchButtonEvent = 0;
    private int touchListEvent = 0;
    private Button currentButton;
    private boolean isMapTouchedDown = false;
    private float jumpingTick = 0.9f, jumpingTickTime = 0;

    private float motoX, motoY;
    private float scaleX, scaleY;

    public TouchControl(float scaleX, float scaleY){
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public void motorControl(float deltaTime, Moto moto){
        if(Gdx.input.justTouched()){
            motoX = Gdx.input.getX(0) * scaleX;
            motoY =  Gdx.input.getY(0) * scaleY;
            moto.setDestination(deltaTime, (int)motoX, (int)motoY);
        }
        else{
            if(Gdx.input.isTouched(0)){
                motoX = Gdx.input.getX(0) * scaleX;
                motoY =  Gdx.input.getY(0) * scaleY;
                moto.setDestination(deltaTime, (int)motoX, (int)motoY);
            }
            else{
                moto.stop();
            }
        }
    }



    public void heroControl(PlatformerScenario platformerScenario, Hero hero, float deltaTime) {

        if(hero.isAction() < Hero.ACTION_DYING){

            /*countJumpingTime(hero, null, deltaTime);*/

            /*int len = touchEvents.size();
            for(int touchEvent = 0; touchEvent < len; touchEvent++){
                TouchEvent event = touchEvents.get(touchEvent);

                switch(event.type){
                    case TouchEvent.TOUCH_DOWN:{
                        shoot(platformerScenario, hero, event.x, event.y);
                        moveHero(hero, event.x, event.y);
                        countJumpingTime(hero, event, 0);
                        break;
                    }
                    case TouchEvent.TOUCH_DRAGGED:{
                        shoot(platformerScenario, hero, event.x, event.y);
                        moveHero(hero, event.x, event.y);
                        countJumpingTime(hero, event, 0);
                        break;
                    }
                    case TouchEvent.TOUCH_UP:{
                        stopActions(hero);
                        countJumpingTime(hero, event, 0);
                        break;
                    }
                }
            }*/
        }
    }


    public void moveHero(Hero hero, int x , int y) {
        if(hero.isAction() == Hero.ACTION_IDLE || hero.isAction() == Hero.ACTION_RUNNING){
            //First We check here for the previous direction. And if hero has to change its direction,
            //we set action to IDLE in order for hero in method run()
            //could properly change action and sprite according to new direction.
            if(isChangingDirection(hero, x)){
                stopActions(hero);
            }

            hero.run();
            setFallingX(hero);
        }
        else if(hero.isAction() == Hero.ACTION_JUMPING){

            isChangingDirection(hero, x);
            hero.setJumpingVector(hero.getSpeedRunning(), hero.getSpeedJumpingY());
        }

        else if(hero.isAction() == Hero.ACTION_FALLING){
            //if user changes direction of a hero while falling, we set to ACTION_IDLE by force
            //in order for hero, could properly change its sprite direction. Because in method fall()
            //of a hero the proper sprite according to new direction cannot be changed.
            if(isChangingDirection(hero, x)){
                hero.setAction(Hero.ACTION_IDLE);
            }

            setFallingX(hero);
        }
    }


    public void setFallingX(Hero hero){
        if(hero.getDirection() == Hero.DIRECTION_RIGHT){
            hero.setSpeedFallingX(hero.getSpeedRunning());
        }
        else{
            hero.setSpeedFallingX(hero.getSpeedRunning() * -1);
        }
    }

    /**
     * Check for touch event x and direction of a hero at the moment. If hero must change
     * direction - returns true.
     * @param hero
     * @param x
     * @return
     */
    public boolean isChangingDirection(Hero hero, int x){
        if(x < hero.getCenterX()){

            //We check here for the previous direction. And if it was RIGHT, and x is to the LEFT,
            //we set direction to the LEFT first, then return true.
            if(hero.getDirection() == Hero.DIRECTION_RIGHT){
                hero.setDirection(Hero.DIRECTION_LEFT);
                return true;
            }
            hero.setDirection(Hero.DIRECTION_LEFT);

        }
        else{
            //We check here for the previous direction. And if it was LEFT, and x is to the RIGHT,
            //we set direction to the RIGHT first, then return true.
            if(hero.getDirection() == Hero.DIRECTION_LEFT){
                hero.setDirection(Hero.DIRECTION_RIGHT);
                return true;
            }
            hero.setDirection(Hero.DIRECTION_RIGHT);
        }
        return false;
    }

    public void jumpHero(Hero hero) {
        hero.jump();
    }

    public void stopActions(Hero hero) {
        hero.stopActions();
    }

    public void shoot(PlatformerScenario platformerScenario, Hero hero,  int x, int y) {
        platformerScenario.shootEnemy(hero, x, y);
    }


    /*public void countJumpingTime(Hero hero, TouchEvent touchEvent, float deltaTime){
        if(isMapTouchedDown) {
            jumpingTickTime += deltaTime;
        }
        if(touchEvent != null){
            if(touchEvent.type == TouchEvent.TOUCH_DOWN){
                isMapTouchedDown = true;
                initEventToJumpX = touchEvent.x;
                initEventToJumpY = touchEvent.y;
            }
            else if(touchEvent.type == TouchEvent.TOUCH_DRAGGED){
                while(jumpingTickTime > jumpingTick){
                    jumpingTickTime = 0;

                    initEventToJumpX = touchEvent.x;
                    initEventToJumpY = touchEvent.y;
                }
            }
            else{
                isMapTouchedDown = false;
                if(Math.abs(initEventToJumpY - touchEvent.y) > MIN_THRASHOLD_Y){
                    if(Math.abs(initEventToJumpX - touchEvent.x) < MIN_THRASHOLD_X){
                        hero.setJumpingVector(0, hero.getSpeedJumpingY());
                    }
                    else{
                        hero.setJumpingVector(hero.getSpeedJumpingX(), hero.getSpeedJumpingY());
                    }
                    jumpHero(hero);
                }
            }
        }
    }*/


    public MapTer touchMap(MapTer[][] mapTer, ScrollMap scrollMap, int numRows, int numCols){
        MapTer targetMapTer = null;
        /*int len = touchEvents.size();
        int tileWidth = 16;
        int tileHeight = 16;
        int offsetX = scrollMap.getWorldOffsetX();
        int offsetY = scrollMap.getWorldOffsetY();

        for(int touchEvent = 0; touchEvent < len; touchEvent++){
            TouchEvent event = touchEvents.get(touchEvent);
            if(event.type == TouchEvent.TOUCH_DOWN){
                isMapTouchedDown = true;

                int row = GeoHelper.getSpatialGridCoord(event.y, offsetY, numRows, tileHeight);
                int col = GeoHelper.getSpatialGridCoord(event.x, offsetX, numCols, tileWidth);

                targetMapTer = mapTer[row][col];
            }
            if(event.type == TouchEvent.TOUCH_DRAGGED){

                if(isMapTouchedDown){
                    int row = GeoHelper.getSpatialGridCoord(event.y, offsetY, numRows, tileHeight);
                    int col = GeoHelper.getSpatialGridCoord(event.x, offsetX, numCols, tileWidth);

                    targetMapTer = mapTer[row][col];
                }
            }
            if(event.type == TouchEvent.TOUCH_UP){
                isMapTouchedDown = false;
            }
        }*/
        return targetMapTer;
    }


    public void scrollMapLayers(ControlScenario controlScenario, ScrollMap level1Map, ScrollMap level2Map, ScrollMap level3Map){
        /*int len = touchEvents.size();
        for (int touchEvent = 0; touchEvent < len; touchEvent++) {

            TouchEvent event = touchEvents.get(touchEvent);
            switch (event.type) {
                case TouchEvent.TOUCH_DOWN: {
                    isMapTouchedDown = true;
                    lastDownX = event.x;
                    lastDownY = event.y;
                    break;
                }
                case TouchEvent.TOUCH_DRAGGED: {
                    if(isMapTouchedDown){
                        distanceX = (int) (lastDownX - event.x);
                        distanceY = (int) (lastDownY - event.y);

                        if (level1Map != null) {
                            level1Map.setWorldOffset(distanceX, distanceY);
                        }
                        if (level2Map != null) {
                            level2Map.setWorldOffset(distanceX, distanceY);
                        }
                        if (level3Map != null) {
                            level3Map.setRoundWorldOffset(distanceX, distanceY);
                        }
                        lastDownX = event.x;
                        lastDownY = event.y;
                    }
                    break;
                }
                case TouchEvent.TOUCH_UP: {
                    isMapTouchedDown = false;
                    break;
                }
            }
        }*/
    }

}
