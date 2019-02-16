package com.example.bluepea.ringingflashlight;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.swIncomingCall)
    Switch swIncomingCall;
    @BindView(R.id.swNotif)
    Switch swNotif;
    @BindView(R.id.swIncomingSMS)
    Switch swIncomingSMS;
    @BindView(R.id.imgPowerBtn)
    ImageView imgPowerBtn;
    @BindView(R.id.imgIncmingCall)
    ImageView imgIncmingCall;
    @BindView(R.id.imgIncmingSms)
    ImageView imgIncmingSms;
    @BindView(R.id.imgNotifi)
    ImageView imgNotifi;
    @BindView(R.id.imgAdvanSettg)
    ImageView imgAdvanSettg;
    private FlashLight.ThreadBlink threadBlink;
    static public boolean isPowerOn = false;
    private SharedPreferences.Editor editor;
    private static Dialog dialogNotifAccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        if(!checkIsHaveCamera())
            getDialogDontHaveCam().show();
        Log.d("----------"+BackgroundService.FLASH_SPEED_CALL,"myservice");
        SharedPreferences sharedPreferences = getSharedPreferences(MSharedPre.NAME_SHARED_PRE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("test","ahihi");
        editor.apply();
        loadUserConfig(sharedPreferences);
        FlashLight.init(this);
        initDialogNotifAccess();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                FlashLight.mCameraManager.
//            }
//        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("mytest","HomeActivity pause     ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mytest","HomeActivity resume     ");

        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();
        if(enabledNotificationListeners == null){
            swNotif.setChecked(false);
            return;
        }
        if ((!enabledNotificationListeners.contains(packageName)) && swNotif.isChecked() && !dialogNotifAccess.isShowing())
        {
            swNotif.setChecked(false);
        }
        /// why need this update when resume
        //updateUserConfig();

    }



    @OnClick({R.id.imgPowerBtn, R.id.imgIncmingCall, R.id.imgIncmingSms, R.id.imgNotifi, R.id.imgAdvanSettg,R.id.swIncomingCall,
    R.id.swIncomingSMS, R.id.swNotif})
    public void onViewClicked(View view)  {
        switch (view.getId()) {
            case R.id.imgPowerBtn:
                powerClick();
                break;
            case R.id.imgIncmingCall:
                incomingCallClick();
                break;
            case R.id.imgIncmingSms:
                incomingSMSClick();
                break;
            case R.id.imgNotifi:
                notifiClick();
                break;
            case R.id.imgAdvanSettg:
                advanSettingClick();
                break;
            case R.id.swIncomingCall:
                swIncomingCallClick();
                break;
            case R.id.swIncomingSMS:
                swIncomingSMSClick();
                break;
            case R.id.swNotif:
                swNotifClick();
                break;
        }
    }

    private void powerClick() {
        if (isPowerOn) {
            FlashLight.offTorch();
            isPowerOn = false;
            imgPowerBtn.setImageResource(R.drawable.power_off_button);

        } else {
            FlashLight.onTorch();
            isPowerOn = true;
            imgPowerBtn.setImageResource(R.drawable.power_on_button);
        }
    }


    private void incomingCallClick() {
        startActivity(new Intent(HomeActivity.this, InComingCallActivity.class));
    }

    private void incomingSMSClick() {
        startActivity(new Intent(HomeActivity.this, InComingSMSActivity.class));
    }

    private void notifiClick() {
        startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
    }

    private void advanSettingClick() {
        startActivity(new Intent(HomeActivity.this, AdvancedSettingActivity.class));
    }

    private void swIncomingCallClick(){
        Log.d("swclick incomingcall","myservice");
        runtimePermission(Manifest.permission.READ_PHONE_STATE,swIncomingCall,BackgroundService.INCOMING_CALL);
        editor.putBoolean(MSharedPre.STATUS_INCOMING_CALL,swIncomingCall.isChecked());
        editor.apply();
    }

    private void swIncomingSMSClick(){
        runtimePermission(Manifest.permission.RECEIVE_SMS,swIncomingSMS,BackgroundService.INCOMING_SMS);
        editor.putBoolean(MSharedPre.STATUS_INCOMING_SMS,swIncomingSMS.isChecked());
        editor.apply();
    }


    private void swNotifClick(){
//        boolean isChecked = swNotif.isChecked();
//        Intent intent = new Intent(HomeActivity.this, NotifListenerService.class);
//        intent.putExtra(BackgroundService.KEY_VALUE,isChecked);
//        startService(intent);
//        if (!NotifListenerService.FLAG_IS_CONNECTED && isChecked)
//        {
//            startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), 0);
//        }
        ////////////
        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();

// check to see if the enabledNotificationListeners String contains our package name
        Intent intent = new Intent(HomeActivity.this, NotifListenerService.class);
        intent.putExtra(BackgroundService.KEY_VALUE,swNotif.isChecked());
        startService(intent);
        if(enabledNotificationListeners == null)
            dialogNotifAccess.show();
        else if ((!enabledNotificationListeners.contains(packageName)) && swNotif.isChecked())
        {
            dialogNotifAccess.show();
        }
        editor.putBoolean(MSharedPre.STATUS_NOTIF,swNotif.isChecked());
        editor.apply();

    }

    private void loadUserConfig(SharedPreferences sharedPre){
        swIncomingCall.setChecked(sharedPre.getBoolean(MSharedPre.STATUS_INCOMING_CALL,false));
        swIncomingSMS.setChecked(sharedPre.getBoolean(MSharedPre.STATUS_INCOMING_SMS,false));
        swNotif.setChecked(sharedPre.getBoolean(MSharedPre.STATUS_NOTIF,false));
    }


    private void runtimePermission(final String permission, final Switch sw, final int event){
        Dexter.withActivity(this)
                .withPermission(permission)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Log.d("onPermissionGranted","000000000000");
                        final Intent intent =new Intent(HomeActivity.this, BackgroundService.class);
                        intent.putExtra(BackgroundService.KEY_EVENT,event);
                        Log.d("event put extra "+event,"myservice");
                        intent.putExtra(BackgroundService.KEY_VALUE,sw.isChecked());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(intent);
                        } else {
                            startService(intent);
                        }
                        //startService(intent);
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        sw.setChecked(false);
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("mytest","HomeActivity start     ");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("mytest","HomeActivity detroy     ");

        Log.d("main detroy", "myservice");
    }



    @Override
    protected void onStop() {
        super.onStop();
        Log.d("mytest","HomeActivity stop     ");

    }



    private void initDialogNotifAccess(){
        dialogNotifAccess = new Dialog(this,R.style.Theme_AppCompat_Dialog);
        dialogNotifAccess.setCanceledOnTouchOutside(false);
        dialogNotifAccess.setContentView(R.layout.dialog_open_notifacess);

        Button cancel = dialogNotifAccess.findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNotifAccess.cancel();
                swNotif.setChecked(false);
            }
        });

        Button settings = dialogNotifAccess.findViewById(R.id.btnSettings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), 0);
                dialogNotifAccess.cancel();
                //swNotif.setChecked(true);
            }
        });
    }

    private Dialog getDialogDontHaveCam(){
        final Dialog dialog = new Dialog(this,R.style.DialogDontHaveCam);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("SORRY :(");
        dialog.setContentView(R.layout.dialog_dont_have_cam);
        return dialog;
    }
    private boolean checkIsHaveCamera(){
        PackageManager pm = this.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    //    private void updateUserConfig(){
//        Intent intent =new Intent(HomeActivity.this, BackgroundService.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if(swIncomingCall.isChecked()){
//                intent.putExtra(BackgroundService.KEY_EVENT,BackgroundService.INCOMING_CALL);
//                intent.putExtra(BackgroundService.KEY_VALUE,true);
//                startForegroundService(intent);
//
//            }
//            if(swIncomingSMS.isChecked()){
//                intent.putExtra(BackgroundService.KEY_EVENT,BackgroundService.INCOMING_SMS);
//                intent.putExtra(BackgroundService.KEY_VALUE,true);
//                startForegroundService(intent);
//            }
//            if(swNotif.isChecked()){
//                intent = new Intent(HomeActivity.this, NotifListenerService.class);
//                startService(intent);
//            }
//        } else {
//            if(swIncomingCall.isChecked()){
//                intent.putExtra(BackgroundService.KEY_EVENT,BackgroundService.INCOMING_CALL);
//                intent.putExtra(BackgroundService.KEY_VALUE,true);
//                startService(intent);
//            }
//            if(swIncomingSMS.isChecked()){
//                intent.putExtra(BackgroundService.KEY_EVENT,BackgroundService.INCOMING_SMS);
//                intent.putExtra(BackgroundService.KEY_VALUE,true);
//                startService(intent);
//            }
//            if(swNotif.isChecked()){
//                intent = new Intent(HomeActivity.this, NotifListenerService.class);
//                intent.putExtra(BackgroundService.KEY_VALUE,true);
//                startService(intent);
//            }
//        }
//
//
//    }

}
