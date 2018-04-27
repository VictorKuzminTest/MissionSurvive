package com.missionsurvive.utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.XmlReader;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.XML;

import java.util.ArrayList;
import java.util.List;

import sun.rmi.runtime.Log;

/**
 * Created by kuzmin on 22.04.18.
 */

public class Assets {

    private static Texture[] textures;
    private static String[] texNames; //array that contains names of textures.
    private static XML xml;
    private static Game thisGame;

    public static void setMapAssets(MSGame game){
        xml = game.getXMLParser();

        thisGame = game;

        XmlReader.Element root = xml.getRoot("xml/assets.xml");
        XmlReader.Element[] assets = xml.getChildNodes(root, "asset");
        int assetsCount = assets.length;

        textures = new Texture[assetsCount];
        texNames = new String[assetsCount];

        for(int i = 0; i < assetsCount; i++){
            String path;
            String name;

            path = xml.getAttrValue(assets[i], "path");
            name = xml.getAttrValue(assets[i], "name");

            textures[i] = new Texture(path);
            texNames[i] = name;
        }
    }

    public static Texture[] getTextures(){
        return textures;
    }

    public static Game getGame(){
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

}
