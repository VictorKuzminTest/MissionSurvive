package com.missionsurvive.andviews;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuzmin on 17.05.18.
 */

public class AndroidLook implements Look {

    public static final int POPUP_SAVE = 0;
    public static final int POPUP_LOAD = 1;
    public static final int POPUP_NEW_MAP = 2;
    public static final int POPUP_NEW_BOT = 3;
    public static final int POPUP_BUY = 4;
    private List<Popup> popups;

    /**
     * In constructor we  add new dialogs for saving and loading levels.
     * @param activity  important to pass activity parameter to dialog constructor instead of context.
     */
    public AndroidLook(Activity activity){
        popups = new ArrayList<Popup>();

        Popup popupSave = new PopupSave(activity);
        addPopup(popupSave);

        Popup popupLoad = new PopupLoad(activity);
        addPopup(popupLoad);

        Popup popupNewMap = new PopupNewMap(activity);
        addPopup(popupNewMap);

        Popup popupNewBot = new PopupBots(activity);
        addPopup(popupNewBot);

        Popup popupBuy = new PopupBuy(activity);
        addPopup(popupBuy);
    }

    @Override
    public List<Popup> getPopups() {
        return popups;
    }

    /**
     * Adds new popup (dialog). Class Popup is abstract, so we add its extended objects.
     * @param popup
     */
    @Override
    public void addPopup(Popup popup) {
        popups.add(popup);
    }
}
