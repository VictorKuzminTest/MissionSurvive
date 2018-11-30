package com.missionsurvive.scenarios.commands;

import com.badlogic.gdx.Screen;
import com.missionsurvive.MSGame;
import com.missionsurvive.screens.EditorScreen;
import com.missionsurvive.screens.GameScreen;

public class ShowLayerCommand implements Command{

    private Screen screen;

    private int layerId;

    @Override
    public String execute(String key, String value) {
        ((EditorScreen)screen).setTilesetLayerToDraw(layerId);
        if(layerId == EditorScreen.FIRST_LAYER){
            //we also reset extremes in ScrollMap Class, so user could edit map easily:
            ((EditorScreen)screen).getMap().getScrollMap().setExtremesForEditing(-(MSGame.SCREEN_WIDTH / 2),
                    ((EditorScreen)screen).getMap().getLevel1Ter()[0].length * 16,  //16 - tileWidth
                    -(MSGame.SCREEN_HEIGHT / 2),
                    ((EditorScreen)screen).getMap().getLevel1Ter().length * 16);    //16 - tileHeight.
        }
        else if(layerId == EditorScreen.ALL_LAYERS){
            ((EditorScreen)screen).getMap().getScrollMap().setExtremesToNormal(false);
        }
        return null;
    }

    public void setScreenAndLayer(EditorScreen editorScreen, int layer){
        this.screen = editorScreen;
        layerId = layer;
    }

    @Override
    public void setScreen(GameScreen gameScreen) {

    }
}
