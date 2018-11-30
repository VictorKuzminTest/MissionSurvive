package com.missionsurvive.objs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

public interface Weapon extends  GameObject {

    @Override
    void drawObject(SpriteBatch batch, int col, int row, int offsetX, int offsetY);

    @Override
    void update(float deltaTime);

    void update(float deltaTime, int worldOffsetX, int worldOffsetY);

    public boolean shoot(int x, int y,
                         int worldOffsetX, int worldOffsetY, int direction);

    public int getX();

    public int getY();

    public int getWidth();

    public int getHeight();

    public boolean hit(boolean isHit);

    public List<Weapon> getWeapon();

    public int getHP();
}
