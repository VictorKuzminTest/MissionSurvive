package com.missionsurvive.andviews;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Game;
import com.missionsurvive.R;
import com.missionsurvive.scenarios.commands.Command;

public class PopupNewMap extends Popup implements View.OnClickListener {

    private Activity activity;
    private Button okButton, cancelButton;
    private EditText levelWidth, levelHeight;
    private Command command;

    /**
     * We have to pass activity parameter (NOT context), otherwise Dialog will not work.
     * @param activity
     */
    public PopupNewMap(Activity activity){
        super(activity);
        RelativeLayout popupViewGroup = activity.findViewById(R.id.newmap);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.fragment_new_map, popupViewGroup);

        this.activity = activity;

        super.setContentView(contentView);
    }

    /**
     * Here we initialize control views.
     */
    @Override
    protected void onStart() {
        super.onStart();

        levelWidth = findViewById(R.id.editText_level_width);
        levelHeight = findViewById(R.id.editText_level_height);

        okButton = findViewById(R.id.button_ok);
        okButton.setOnClickListener(this);

        cancelButton = findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);
    }

    /**
     * Method handles on buttons clicks.
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_ok:
                command.execute("width", levelWidth.getText().toString());
                command.execute("height", levelHeight.getText().toString());
                command.execute("newMap", null);
                dismiss();
                break;
            case R.id.button_cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void setCommand(Command command) {
        this.command = command;
    }
}
