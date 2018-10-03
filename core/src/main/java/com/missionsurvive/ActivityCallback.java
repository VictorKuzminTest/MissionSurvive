package com.missionsurvive;

import com.missionsurvive.scenarios.commands.Command;

/**
 * Created by kuzmin on 17.05.18.
 */

public interface ActivityCallback {

    public void showPopup(int dialogId);

    public void setCommand(Command command, String popupName);

    public String getSharedPrefs(String key);

    public void setIntoSharedPrefs(String key, String value);
}
