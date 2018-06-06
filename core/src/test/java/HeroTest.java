import com.missionsurvive.map.MapEditor;
import com.missionsurvive.map.MapTer;
import com.missionsurvive.map.ScrollMap;
import com.missionsurvive.objs.Gun;
import com.missionsurvive.objs.actors.Hero;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kuzmin on 28.05.18.
 */

public class HeroTest {

    private Hero hero = new Hero("hero", 12, 0, Hero.DIRECTION_RIGHT);

    public MapEditor mapEditor;
    public MapTer[][] map;
    public ScrollMap scrollMap;
    public int tileSize;
    public int worldWidth, worldHeight;


    public void setMap(int tileSize){
        this.tileSize = tileSize;
        worldHeight = 20; worldWidth = 20;
        map = new MapTer[worldHeight][worldWidth];
        for(int row = 0; row < map.length; row++){
            for(int col = 0; col < map[row].length; col++){
                map[row][col] = new MapTer(row, col, 0, 0, tileSize, tileSize);

                //left side
                if(col == 0){
                    map[row][col].setBlocked(true);
                }
                //right side
                if(col == 19){
                    map[row][col].setBlocked(true);
                }
                //horizontal
                if(row == 19){
                    map[row][col].setBlocked(true);
                }
            }
        }

        //blocked tiles:
        map[18][8].setBlocked(true);
        map[18][9].setBlocked(true);
        map[18][10].setBlocked(true);
        map[17][8].setBlocked(true);
        map[17][9].setBlocked(true);
        map[17][10].setBlocked(true);

        map[11][8].setBlocked(true); map[11][8].setLadder(true);
        map[10][9].setBlocked(true); map[10][9].setLadder(true);

        scrollMap = new ScrollMap(tileSize, tileSize, worldHeight, worldWidth, false);
        mapEditor = new MapEditor(worldWidth, worldHeight);
        mapEditor.setScrollMap(scrollMap);
    }


    @Test
    public void cut_running_vector(){
        setMap(16);

        //case 1:
        hero = new Hero("hero", 56, 219, Hero.DIRECTION_RIGHT);
        hero.run();
        hero.running(map, mapEditor, tileSize);
        assertEquals(2, hero.getMovingVector().getX());

        //case 2:
        hero = new Hero("hero", 94, 219, Hero.DIRECTION_RIGHT);
        hero.run();
        hero.running(map, mapEditor, tileSize);
        assertEquals(2, hero.getMovingVector().getX());
    }


    @Test
    public void hero_jump(){
        Hero hero = new Hero("hero", 100, 150, Hero.DIRECTION_RIGHT);

        //case 1: command for hero to start jumping.
        hero.setAction(Hero.ACTION_IDLE);
        hero.jump();
        assertTrue(hero.isAction() == Hero.ACTION_JUMPING);

        //case 2: hero cannot jump when it is falling.
        hero.setAction(Hero.ACTION_FALLING);
        hero.jump();
        assertFalse(hero.isAction() == Hero.ACTION_JUMPING);
    }

    @Test
    public void stop_actions(){
        Hero hero = new Hero("hero", 0, 0, Hero.DIRECTION_RIGHT);

        //case 1:
        hero.run();
        assertTrue(hero.isAction() == Hero.ACTION_RUNNING);
        hero.stopActions();
        assertTrue(hero.isAction() == Hero.ACTION_IDLE);

        //case 2 action cannot be stopped when hero is falling or jumping:
        hero.jump();
        hero.stopActions();
        assertFalse(hero.isAction() == Hero.ACTION_IDLE);
        hero.setAction(Hero.ACTION_FALLING);
        hero.stopActions();
        assertFalse(hero.isAction() == Hero.ACTION_IDLE);
    }

    @Test
    public void shoot(){
        setMap(16);
        Hero hero = new Hero("hero", 100, 100, Hero.DIRECTION_RIGHT);

        //case 1: shoot.
        hero.shoot(0, 0, 50, 50, true);
        assertTrue(hero.isAction() == Hero.ACTION_SHOOTING);

        //case 2: hero cannot shoot while jumping.
        hero.setAction(Hero.ACTION_JUMPING);
        hero.shoot(0, 0, 50, 50, true);
        assertFalse(hero.isAction() == Hero.ACTION_SHOOTING);

        //case 3: hero cannot shoot while falling.
        hero.setAction(Hero.ACTION_FALLING);
        hero.shoot(0, 0, 50, 50, true);
        assertFalse(hero.isAction() == Hero.ACTION_SHOOTING);
    }


    @Test
    public void heroCannotRunWhileShooting(){
        setMap(16);
        newHero(100, 100, Hero.DIRECTION_RIGHT);
        hero.setWeapon(new Gun());

        hero.shoot(0, 0, 50, 50, true);
        assertEquals(4, hero.getAnimation().getCurrentFrame());
        hero.animate(map, mapEditor, 20, 20, 0.081f);
        assertEquals(5, hero.getAnimation().getCurrentFrame());
        hero.run();
        assertTrue(hero.isAction() == Hero.ACTION_SHOOTING);
    }

    @Test
    public void heroRunsOnAParticularFrameOfShootingAnimation(){
        setMap(16);
        newHero(100, 100, Hero.DIRECTION_RIGHT);
        hero.setWeapon(new Gun());

        hero.shoot(0, 0, 50, 50, true);
        assertEquals(4, hero.getAnimation().getCurrentFrame());
        hero.animate(map, mapEditor, 20, 20, 0.081f);
        assertEquals(5, hero.getAnimation().getCurrentFrame());
        hero.shoot(0, 0, 50, 50, false);
        hero.animate(map, mapEditor, 20, 20, 0.3f);
        hero.run();
        assertTrue(hero.isAction() == Hero.ACTION_RUNNING);
    }

    @Test
    public void heroDie(){
        newHero(100, 100, Hero.DIRECTION_RIGHT);
        hero.setAction(Hero.ACTION_JUMPING);
        hero.die();
        assertEquals(Hero.ACTION_DYING, hero.isAction());
    }

    @Test
    public void heroIsDeadAfterLastAnimationFrame(){
        setMap(16);
        newHero(100, 100, Hero.DIRECTION_RIGHT);
        hero.die();
        int numDirections = 2;
        int numDyingSprites = hero.getAnimation().
                getActionFrames()[Hero.SPRITES_DYING * numDirections + hero.getDirection()];
        for(int i = 0; i < numDyingSprites; i++){
            hero.animate(map, mapEditor, 20, 20, 0.081f);
        }
        assertEquals(Hero.ACTION_DEAD, hero.isAction());
    }

    @Test
    public void heroBecomesResistibleAfterSomeTime(){
        setMap(16);
        newHero(100, 100, Hero.DIRECTION_RIGHT);
        hero.updateResistence(Hero.IRRESISTIBLE_TICK + 0.001f);
        assertFalse(hero.isIrresistible());
    }

    public Hero newHero(int x, int y, int direction){
        return hero = new Hero("hero", x, y, direction);
    }


}
