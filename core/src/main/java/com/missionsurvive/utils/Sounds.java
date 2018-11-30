package com.missionsurvive.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sounds {

    public static final int MUSIC = 0;
    public static final int BOSS = 1;

    public static Sound gun;
    public static Sound explosion;
    public static Sound fireball;
    public static Sound carwhish;

    public static Music music;
    public static Music bossMusic;

    public static boolean mute;

    public static int musicToPlay;

    public static void loadSounds(){
        gun = Gdx.audio.newSound(Gdx.files.internal("sound/sounds/gun.mp3"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("sound/sounds/explosion.mp3"));
        fireball = Gdx.audio.newSound(Gdx.files.internal("sound/sounds/fireball.mp3"));
        carwhish = Gdx.audio.newSound(Gdx.files.internal("sound/sounds/carwhish.mp3"));
    }

    public static void pauseAllMusic(){
        pauseMusic();
        pauseBossMusic();
    }

    public static void unpauseAllMusic(){
        switch (musicToPlay){
            case MUSIC:
                playMusic();
                break;
            case BOSS:
                playBossMusic();
                break;
        }
    }

    public static void pauseMusic(){
        if(music != null){
            if(music.isPlaying()){
                music.pause();
            }
        }
    }

    public static void pauseBossMusic(){
        if(bossMusic != null){
            if(bossMusic.isPlaying()){
                bossMusic.pause();
            }
        }
    }

    public static void playSound(Sound sound){
        if(!mute){
            sound.play();
        }
    }

    public static void loadBossMusic(){
        if(bossMusic != null){
            bossMusic.dispose();
        }
        bossMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/music/boss.mp3"));
    }

    public static void playBossMusic(){
        stopMusic();
        musicToPlay = BOSS;
        if(!mute){
            if(bossMusic != null){
                bossMusic.setLooping(true);
                bossMusic.play();
            }
        }
    }

    public static void stopBossMusic(){
        if(bossMusic != null){
            bossMusic.stop();
        }
    }

    public static void stopMusic(){
        if(music != null){
            music.stop();
        }
    }

    public static void replayMusic(){
        stopMusic();
        stopBossMusic();
        playMusic();
    }

    public static void playMusic(){
        musicToPlay = MUSIC;
        if(!mute){
            if(music != null){
                music.setLooping(true);
                music.play();
            }
        }
    }

    public static void loadMusic(String fileName){
        disposeMusic();
        music = Gdx.audio.newMusic(Gdx.files.internal("sound/music/" + fileName));
    }

    public static void disposeMusic(){
        if(music != null){
            music.dispose();
        }
    }
}
