package com.missionsurvive.utils;

public class Progress {
    public static final String BEGINNER = "Beginner";
    public static final String EXPERIENCED = "Experienced";

    private static int levelProgressBeginner;
    private static int levelProgressExperienced;

    public static int getProgress(String difficulty){
        if(difficulty.equalsIgnoreCase(BEGINNER)){
            return levelProgressBeginner;
        }
        else if(difficulty.equalsIgnoreCase(EXPERIENCED)){
            return levelProgressExperienced;
        }
        return 0;
    }

    public static void loadProgress(){
        ...
    }

    public static void setProgress(String difficulty, int progress){
        ...
    }

    public static void saveProgress(){
        ...
    }

    public static boolean isPurchased(){
        ...
        return false;
    }

    public static void purchase(){
        ...
    }
}
