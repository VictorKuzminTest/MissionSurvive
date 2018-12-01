package com.missionsurvive.scenarios.commands;

import com.missionsurvive.map.MapTer;
import com.missionsurvive.objs.PowerUp;
import com.missionsurvive.objs.actors.Zombie;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.Scenario;
import com.missionsurvive.scenarios.SpawnBot;
import com.missionsurvive.scenarios.SpawnScenario;
import com.missionsurvive.screens.GameScreen;

import java.util.ArrayList;

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
        ...
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

    @Override
    public void setScreen(GameScreen gameScreen) {

    }
}
