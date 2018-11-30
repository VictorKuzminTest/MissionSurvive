package com.missionsurvive.screens;

public abstract class GameScreen {

    public abstract void putPlayer(int x, int y);

    public abstract void pause(boolean pause);

    public abstract boolean onPause();

    public abstract void setScreenPos(int x, int y);
}
