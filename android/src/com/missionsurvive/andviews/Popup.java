package com.missionsurvive.andviews;

import android.app.Dialog;
import android.content.Context;

import com.missionsurvive.scenarios.commands.Command;

/**
 * Created by kuzmin on 17.05.18.
 */

public abstract class Popup extends Dialog {

    /**
     * @param context Activity is passed (NOT context), otherwise Dialog will not work.
     */
    public Popup(Context context) {
        super(context);
    }

    public abstract void setCommand(Command command);
}
