package com.example.bluepea.ringingflashlight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NotificationAdap extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<ItemAddMoreApp> listStorage;

    public NotificationAdap(Context context, ArrayList<ItemAddMoreApp> listStorage) {
        this.layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listStorage = listStorage;
    }


    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AddMoreAppAdap.TempItem temp = new AddMoreAppAdap.TempItem();
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.item_notification,parent,false);
            temp.icon = (ImageView)convertView.findViewById(R.id.iconApp);
            temp.nameApp = (TextView) convertView.findViewById(R.id.nameApp);
            temp.borderBot = (View)convertView.findViewById(R.id.borderBot);
            convertView.setTag(temp);
        }
        else
            temp = (AddMoreAppAdap.TempItem) convertView.getTag();
        temp.nameApp.setText(listStorage.get(position).getNameApp());
        temp.icon.setImageDrawable(listStorage.get(position).getIcon());

        return convertView;
    }

    static class TempItem{
        public ImageView icon;
        public TextView nameApp;
        public View borderBot;
    }

}
