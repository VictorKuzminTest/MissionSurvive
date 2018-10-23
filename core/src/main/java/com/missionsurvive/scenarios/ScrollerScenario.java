package com.missionsurvive.scenarios;

import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.framework.TouchControl;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.geom.Hitbox;
import com.missionsurvive.map.Map;
import com.missionsurvive.objs.Blockage;
import com.missionsurvive.objs.Bot;
import com.missionsurvive.objs.Obstacle;
import com.missionsurvive.objs.RightSide;
import com.missionsurvive.objs.RocketL4;
import com.missionsurvive.objs.Springboard;
import com.missionsurvive.objs.Tear;
import com.missionsurvive.objs.actors.Moto;
import com.missionsurvive.scenarios.controlscenarios.ControlScenario;
import com.missionsurvive.screens.GameScreen;
import com.missionsurvive.screens.ScrollerScreen;
import com.missionsurvive.utils.Progress;
import com.missionsurvive.utils.Sounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by kuzmin on 04.05.18.
 */

public class ScrollerScenario implements Scenario {
    private static final int BEGINNER = 0;
    private static final int EXPERIENCED = 1;

    private Random random = new Random();
    private ScrollerScreen scrollerScreen;
    private Moto moto;
    private PlayScript playScript;
    private ControlScenario controlScenario;
    private float obstacleTickTime = 0, obstacleTick =  4.0f;
    private float movingTearTickTime = 0, movingTearTick = 0.005f;
    private int switchSection = 0, switchScene = 0;
    private Obstacle springBoard;
    private Obstacle tearLeft1, tearLeft2;
    private Obstacle tearRight1, tearRight2, tearRight3;
    private Obstacle carTop11, carTop12, carMiddle11, carMiddle12, carMiddle21, carMiddle22, carBottom11,
            carBottom12;
    private Obstacle rocket1, rocket2, rocket3, rocket4, rocket5;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Obstacle> tear;
    private ArrayList<PlaceObstacle> currentSection;
    private ArrayList<PlaceObstacle> section1;
    private ArrayList<PlaceObstacle> section2;
    private ArrayList<PlaceObstacle> section3;
    private ArrayList<PlaceObstacle> section4;
    private int obstacleWidth = 70, obstacleHeight = 60;
    private int carHitboxWidth = 120, carHitboxHeight = 25;
    private int spriteWidth = 96, spriteHeight = 80;
    private int startScreenY = 89;
    private int difficulty;

    public ScrollerScenario(ScrollerScreen scrollerScreen, PlayScript playScript,
                            int screenX, int screenY, String difficulty){
        if(difficulty.equalsIgnoreCase(Progress.BEGINNER)){
            this.difficulty = BEGINNER;
        }
        else if(difficulty.equalsIgnoreCase(Progress.EXPERIENCED)){
            this.difficulty = EXPERIENCED;
        }
        this.scrollerScreen = scrollerScreen;
        this.playScript = playScript;
        newObstacles(screenX, screenY);
    }

    @Override
    public GameScreen getGameScreen() {
        return scrollerScreen;
    }

    @Override
    public void update(Map map, TouchControl touchControl, float deltaTime) {
        if(moto != null){
            if(!moto.isDead()){
                scrollerScreen.updateObstacleBlinking(deltaTime);
                for(Obstacle obstacle : obstacles){
                    obstacle.moving(deltaTime);
                }
                movingTearTickTime += deltaTime;
                while(movingTearTickTime > movingTearTick){ //for objects of a tear could move synchronously.
                    movingTearTickTime -= movingTearTick;

                    for(Obstacle obstacle : tear){
                        obstacle.moving();
                    }
                }
                setObstacles(deltaTime, map);

                map.horizontScroll(deltaTime, 3);
            }
            updateMoto(touchControl, deltaTime);

            if(moto.isDead()){
                scrollerScreen.darkenScreen(deltaTime);
            }
        }
    }

    @Override
    public void collideObject() {
        collideObstacles(obstacles);
        collideObstacles(tear);
    }

    @Override
    public void addBot(Bot bot, int criteria) {

    }

    @Override
    public void removeBot(Bot bot, int criteria) {

    }

    @Override
    public List<Bot> getBots(int criteria){
        return null;
    }

    @Override
    public void placeObject(int x, int y) {
        newMoto(x, y);
    }

    public void updateMoto(TouchControl touchControl, float deltaTime){
        touchControl.motorControl(deltaTime, moto);
        moto.moving(deltaTime);
    }

    @Override
    public void setScroll(boolean horizontal, boolean vertical) {}

    @Override
    public void setControlScenario(ControlScenario controlScenario) {
        this.controlScenario = controlScenario;
    }

    @Override
    public ControlScenario getControlScenario() {
        return controlScenario;
    }

    /**
     * Generates new motorcycle.
     */
    public void newMoto(int x, int y){
        moto = new Moto("motorcycle", this, playScript, x, y);
    }

    public Moto getMoto(){
        return moto;
    }

    public void newObstacles(int screenX, int screenY){
        obstacles = new ArrayList<Obstacle>();
        tear = new ArrayList<Obstacle>();
        section1 = new ArrayList<PlaceObstacle>();
        setSection1();
        section2 = new ArrayList<PlaceObstacle>();
        setSection2();
        section3 = new ArrayList<PlaceObstacle>();
        setSection3();
        section4 = new ArrayList<PlaceObstacle>();
        setSection4();

        setCurrentSection();

        tearLeft1 = new Tear(new Hitbox(475, startScreenY, obstacleWidth, 70, 5, 25),
                "springBoard", screenX, screenY, spriteWidth, 112, 1); //112 - tear with railing height.

        springBoard = new Springboard(new Hitbox(475, startScreenY + 112, obstacleWidth, obstacleHeight, 5, 5),
                "springBoard", screenX, screenY,
                spriteWidth, spriteHeight, 114);

        tearLeft2 = new Tear(new Hitbox(475, startScreenY + 112 + spriteHeight, obstacleWidth, obstacleHeight, -3, 5),
                "springBoard", screenX, screenY, spriteWidth, spriteHeight, 196);


        tearRight1 = new RightSide(new Hitbox(900, startScreenY, obstacleWidth, 80, 5, 25),
                "springBoard", screenX, screenY, spriteWidth, 112, 1);
        tearRight2 = new RightSide(new Hitbox(900, startScreenY + 112, obstacleWidth, obstacleHeight, 5, 5),
                "springBoard", screenX, screenY,
                spriteWidth, spriteHeight, 114);
        tearRight3 = new RightSide(new Hitbox(900, startScreenY + 112 + spriteHeight, obstacleWidth, obstacleHeight, 5, 5),
                "springBoard", screenX, screenY, spriteWidth, spriteHeight, 196);


        carTop11 = new Blockage(new Hitbox(500, 100 + PlaceObstacle.CAR_TOP * obstacleHeight, carHitboxWidth, carHitboxHeight, 10, 14),
                "cars", screenX, screenY,
                154, 64);
        carTop12 = new Blockage(new Hitbox(500, 100 + PlaceObstacle.CAR_TOP * obstacleHeight, carHitboxWidth, carHitboxHeight, 10, 14),
                "cars", screenX, screenY,
                154, 64);
        carMiddle11 = new Blockage(new Hitbox(500, 100 + PlaceObstacle.CAR_MIDDLE * obstacleHeight, carHitboxWidth, carHitboxHeight, 10, 14),
                "cars", screenX, screenY,
                154, 64);
        carMiddle12 = new Blockage(new Hitbox(500, 100 + PlaceObstacle.CAR_MIDDLE * obstacleHeight, carHitboxWidth, carHitboxHeight, 10, 14),
                "cars", screenX, screenY,
                154, 64);
        carMiddle21 = new Blockage(new Hitbox(500, 100 + PlaceObstacle.CAR_MIDDLE * obstacleHeight, carHitboxWidth, carHitboxHeight, 10, 14),
                "cars", screenX, screenY,
                154, 64);
        carMiddle22 = new Blockage(new Hitbox(500, 100 + PlaceObstacle.CAR_MIDDLE * obstacleHeight, carHitboxWidth, carHitboxHeight, 10, 14),
                "cars", screenX, screenY,
                154, 64);
        carBottom11 = new Blockage(new Hitbox(500, 100 + PlaceObstacle.CAR_BOTTOM * obstacleHeight, carHitboxWidth, carHitboxHeight, 10, 14),
                "cars", screenX, screenY,
                154, 64);
        carBottom12 = new Blockage(new Hitbox(500, 100 + PlaceObstacle.CAR_BOTTOM * obstacleHeight, carHitboxWidth, carHitboxHeight, 10, 14),
                "cars", screenX, screenY,
                154, 64);

        rocket1 = new RocketL4(new Hitbox(-100, -100, 45, 36, 0, 0), "rocket", -100, -100, 45, 36);
        rocket2 = new RocketL4(new Hitbox(-100, -100, 45, 36, 0, 0), "rocket", -100, -100, 45, 36);
        rocket3 = new RocketL4(new Hitbox(-100, -100, 45, 36, 0, 0), "rocket", -100, -100, 45, 36);
        rocket4 = new RocketL4(new Hitbox(-100, -100, 45, 36, 0, 0), "rocket", -100, -100, 45, 36);
        rocket5 = new RocketL4(new Hitbox(-100, -100, 45, 36, 0, 0), "rocket", -100, -100, 45, 36);

        obstacles.add(carTop11);
        obstacles.add(carTop12);
        obstacles.add(carMiddle11);
        obstacles.add(carMiddle12);
        obstacles.add(carMiddle21);
        obstacles.add(carMiddle22);
        obstacles.add(carBottom11);
        obstacles.add(carBottom12);
        obstacles.add(rocket1);
        obstacles.add(rocket2);
        obstacles.add(rocket3);
        obstacles.add(rocket4);
        obstacles.add(rocket5);

        tear.add(springBoard);
        tear.add(tearLeft1);
        tear.add(tearLeft2);
        tear.add(tearRight1);
        tear.add(tearRight2);
        tear.add(tearRight3);
    }

    public void setObstacles(float deltaTime, Map map){
        obstacleTickTime += deltaTime;

        while(obstacleTickTime > obstacleTick) {
            obstacleTickTime -= obstacleTick;

            if(switchScene < currentSection.size()){
                if((switchScene + 1) < currentSection.size()){  //set tick before new obstacle.
                    obstacleTick = currentSection.get(switchScene + 1).tick;
                }
                switch (currentSection.get(switchScene).obstacleId){
                    case PlaceObstacle.CAR_TOP_11:
                        placeCar(currentSection, carTop11);
                        switchScene++;
                        break;
                    case PlaceObstacle.CAR_TOP_12:
                        placeCar(currentSection, carTop12);
                        switchScene++;
                        break;
                    case PlaceObstacle.CAR_MIDDLE_11:
                        placeCar(currentSection, carMiddle11);
                        switchScene++;
                        break;
                    case PlaceObstacle.CAR_MIDDLE_12:
                        placeCar(currentSection, carMiddle12);
                        switchScene++;
                        break;
                    case PlaceObstacle.CAR_BOTTOM_11:
                        placeCar(currentSection, carBottom11);
                        switchScene++;
                        break;
                    case PlaceObstacle.CAR_BOTTOM_12:
                        placeCar(currentSection, carBottom12);
                        switchScene++;
                        break;
                    case PlaceObstacle.TEAR:
                        placeTear(scrollerScreen, map);
                        switchScene++;
                        break;
                    case PlaceObstacle.ROCKET:
                        switch (currentSection.get(switchScene).rocketId){
                            case PlaceObstacle.ROCKET1:
                                placeRocket(rocket1, currentSection.get(switchScene).isRandom);
                                break;
                            case PlaceObstacle.ROCKET2:
                                placeRocket(rocket2, currentSection.get(switchScene).isRandom);
                                break;
                            case PlaceObstacle.ROCKET3:
                                placeRocket(rocket3, currentSection.get(switchScene).isRandom);
                                break;
                            case PlaceObstacle.ROCKET4:
                                placeRocket(rocket4, currentSection.get(switchScene).isRandom);
                                break;
                            case PlaceObstacle.ROCKET5:
                                placeRocket(rocket5, currentSection.get(switchScene).isRandom);
                                break;
                        }
                        switchScene++;
                        break;
                }
            }
            else{
                obstacleTickTime = 0;
                switchScene = 0;
                switchSection++;
                setCurrentSection();
                if(switchScene < currentSection.size()){  //set tick before new obstacle.
                    obstacleTick = currentSection.get(switchScene).tick;
                }
            }
        }
    }

    public void setCurrentSection(){
        switch (switchSection){
            case 0: currentSection = section1;
                break;
            case 1: currentSection = section2;
                break;
            case 2: currentSection = section3;
                break;
            case 3: currentSection = section4;
                break;
            default:
                scrollerScreen.pause(true);
                showEndLevelPanel();
                break;
        }
    }

    public void showEndLevelPanel(){
        int numPanels = getControlScenario().getControlPanels().size();
        for(int i  = 0; i < numPanels; i++){
            ControlPanel cp = controlScenario.getControlPanels().get(i);
            if(cp.getName().equalsIgnoreCase("EndLevelMenu")){
                cp.setActivated(true);
            }
        }
    }

    public void placeRocket(Obstacle rocket, boolean isRandom){
        if(!rocket.isPlaced()){
            if(isRandom){
                rocket.placeObstacle(random.nextInt(480), 120 + random.nextInt( 200)); //120 - limit top in class Moto.
            }
            else{
                rocket.placeObstacle(moto.getHitbox().getCenterX(), moto.getHitbox().getCenterY());
            }
            obstacleTickTime = 0;
        }
    }

    public void placeTear(ScrollerScreen scrollerScreen, Map map){
        if(!springBoard.isPlaced()){
            int startCol = map.getScrollMap().getEndColOffset() + 5;
            scrollerScreen.setNoStartCol(startCol);
            scrollerScreen.setNoEndCol(startCol + 13);

            //73 - tileWidth, 13 - num tiles to fly, minus 20 pxs - to place (fit) springboard to a target tile:
            int startScreenX = startCol * 73 - map.getScrollMap().getWorldOffsetX() - 20;
            int endScreenX = (startCol + 13) * 73 - map.getScrollMap().getWorldOffsetX();
            tearLeft1.placeObstacle(startScreenX , startScreenY);
            springBoard.placeObstacle(startScreenX, startScreenY + 112 - 1); //112 - tear with railing height.
            tearLeft2.placeObstacle(startScreenX, startScreenY + 112 + spriteHeight - 2);

            tearRight1.placeObstacle(endScreenX, startScreenY);
            tearRight2.placeObstacle(endScreenX, startScreenY + 112 - 1);
            tearRight3.placeObstacle(endScreenX, startScreenY + 112 + spriteHeight - 2);

            obstacleTickTime = 0;
        }
    }

    public void placeCar(ArrayList<PlaceObstacle> section, Obstacle car){
        if(!car.isPlaced()){
            car.setAssetY(getRandomCar());
            car.placeObstacle(section.get(switchScene).screenX,
                    section.get(switchScene).screenY);

            obstacleTickTime = 0;
        }
    }

    public int getRandomCar(){
        int carY = 0;
        float car = random.nextFloat();
        if(car >= 0 && car <= 0.4f){
            carY = 0; //white
        }
        else if(car > 0.4 && car <= 0.8f){
            carY = 1; //grey
        }
        else if(car > 0.8f && car <= 1f){
            carY = 2; //black
        }
        return carY;
    }

    public ArrayList<Obstacle> getObstacles(){
        return obstacles;
    }

    public ArrayList<Obstacle> getTear(){
        return tear;
    }

    public void collideObstacles(ArrayList<Obstacle> obstacles){
        if(obstacles != null){
            for (Obstacle obstacle : obstacles) {
                if(obstacle.isPlaced()){
                    Hitbox motoHitbox = moto.getHitbox();
                    Hitbox springboardHitbox = obstacle.getHitbox();
                    if(GeoHelper.overlapRectangles(motoHitbox.getLeft(), motoHitbox.getTop(), motoHitbox.getHitboxWidth(),
                            motoHitbox.getHitboxHeight(), springboardHitbox.getLeft(), springboardHitbox.getTop(),
                            springboardHitbox.getHitboxWidth(), springboardHitbox.getHitboxHeight())){
                        switch (obstacle.getObstacleId()){
                            case Obstacle.SPRINGBOARD: moto.jump();
                                break;
                            case Obstacle.TEAR: moto.fall();
                                break;
                            case Obstacle.BLOCKAGE: moto.smash(obstacle);
                                break;
                            case Obstacle.LAND:
                                break;
                            case Obstacle.ROCKET:
                                if(obstacle.isExploaded()){
                                    moto.smash(obstacle);
                                }
                                break;
                        }
                        break;  //go out of the for-each
                    }
                }
            }
        }
    }

    public void newAttempt(){
        if(moto.getLives() >= -1){
            startSection();
        }
        else{
            startLevel();
            playScript.newLives();
        }
        moto.setDead(false);
        moto.setScreenX(150);
        moto.setScreenY(240);
        moto.getHitbox().setPos(150, 240);
    }

    public void startSection(){
        switchScene = 0;
        newObstacles(500, 220);
    }

    public void startLevel(){
        switchSection = 0;
        switchScene = 0;
        newObstacles(500, 220);
    }

    /**
     * The class helps to set obstacles.
     */
    public class PlaceObstacle {

        private static final int CAR_TOP = 0, CAR_MIDDLE = 1, CAR_BOTTOM = 2,
                CAR_TOP_11 = 31, CAR_TOP_12 = 32, CAR_BOTTOM_11 = 41, CAR_BOTTOM_12 = 42,
                CAR_MIDDLE_11 = 51, CAR_MIDDLE_12 = 52, TEAR = 3, ROCKET = 6;

        private static final int ROCKET1 = 0, ROCKET2 = 1, ROCKET3 = 2, ROCKET4 = 3, ROCKET5 = 4;

        private float tick;
        private int obstacleId, rocketId;
        private int screenX;
        private int screenY;
        private boolean isRandom;

        public PlaceObstacle(int obstacleId, float tick, int screenX, int screenY) {
            this.tick = tick;
            this.obstacleId = obstacleId;
            this.screenX = screenX;
            this.screenY = screenY;
        }

        public PlaceObstacle(int obstacleId, int rocketId, float tick, boolean isRandom) {
            this.tick = tick;
            this.obstacleId = obstacleId;
            this.rocketId = rocketId;
            this.isRandom = isRandom;
        }
    }

    public void setSection1(){
        switch(difficulty){
            case BEGINNER:
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 3.0f, 900, 150));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.5f, 900, 210));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 1.8f, 900, 150));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 1.5f, 800, 100));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.5f, 800, 150));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_12, 1.5f, 800, 250));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 1.3f, 800, 180));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.3f, 750, 120));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 1.3f, 750, 105));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 1.3f, 800, 180));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_12, 0.5f, 700, 105));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 1.0f, 800, 250));
                //pair - tear
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 1.3f, 750, 150));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.2f, 750, 210));
                section1.add(new PlaceObstacle(PlaceObstacle.TEAR, 2.0f, 900, 0));

                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 5.0f, 900, 150));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.2f, 800, 100));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.8f, 800, 210));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.8f, 800, 250));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.2f, 800, 150));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_12, 0.1f, 800, 250));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 1.1f, 800, 100));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.2f, 750, 250));
                section1.add(new PlaceObstacle(PlaceObstacle.TEAR, 2.0f, 900, 0));
                break;
            case EXPERIENCED:
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 3.0f, 900, 150));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.5f, 900, 210));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 1.8f, 900, 150));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 1.5f, 800, 100));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.5f, 800, 150));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_12, 1.5f, 800, 250));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 1.3f, 800, 180));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.3f, 750, 120));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 1.3f, 750, 105));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 1.3f, 800, 180));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_12, 0.5f, 700, 105));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 1.0f, 800, 250));
                //pair - tear
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 1.3f, 750, 150));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.2f, 750, 210));
                section1.add(new PlaceObstacle(PlaceObstacle.TEAR, 2.0f, 900, 0));

                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 5.0f, 900, 150));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.2f, 800, 100));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.8f, 800, 210));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.8f, 800, 250));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.2f, 800, 150));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_12, 0.1f, 800, 250));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 1.1f, 800, 100));
                section1.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.2f, 750, 250));
                section1.add(new PlaceObstacle(PlaceObstacle.TEAR, 2.0f, 900, 0));
                break;
        }
    }

    public void setSection2(){
        switch (difficulty){
            case BEGINNER:
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 9.0f, 900, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.4f, 900, 210));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.8f, 800, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.8f, 800, 105));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.9f, 900, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 800, 210));
                //repeat the abive part in mirror
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 4.0f, 900, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.4f, 900, 210));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.8f, 800, 105));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.8f, 800, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.9f, 900, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 800, 210));

                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.5f, 800, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 1.1f, 800, 100));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_12, 0.4f, 750, 120));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.6f, 800, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.6f, 800, 210));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.8f, 750, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_12, 0.8f, 750, 200));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.8f, 750, 100));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.6f, 800, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.2f, 750, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 800, 210));
                //with tear
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 1.6f, 800, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 800, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.0f, 800, 210));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.1f, 800, 100));
                section2.add(new PlaceObstacle(PlaceObstacle.TEAR, 1.5f, 900, 0));
                //with tear
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 4.0f, 800, 210));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.1f, 800, 100));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.0f, 800, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 800, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.TEAR, 1.5f, 900, 0));
                //with tear
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 4.0f, 800, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 800, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.0f, 800, 210));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.1f, 800, 100));
                section2.add(new PlaceObstacle(PlaceObstacle.TEAR, 1.5f, 900, 0));
                break;
            case EXPERIENCED:
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 9.0f, 900, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.4f, 900, 210));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.8f, 800, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.8f, 800, 105));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.9f, 900, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 800, 210));
                //repeat the abive part in mirror
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 4.0f, 900, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.4f, 900, 210));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.8f, 800, 105));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.8f, 800, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.9f, 900, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 800, 210));

                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.5f, 800, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 1.1f, 800, 100));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_12, 0.4f, 750, 120));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.6f, 800, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.6f, 800, 210));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.8f, 750, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_12, 0.8f, 750, 200));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.8f, 750, 100));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.6f, 800, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.2f, 750, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 800, 210));
                //with tear
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 1.6f, 800, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 800, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.0f, 800, 210));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.1f, 800, 100));
                section2.add(new PlaceObstacle(PlaceObstacle.TEAR, 1.5f, 900, 0));
                //with tear
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 4.0f, 800, 210));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.1f, 800, 100));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.0f, 800, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 800, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.TEAR, 1.5f, 900, 0));
                //with tear
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 4.0f, 800, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 800, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.0f, 800, 210));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.1f, 800, 100));
                section2.add(new PlaceObstacle(PlaceObstacle.TEAR, 1.5f, 900, 0));

                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 4.0f, 600, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_12, 0.1f, 600, 210));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.1f, 600, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.1f, 500, 100));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 500, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_12, 0.1f, 500, 210));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 1.0f, 500, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_12, 0.1f, 500, 210));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.1f, 500, 150));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 1.0f, 500, 100));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 500, 210));
                section2.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_12, 0.1f, 600, 250));
                section2.add(new PlaceObstacle(PlaceObstacle.TEAR, 0.8f, 700, 0));
                break;
        }
    }

    public void setSection3(){
        switch (difficulty){
            case BEGINNER:
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET1, 9.0f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET2, 1.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET3, 1.5f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET4, 1.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET5, 1.5f, true));

                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET1, 2.0f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET2, 1.5f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET3, 1.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET4, 1.5f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET5, 1.5f, true));

                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET1, 1.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET2, 1.5f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET3, 1.5f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET4, 1.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET5, 1.5f, true));

                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET1, 2.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET2, 0.2f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET3, 1.5f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET4, 0.2f, false));

                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET1, 2.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET2, 0.2f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET3, 1.5f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET4, 0.2f, false));

                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET1, 2.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET2, 0.2f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET3, 1.5f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET4, 0.2f, false));
                break;
            case EXPERIENCED:
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET1, 9.0f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET2, 1.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET3, 1.5f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET4, 1.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET5, 1.5f, true));

                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET1, 2.0f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET2, 1.5f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET3, 1.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET4, 1.5f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET5, 1.5f, true));

                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET1, 1.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET2, 1.5f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET3, 1.5f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET4, 1.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET5, 1.5f, true));

                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET1, 2.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET2, 0.2f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET3, 1.5f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET4, 0.2f, false));

                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET1, 2.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET2, 0.2f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET3, 1.5f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET4, 0.2f, false));

                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET1, 2.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET2, 0.2f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET3, 1.5f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET4, 0.2f, false));
                //new for experienced:
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET1, 2.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET2, 0.2f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET3, 0.2f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET4, 1.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET5, 0.2f, true));

                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET1, 2.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET2, 0.2f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET3, 0.2f, true));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET4, 1.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET5, 0.2f, true));

                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET1, 2.5f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET2, 0.8f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET3, 0.8f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET4, 0.8f, false));
                section3.add(new PlaceObstacle(PlaceObstacle.ROCKET, PlaceObstacle.ROCKET5, 0.8f, false));
                break;
        }
    }

    public void setSection4(){
        switch (difficulty){
            case BEGINNER:
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 9.0f, 800, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 800, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.5f, 800, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 800, 250));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 2.0f, 800, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 800, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.5f, 800, 250));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.1f, 800, 100));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 2.0f, 800, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 800, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.5f, 800, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 800, 250));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 2.5f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.5f, 460, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 460, 250));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 1.1f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.5f, 460, 250));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.1f, 460, 100));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 1.1f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.5f, 460, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 460, 250));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 1.1f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.5f, 460, 250));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.1f, 460, 100));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_12, 0.8f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.1f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_12, 0.1f, 460, 250));
                section4.add(new PlaceObstacle(PlaceObstacle.TEAR, 0.8f, 900, 0));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 3.0f, 460, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 460, 160));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.TEAR, 0.8f, 900, 0));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_12, 3.0f, 460, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.1f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_12, 0.1f, 460, 255));
                section4.add(new PlaceObstacle(PlaceObstacle.TEAR, 0.8f, 900, 0));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 3.0f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.6f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.5f, 460, 260));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.1f, 460, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.5f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_12, 0.1f, 460, 120));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.1f, 460, 190));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.4f, 460, 270));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.8f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.6f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.5f, 460, 110));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 460, 270));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_12, 0.5f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_12, 0.1f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.1f, 460, 190));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.4f, 460, 270));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.6f, 460, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.3f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_12, 0.5f, 460, 260));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.5f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.2f, 460, 210));
                //End:
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_12, 5.0f, 480, 1500));
                break;
            case EXPERIENCED:
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 9.0f, 800, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 800, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.5f, 800, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 800, 250));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 2.0f, 800, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 800, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.5f, 800, 250));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.1f, 800, 100));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 2.0f, 800, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 800, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.5f, 800, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 800, 250));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 2.5f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.5f, 460, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 460, 250));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 1.1f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.5f, 460, 250));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.1f, 460, 100));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 1.1f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.5f, 460, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 460, 250));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 1.1f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.5f, 460, 250));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.1f, 460, 100));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_12, 0.8f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.1f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_12, 0.1f, 460, 250));
                section4.add(new PlaceObstacle(PlaceObstacle.TEAR, 0.8f, 900, 0));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 3.0f, 460, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 460, 160));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.TEAR, 0.8f, 900, 0));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_12, 3.0f, 460, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.1f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_12, 0.1f, 460, 255));
                section4.add(new PlaceObstacle(PlaceObstacle.TEAR, 0.8f, 900, 0));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 3.0f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.6f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.5f, 460, 260));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.1f, 460, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.5f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_12, 0.1f, 460, 120));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.1f, 460, 190));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.4f, 460, 270));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.8f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.6f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.5f, 460, 110));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 460, 270));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_12, 0.5f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_12, 0.1f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.1f, 460, 190));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.4f, 460, 270));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 0.6f, 460, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.3f, 460, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_12, 0.5f, 460, 260));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 0.5f, 460, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.2f, 460, 210));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 3.0f, 480, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_12, 0.1f, 480, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 480, 255));
                section4.add(new PlaceObstacle(PlaceObstacle.TEAR, 0.8f, 900, 0));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 3.0f, 480, 100));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_12, 0.1f, 480, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_12, 0.1f, 480, 255));
                section4.add(new PlaceObstacle(PlaceObstacle.TEAR, 0.8f, 900, 0));

                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_11, 3.0f, 480, 150));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_MIDDLE_12, 0.1f, 480, 210));
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_BOTTOM_11, 0.1f, 480, 255));
                section4.add(new PlaceObstacle(PlaceObstacle.TEAR, 0.8f, 900, 0));
                //End:
                section4.add(new PlaceObstacle(PlaceObstacle.CAR_TOP_11, 5.0f, 480, 1500));
                break;
        }
    }
}
