package com.example.bluepea.ringingflashlight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMoreAppActivity extends AppCompatActivity {
    public static ArrayList<ItemAddMoreApp> listAppChecked;
    private static ArrayList<ItemAddMoreApp> listApp;
    private static AddMoreAppAdap adapter;
    private static SharedPreferences.Editor editor;
    @BindView(R.id.imgArrowBack)
    ImageView imgArrowBack;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.txtViewResultChecked)
    TextView txtViewResultChecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("oncreate","ActivityTestAddMore");
        setContentView(R.layout.activity_add_more_app);
        ButterKnife.bind(this);
        listApp = getListApp();

        adapter = new AddMoreAppAdap(this,listApp,listAppChecked.size(),txtViewResultChecked);
        listView.setAdapter(adapter);

        SharedPreferences sharedPreferences = getSharedPreferences(MSharedPre.NAME_SHARED_PRE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        
    }

    @OnClick(R.id.imgArrowBack)
    public void onViewClicked() {
        onBackPressed();
    }

    private ArrayList<ItemAddMoreApp> getListApp() {
        ArrayList<ItemAddMoreApp> result = new ArrayList<>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(PackageManager.INSTALL_REASON_UNKNOWN);
        String appName;
        Drawable icon;
        boolean isChecked;
        ItemAddMoreApp item;
        listAppChecked = new ArrayList<>();
        for (int index = 0; index < packs.size(); index++) {
            PackageInfo p = packs.get(index);
            if (!(isSystemApp(p.applicationInfo))) {
                appName = p.applicationInfo.loadLabel(getPackageManager()).toString();

                icon = p.applicationInfo.loadIcon(getPackageManager());
                isChecked = UserConfig.ListNamePackageChecked.contains(p.packageName);
                item = new ItemAddMoreApp(appName, p.packageName, icon,isChecked);
                if(isChecked)
                    listAppChecked.add(item);
                result.add(item);
            }
        }
        return result;
    }
    private boolean isSystemApp(ApplicationInfo info){
        if((info.flags & ApplicationInfo.FLAG_SYSTEM)!=0)
            return true;
        return false;
    }
    private ArrayList<ItemAddMoreApp> getListAppChecked(ArrayList<ItemAddMoreApp> listApp){
        ArrayList<ItemAddMoreApp> result = new ArrayList<>();
        for(int index=0; index < listApp.size(); index++){
            if(listApp.get(index).isSelected())
                result.add(listApp.get(index));
        }
        return result;
    }

    private void saveUserConfig(ArrayList<ItemAddMoreApp> listAppChecked){
        Set<String> listNamePackage = new HashSet<>();
        for(ItemAddMoreApp item : listAppChecked){
            listNamePackage.add(item.getNamePackage());
        }
        editor.putStringSet(MSharedPre.LIST_APP, listNamePackage);
        editor.apply();
        //Intent intent = new Intent(AddMoreAppActivity.this, NotifListenerService.class);
        //startService(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserConfig.ListNamePackageChecked = adapter.getListNamePkageChecked();
        listAppChecked = adapter.getListChecked();
        Log.d("onbackpress","ActivityTestAddMore");
    }

    @Override
    protected void onStop() {
        super.onStop();

            listAppChecked = adapter.getListChecked();
            saveUserConfig(listAppChecked);

    }
}
