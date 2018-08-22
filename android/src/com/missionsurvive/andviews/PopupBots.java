package com.missionsurvive.andviews;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.missionsurvive.R;
import com.missionsurvive.scenarios.commands.Command;
import com.missionsurvive.scenarios.commands.PutBotCommand;
import com.missionsurvive.utils.Assets;
import com.missionsurvive.utils.Commands;

import java.util.ArrayList;

/**
 * Created by kuzmin on 17.05.18.
 */

public class PopupBots extends Popup implements View.OnClickListener {

    private Activity activity;
    private Button okButton, cancelButton;
    private ListView listView;
    private TextView chosenBot;
    private EditText direction;
    private Command putBotCommand;

    /**
     * We have to pass activity parameter (NOT context), otherwise Dialog will not work.
     * @param activity
     */
    public PopupBots(Activity activity){
        super(activity);
        RelativeLayout popupViewGroup = (RelativeLayout) activity.findViewById(R.id.botchoose);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.fragment_bot_choose, popupViewGroup);

        this.activity = activity;

        super.setContentView(contentView);
    }

    @Override
    public void setCommand(Command command){
        putBotCommand = command;
    }

    /**
     * Here we initialize control views.
     */
    @Override
    protected void onStart() {
        super.onStart();

        chosenBot = findViewById(R.id.textview_chosenbot);

        okButton = findViewById(R.id.button_ok);
        okButton.setOnClickListener(this);

        cancelButton = findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);

        direction = findViewById(R.id.edittext_direction);
        direction.addTextChangedListener(new directionTextWatcher());

        //Set adapter with file names to listview:
        ArrayList<String> bots = getBots();
        if(bots != null){
            listView = findViewById(R.id.listview_bots);
            BotsListviewAdapter adapter = new BotsListviewAdapter(activity,
                    R.layout.list_item_bot, bots);
            listView.setAdapter(adapter);
            setListViewClickEvents(bots);
        }
    }

    public ArrayList<String> getBots(){
        ArrayList<String> bots = new ArrayList<String>();

        bots.add("zombie");
        bots.add("shotgunzombie");
        bots.add("soldierzombie");
        bots.add("l1b");
        bots.add("l3b");
        bots.add("l5b");
        bots.add("l6b");
        bots.add("powerup");
        bots.add("wreckage");
        bots.add("helicopter");
        bots.add("endGame");

        return bots;
    }

    /**
     * Method handles on ListView items clicking events.
     */
    public void setListViewClickEvents(final ArrayList<String> bots){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                chosenBot.setText(bots.get(position));
            }
        });
    }

    /**
     * Method handles on buttons clicks.
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_ok:
                //setBot();
                putBotCommand.execute(PutBotCommand.PUT,
                        chosenBot.getText().toString());
                putBotCommand.execute(PutBotCommand.DIRECTION,
                        direction.getText().toString());
                dismiss();
                break;
            case R.id.button_cancel:
                dismiss();
                break;
        }
    }

    /**
     * This class implements TextWatcher to listen and to set text into EditText direction.
     */
    private class directionTextWatcher implements TextWatcher {
        int previousLength;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            previousLength = charSequence.length();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(previousLength < charSequence.length()){
                if(charSequence.toString().equalsIgnoreCase("e")){
                    direction.setText("east");
                }
                else if(charSequence.toString().equalsIgnoreCase("w")){
                    direction.setText("west");
                }
                else if(charSequence.toString().equalsIgnoreCase("n")){
                    direction.setText("north");
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
