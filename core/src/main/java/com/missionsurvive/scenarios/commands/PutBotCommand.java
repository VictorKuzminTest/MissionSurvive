package com.missionsurvive.scenarios.commands;

import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.PowerUp;
import com.missionsurvive.objs.actors.Zombie;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.SpawnBot;
import com.missionsurvive.scenarios.SpawnScenario;

import java.util.ArrayList;

/**
 * Created by kuzmin on 18.05.18.
 */

public class PutBotCommand implements Command{

    public static final String PUT = "put";
    public static final String DIRECTION = "direction";

    private String bot;
    private ArrayList<MapTer> mapTerArrayList;
    private Scenario scenario;

    public void setScenario(Scenario scenario, ArrayList<MapTer> mapTerArrayList){
        this.scenario = scenario;
        this.mapTerArrayList = mapTerArrayList;
    }

    @Override
    public String execute(String key, String value) {
        if(key != null){
            if(key.equalsIgnoreCase(PUT)){
                initBot(value);
            }
            else if(key.equalsIgnoreCase(DIRECTION)){
                setBot(value);
            }
        }
        return null;
    }

    /**
     * When we made all the manupulations (set bot), we set string of a nameof a bot to null
     * to use it correctly in future.
     */
    public void setBotToNull(){
        bot = null;
    }

    /**
     * First we have to initialize the string of a name of an object to further manipulate it.
     * @param bot
     */
    public void initBot(String bot){
        this.bot = bot;
    }

    /**
     * When we have a name of a bot we want to set, we pass the last parameter to finish the bot spawn.
     * @param direction
     */
    public void setBot(String direction){
        if(bot != null){
            if(bot.equalsIgnoreCase("zombie")){
                if(direction.equalsIgnoreCase("east")){
                    putBot(SpawnBot.ZOMBIE, Zombie.EAST);
                }
                else if(direction.equalsIgnoreCase("west")){
                    putBot(SpawnBot.ZOMBIE, Zombie.WEST);
                }
                else{
                    putBot(SpawnBot.ZOMBIE, Zombie.EAST);
                }
            }
            else if(bot.equalsIgnoreCase("shotgunzombie")){
                if(direction.equalsIgnoreCase("east")){
                    putBot(SpawnBot.SHOTGUN_ZOMBIE, Zombie.EAST);
                }
                else if(direction.equalsIgnoreCase("west")){
                    putBot(SpawnBot.SHOTGUN_ZOMBIE, Zombie.WEST);
                }
                else{
                    putBot(SpawnBot.SHOTGUN_ZOMBIE, Zombie.EAST);
                }
            }
            else if(bot.equalsIgnoreCase("soldierzombie")){
                if(direction.equalsIgnoreCase("east")){
                    putBot(SpawnBot.SOLDIER_ZOMBIE, Zombie.EAST);
                }
                else if(direction.equalsIgnoreCase("west")){
                    putBot(SpawnBot.SOLDIER_ZOMBIE, Zombie.WEST);
                }
                else{
                    putBot(SpawnBot.SOLDIER_ZOMBIE, Zombie.EAST);
                }
            }
            else if(bot.equalsIgnoreCase("l1b")){
                putBot(SpawnScenario.LEVEL_1_SCENE, 0);
            }
            else if(bot.equalsIgnoreCase("l3b")){
                putBot(SpawnBot.LEVEL_3_BOSS, Zombie.EAST);
            }
            else if(bot.equalsIgnoreCase("l5b")){
                putBot(SpawnBot.LEVEL_5_BOSS, Zombie.EAST);
                //putBot(SpawnScenario.SCENE_TEST, Zombie.EAST);
            }
            else if(bot.equalsIgnoreCase("powerup")){
                if(direction.equalsIgnoreCase("eastlife")){
                    putBot(SpawnBot.POWER_UP_LIFE, PowerUp.DIRECTION_EAST);
                }
                else if(direction.equalsIgnoreCase("eastgun")){
                    putBot(SpawnBot.POWER_UP_GUN, PowerUp.DIRECTION_EAST);
                }
                else if(direction.equalsIgnoreCase("northlife")){
                    putBot(SpawnBot.POWER_UP_LIFE, PowerUp.DIRECTION_NORTH);
                }
                else if(direction.equalsIgnoreCase("northgun")){
                    putBot(SpawnBot.POWER_UP_GUN, PowerUp.DIRECTION_NORTH);
                }
            }
            else if(bot.equalsIgnoreCase("wreckage")){
                putBot(SpawnBot.WRECKAGE, 0);
            }
            else if(bot.equalsIgnoreCase("l6b")){
                putBot(SpawnBot.LEVEL_6_BOSS, 0);
            }
            else if(bot.equalsIgnoreCase("helicopter")){
                putBot(SpawnScenario.LEVEL_2_SCENE, 0);
            }
            else if(bot.equalsIgnoreCase("endGame")){
                putBot(SpawnScenario.END_GAME_SCENE, 0);
            }
        }
        setBotToNull();
    }

    /**
     * Puts bot to chosen MapTers.
     * @param bot
     * @param direction
     */
    public void putBot(int bot, int direction){
        int len = mapTerArrayList.size();
        for(int mapTerNum = 0; mapTerNum < len; mapTerNum++){
            mapTerArrayList.get(mapTerNum).setEditing(true);
            ((PlatformerScenario)scenario).setSpawn(mapTerArrayList.get(mapTerNum).getCol(),
                    mapTerArrayList.get(mapTerNum).getRow(), bot, direction);
        }
        mapTerArrayList.clear();
    }
}
