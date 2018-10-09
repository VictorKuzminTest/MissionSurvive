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
        String progress = Assets.getGame().getActivityCallback()
                .getSharedPrefs("progress");
        if(!progress.equalsIgnoreCase("")){
            String fragmentDelims = "\\:";

            String[] fragmentTokens = progress.split(fragmentDelims);
            int len = (fragmentTokens.length);

            for(int whichChar = 0; whichChar < len; whichChar++) {
                if (fragmentTokens[whichChar].contains("\n")) {
                    fragmentTokens[whichChar] = fragmentTokens[whichChar].replaceAll("\n", "");
                }
                if(fragmentTokens[whichChar].equalsIgnoreCase(BEGINNER)){
                    levelProgressBeginner = Integer.parseInt(fragmentTokens[whichChar + 1]);
                }
                if(fragmentTokens[whichChar].equalsIgnoreCase(EXPERIENCED)){
                    levelProgressExperienced = Integer.parseInt(fragmentTokens[whichChar + 1]);
                }
            }
        }
    }

    public static void setProgress(String difficulty, int progress){
        if(difficulty.equalsIgnoreCase(BEGINNER)){
            if(progress > levelProgressBeginner){
                levelProgressBeginner = progress;
            }
        }
        else if(difficulty.equalsIgnoreCase(EXPERIENCED)){
            if(progress > levelProgressExperienced){
                levelProgressExperienced = progress;
            }
        }
        saveProgress();
    }

    public static void saveProgress(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(":");
        stringBuilder.append(BEGINNER);
        stringBuilder.append(":");
        stringBuilder.append(levelProgressBeginner);
        stringBuilder.append(":");
        stringBuilder.append(EXPERIENCED);
        stringBuilder.append(":");
        stringBuilder.append(levelProgressExperienced);
        stringBuilder.append(":");
        Assets.getGame().getActivityCallback().setIntoSharedPrefs("progress", stringBuilder.toString());
    }

    public static boolean isPurchased(){
        String progress = Assets.getGame().getActivityCallback()
                .getSharedPrefs("purchase");

        if(progress.equalsIgnoreCase("purchase")){
            return true;
        }
        return false;
    }

    public static void purchase(){
        Assets.getGame().getActivityCallback().setIntoSharedPrefs("purchase", "purchase");
    }
}
