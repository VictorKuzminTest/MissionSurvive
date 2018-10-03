package com.missionsurvive.andviews;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.missionsurvive.R;
import com.missionsurvive.scenarios.commands.Command;

public class PopupBuy extends Popup implements View.OnClickListener {

    private Activity activity;

    /**
     * We have to pass activity parameter (NOT context), otherwise Dialog will not work.
     * @param activity
     */
    public PopupBuy(Activity activity){
        super(activity);
        RelativeLayout popupViewGroup = (RelativeLayout) activity.findViewById(R.id.buy);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.fragment_buy, popupViewGroup);

        this.activity = activity;

        super.setContentView(contentView);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setCommand(Command command) {

    }
}
