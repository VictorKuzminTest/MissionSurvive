package com.missionsurvive.scenarios.commands;

import com.missionsurvive.ActivityCallback;

/**
 * Created by kuzmin on 17.05.18.
 */

public class ShowPopupCommand implements Command{

    //ints from -android- AndroidLook. We have to duplicate them,
    // because we cannot invoke them directly from -core-.
    public static final int POPUP_SAVE = 0;
    public static final int POPUP_LOAD = 1;
    public static final int POPUP_NEW_MAP = 2;
    public static final int POPUP_NEW_BOT = 3;

    private int popupId = -1;

    private ActivityCallback activityCallback;

    public void setActivity(ActivityCallback activityCallback, int popupId){
        this.activityCallback = activityCallback;
        this.popupId = popupId;
    }

    @Override
    public String execute(String key, String value) {
        activityCallback.showPopup(popupId);
        return null;
    }
}
