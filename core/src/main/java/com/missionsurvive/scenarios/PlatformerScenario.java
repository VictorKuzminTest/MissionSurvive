package com.missionsurvive.scenarios;

import com.missionsurvive.framework.TouchControl;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.map.Map;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.map.ScrollMap;
import com.missionsurvive.objs.Bot;
import com.missionsurvive.objs.EnemyBullet;
import com.missionsurvive.objs.Weapon;
import com.missionsurvive.objs.actors.Zombie;
import com.missionsurvive.objs.actors.Hero;
import com.missionsurvive.scenarios.controlscenarios.ControlScenario;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuzmin on 03.05.18.
 */

public class PlatformerScenario implements Scenario {

    public static final int MAX_NUM_ZOMBIES = 3;

    private Hero hero;
    private MapTer currentMapTer;
    private MapEditor mapEditor;
    private List<Bot> bots = new ArrayList<Bot>();
    private List<Bot> zombieBots = new ArrayList<Bot>();
    private List<Bot> wreckages = new ArrayList<Bot>();
    private List<Bot> otherBots = new ArrayList<Bot>();
    //a chain of objects spawned on the level. I need it to reuse spawns already placed
    //on the level (method: setFirstTimeSpawned):
    private ArrayList<Spawn> spawnsFakeList = new ArrayList<Spawn>();
    private PlayScript playScript;
    private ControlScenario controlScenario;
    private TouchControl touchControl;

    private float checkOffScreenTickTime = 0, checkOffscreenTick = 1.0f;
    private float resurrectionTickTime = 0, resurrectionTick = 2.0f;

    private boolean isHorizontal = true;
    private boolean isVertical = false;
    private boolean isPlayerControl = true;

    public PlatformerScenario(MapEditor mapEditor, PlayScript playScript,
                              TouchControl touchControl){
        this.touchControl = touchControl;
        this.mapEditor = mapEditor;
        this.playScript = playScript;
        fillSpawnsFakeList();

        if(mapEditor.isHorizontal()){
            setScroll(true, false);
        }
        else{
            setScroll(false, true);
        }
    }

    /**
     * fills the spawns fake list for setting firstTimeSpawned from data of MapEditor.
     */
    public void fillSpawnsFakeList(){
        int height = mapEditor.getSpawns().length;
        int width = mapEditor.getSpawns()[0].length;
        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){

                if(mapEditor.getSpawns()[row][col] != null){
                    spawnsFakeList.add(mapEditor.getSpawns()[row][col]);
                }
            }
        }
    }

    @Override
    public void update(Map map, TouchControl touchControl, float deltaTime) {
        int worldHeight = mapEditor.getLevel1Ter().length;
        int worldWidth = mapEditor.getLevel1Ter()[0].length;

        checkHeroOffScreen();
        updateHero(mapEditor, worldWidth, worldHeight, touchControl, deltaTime);
        updateEnemies(mapEditor.getLevel1Ter(), mapEditor, worldWidth, worldHeight, deltaTime);
        spawnEnemy(mapEditor, deltaTime, worldWidth, worldHeight);
        checkBotsOffScreen(deltaTime);
    }

    @Override
    public void collideObject() {
        collideEnemy();
    }

    @Override
    public void addBot(Bot bot, int criteria) {
        bot.setScenario(this);
        bots.add(bot);

        if(criteria == SpawnBot.ZOMBIE){
            zombieBots.add(bot);
        }
        else if(criteria == SpawnBot.WRECKAGE){
            wreckages.add(bot);
        }
        else{
            otherBots.add(bot);
        }
    }

    @Override
    public void removeBot(Bot bot, int criteria) {
        bots.remove(bot);

        if(criteria == SpawnBot.ZOMBIE){
            zombieBots.remove(bot);
        }
        else if(criteria == SpawnBot.WRECKAGE){
            wreckages.remove(bot);
        }
        else{
            otherBots.remove(bot);
        }
    }

    public void removeAllBots(){
        bots.clear();
        zombieBots.clear();
        wreckages.clear();
        otherBots.clear();
    }

    @Override
    public List<Bot> getBots(int criteria){
        if(criteria == SpawnBot.ZOMBIE){
            return zombieBots;
        }
        else if(criteria == SpawnBot.WRECKAGE){
            return wreckages;
        }
        else{
            return otherBots;
        }
    }

    @Override
    public void placeObject(int x, int y) {
        newHero(x, y);
    }

    /**
     * Generates new Hero.
     * @param x logic pos on screen
     * @param y logic pos on screen
     */
    public void newHero(int x, int y){
        int direction = Hero.DIRECTION_RIGHT;
        if(hero != null){
            direction = this.hero.getDirection();
        }
        hero = new Hero("hero", this, x, y, direction, isHorizontal, isVertical);
        hero.setPreviousMapTer(currentMapTer);
    }

    public void resurrectHero(MapEditor mapEditor){
        if(playScript.getLives() >= 0){
            playScript.subtractLife();
            currentMapTer = hero.getPreviosMapTer();
            playScript.resurrectHero(currentMapTer, mapEditor, hero);
        }
    }

    public void updateHero(MapEditor mapEditor, int worldWidth, int worldHeight,
                           TouchControl touchControl, float deltaTime){
        if(hero != null){
            if(isPlayerControl) {
                touchControl.heroControl(this, hero, deltaTime);
            }
            hero.moving(deltaTime, mapEditor.getLevel1Ter(), mapEditor, worldWidth, worldHeight);

            if(hero.isAction() == hero.ACTION_DEAD){
                resurrectionTickTime += deltaTime;
                while(resurrectionTickTime > resurrectionTick) {
                    resurrectionTickTime = 0;
                    resurrectHero(mapEditor);
                }
            }
            else if(hero.isAction() == hero.ACTION_BEYOND_SCREEN){
                hero.fallingBeyondScreen(deltaTime);
                resurrectionTickTime += deltaTime;
                while(resurrectionTickTime > resurrectionTick) {
                    resurrectionTickTime = 0;
                    resurrectHero(mapEditor);
                }
            }
        }
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

    public void updateEnemies(MapTer[][] level1Ter, MapEditor mapEditor,
                              int worldWidth, int worldHeight,float deltaTime){
        for(int enemyNum = 0; enemyNum < bots.size(); enemyNum++){
            bots.get(enemyNum).moving(deltaTime, level1Ter, mapEditor, worldWidth, worldHeight);
        }
    }

    /**
     * When hero collides enemy (bot).
     */
    public void collideEnemy(){
        if(hero != null){
            if(!hero.isIrresistible()){
                if(!bots.equals(null)){
                    int len = bots.size();
                    for(int whichEnemy = 0; whichEnemy < len; whichEnemy++){
                        Bot enemy = bots.get(whichEnemy);
                        if(!enemy.equals(null)){
                            if(GeoHelper.overlapRectangles(hero.getLeft(), hero.getTop(), hero.getHitboxWidth(),
                                    hero.getHitboxHeight(), enemy.getLeft(), enemy.getTop(),
                                    enemy.getHitboxWidth(), enemy.getHitboxHeight())){

                                enemy.collide(hero);
                            }
                        }
                    }
                }
            }
        }
    }

    public void shotEnemy(Weapon weapon){
        if(!bots.equals(null)){
            int len = bots.size();
            for(int whichEnemy = 0; whichEnemy < len; whichEnemy++){
                Bot enemy = bots.get(whichEnemy);
                if(!enemy.equals(null)){
                    if(GeoHelper.overlapRectangles(weapon.getX(), weapon.getY(), weapon.getWidth(),
                            weapon.getHeight(), enemy.getLeft(), enemy.getTop(),
                            enemy.getHitboxWidth(), enemy.getHitboxHeight())){
                        enemy.hit(weapon);
                    }
                }
            }
        }
    }

    public void shotPlayer(EnemyBullet bullet){
        if(hero != null){
            if(!hero.isIrresistible()){
                if(!bots.equals(null)) {
                    if (GeoHelper.overlapRectangles(bullet.getScreenX(), bullet.getScreenY(), bullet.getWidth(),
                            bullet.getHeight(), hero.getLeft(), hero.getTop(),
                            hero.getHitboxWidth(), hero.getHitboxHeight())) {
                        hero.collideBullet(bullet);
                        bullet.gotIt(0);
                    }
                }
            }
        }
    }

    public Hero getHero(){
        return hero;
    }

    /**
     * Generates enemy depending on isHorizontal flag.
     * @param mapEditor
     * @param deltaTime
     * @param worldWidth
     * @param worldHeight
     */
    public void spawnEnemy(MapEditor mapEditor, float deltaTime,
                           int worldWidth, int worldHeight){
        if(isVertical){
            spawnVertEnemy(mapEditor, deltaTime, mapEditor.getScrollLevel1Map(),
                    worldWidth, worldHeight);
        }
        else{
            spawnHorEnemy(mapEditor, deltaTime, mapEditor.getScrollLevel1Map(),
                    worldWidth, worldHeight);
        }
    }

    /**
     * Here we generate enemy for horizontal map (scroll).
     * @param mapEditor
     * @param deltaTime
     * @param scrollMap
     */
    public void spawnHorEnemy(MapEditor mapEditor, float deltaTime,
                              ScrollMap scrollMap, int worldWidth, int worldHeight){
        int startColOffset = scrollMap.getStartColOffset();
        int endColOffset = scrollMap.getEndColOffset();

        if(mapEditor.getSpawns()[0][startColOffset + 20] != null){  //here we generate zombie from above (startColOffset + 20 means zombie is generate approximately in the middle of the screen)
            mapEditor.getSpawns()[0][startColOffset + 20].spawnBot(this, mapEditor, deltaTime);
        }
        for(int row = 1; row < worldHeight; row++){  //generate zombies in front...
            if(mapEditor.getSpawns()[row][startColOffset] != null){
                mapEditor.getSpawns()[row][startColOffset].spawnBot(this, mapEditor, deltaTime);
            }
            if(mapEditor.getSpawns()[row][endColOffset] != null){  //generate zombies from behind...
                mapEditor.getSpawns()[row][endColOffset].spawnBot(this, mapEditor, deltaTime);
            }
        }
    }

    /**
     * Here we generate enemy for vertical map (scroll). Enemies are generated only from above.
     * @param mapEditor
     * @param deltaTime
     * @param scrollMap
     */
    public void spawnVertEnemy(MapEditor mapEditor, float deltaTime,
                               ScrollMap scrollMap, int worldWidth, int worldHeight){
        int startRowOffset = scrollMap.getStartRowOffset();
        int startRow = GeoHelper.checkRowCol(startRowOffset - 1, worldHeight);
        for(int row = startRow; row <= startRowOffset; row++){
            for(int col = 1; col < worldWidth; col++){  //generate zombies from above...
                if(mapEditor.getSpawns()[row][col] != null){
                    mapEditor.getSpawns()[row][col].spawnBot(this, mapEditor, deltaTime);
                }
            }
        }
    }

    /**
     * Checks if bot is off the screen then removes ("kills") it from array list.
     * @param deltaTime
     */
    public void checkBotsOffScreen(float deltaTime){
        checkOffScreenTickTime += deltaTime;
        while(checkOffScreenTickTime > checkOffscreenTick){
            checkOffScreenTickTime -= checkOffscreenTick;

            int numBots = bots.size();
            for(int botNum = 0; botNum < numBots; botNum++){
                Bot bot = bots.get(botNum);
                if(!GeoHelper.overlapRectangles(bot.getX(), bot.getY(),
                        bot.getSpriteWidth(), bot.getSpriteHeight(),
                        -150, -150, 780 , 620)){  //coordinate of the screen +- 150
                    if(bot instanceof Zombie){
                        removeBot(bot, SpawnBot.ZOMBIE);
                    }
                    else{
                        removeBot(bot, 0);
                    }
                    break;
                }
            }
        }
    }

    /**
     * When we try to shoot enemy, we calculate the position of it basing on not on hitbox coordinates,
     * but on its sprite coordinates.
     * @param hero
     * @param lastDownX
     * @param lastDownY
     */
    public void shootEnemy(Hero hero, int lastDownX, int lastDownY){
        if(!bots.equals(null)){
            int len = bots.size();
            for(int whichEnemy = 0; whichEnemy < len; whichEnemy++){
                Bot bot = bots.get(whichEnemy);
                if(!bot.equals(null)){
                    if(bot.onTouch()){
                        if(GeoHelper.inBoundsSpace(lastDownX, lastDownY, bot.getX(), bot.getY(), bot.getX() + bot.getSpriteWidth(), bot.getY() + bot.getSpriteHeight())){
                            hero.shoot(bot.getX(), bot.getY(), bot.getX() + bot.getSpriteWidth(),
                                    bot.getY() + bot.getSpriteHeight(), true);
                            return;
                        }
                    }
                }
            }
        }
        hero.shoot(0, 0, 0, 0, false);
    }

    /**
     * Sets generation (sets a row-col to spawn) of an enemy.
     * @param col
     * @param row
     * @param botId
     * @param direction
     */
    public void setSpawn(int col, int row, int botId, int direction){
        Spawn spawn;
        if(botId >= SpawnScenario.LEVEL_1_SCENE){
            spawn = new SpawnScenario(botId, direction, row, col);
        }
        else{
            spawn = new SpawnBot(botId, direction, row, col);
        }
        mapEditor.getSpawns()[row][col] = spawn;
        spawnsFakeList.add(spawn);
    }

    /**
     * Sets all SpawnBot objects to isFirstTimeSpawn.
     */
    public void setFirstTimeSpawned(){
        int size = spawnsFakeList.size();
        for(int i = 0; i < size; i++){
            spawnsFakeList.get(i).setFirstTimeSpawn(true);
        }
    }

    public Spawn[][] getSpawnEnemies(){
        return mapEditor.getSpawns();
    }

    public boolean checkHeroOffScreen(){
        if(hero.getBottom() >= 330){
            hero.setAction(Hero.ACTION_BEYOND_SCREEN);
            return true;
        }
        else{
            return false;
        }
    }

    public void addLife(){
        playScript.addLife();
    }

    @Override
    public void setScroll(boolean horizontal, boolean vertical) {
        this.isVertical = vertical;
        this.isHorizontal = horizontal;
        mapEditor.setHorizontal(horizontal);
        if(hero != null){
            hero.setVerticalScroll(vertical);
            hero.setHorizontalScroll(horizontal);
        }
    }

    public boolean isHorizontal(){
        return isHorizontal;
    }

    public boolean isVertical(){
        return isVertical;
    }

    @Override
    public void setControlScenario(ControlScenario controlScenario) {
        this.controlScenario = controlScenario;
    }

    @Override
    public ControlScenario getControlScenario() {
        return controlScenario;
    }

    public void setPlayerControl(boolean isPlayerControl){
        this.isPlayerControl = isPlayerControl;
    }

    public boolean isPlayerControl(){
        return isPlayerControl;
    }

    public TouchControl getTouchControl(){
        return touchControl;
    }

}
