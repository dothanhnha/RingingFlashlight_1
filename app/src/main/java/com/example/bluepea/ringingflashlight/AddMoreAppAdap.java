package com.example.bluepea.ringingflashlight;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

public class AddMoreAppAdap extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<ItemAddMoreApp> listStorage;
    private int totalNumber;
    private int checkedNumber;
    private TextView txtViewResultChecked;

    public AddMoreAppAdap(Context context, ArrayList<ItemAddMoreApp> listStorage, int checkedNumber, TextView txtViewResultChecked) {
        this.layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listStorage = listStorage;
        this.totalNumber = listStorage.size();
        this.checkedNumber = checkedNumber;
        this.txtViewResultChecked = txtViewResultChecked;
        this.txtViewResultChecked.setText(checkedNumber+"/"+totalNumber);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        TempItem temp = new TempItem();

        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.item_add_more_app,parent,false);
            temp.icon = (ImageView)convertView.findViewById(R.id.iconApp);
            temp.nameApp = (TextView) convertView.findViewById(R.id.nameApp);
            temp.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            Log.d("position", String.valueOf(position));
            temp.borderBot = (View)convertView.findViewById(R.id.borderBot);
            convertView.setTag(temp);
        }
        else
            temp = (TempItem) convertView.getTag();
        temp.nameApp.setText(listStorage.get(position).getNameApp());
        temp.icon.setImageDrawable(listStorage.get(position).getIcon());
        temp.checkBox.setOnCheckedChangeListener(null);
        temp.checkBox.setChecked(listStorage.get(position).isSelected());
        final TempItem finalTemp = temp;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalTemp.checkBox.isChecked())
                    finalTemp.checkBox.setChecked(false);
                else
                    finalTemp.checkBox.setChecked(true);
            }
        });
        temp.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listStorage.get(position).setSelected(isChecked);
                if(isChecked){
                    checkedNumber++;
//                    listChecked.add(listStorage.get(position));
                }

                else {
                    checkedNumber--;
//                    listChecked.remove(position);
                }
                txtViewResultChecked.setText(checkedNumber+"/"+totalNumber);
            }
        });

        return convertView;
    }

    public ArrayList<ItemAddMoreApp> getListChecked() {
        ArrayList<ItemAddMoreApp> result = new ArrayList<>();
        for(ItemAddMoreApp item : listStorage){
            if(item.isSelected())
                result.add(item);
        }
        return result;
    }
    public HashSet<String> getListNamePkageChecked() {
        HashSet<String> result = new HashSet<String>();
        for(ItemAddMoreApp item : listStorage){
            if(item.isSelected())
                result.add(item.getNamePackage());
        }
        return result;
    }

    static class TempItem{
        public ImageView icon;
        public TextView nameApp;
        public CheckBox checkBox;
        public View borderBot;
    }

}
