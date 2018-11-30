package com.missionsurvive;

import com.missionsurvive.scenarios.commands.Command;

public interface ActivityCallback {

    public void showPopup(int dialogId);

    public void setCommand(Command command, String popupName);

    public String getSharedPrefs(String key);

    public void setIntoSharedPrefs(String key, String value);

    public void purchaseFullVersion();
}
