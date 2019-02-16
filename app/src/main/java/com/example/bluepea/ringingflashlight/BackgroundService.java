package com.example.bluepea.ringingflashlight;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

public class BackgroundService extends Service {
    public static final int INCOMING_CALL = 1;
    public static final int INCOMING_SMS = 2;
    public static final String KEY_EVENT = "event";
    public static final String KEY_VALUE = "value";
    public static int FLASH_SPEED_CALL = MSeekBar.MIN_FLASH_SPEED;
    public static int FLASH_SPEED_SMS = MSeekBar.MIN_FLASH_SPEED;
    public static int NUMBER_STROBES_SMS = MSeekBar.MIN_STROBES;
    public static boolean FLAG_APP_LAUNCH =false;

    public static FlashLight.ThreadBlink blinkIncomingCall;
    public static FlashLight.ThreadBlink blinkIncomingSMS;

    private InComingCallReceiver inComingCallReceiver ;
    private InComingSMSReceiver inComingSMSReceiver ;

    public static void initBackgroundService(){
        blinkIncomingCall
                = new FlashLight.ThreadBlink(0,0);
        blinkIncomingSMS
                = new FlashLight.ThreadBlink(0,0);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("on start command","myservice");
        checkAction(intent);
        return START_STICKY;
    }

    @Nullable

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FlashLight.init(this);
        BackgroundService.initBackgroundService();
        inComingCallReceiver = new InComingCallReceiver();
        inComingSMSReceiver = new InComingSMSReceiver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForeground(1234,createNotification());
            ///foreground service run just 1 time, sys dont auto start anymore(dont rebuild class),
            /// dont need loadConfig
        }
        else if(!FLAG_APP_LAUNCH)
            loadConfig();///update cofig user to class service when system create a new service and rebuild class
        Log.d("___________"+FLAG_APP_LAUNCH,"myservice");


        Log.d("___________"+FLASH_SPEED_CALL,"myservice");
        Log.d("create service","myservice");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification createNotification(){
        NotificationCompat.Builder mBuilder;
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("chanel","Running on background", importance);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        mBuilder= new NotificationCompat.Builder(this,"chanel")
                .setSmallIcon(R.drawable.flashlight_icon)
                .setContentText("Running on background")
                .setShowWhen(false);
        return mBuilder.build();
    }

    @Override
    public void onDestroy() {
        Log.d("detroy service","myservice");
        super.onDestroy();
        //onCreate();
    }


    public void register(BroadcastReceiver receiver, String[] action){
        IntentFilter intentFilter = new IntentFilter();
        for (String item: action) {
            intentFilter.addAction(item);
        }
        registerReceiver(receiver, intentFilter);
    }

    private void checkAction(Intent intent){
           Log.d("zo checkaction","myservice");
        if(intent == null){
            Log.d("intent null","myservice");
            return;
        }

        SharedPreferences sharedPre = getSharedPreferences(MSharedPre.NAME_SHARED_PRE,MODE_PRIVATE);

        if(!sharedPre.getBoolean(MSharedPre.STATUS_NOTIF,false)
                && !sharedPre.getBoolean(MSharedPre.STATUS_INCOMING_CALL,false)
                && !sharedPre.getBoolean(MSharedPre.STATUS_INCOMING_SMS,false)){
            this.stopSelf();
            return;
        }

        Bundle bundle = intent.getExtras();
        int event = (int)bundle.getInt(KEY_EVENT);
        Log.d("event "+event,"myservice");
        boolean checked = (boolean)bundle.getBoolean(KEY_VALUE,false);

        switch (event){
            case INCOMING_CALL:
                Log.d("status "+checked,"myservice");
                if(checked)
                    register(inComingCallReceiver,new String[]{"android.intent.action.PHONE_STATE"});
                else
                    unregisterReceiver(inComingCallReceiver);
                break;
            case INCOMING_SMS:
                if(checked)
                    register(inComingSMSReceiver,new String[]{"android.provider.Telephony.SMS_RECEIVED"});
                else
                    unregisterReceiver(inComingSMSReceiver);
                break;
        }
    }

    ///update cofig user to class service when system create a new service and rebuild class
    public void loadConfig(){
        Log.d("loaduserconfig","myservice");
        SharedPreferences sharedPre = getSharedPreferences(MSharedPre.NAME_SHARED_PRE,MODE_PRIVATE);

        if(sharedPre.getBoolean(MSharedPre.STATUS_INCOMING_CALL,false))
            register(inComingCallReceiver,new String[]{"android.intent.action.PHONE_STATE"});

        if(sharedPre.getBoolean(MSharedPre.STATUS_INCOMING_SMS,false))
            register(inComingSMSReceiver,new String[]{"android.provider.Telephony.SMS_RECEIVED"});
        updateUserConfig(sharedPre,this);


    }
    ///update cofig user to class service when system create a new service and rebuild class
    public static void updateUserConfig(SharedPreferences sharedPre, Context context){
        UserConfig.FLASH_SPEED_CALL =  sharedPre.getInt(MSharedPre.FLASH_SPEED_INCOMINGCALL,MSeekBar.MIN_FLASH_SPEED);
        UserConfig.FLASH_SPEED_SMS =  sharedPre.getInt(MSharedPre.FLASH_SPEED_INCOMINGSMS,MSeekBar.MIN_FLASH_SPEED);
        UserConfig.NUMBER_STROBES_SMS =  sharedPre.getInt(MSharedPre.NUMBER_STROBES_INCOMINGSMS,MSeekBar.MIN_STROBES);
        Log.d("updateUserConfig","advancedsetting");
        UserConfig.updateUserAdvanceConfig(sharedPre,context);
    }

    public static int getLevelBattery(Context context){
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 100);
    }

    public static String getTextCurrentTime(){
        Calendar calCurrent = Calendar.getInstance();
        return calCurrent.get(Calendar.HOUR_OF_DAY) + TextViewTimer.CHARACTER_SPLIT
                + calCurrent.get(Calendar.MINUTE);
    }
    public static boolean advancedSettingCondition(String textCurrentTime, int levelBatteryCurrent){
        return ( (!UserConfig.IS_ENABLED_TIME||(UserConfig.IS_ENABLED_TIME
                &&(TextViewTimer.before(textCurrentTime,UserConfig.TIME_FROM)
                || TextViewTimer.after(textCurrentTime,UserConfig.TIME_TO))))
                &&(!UserConfig.IS_ENABLED_BATTERY ||(UserConfig.IS_ENABLED_BATTERY
                && levelBatteryCurrent > UserConfig.BATERRY_LEVEL)));
    }


}
