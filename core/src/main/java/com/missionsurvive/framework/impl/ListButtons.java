package com.missionsurvive.framework.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionsurvive.scenarios.commands.Command;
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

    public static final int LAYOUT_GRID = 0;
    public static final int LAYOUT_VERTICAL = 1;
    public static final int LAYOUT_HORIZONTAL = 2;

    //Название списка. Поскольку мы будем добавлять элементы списка из сценария программно,
    //нам нужно знать что и в какой список добавляем.
    private String name;
    private Texture texture;
    private String layout;
    private ArrayList<Button> buttons;
    //We are using composite pattern (GOF) to keeping track of list of buttons on the control panel:
    private ArrayList<ListButtons> lists;
    private Listener listener; //listens to other lists

    private int startX;  //listing startX
    private int startY;   //listing startY
    private int listingWidth;
    private int listingHeight;
    private int spaceBetweenButtons; //space between buttons in pixels
    private int srcBgX, srcBgY; //откуда начинается отрисовка asset для background.
    private int layoutId;

    /**
     * Constructor for root lists, which contains other lists of buttons.
     * @param listener
     */
    public ListButtons(Listener listener){
        this.listener = listener;
        lists = new ArrayList<ListButtons>();
    }

    public ListButtons(String name, String assetName, int spaceBetweenButtons,
                       int startX, int startY, int srcBgX, int srcBgY,
                       int listingWidth, int listingHeight, String layout){
        lists = new ArrayList<ListButtons>();
        buttons = new ArrayList<Button>();

        this.startX = startX;
        this.startY = startY;
        this.listingWidth = listingWidth;
        this.listingHeight = listingHeight;

        this.name = name;

        if(layout != null){
            if(layout.equalsIgnoreCase("horizontal")){
                layoutId = LAYOUT_HORIZONTAL;
            }
            else if(layout.equalsIgnoreCase("vertical")){
                layoutId = LAYOUT_VERTICAL;
            }
            this.layout = layout;
        }

        this.spaceBetweenButtons = spaceBetweenButtons;

        this.srcBgX = srcBgX;
        this.srcBgY = srcBgY;

        if(assetName != null){
            texture = Assets.getTextures()[Assets.getWhichTexture(assetName)];
        }
    }

    public void addNewButton(String assetName,
                             int col, int row, int assetStartX, int assetStartY,
                             int buttonWidth, int buttonHeight, Command command){
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
                startX, startY, assetStartX, assetStartY, buttonWidth, buttonHeight, command);
        buttons.add(button);
    }

    /**
     * Method for root list of buttons.
     */
    public void drawLists(SpriteBatch batch){
        int listsCount = lists.size();
        for(int i = 0; i < listsCount; i++){
            lists.get(i).drawButtons(batch);
        }
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
    public void scrollButtons(float distanceX, float distanceY){
        switch (layoutId){
            case LAYOUT_GRID:
                scroll(0, getVertBounds(distanceY));
                scroll(getHorBounds(distanceX), 0);
                break;
            case LAYOUT_VERTICAL:
                scroll(0, getVertBounds(distanceY));
                break;
            case LAYOUT_HORIZONTAL:
                scroll(getHorBounds(distanceX), 0);
                break;
        }
    }

    public float getVertBounds(float distanceY){
        if(distanceY > 0){
            Button firstButton = buttons.get(0);
            if((firstButton.getStartY() + distanceY) > startY){
                distanceY = startY - firstButton.getStartY();
                return distanceY;
            }
        }
        else if(distanceY < 0){
            Button lastButton = buttons.get(buttons.size() - 1);
            if((lastButton.getStartY() + lastButton.getButtonHeight() + distanceY) < (startY + listingHeight)){
                distanceY = (startY + listingHeight) - (lastButton.getStartY() + lastButton.getButtonHeight());
                return distanceY;
            }
        }
        return distanceY;
    }

    public float getHorBounds(float distanceX){
        if(distanceX > 0){
            Button firstButton = buttons.get(0);
            if((firstButton.getStartX() + distanceX) > startX){
                distanceX = startX - firstButton.getStartX();
                return distanceX;
            }
        }
        else if(distanceX < 0){
            Button lastButton = buttons.get(buttons.size() - 1);
            if((lastButton.getStartX() + lastButton.getButtonWidth() + distanceX) < (startX + listingWidth)){
                distanceX = (startX + listingWidth) - (lastButton.getStartX() + lastButton.getButtonWidth());
                return distanceX;
            }
        }
        return distanceX;
    }

    public void scroll(float distanceX, float distanceY){
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
