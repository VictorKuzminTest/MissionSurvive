package com.missionsurvive.objs.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Decorator;
import com.missionsurvive.framework.impl.ObjAnimation;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.Bot;
import com.missionsurvive.objs.Weapon;
import com.missionsurvive.scenarios.EnemyScenario;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.SpawnBot;
import com.missionsurvive.utils.Assets;

/**
 * Created by kuzmin on 03.05.18.
 */

public class Enemy implements Bot {

    public static final int EAST = 0;
    public static final int WEST = 1;
    private int x;
    private int y;
    private int spritesetSpriteWidth;
    private int spritesetSpriteHeight;
    private int spriteWidth;
    private int spriteHeight;
    private int hitboxWidth;
    private int hitboxHeight;
    ObjAnimation animation;
    MapEditor mapEditor;
    private EnemyScenario enemyScenario;
    private PlatformerScenario platformerScenario;
    private Texture texture;

    private int halfHeroHeight; //"half...", because basically we need to know the half of "real" object width-height to calculate bounding (colliding) points.
    private int halfHeroWidth;
    private int centerX, centerY, top, bottom, left, right;  //ограничивающие точки "тела" героя (top, bootom, left, right). centerX-centerY - центр героя.
    private int vectorX, vectorY;  //Вектор передвижения героя в данный момент времени.

    private float animationTickTime = 0;
    private float movingTickTime = 0;
    private static float animationTick = 0.08f;
    private static float movingTick =  0.03f;
    private float bitingTickTime = 0, bitingTick = 0.6f;

    private final int IDLE_ACTION = 0;
    private final int RUN_ACTION = 1;
    private final int JUMP_ACTION = 2;
    private final int FALLING_ACTION = 3;
    private final int DYING = 4;
    private final int BUILDING_FALLING_ACTION = 5;
    private final int BITING_ACTION = 6;

    private int[] actions; //массив, в котором содержится инфа о действиях игрока и количестве фреймах в них (номер элемента массива - row, содержание элемента - количество фреймов).
    private int numDirections; //количество направлений для действий (сторон света).
    private int numActions; //количество действий.
    private int currentAction; //currentAction
    private int direction; //direction of action.  0 - right, 1 - left.
    private int runningSpeed = 2; //скорость бега в пикселях.
    private int fallingSpeed = 3; //falling speed in pixels.
    private int hits;

    private int startFallingFrame = 0, numFallingFrames = 1;

    private float zombieDyingTick = 0.5f, zombieDyingTickTime = 0;

    private boolean isNorth, isEast, isSouth, isWest; //переменный, указывающие, какие тайлы мира заблокированы.
    private MapTer northTer, eastTer, southTer, westTer; //переменные, хранящие MapTer смежных с героем тайлов.

    private int isAction; //this variable determines the action our hero is using.
    private boolean isRunning = false;
    private int targetX = -1, targetY = -1;

    public Enemy(String assetName, MapEditor mapEditor, int x, int y, int direction) {
        this.x = x;
        this.y = y;
        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }
        spriteWidth = 54;
        spriteHeight = 70;
        spritesetSpriteWidth = spriteWidth + 2;
        spritesetSpriteHeight = spriteHeight + 2;
        hitboxWidth = 18;
        hitboxHeight = 48;
        halfHeroHeight = hitboxHeight / 2;
        halfHeroWidth = hitboxWidth / 2;
        hits = 0;
        this.mapEditor = mapEditor;

        setPos();
        this.direction = direction;
        enemyScenario = new EnemyScenario(mapEditor);
        enemyScenario.setDirection(centerX, centerY, 16, direction);
        targetX = enemyScenario.getTargetX();
        setDirection(targetX);

        numDirections = 2;
        numActions = 7;
        actions = new int[numDirections * numActions];
        for(int whichAction = 0; whichAction < actions.length; whichAction++){
            if(whichAction >= IDLE_ACTION * numDirections && whichAction <= IDLE_ACTION * numDirections + 1) actions[whichAction] = 1;
            else if(whichAction >= RUN_ACTION * numDirections && whichAction <= RUN_ACTION * numDirections + 1) actions[whichAction] = 10;
            else if(whichAction >= JUMP_ACTION * numDirections && whichAction <= JUMP_ACTION * numDirections + 1) actions[whichAction] = 10;
            else if(whichAction >= FALLING_ACTION * numDirections && whichAction <= FALLING_ACTION * numDirections + 1) actions[whichAction] = 2;
            else if(whichAction >= DYING * numDirections && whichAction <= DYING * numDirections + 1) actions[whichAction] = 2;
            else if(whichAction >= BUILDING_FALLING_ACTION * numDirections && whichAction <= BUILDING_FALLING_ACTION * numDirections + 1) actions[whichAction] = 2;
            else if(whichAction >= BITING_ACTION * numDirections && whichAction <= BITING_ACTION * numDirections + 1) actions[whichAction] = 2;
        }
        animation = new ObjAnimation(actions, spriteWidth, spriteHeight);
    }


    @Override
    public void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY) {
        batch.begin();
        batch.draw(texture, MSGame.SCREEN_OFFSET_X + x - mapEditor.getScrollLevel1Map().getWorldOffsetX(),
                MSGame.SCREEN_OFFSET_Y +
                        GeoHelper.transformCanvasYCoordToGL(y - mapEditor.getScrollLevel1Map().getWorldOffsetY(),
                                MSGame.SCREEN_HEIGHT, spriteHeight),
                1 + animation.getCurrentFrame() * spritesetSpriteWidth,
                1 + animation.getSetOfFrames() * spritesetSpriteHeight,
                spriteWidth, spriteHeight);
        batch.end();
    }

    @Override
    public void drawObject(SpriteBatch batch, int screenX, int screenY){

    }

    @Override
    public boolean onTouch() {
        if(isAction < 6){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void update(float deltaTime) {

    }

    ///////////////////////////////THESE ARE MAIN METHODS FOR MOVING AND ANIMATING HERO/////////////////////////////////////////////
    @Override
    public void moving(float deltaTime, MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){
        if(isAction == 7){  //dead
            setOpacity(deltaTime);
            return;
        }

        move(mapTer, mapEditor, worldWidth, worldHeight, deltaTime);
        animate(mapTer, mapEditor, worldWidth, worldHeight, deltaTime);
    }

    public void animate(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight, float deltaTime){
        switch (isAction){
            case 0: animationTickTime += deltaTime;
                idlingAnimation();
                break;
            case 1: animationTickTime += deltaTime;
                fallingAnimation();
                break;
            case 2: animationTickTime += deltaTime;
                jumping(mapTer, mapEditor, worldWidth, worldHeight);
                break;
            case 3: animationTickTime += deltaTime;
                runningAnimation();
                break;
            case 4: animationTickTime += deltaTime;
                bitingTickTime += deltaTime;
                biting();
                break;
            /*case 5: fallingFromBuildingAnimation();
                    break;*/
            case 6: zombieDyingTickTime += deltaTime;
                dying();
                break;
            default: break;
        }
    }


    public void move(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight, float deltaTime){
        movingTickTime += deltaTime;

        while(movingTickTime > movingTick) {
            movingTickTime -= movingTick;

            setPos();
            tilemapCollision(mapTer, mapEditor, worldWidth, worldHeight);
            run();
            fall();

            switch(isAction){
                case 0: idling();
                    break;
                case 1: falling(mapTer, mapEditor, worldWidth, worldHeight);
                    break;
                case 3:running(mapTer, mapEditor, worldWidth, worldHeight);
                    break;
                default: break;
            }
        }
    }
    ///////////////////////////////THESE ARE MAIN METHODS FOR MOVING AND ANIMATING HERO/////////////////////////////////////////////

    @Override
    public void collide(Hero hero){
        bite(hero);
    }


    /**
     * If enemy is not jumping or falling or dying, it gets the hero position and sets the direction according to it.
     * Then bites hero.
     * @param hero
     */
    public void bite(Hero hero){
        if(isAction < 5){  //falling (jumping) from building. But we refused this action. So here it means if it's not dying.
            if(!platformerScenario.getHero().equals(null)){
                this.targetX = platformerScenario.getTargetX() + mapEditor.getScrollLevel1Map().getWorldOffsetX();
                this.targetY = platformerScenario.getTargetY() + mapEditor.getScrollLevel1Map().getWorldOffsetY();
                setDirection(targetX);
            }
            hero.die();
            isAction = 4;   //biting
        }
    }


    public void biting(){
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            setActionAnimationFrames(BITING_ACTION);
            animation.nextFrame();
        }

        while(bitingTickTime > bitingTick){
            bitingTickTime = 0;
            isAction = 0;
            targetX = enemyScenario.getTargetX();
            setDirection(targetX);
        }
    }


    public void calculateVectorX(int speedInPixels, MapEditor mapEditor){
        if(direction == EAST){  //east
            if(eastTer != null){
                if(eastTer.isBlocked()){
                    if(!eastTer.isLadder()){
                        enemyScenario.setDirection(centerX, centerY, 16, EAST);
                        targetX = enemyScenario.getTargetX();
                        setDirection(targetX);
                        int eastTerLeft = eastTer.getLeft(mapEditor.getScrollLevel1Map());
                        if((right + speedInPixels) > eastTerLeft){  //если при runningSpeed герой окажется внутри тайла... В данном случае - это (right + runningSpeed) > eastTerLeft.
                            vectorX = eastTerLeft - right;  //vectorX изменит свое значение ровно до позиции столкновения с тайлом.
                            return;
                        }
                    }
                }
                else{  //в ином случае: пусть герой ещо пробежит со своей "крейсерской" скоростью.
                    vectorX = speedInPixels;
                    return;
                }
            }
            vectorX = speedInPixels;
            return;
        }
        else if(direction == WEST){ //west
            if(westTer != null){ //на всякий случай сначала проверяем на null.
                if(westTer.isBlocked()){
                    if(!westTer.isLadder()){
                        enemyScenario.setDirection(centerX, centerY, 16, WEST);
                        targetX = enemyScenario.getTargetX();
                        setDirection(targetX);
                        int westTerRight = westTer.getRight(mapEditor.getScrollLevel1Map());
                        if((left - speedInPixels) <= westTerRight){  //если при runningSpeed герой окажется внутри тайла... В данном случае - это (left - runningSpeed) > westTerRight.
                            vectorX = left - westTerRight;  //vectorX изменит свое значение ровно до позиции столкновения с тайлом.
                            return;
                        }
                    }
                    else{
                        vectorX = speedInPixels;
                        return;
                    }
                }
            }
            vectorX = speedInPixels;
            return;
        }
    }


    public void calculateVectorY(int direction, int speedInPixels, MapEditor mapEditor){
        if(!isSouth){
            vectorY = speedInPixels;
            return;
        }
        else{
            if(southTer != null){
                int southTerTop = southTer.getTop(mapEditor.getScrollLevel1Map());
                if((bottom + speedInPixels) > southTerTop){  //если при runningSpeed герой окажется внутри тайла... В данном случае - это (bottom + speedInPixels) > southTerTop).
                    vectorY = southTerTop - bottom;  //vectorX изменит свое значение ровно до позиции столкновения с тайлом.
                    return;
                }
                else{
                    vectorY = speedInPixels;
                    return;
                }
            }
        }
    }


    public void die(){
        if(isAction < 6){  //not dying
            isAction = 6;  //dying
        }
    }


    public void dying(){
        while(zombieDyingTickTime > zombieDyingTick){
            zombieDyingTickTime -= zombieDyingTick;

            setActionAnimationFrames(DYING);

            if(animation.getCurrentFrame() == actions[DYING * numDirections] - 1){
                animation.setSetOfFrames(currentAction * numDirections + direction);
                isAction = 7;  //dead;
                return;
            }
            animation.nextFrame();
        }
    }

    public void fall(){
        if(isAction != 2){  //not jumping
            if(!isSouth){
                isAction = 1;  //falling
            }
        }
    }

    public void falling(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){
        if(isAction != 2){ //not jumping
            setActionAnimationFrames(FALLING_ACTION);

            if(!isSouth){
                calculateVectorY(direction, fallingSpeed, mapEditor);
                y += vectorY;

                if(direction == EAST){
                    calculateVectorX(1, mapEditor);
                    x += vectorX;
                }
                else if(direction == WEST){
                    calculateVectorX(1, mapEditor);
                    x -= vectorX;
                }

                isAction = 1;  //falling
                if((y - mapEditor.getScrollLevel1Map().getWorldOffsetY()) > mapEditor.getScrollLevel1Map().getScreenHeight()){ //if zombie fell down lower the screen height.
                    platformerScenario.removeBot(this, SpawnBot.ZOMBIE);
                    return;
                }
            }
            else{
                calculateVectorY(direction, fallingSpeed, mapEditor);
                if(vectorY == 0){  //so we stood on the ground.
                    animation.setCurrentFrame(1); //current frame = our hero landed on the ground.
                    isAction = 0;  //idling
                }
                isAction = 0;
            }
        }
    }

    public void fallingAnimation(){
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            animation.animate(startFallingFrame, numFallingFrames);
        }
    }

    @Override
    public void hit(Weapon weapon){
        hits--;
        if(hits < 0){
            die();
        }
        if(isAction < 7 ){  //if zombie is not dead...
            if(isAction == 6){
                if(animation.getCurrentFrame() != 1){ //if zombie didn't actually fell while dying...
                    weapon.hit(true);
                }
            }
        }
    }

    public void idling(){
        if(isRunning){
            isAction = 3;  //running
        }
    }

    public void idlingAnimation(){
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            setActionAnimationFrames(IDLE_ACTION);
            animation.nextFrame();
        }
    }


    @Override
    public void jump(int destX, int destY){
        setDirection(destX);
        currentAction = JUMP_ACTION;
        setSetOfFrames(currentAction * numDirections + direction);
        isAction = 2;
    }


    public void jumping(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            setJumpingXY(animation.getCurrentFrame(), mapTer, mapEditor, worldWidth, worldHeight);
            animation.nextFrame();

            if(animation.getCurrentFrame() == actions[JUMP_ACTION * numDirections] - 1){
                if(!isSouth){
                    currentAction = FALLING_ACTION;

                }
                else{
                    currentAction = IDLE_ACTION;
                }
                animation.nextFrame();
                animation.setSetOfFrames(currentAction * numDirections + direction);
                isAction = 1;
            }
        }
    }


    @Override
    public void run(){
        if(hits < 0){
            die();
            return;
        }
        if(isAction < 5){ //if zombie is not dying.
            if(targetX >= 0 && targetY > 0){
                isRunning = true;
            }
            if(isAction != 2 && isAction != 1 && isAction != 4){
                currentAction = RUN_ACTION;

                setDirection(targetX);
                setSetOfFrames(currentAction * numDirections + direction);
                isAction = 3;
            }
        }
    }


    public void running(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){
        if(x + 23 < targetX){ //23 - because this is approximate center of zombie's centerX.
            calculateVectorX(runningSpeed, mapEditor);
            x += vectorX;
            if(x + 23 >= targetX){  //23 - because this is approximate center of zombie's centerX.
                stopRunning(EAST);
            }
        }
        if(x + 23 > targetX){ //23 - because this is approximate center of zombie's centerX.
            calculateVectorX(runningSpeed, mapEditor);
            x -= vectorX;
            if(x + 23 <= targetX){  //23 - because this is approximate center of zombie's centerX.
                stopRunning(WEST);
            }
        }
    }

    public void runningAnimation(){
        while(animationTickTime > animationTick) {
            animationTickTime -= animationTick;

            setDirection(targetX);
            setActionAnimationFrames(RUN_ACTION);
            animation.nextFrame();
        }
    }


    public void setActionAnimationFrames(int action){
        if(currentAction != action){
            currentAction = action;
            animation.setSetOfFrames(currentAction * numDirections + direction);
        }
    }



    public void setDirection(int x){
        if(this.x + 23 < x){  //23 - because this is approximate center of zombie's centerX.
            direction = 0;  //right
        }
        if(this.x + 23 > x){ //23 - because this is approximate center of zombie's centerX.
            direction = 1;  //left
        }
    }


    public void setOpacity(float deltaTime){
        animationTickTime += deltaTime;

        while(animationTickTime > animationTick){
            animationTickTime -= animationTick;
            /*int alpha = paint.getAlpha();
            alpha -= 10;
            if(alpha < 0){
                platformerScenario.removeBot(this, SpawnBot.ZOMBIE);
                return;
            }
            paint.setAlpha(alpha);*/
        }
    }


    public void setPos(){
        left = (x + 17) - mapEditor.getScrollLevel1Map().getWorldOffsetX();
        top = (y + 17) - mapEditor.getScrollLevel1Map().getWorldOffsetY();
        bottom = top + hitboxHeight;
        right = left + hitboxWidth;

        centerX = left + halfHeroWidth;
        centerY = top + halfHeroHeight;
    }


    public void setJumpingXY(int actionFrame, MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){
        /**
         * в зависимости от значения actionFrame мы подбираем, насколько должен переместиться герой по координатам x-y.
         */

        if(actionFrame >= 2 && actionFrame <= 4){  //набор высоты.
            //ифы для координаты х:
            if(direction == 0){ //right
                calculateVectorX(8, mapEditor);
                x += vectorX;
            }
            if(direction == 1){  //left
                calculateVectorX(8, mapEditor);
                x -= vectorX;
            }

            calculateVectorY(0, 22, mapEditor);
            y -= vectorY;
        }
        else if(actionFrame == 5){  //пик высоты. Двигаемся только вперед по x.
            if(direction == 0){ //right
                calculateVectorX(8, mapEditor);
                x += vectorX;
            }
            else if(direction == 1){  //left
                calculateVectorX(8, mapEditor);
                x -= vectorX;
            }
        }
        else if(actionFrame >= 6 && actionFrame <= 8){  //летим вниз.
            if(direction == 0){ //right
                calculateVectorX(8, mapEditor);
                x += vectorX;
            }
            if(direction == 1){  //left
                calculateVectorX(8, mapEditor);
                x -= vectorX;
            }

            calculateVectorY(1, 22, mapEditor);
            y += vectorY;
        }
    }


    public void setSetOfFrames(int setOfFrames){
        animation.setSetOfFrames(setOfFrames);
    }

    public void stopRunning(int direction){
        isRunning = false;
        isAction = 1;
        enemyScenario.setDirection(centerX, centerY, 16, direction);
        targetX = enemyScenario.getTargetX();
        setDirection(targetX);
    }


    public void tilemapCollision(MapTer[][] mapTer, MapEditor mapEditor, int worldWidth, int worldHeight){

        int tileWidth = 16;
        int tileHeight = 16;

        int centerCol = ((centerX) + mapEditor.getScrollLevel1Map().getWorldOffsetX()) / tileWidth;
        int leftCol = ((left) + mapEditor.getScrollLevel1Map().getWorldOffsetX()) / tileWidth;
        int rightCol = ((right) + mapEditor.getScrollLevel1Map().getWorldOffsetX()) / tileWidth;

        int centerRow = ((centerY) + mapEditor.getScrollLevel1Map().getWorldOffsetY()) / tileHeight;
        if(centerRow < 0) centerRow = 0;
        if(centerRow >= worldHeight) centerRow = worldHeight - 1;

        int topRow = ((top) + mapEditor.getScrollLevel1Map().getWorldOffsetY()) / tileHeight;
        if(topRow < 0) topRow = 0;
        if(topRow >= worldHeight) topRow = worldHeight - 1;

        int bottomRow = ((bottom) + mapEditor.getScrollLevel1Map().getWorldOffsetY()) / tileHeight;
        if(bottomRow < 0) bottomRow = 0;
        if(bottomRow >= worldHeight) bottomRow = worldHeight - 1;

        if(mapTer[topRow][centerCol].isBlocked()){  //to the north
            isNorth = true;
            northTer = mapTer[topRow][centerCol];
        }
        else{
            isNorth = false;
            northTer = null;
        }
        if(mapTer[bottomRow][centerCol].isBlocked()){  //south
            isSouth = true;
            southTer = mapTer[bottomRow][centerCol];
        }
        else{
            isSouth = false;
            southTer = null;
        }
        if(mapTer[centerRow][leftCol].isBlocked()){  //west
            isWest = true;
            westTer = mapTer[centerRow][leftCol];
        }
        else{
            isWest = false;
            westTer = null;
        }
        if(mapTer[centerRow][rightCol].isBlocked()){  //east
            isEast = true;
            eastTer = mapTer[centerRow][rightCol];
        }
        else{
            isEast = false;
            eastTer = null;
        }
    }


    @Override
    public int getX(){
        return x - mapEditor.getScrollLevel1Map().getWorldOffsetX();
    }

    @Override
    public int getY(){
        return y - mapEditor.getScrollLevel1Map().getWorldOffsetY();
    }

    @Override
    public int getSpriteWidth(){
        return spriteWidth;
    }

    @Override
    public int getSpriteHeight(){
        return spriteHeight;
    }

    @Override
    public int getLeft(){
        return left;
    }

    public int getRight(){
        return right;
    }

    @Override
    public int getTop(){
        return top;
    }

    public int getBottom(){
        return bottom;
    }

    @Override
    public int getHitboxWidth(){
        return hitboxWidth;
    }

    @Override
    public int getHitboxHeight(){
        return hitboxHeight;
    }

    public int getCenterX(){
        return centerX;
    }

    public int getCenterY() { return centerY; }

    public int getVectorX(){
        return vectorX;
    }

    public int getVectorY(){
        return vectorY;
    }

    @Override
    public void setScenario(Scenario scenario) {
        platformerScenario = (PlatformerScenario) scenario;
    }

    @Override
    public int isAction() {
        return 0;
    }

    @Override
    public Decorator getDecorator() {
        return null;
    }

    /**
     ____________>             _                            _
     |_ |             idleRight  |  currentAction    right    | numDirections
     |_ |             idleLeft  _|                   left    _|
     |_ |_ |_ |_ |_ > runRight   |  currentAction
     |_ |_ |_ |_ |_ >   ...     _|
     |_ |_ |_ |_ |_ > jumpRight  |  currentAction
     |_ |_ |_ |_ |_ >   ...     _|       _
     |_ |_ |_ >       shootStraightRight  | ...
     |_ |_ |_ >         ...               |
     \/

     formula:
     setOfFrames = (currentAction * numDirections) + Direction

     => idleRight = (0 * 2) + 0;
     jumpLeft = (2 * 2) + 1;

     */
}
