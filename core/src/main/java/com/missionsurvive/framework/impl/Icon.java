package com.missionsurvive.framework.impl;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kuzmin on 04.05.18.
 */

public class Icon {
    private int x;
    private int y;
    private int assetStartX;
    private int assetStartY;
    private int assetWidth;
    private int assetHeight;

    private Pixmap pixmap;
    private String iconName;

    public Icon(String iconName, String assetName, int x, int y, int srcX, int srcY, int width, int height){
        this.x = x;
        this.y = y;
        this.assetStartX = srcX;
        this.assetStartY = srcY;
        this.assetWidth = width;
        this.assetHeight = height;
        this.iconName = iconName;

        if(assetName != null){
            this.pixmap = Assets.pixmaps[Assets.getWhichPixmap(assetName)];
        }
    }


    public void drawIcon(SpriteBatch batch){
		/*g.drawPixmap(pixmap, x, y,
				1 + assetStartX * (assetWidth + 2), 1 + assetStartY * (assetHeight + 2),
				assetWidth, assetHeight, null);*/
    }

    public void drawIcon(Graphics g, int offsetStartX, int offsetStartY, int offsetWidth, int offsetHeight){
		/*g.drawPixmap(pixmap, x, y,
				assetStartX + offsetStartX, assetStartY + offsetStartY,
				assetWidth + offsetWidth, assetHeight + offsetHeight, null);*/
    }

    public void drawIconRect(Graphics g, int color, int x, int y, int iconWidth, int iconHeight){
        g.drawRect(x, y, iconWidth, iconHeight, color, null);
    }

    public String getIconName(){
        return iconName;
    }

    public int getX(){
        return x;
    }

    public void setX(int x){
        this.x -= x;
    }

    public int getY(){
        return y;
    }

    public void setY(int y){
        this.y -= y;
    }

    public int getAssetStartX(){
        return this.assetStartX;
    }

    public void setAssetStartX(int assetStartX){
        this.assetStartX = assetStartX;
    }

    public int getAssetStartY(){
        return this.assetStartY;
    }

    public void setAssetStartY(int assetStartY){
        this.assetStartY = assetStartY;
    }

    public int getAssetWidth(){
        return this.assetWidth;
    }

    public void setAssetWidth(int assetWidth){
        this.assetWidth = assetWidth;
    }

    public int getAssetHeight(){
        return this.assetHeight;
    }

    public void setAssetHeight(int assetHeight){
        this.assetHeight = assetHeight;
    }
}
