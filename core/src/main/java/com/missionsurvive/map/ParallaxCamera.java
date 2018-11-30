package com.missionsurvive.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class ParallaxCamera extends OrthographicCamera {
    private Matrix4 parallaxView = new Matrix4();
    private Matrix4 parallaxCombined = new Matrix4();
    private Vector3 tmp = new Vector3();
    private Vector3 tmp2 = new Vector3();

    public ParallaxCamera (float viewportWidth, float viewportHeight) {
        super(viewportWidth, viewportHeight);
    }

    public Matrix4 calculateParallaxMatrix (float parallaxX, float parallaxY) {
        update();
        tmp.set(position);
        tmp.x *= parallaxX;
        tmp.y *= parallaxY;

        parallaxView.setToLookAt(tmp, tmp2.set(tmp).add(direction), up);
        parallaxCombined.set(projection);
        Matrix4.mul(parallaxCombined.val, parallaxView.val);
        return parallaxCombined;
    }
}
