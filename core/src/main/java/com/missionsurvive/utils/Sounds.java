package com.missionsurvive.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by kuzmin on 03.05.18.
 */

public class Sounds {
    public static Sound gun;
    public static Sound explosion;
    public static Sound fireball;
    public static Sound carwhish;

    public static Music music;

    public static void loadSounds(){
        gun = Gdx.audio.newSound(Gdx.files.internal("sound/sounds/gun.mp3"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("sound/sounds/explosion.mp3"));
        fireball = Gdx.audio.newSound(Gdx.files.internal("sound/sounds/fireball.mp3"));
        carwhish = Gdx.audio.newSound(Gdx.files.internal("sound/sounds/carwhish.mp3"));
    }

    public static void loadMusic(String fileName){
        if(music != null){
            music.dispose();
        }
        music = Gdx.audio.newMusic(Gdx.files.internal("sound/music/" + fileName));
    }

}
