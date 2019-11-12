package fr.istic.mob.graphcd.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import fr.istic.mob.graphcd.R;

/**
 * File Adapter
 * @version 1.0.1
 * @author Charly C, Pierre D
 */
public class FileAdapter extends BaseAdapter {

    private ArrayList<String> filesList;
    private Context context;
    private LayoutInflater inflater;

    /**
     * FileAdapater constructor
     * @param context Context
     * @param filesList ArrayList<String>
     */
    public FileAdapter(Context context, ArrayList<String> filesList){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.filesList = filesList;
    }

    /**
     * Get amount of files in the list
     * @return size int
     */
    @Override
    public int getCount() {
        return this.filesList.size();
    }

    /**
     * Get item by index
     * @param index int
     * @return Object o
     */
    @Override
    public Object getItem(int index) {
        return filesList.get(index);
    }

    /**
     * @param index, int
     * @return current item id
     */
    @Override
    public long getItemId(int index) {
        return index;
    }

    private class ViewHolder {
        ImageView imageFile;
        TextView textFile;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder() ;
            convertView = inflater.inflate(R.layout.file_list, parent, false);
            holder.textFile = convertView.findViewById(R.id.textFile);
            holder.imageFile = convertView.findViewById(R.id.imageFile);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.imageFile.setId(index);
        holder.imageFile.setClickable(true);
        holder.textFile.setText(filesList.get(index));

        return convertView;
    }
}
