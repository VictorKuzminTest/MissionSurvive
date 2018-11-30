package com.missionsurvive.framework;

import java.util.List;

public interface Animation {

    public void animate(int x1, int x2);

    public int getCurrentFrame();

    public int getSetOfFrames();

    public void setSetOfFrames(int setOfFrames);

    public void setCurrentFrame(int currentFrame);

    public int[] getActionFrames();

    public int getX();

    public int getY();

    public void addChild(Animation animation);

    public List<Animation> getChildren();
}
