package com.example.bluepea.ringingflashlight;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;


public class NotifListenerService extends NotificationListenerService{
    public static final String KEY_VALUE = "value";
    private static Set<String> ListNamePackage ;
//    public static int FLASH_SPEED;
//    public static int NUMBER_STROBES;
//    private static boolean ENABLE =false ;
    private static Thread blink;
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        if(!UserConfig.IS_ENABLED_NOTIF)
            return;
        String textCurrentTime = BackgroundService.getTextCurrentTime();
        int levelBatteryCurrent = BackgroundService.getLevelBattery(this);

        if(!BackgroundService.advancedSettingCondition(textCurrentTime, levelBatteryCurrent))
            return;

        if(UserConfig.ListNamePackageChecked !=null)
            for(String namePackage : UserConfig.ListNamePackageChecked){
                if(namePackage.equals(sbn.getPackageName())){
                    if(blink!=null)
                            blink.interrupt();
                    blink =new FlashLight.ThreadBlink(UserConfig.FLASH_SPEED_NOTIF,
                            UserConfig.NUMBER_STROBES_NOTIF);
                    blink.start();
                }
                break;
            }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        // Implement what you want here
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FlashLight.init(this);
        Log.d("myNotif","create");
        Log.d("myNotif",String.valueOf(!BackgroundService.FLAG_APP_LAUNCH));
        if(!BackgroundService.FLAG_APP_LAUNCH)
            loadUserConfig();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        loadUserConfig();
        Log.d("myNotif","startcommand");
//        if(!FLAG){
//            stopSelf();
//            return START_NOT_STICKY;
//        }
        return START_STICKY;
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d("myNotif","connected");
    }

    @Override
    public void onDestroy() {
        //startService(new Intent(NotifListenerService.this,NotifListenerService.class));
        Log.d("myNotif","destroy");
        super.onDestroy();

    }

    private void loadUserConfig() {
        SharedPreferences sharedPre = getSharedPreferences(MSharedPre.NAME_SHARED_PRE,MODE_PRIVATE);
        UserConfig.IS_ENABLED_NOTIF = sharedPre.getBoolean(MSharedPre.STATUS_NOTIF,false);
        if(!UserConfig.IS_ENABLED_NOTIF)
            return;
        UserConfig.ListNamePackageChecked = (HashSet<String>) sharedPre.getStringSet(MSharedPre.LIST_APP,null);
        UserConfig.NUMBER_STROBES_NOTIF = sharedPre.getInt(MSharedPre.NUMBER_STROBES_NOTIF,MSeekBar.MIN_STROBES);
        UserConfig.FLASH_SPEED_NOTIF = sharedPre.getInt(MSharedPre.FLASH_SPEED_NOTIF,MSeekBar.MIN_FLASH_SPEED);
        UserConfig.updateUserAdvanceConfig(sharedPre,this);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d("myNotif","task remove");
    }
}
