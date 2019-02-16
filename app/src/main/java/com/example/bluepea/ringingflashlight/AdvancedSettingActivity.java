package com.example.bluepea.ringingflashlight;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvancedSettingActivity extends AppCompatActivity {

    @BindView(R.id.imgArrowBack)
    ImageView imgArrowBack;
    @BindView(R.id.txtVFromTime)
    TextViewTimer txtVFromTime;
    @BindView(R.id.txtVToTime)
    TextViewTimer txtVToTime;
    @BindView(R.id.mSeekBar)
    MSeekBar mSeekBar;
    @BindView(R.id.txtViewPercent)
    TextView txtViewPercent;
    @BindView(R.id.swBatteryMode)
    Switch swBatteryMode;
    @BindView(R.id.swTimeMode)
    Switch swTimeMode;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_setting);
        ButterKnife.bind(this);
        loadUserConfig();
    }

    @OnClick({R.id.imgArrowBack, R.id.swBatteryMode, R.id.swTimeMode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgArrowBack:
                arrowBackClick();
                break;
            case R.id.swTimeMode:
                swTimeModeClick();
                break;
            case R.id.swBatteryMode:
                swBatteryModeClick();
                break;
        }
    }

    private void arrowBackClick() {
        onBackPressed();
    }

    private void swTimeModeClick() {
        UserConfig.IS_ENABLED_TIME = swTimeMode.isChecked();
        editor.putBoolean(MSharedPre.TIME_MODE,swTimeMode.isChecked());
        editor.apply();
    }

    private void swBatteryModeClick() {
        UserConfig.IS_ENABLED_BATTERY = swBatteryMode.isChecked();
        editor.putBoolean(MSharedPre.BATTERY_MODE,swBatteryMode.isChecked());
        editor.apply();
    }

    private void loadUserConfig(){
        SharedPreferences sharedPreferences = getSharedPreferences(MSharedPre.NAME_SHARED_PRE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mSeekBar.init(MSeekBar.MIN_PERCENT, MSeekBar.MAX_PERCENT, txtViewPercent,
                sharedPreferences, MSharedPre.BATTERY_LEVEL, MSeekBar.MIN_PERCENT);
        mSeekBar.setMethodUpdateConfig(new MSeekBar.UpdateConfig() {
            @Override
            public void updateConfig() {
                UserConfig.BATERRY_LEVEL = mSeekBar.getCurValue();
            }
        });
        txtVToTime.init(sharedPreferences, MSharedPre.TIME_TO);
        txtVToTime.setMethodUpdateConfig(new TextViewTimer.UpdateConfig() {
            @Override
            public void updateConfig() {
                    UserConfig.TIME_TO = txtVToTime.getTextTime24h();
            }
        });

        txtVFromTime.init(sharedPreferences, MSharedPre.TIME_FROM);
        txtVFromTime.setMethodUpdateConfig(new TextViewTimer.UpdateConfig() {
            @Override
            public void updateConfig() {
                    UserConfig.TIME_FROM = txtVFromTime.getTextTime24h();
            }
        });

        swBatteryMode.setChecked(sharedPreferences.getBoolean(MSharedPre.BATTERY_MODE,false));
        swTimeMode.setChecked(sharedPreferences.getBoolean(MSharedPre.TIME_MODE,false));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
