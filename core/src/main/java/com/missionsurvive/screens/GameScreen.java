package com.missionsurvive.screens;

import com.badlogic.gdx.Screen;

/**
 * Created by kuzmin on 29.05.18.
 */

public abstract class GameScreen {

    public abstract void putPlayer(int x, int y);

    public abstract void pause(boolean pause);

    public abstract boolean onPause();

    public abstract void setScreenPos(int x, int y);
}
