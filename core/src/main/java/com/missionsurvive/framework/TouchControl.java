package com.missionsurvive.framework;

import com.badlogic.gdx.Gdx;
import com.missionsurvive.MSGame;
import com.missionsurvive.geom.GeoHelper;
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

    public static final int EVENT_NONE = -1;
    public static final int EVENT_DOWN = 0;
    public static final int EVENT_DRAGGED = 1;
    public static final int EVENT_UP = 2;

    private int touchEvent;
    private float lastDownX;  //Последнее зафиксированная координата X пальца на экране в состоянии  down.
    private float lastDownY;   //Последнее зафиксированная координата Y пальца на экране в состоянии  down.
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


    /**
     * In this method we also trace touch input events like EVENT_UP, because default (ligdx) framework
     * doesn't provide this feature directly.
     * @param platformerScenario
     * @param hero
     * @param deltaTime
     */
    public void heroControl(PlatformerScenario platformerScenario, Hero hero, float deltaTime) {

        if(hero.isAction() < Hero.ACTION_DYING){

            countJumpingTime(hero, EVENT_NONE, EVENT_NONE, EVENT_NONE, deltaTime);

            if(Gdx.input.justTouched()){
                touchEvent = EVENT_DOWN;
                int logicX = (int)(Gdx.input.getX(0) * scaleX);
                int logicY = (int)(Gdx.input.getY(0) * scaleY);
                shoot(platformerScenario, hero, logicX, logicY);
                moveHero(hero, logicX, logicY);
                countJumpingTime(hero, EVENT_DOWN, logicX, logicY, 0);
            }
            else{
                if(touchEvent != EVENT_NONE){
                    if(Gdx.input.isTouched(0)){
                        touchEvent = EVENT_DRAGGED;
                        int logicX = (int)(Gdx.input.getX(0) * scaleX);
                        int logicY = (int)(Gdx.input.getY(0) * scaleY);
                        shoot(platformerScenario, hero, logicX, logicY);
                        moveHero(hero, logicX, logicY);
                        countJumpingTime(hero, EVENT_DRAGGED, logicX, logicY, 0);
                    }
                    else{
                        if(touchEvent < EVENT_UP){
                            touchEvent = EVENT_UP;
                            stopActions(hero);
                            countJumpingTime(hero, EVENT_UP,
                                        (int)(Gdx.input.getX(0) * scaleX), (int)(Gdx.input.getY(0) * scaleY), 0);
                        }
                        else{
                            touchEvent = EVENT_NONE;
                        }
                    }
                }
            }
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

            if(GeoHelper.inBoundsSpaceX(x,
                    hero.getCenterX() - hero.getSpeedRunning(),
                    hero.getCenterX() + hero.getSpeedRunning()) != GeoHelper.INSIDE){
                hero.run();
                setFallingX(hero);
            }
            else{
                stopActions(hero);
            }
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


    public void countJumpingTime(Hero hero, int touchEvent,
                                 int eventX, int eventY, float deltaTime){
        if(isMapTouchedDown) {
            jumpingTickTime += deltaTime;
        }

        switch(touchEvent){
            case EVENT_DOWN:
                isMapTouchedDown = true;
                initEventToJumpX = eventX;
                initEventToJumpY = eventY;
                break;
            case EVENT_DRAGGED:
                while(jumpingTickTime > jumpingTick){
                    jumpingTickTime = 0;

                    initEventToJumpX = eventX;
                    initEventToJumpY = eventY;
                }
                break;
            case EVENT_UP:
                isMapTouchedDown = false;
                if(Math.abs(initEventToJumpY - eventY) > MIN_THRASHOLD_Y){
                    if(Math.abs(initEventToJumpX - eventX) < MIN_THRASHOLD_X){
                        hero.setJumpingVector(0, hero.getSpeedJumpingY());
                    }
                    else{
                        hero.setJumpingVector(hero.getSpeedJumpingX(), hero.getSpeedJumpingY());
                    }
                    jumpHero(hero);
                }
                break;
        }
    }


    public MapTer touchMap(MapTer[][] mapTer, ScrollMap scrollMap, int numRows, int numCols){
        MapTer targetMapTer = null;
        int offsetX = scrollMap.getWorldOffsetX();
        int offsetY = scrollMap.getWorldOffsetY();
        int tileWidth = 16;
        int tileHeight = 16;

        if(Gdx.input.justTouched()){
            isMapTouchedDown = true;
            int row = GeoHelper.getSpatialGridCoord((int)(Gdx.input.getY(0) * scaleY),
                    offsetY, numRows, tileHeight);
            int col = GeoHelper.getSpatialGridCoord((int)(Gdx.input.getX(0) * scaleX),
                    offsetX, numCols, tileWidth);

            targetMapTer = mapTer[row][col];
        }
        else{
            if(Gdx.input.isTouched(0)){
                if(isMapTouchedDown){
                    int row = GeoHelper.getSpatialGridCoord((int)(Gdx.input.getY(0) * scaleY),
                            offsetY, numRows, tileHeight);
                    int col = GeoHelper.getSpatialGridCoord((int)(Gdx.input.getX(0) * scaleX),
                            offsetX, numCols, tileWidth);

                    targetMapTer = mapTer[row][col];
                }
            }
            else{
                isMapTouchedDown = false;
            }
        }
        return targetMapTer;
    }


    public void scrollMapLayer(ControlScenario controlScenario, ScrollMap scrollMap){
        if(Gdx.input.justTouched()){
            isMapTouchedDown = true;
            lastDownX = Gdx.input.getX(0) * scaleX;
            lastDownY = Gdx.input.getY(0) * scaleY;
        }
        else{
            if(Gdx.input.isTouched(0)){
                if(isMapTouchedDown){

                    float eventX = Gdx.input.getX(0) * scaleX;
                    float eventY = Gdx.input.getY(0) * scaleY;

                    distanceX = (int)(lastDownX - eventX);
                    distanceY = (int)(lastDownY - eventY);

                    if (scrollMap != null) {
                        scrollMap.setWorldOffset(distanceX, distanceY);
                    }
                    lastDownX = eventX;
                    lastDownY = eventY;
                }
            }
            else{
                isMapTouchedDown = false;
            }
        }
    }

}
