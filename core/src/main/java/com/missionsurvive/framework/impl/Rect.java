package com.missionsurvive.framework.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.missionsurvive.MSGame;
import com.missionsurvive.geom.GeoHelper;

public class Rect extends Icon{

    private ShapeRenderer rect;

    //transforming logic coords into real (drawing rects):
    private float scaleToDrawX;
    private float scaleToDrawY;

    public Rect(String iconName, String assetName, int x, int y, int srcX, int srcY, int width, int height,
                float r, float g, float b, float a){
        super(iconName, assetName, x, y, srcX, srcY, width, height);

        scaleToDrawX = (float)Gdx.graphics.getBackBufferWidth() / MSGame.SCREEN_WIDTH;
        scaleToDrawY = (float)Gdx.graphics.getBackBufferHeight() / MSGame.SCREEN_HEIGHT;

        rect = new ShapeRenderer();
        rect.setColor(r, g, b, a);
    }

    @Override
    public void drawIcon(SpriteBatch batch) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        rect.begin(ShapeRenderer.ShapeType.Filled);
        rect.rect(super.getX() * scaleToDrawX,
                GeoHelper.transformCanvasYCoordToGL(super.getY(), MSGame.SCREEN_HEIGHT,
                        super.getAssetHeight()) * scaleToDrawY,
                super.getAssetWidth() * scaleToDrawX, super.getAssetHeight() * scaleToDrawY);
        rect.end();
    }
}
