package com.missionsurvive.andviews;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.missionsurvive.R;
import com.missionsurvive.scenarios.commands.Command;
import com.missionsurvive.utils.ExternalStorage;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by kuzmin on 17.05.18.
 */

public class PopupLoad extends Popup implements View.OnClickListener{

    private Activity activity;
    private Button okButton, cancelButton;
    private ListView listView;
    private TextView chosenLevel;
    private Command command;

    /**
     * We have to pass activity parameter (NOT context), otherwise Dialog will not work.
     * @param activity
     */
    public PopupLoad(Activity activity){
        super(activity);
        RelativeLayout popupViewGroup = (RelativeLayout) activity.findViewById(R.id.levelload);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.fragment_level_load, popupViewGroup);

        this.activity = activity;

        super.setContentView(contentView);
    }


    /**
     * Here we initialize control views.
     */
    @Override
    protected void onStart() {
        super.onStart();

        chosenLevel = findViewById(R.id.textview_chosenlevel);

        okButton = findViewById(R.id.button_ok);
        okButton.setOnClickListener(this);

        cancelButton = findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);

        //Set adapter with file names to listview:
        ArrayList<String> fileNames = ExternalStorage.getListFiles("trf");
        if(fileNames != null){
            listView = findViewById(R.id.listview_levels);
            FilesListviewAdapter adapter = new FilesListviewAdapter(activity,
                    R.layout.list_item_filename, fileNames);
            listView.setAdapter(adapter);
            setListViewClickEvents(fileNames);
        }
    }


    /**
     * Method handles on ListView items clicking events.
     */
    public void setListViewClickEvents(final ArrayList<String> fileNames){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                chosenLevel.setText(fileNames.get(position));
            }
        });
    }

    @Override
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * Method handles on buttons clicks.
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_ok:
                command.execute(null, ExternalStorage.loadStringFromExternalFile("trf",
                        chosenLevel.getText().toString()));
                dismiss();
                break;
            case R.id.button_cancel:
                dismiss();
                break;
        }
    }
}
