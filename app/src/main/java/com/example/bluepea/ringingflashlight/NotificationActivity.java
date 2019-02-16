package com.example.bluepea.ringingflashlight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationActivity extends AppCompatActivity {
    public static HashSet<String> listNamePackageChecked = new HashSet<>();
    private static ArrayList<ItemAddMoreApp> listAppChecked = new ArrayList<>();
    private static FlashLight.ThreadBlink blink;
    private static boolean FIRST_LOADED_CONFIG = false;
    @BindView(R.id.imgArrowBack)
    ImageView imgArrowBack;
    @BindView(R.id.imgBtnTestOn)
    ImageView imgBtnTestOn;
    @BindView(R.id.imgBtnAddApp)
    ImageView imgBtnAddApp;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.txtViewTimesNotif)
    TextView txtViewTimes;
    @BindView(R.id.txtViewmsNotif)
    TextView txtViewms;
    @BindView(R.id.mSeekBTimesNotif)
    MSeekBar mSeekBTimes;
    @BindView(R.id.mSeekBmsNotif)
    MSeekBar mSeekBms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("oncreate","ActivityTestNotif");
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        initBarCollapse();
        blink = new FlashLight.ThreadBlink(mSeekBms.getCurValue(),5);
        loadUserConfig();
    }
    private void initBarCollapse(){
//        final AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
//        final AnimBarNotification noAnim = findViewById(R.id.custom);
//        noAnim.title = findViewById(R.id.titleahihi);
//        noAnim.setScrollView((ScrollView) findViewById(R.id.cuon));
//        CoordinatorLayout coordinatorLayout = findViewById(R.id.CoordinatorLayout);
//        coordinatorLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (noAnim.isUnderThreshold) {
//                        appBarLayout.setExpanded(false);
//                    } else {
//                        appBarLayout.setExpanded(true);
//                    }
//                }
//                return false;
//            }
//        });
    }



    @OnClick({R.id.imgArrowBack, R.id.imgBtnTestOn, R.id.imgBtnAddApp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgArrowBack:
                arrowBackClick();
                break;
            case R.id.imgBtnTestOn:
                imgBtnTestOnClick();
                break;
            case R.id.imgBtnAddApp:
                imgBtnAddAppClick();
                break;
        }
    }

    private void arrowBackClick(){
        onBackPressed();
    }
    private void imgBtnTestOnClick() {
        blink.interrupt();
        blink = new FlashLight.ThreadBlink(mSeekBms.getCurValue(),5);
        blink.start();

    }
    private void imgBtnAddAppClick(){
        startActivity(new Intent(NotificationActivity.this,AddMoreAppActivity.class));
    }

    private void initListCheckApp(ArrayList<ItemAddMoreApp> listAppChecked){
        linearLayout.removeAllViews();
        LayoutInflater layoutInflater =(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(int index =0 ;index<listAppChecked.size();index++){

            ItemAddMoreApp item = listAppChecked.get(index);
            View view = layoutInflater.inflate(R.layout.item_notification,null);
            ImageView icon = (ImageView)view.findViewById(R.id.iconApp);
            TextView nameApp = (TextView) view.findViewById(R.id.nameApp);
            icon.setImageDrawable(item.getIcon());
            nameApp.setText(item.getNameApp());
            linearLayout.addView(view);
        }
    }

    private void loadUserConfig(){
        SharedPreferences sharedPreferences = getSharedPreferences(MSharedPre.NAME_SHARED_PRE, Context.MODE_PRIVATE);
        mSeekBTimes.init(MSeekBar.MIN_STROBES,MSeekBar.MAX_STROBES,txtViewTimes,
                sharedPreferences,MSharedPre.NUMBER_STROBES_NOTIF,MSeekBar.MIN_STROBES);
        mSeekBTimes.setMethodUpdateConfig(new MSeekBar.UpdateConfig() {
            @Override
            public void updateConfig() {
                UserConfig.NUMBER_STROBES_NOTIF = mSeekBTimes.getCurValue();
            }
        });

        mSeekBms.init(MSeekBar.MIN_FLASH_SPEED,MSeekBar.MAX_FLASH_SPEED,txtViewms,
                sharedPreferences,MSharedPre.FLASH_SPEED_NOTIF,MSeekBar.MIN_FLASH_SPEED);
        mSeekBms.setMethodUpdateConfig(new MSeekBar.UpdateConfig() {
            @Override
            public void updateConfig() {
                UserConfig.FLASH_SPEED_NOTIF = mSeekBms.getCurValue();
            }
        });
        if(!FIRST_LOADED_CONFIG){
            FIRST_LOADED_CONFIG = true;
            UserConfig.ListNamePackageChecked =
                    (HashSet<String>) sharedPreferences.getStringSet(MSharedPre.LIST_APP, new HashSet<String>());
        }


        listAppChecked = getListApp(UserConfig.ListNamePackageChecked);
        initListCheckApp(listAppChecked);

    }
//    private void saveUserConfig(){
//        mSeekBTimes.saveUserConfig();
//        mSeekBms.saveUserConfig();
//        Intent intent = new Intent(NotificationActivity.this, NotifListenerService.class);
//        startService(intent);
//    }


    private ArrayList<ItemAddMoreApp> getListApp(HashSet<String> listNamePackage){

        ArrayList<ItemAddMoreApp> listApp = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Iterator<String> iterator = listNamePackage.iterator();
        while (iterator.hasNext()){
            String item = iterator.next();
            String appName = null;
            try {
                appName = (String)packageManager.getApplicationLabel(packageManager.getApplicationInfo(item, PackageManager.INSTALL_REASON_UNKNOWN));
                Drawable icon = packageManager.getApplicationIcon(item);
                listApp.add(new ItemAddMoreApp(appName, item, icon));
            } catch (PackageManager.NameNotFoundException e) {
                continue;
            }
        }
        return listApp;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onresume","ActivityTestNotif");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("onrestart","ActivityTestNotif");
        if(AddMoreAppActivity.listAppChecked != null){
            initListCheckApp(AddMoreAppActivity.listAppChecked);
//            SharedPreferences sharedPreferences = getSharedPreferences(MSharedPre.NAME_SHARED_PRE, Context.MODE_PRIVATE);
//            listNamePackageChecked = (HashSet<String>) sharedPreferences.getStringSet(MSharedPre.LIST_APP,new HashSet<String>());
        }
        else
            initListCheckApp(listAppChecked);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //saveUserConfig();
        Log.d("onstop","ActivityTestNotif");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("onbackpress","ActivityTestNotif");
    }
}
