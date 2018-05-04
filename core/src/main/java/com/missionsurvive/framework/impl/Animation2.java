package com.missionsurvive.framework.impl;

import com.missionsurvive.framework.Animation;

/**
 * Created by kuzmin on 03.05.18.
 */

public class Animation2 implements Animation {

    private List<Animation> animation;
    private float[] frameVertices;

    private int assetX;
    private int assetY;
    private int currentFrame;
    private int setOfFrames;

    public Animation2(int assetX, int assetY,
                      int spriteWidth, int spriteHeight){
        this.assetX = assetX;
        this.assetY = assetY;
        animation = new ArrayList<Animation>();

        setActionFrame(assetX, assetY, spriteWidth, spriteHeight,
                pixmap.getWidth(), pixmap.getHeight());
    }

    /**
     * vertices.setVertices(new float[]{
     0, 0, 0, 1,
     480, 0, 1, 1,
     480, 320, 1, 0,
     0, 320, 0, 0}, 0, 16);
     * @param srcX
     * @param srcY
     * @param srcWidth
     * @param srcHeight
     * @param pixmapWidth
     * @param pixmapHeight
     */
    public void setActionFrame(float srcX, float srcY, float srcWidth, float srcHeight,
                               float pixmapWidth, float pixmapHeight){
        frameVertices = new float[8];

        frameVertices[0] = srcX / pixmapWidth;
        frameVertices[1] = (srcY + srcHeight) / pixmapHeight;

        frameVertices[2] = (srcX + srcWidth) / pixmapWidth;
        frameVertices[3] = (srcY + srcHeight) / pixmapHeight;

        frameVertices[4] = (srcX + srcWidth) / pixmapWidth;
        frameVertices[5] = srcY / pixmapHeight;

        frameVertices[6] = srcX / pixmapWidth;
        frameVertices[7] = srcY / pixmapHeight;
    }

    @Override
    public void animate(int startFrame, int endFrame) {
        if(!(currentFrame == endFrame)){
            int step = (endFrame - startFrame) > 0 ? 1 : -1;
            currentFrame += step;
        }
    }

    @Override
    public int getCurrentFrame() {
        return currentFrame;
    }

    @Override
    public int getSetOfFrames() {
        return setOfFrames;
    }

    @Override
    public void setSetOfFrames(int setOfFrames) {
        this.setOfFrames = setOfFrames;
    }

    @Override
    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    @Override
    public int[] getActionFrames() {
        return null;
    }

    @Override
    public int getX() {
        return assetX;
    }

    @Override
    public int getY() {
        return assetY;
    }

    @Override
    public void addChild(Animation animation) {
        this.animation.add(animation);
    }

    @Override
    public List<Animation> getChildren() {
        return animation;
    }

    @Override
    public float[] getRectVertices() {
        return frameVertices;
    }
}
