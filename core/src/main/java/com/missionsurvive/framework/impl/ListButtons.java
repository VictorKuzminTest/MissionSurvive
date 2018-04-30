package com.missionsurvive.framework.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.commands.Command;
import com.missionsurvive.framework.Button;
import com.missionsurvive.framework.Listener;
import com.missionsurvive.framework.Observer;
import com.missionsurvive.geom.GeoHelper;
import com.missionsurvive.utils.Assets;

import java.util.ArrayList;

/**
 * Created by kuzmin on 27.04.18.
 */

public class ListButtons implements Observer{

    //Название списка. Поскольку мы будем добавлять элементы списка из сценария программно,
    //нам нужно знать что и в какой список добавляем.
    private String name;
    private Texture texture;
    private String layout;
    private ArrayList<Button> buttons = new ArrayList<Button>();
    //We are using composite pattern (GOF) to keeping track of list of buttons on the control panel:
    private ArrayList<ListButtons> lists = new ArrayList<ListButtons>();
    private Listener listener;

    private int startX;  //listing startX
    private int startY;   //listing startY
    private int listingWidth;
    private int listingHeight;
    private int spaceBetweenButtons; //space between buttons in pixels
    private int srcBgX, srcBgY; //откуда начинается отрисовка asset для background.

    /**
     * Constructor for root lists, which contain other list of buttons.
     * @param name
     */
    public ListButtons(String name, Listener listener){
        if(name != null){
            this.name = name;
        }
        this.listener = listener;
    }

    public ListButtons(String name, String assetName, int spaceBetweenButtons,
                       int startX, int startY, int srcBgX, int srcBgY,
                       int listingWidth, int listingHeight, String layout){
        this.startX = startX;
        this.startY = startY;
        this.listingWidth = listingWidth;
        this.listingHeight = listingHeight;

        this.name = name;
        this.layout = layout;
        this.spaceBetweenButtons = spaceBetweenButtons;

        this.srcBgX = srcBgX;
        this.srcBgY = srcBgY;

        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }
    }

    public void addNewButton(String assetName,
                             int col, int row, int assetStartX, int assetStartY,
                             int buttonWidth, int buttonHeight,
                             String action, Command command){
        int startX;
        int startY;

        if(layout.equalsIgnoreCase("horizontal")){  //horizontal layout.
            startX = this.startX + this.buttons.size() * (buttonWidth + spaceBetweenButtons);
            startY = this.startY;
        }
        else if(layout.equalsIgnoreCase("vertical")){  //vertical layout.
            startX = this.startX;
            startY = this.startY + this.buttons.size() * (buttonHeight + spaceBetweenButtons);
        }
        else{  //для grid Layout (row & col передаются извне).
            startX = this.startX + col * (buttonWidth + spaceBetweenButtons);
            startY = this.startY + row * (buttonHeight + spaceBetweenButtons);
        }

        Button button = new ActionButton(assetName,
                startX, startY, assetStartX, assetStartY, buttonWidth, buttonHeight, action);
        buttons.add(button);
        buttons.get(buttons.size() - 1).setCommand(command);
    }

    public void drawButtons(SpriteBatch batch){
        if(texture != null){
            batch.begin();
            batch.draw(texture, -240 + startX,
                    -160 + GeoHelper.transformCanvasYCoordToGL(startY, 320, listingHeight),
                    srcBgX, srcBgY,
                    listingWidth, listingHeight);
            batch.end();
        }

        int numbuttons = buttons.size();
        for(int whichButton = 0; whichButton < numbuttons; whichButton++){
            Button button = buttons.get(whichButton);

            int buttonStartX = button.getStartX();
            int buttonAssetWidth = button.getButtonWidth();
            int buttonStartY = button.getStartY();
            int buttonAssetHeight = button.getButtonHeight();

            if(!GeoHelper.overlapRectangles(buttonStartX, buttonStartY, buttonAssetWidth, buttonAssetHeight,
                    this.startX, this.startY, this.listingWidth, this.listingHeight)){
                //button is absolutely beyond listing
                continue;
            }
            else{
                int offsetX = 0;
                int offsetY = 0;
                int offsetWidth = 0;
                int offsetHeight = 0;

                if(buttonStartX <= this.startX){ //часть иконки за пределами листинга левой части листинга.
                    offsetX = this.startX - buttonStartX;
                    offsetWidth = -offsetX;
                }
                if(buttonStartX + buttonAssetWidth > this.startX + listingWidth){
                    offsetWidth = ((buttonStartX + buttonAssetWidth) - (this.startX  + listingWidth)) * -1;
                }
                if(buttonStartY <= this.startY){
                    offsetY = this.startY - buttonStartY;
                    offsetHeight = -offsetY;
                }
                if(buttonStartY + buttonAssetHeight > this.startY + listingHeight){
                    offsetHeight = ((buttonStartY + buttonAssetHeight) - (this.startY  + listingHeight)) * -1;
                }
                //drawing button with offset
                button.drawButton(batch, offsetX, offsetY, offsetWidth, offsetHeight);
            }
        }
    }

    public int getOffsetX(int buttonStartX){
        return this.startX - buttonStartX;
    }

    public int getOffsetY(int buttonStartY){
        return this.startY - buttonStartY;
    }

    @Override
    public void update() {

    }

    /**
     * Scrolling buttons inside the listing.
     * @param distanceX delta distance x
     * @param distanceY delta distance y
     */
    public void scrollButtons(int distanceX, int distanceY){
        int len = buttons.size();
        for(int whichButton = 0; whichButton < len; whichButton++){
            Button button = buttons.get(whichButton);

            button.setStartX(distanceX);
            button.setStartY(distanceY);
        }
    }

    public ArrayList<Button> getButtons(){
        return buttons;
    }


    public void addList(ListButtons listButtons){
        lists.add(listButtons);
    }

    public ArrayList<ListButtons> getLists(){
        return lists;
    }

    public void clearList(){
        buttons.clear();
    }

    public String getName(){
        return name;
    }

    public int getStartX(){
        return startX;
    }

    public int getStartY(){
        return startY;
    }

    public int getListWidth(){
        return listingWidth;
    }

    public int getListHeight(){
        return listingHeight;
    }

    public Listener getListener(){
        return listener;
    }



}
