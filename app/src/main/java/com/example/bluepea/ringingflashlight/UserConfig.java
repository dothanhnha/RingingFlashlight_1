package com.example.bluepea.ringingflashlight;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class UserConfig {
    public static boolean LOADED_ADVANCED_CONFIG = false;
    private static final String pattern = "HH:mm";
    public static SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

    public static int FLASH_SPEED_CALL = MSeekBar.MIN_FLASH_SPEED;
    public static int FLASH_SPEED_SMS = MSeekBar.MIN_FLASH_SPEED;
    public static int NUMBER_STROBES_SMS = MSeekBar.MIN_STROBES;
    public static int NUMBER_STROBES_NOTIF = MSeekBar.MIN_STROBES;
    public static int FLASH_SPEED_NOTIF = MSeekBar.MIN_FLASH_SPEED;

    public static boolean IS_ENABLED_NOTIF = false;

    public static int BATERRY_LEVEL = MSeekBar.MIN_PERCENT;
    public static String TIME_FROM = TextViewTimer.DEF_TIME;
    public static String TIME_TO = TextViewTimer.DEF_TIME;


//    public static int HOURS_OFDAY_TO = TextViewTimer.DEF_HOUR_OF_DAY;
//    public static int MINUTES_TO = TextViewTimer.DEF_MINUTE;
//    public static int HOURS_OFDAY_FROM = TextViewTimer.DEF_HOUR_OF_DAY;
//    public static int MINUTES_FROM = TextViewTimer.DEF_MINUTE;


    public static boolean IS_ENABLED_TIME = false;
    public static boolean IS_ENABLED_BATTERY = false;
    public static HashSet<String> ListNamePackageChecked;

    public static void updateUserAdvanceConfig(SharedPreferences sharedPre, Context context){
        if(LOADED_ADVANCED_CONFIG)
            return;
        FlashLight.init(context);
        LOADED_ADVANCED_CONFIG = true;
        Log.d("DOOupdateUserAdConfig","advancedsetting");
        UserConfig.BATERRY_LEVEL = sharedPre.getInt(MSharedPre.BATTERY_LEVEL,MSeekBar.MIN_PERCENT);
        UserConfig.IS_ENABLED_BATTERY = sharedPre.getBoolean(MSharedPre.BATTERY_MODE,false);
        UserConfig.TIME_FROM = sharedPre.getString(MSharedPre.TIME_FROM,TextViewTimer.DEF_TIME);
        UserConfig.TIME_TO = sharedPre.getString(MSharedPre.TIME_TO,TextViewTimer.DEF_TIME);
        UserConfig.IS_ENABLED_TIME = sharedPre.getBoolean(MSharedPre.TIME_MODE,false);

    }


}
