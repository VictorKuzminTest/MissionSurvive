package com.missionsurvive.geom;

/**
 * Created by kuzmin on 03.05.18.
 */

public class Isometric {

    private int isoX;  //Переменные, которые служат для перевода из декартовой в изометрическую систему координат.
    private int isoY;
    private int cartX;
    private int cartY;

    public Isometric(){

    }


    public int getIsoX(int cartX, int cartY){
        return isoX = cartX - cartY;
    }

    public int getIsoY(int cartX, int cartY){
        return isoY = (cartX + cartY) / 2;
    }

    public int getCartX (int isoX, int isoY){
        return cartX = (2 * isoY + isoX) / 2;
    }

    public int getCartY(int isoX, int isoY){
        return cartY = (2 * isoY - isoX) / 2;
    }
}
