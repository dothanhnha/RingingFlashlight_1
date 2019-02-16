package com.example.bluepea.ringingflashlight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.camera2.CameraManager;
import android.os.BatteryManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class InComingCallReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("do action", "myservice");
        String pattern = "HH:mm";
        String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
        if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            Log.d("ringing", "myservice");

            String textCurrentTime = BackgroundService.getTextCurrentTime();
            int levelBatteryCurrent = BackgroundService.getLevelBattery(context);

            if(BackgroundService.advancedSettingCondition(textCurrentTime, levelBatteryCurrent)){
                BackgroundService.blinkIncomingCall.interrupt();
                BackgroundService.blinkIncomingCall
                        = new FlashLight.ThreadBlink(UserConfig.FLASH_SPEED_CALL,0);
                BackgroundService.blinkIncomingCall.start();
            }

        }
        else{
            BackgroundService.blinkIncomingCall.interrupt();

        }


    }


}

