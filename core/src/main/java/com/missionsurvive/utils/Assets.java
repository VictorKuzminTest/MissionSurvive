package com.missionsurvive.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.XmlReader;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.XML;

public class Assets {

    private static Texture[] textures;
    //array that contains names of textures.
    private static String[] texNames;
    private static XML xml;
    private static MSGame thisGame;
    private static String currentLevel;

    public static void setMapAssets(MSGame game){
        ...
    }

    public static Texture[] getTextures(){
        return textures;
    }

    public static void setGame(MSGame game){
        thisGame = game;
    }

    public static MSGame getGame(){
        return thisGame;
    }

    public static int getWhichTexture(String assetName){
        int numPixmaps = texNames.length;
        for(int whichPixmap = 0; whichPixmap < numPixmaps; whichPixmap++){
            if(texNames[whichPixmap].equalsIgnoreCase(assetName)){
                return whichPixmap;
            }
        }
        return 0;
    }

    public static void setCurrentLevel(String level){
        currentLevel = level;
    }

    public static String getCurrentLevel(){
        return currentLevel;
    }
}
