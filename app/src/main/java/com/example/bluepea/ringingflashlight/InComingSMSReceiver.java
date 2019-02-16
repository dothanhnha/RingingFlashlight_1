package com.example.bluepea.ringingflashlight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;

public class InComingSMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String textCurrentTime = BackgroundService.getTextCurrentTime();
        int levelBatteryCurrent = BackgroundService.getLevelBattery(context);

        if(BackgroundService.advancedSettingCondition(textCurrentTime, levelBatteryCurrent)){
            BackgroundService.blinkIncomingSMS.interrupt();
            BackgroundService.blinkIncomingSMS
                    = new FlashLight.ThreadBlink(UserConfig.FLASH_SPEED_SMS,
                    UserConfig.NUMBER_STROBES_SMS);

            BackgroundService.blinkIncomingSMS.start();
        }

    }
}
