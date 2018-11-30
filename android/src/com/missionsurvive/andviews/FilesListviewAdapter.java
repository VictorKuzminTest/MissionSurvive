package com.missionsurvive.andviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.missionsurvive.R;

import java.util.ArrayList;

public class FilesListviewAdapter extends ArrayAdapter<String> {

    private final Context context;

    public FilesListviewAdapter(Context context, int id, ArrayList<String> files) {
        super(context, id, files);
        this.context = context;
    }

    /**
     * List item displays only the TextView with a name of a file.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_filename, null);
            holder = new ViewHolder();
            holder.filename = (TextView)convertView.findViewById(R.id.textview_filename);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        String filenameString = getItem(position);
        holder.filename.setText(filenameString + "");

        return convertView;
    }

    static class ViewHolder {
        private TextView filename;
    }
}
