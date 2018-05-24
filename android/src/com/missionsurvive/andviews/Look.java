package com.missionsurvive.andviews;

import java.util.List;

/**
 * Created by kuzmin on 17.05.18.
 */

public interface Look {

    public List<Popup> getPopups();

    public void addPopup(Popup popup);
}
