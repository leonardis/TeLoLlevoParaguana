package com.slice_group.telollevoparaguana;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pancracio on 19/05/15.
 * e-Mail: leonardisrojas@gmail.com
 */
public class DrawerAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<DrawerModel> drawerModels;

    public DrawerAdapter(Context context, ArrayList<DrawerModel> drawerModels){
        this.context = context;
        this.drawerModels = drawerModels;
    }

    @Override
    public int getCount() {
        return drawerModels.size();
    }

    @Override
    public Object getItem(int position) {
        return drawerModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_drawer, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.drawer_icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.drawer_title);

        imgIcon.setImageResource(drawerModels.get(position).getIcon());
        txtTitle.setText(drawerModels.get(position).getTitle());

        return convertView;
    }
}
