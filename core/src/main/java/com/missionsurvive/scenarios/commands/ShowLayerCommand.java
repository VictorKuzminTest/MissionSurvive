package com.missionsurvive.scenarios.commands;

import com.badlogic.gdx.Screen;
import com.missionsurvive.MSGame;
import com.missionsurvive.screens.PlatformerScreen;

/**
 * Created by kuzmin on 14.05.18.
 */

public class ShowLayerCommand implements Command{

    private Screen screen;

    private int layerId;

    @Override
    public String execute(String key, String value) {
        ((PlatformerScreen)screen).setTilesetLayerToDraw(layerId);
        if(layerId == PlatformerScreen.FIRST_LAYER){
            //we also reset extremes in ScrollMap Class, so user could edit map easily:
            ((PlatformerScreen)screen).getMap().getScrollMap().setExtremesForEditing(-(MSGame.SCREEN_WIDTH / 2),
                    ((PlatformerScreen)screen).getMap().getLevel1Ter()[0].length * 16,  //16 - tileWidth
                    -(MSGame.SCREEN_HEIGHT / 2),
                    ((PlatformerScreen)screen).getMap().getLevel1Ter().length * 16);    //16 - tileHeight.
        }
        else if(layerId == PlatformerScreen.ALL_LAYERS){
            ((PlatformerScreen)screen).getMap().getScrollMap().setExtremesToNormal(false);
        }
        return null;
    }

    public void setScreenAndLayer(PlatformerScreen platformerScreen, int layer){
        this.screen = platformerScreen;
        layerId = layer;
    }
}
