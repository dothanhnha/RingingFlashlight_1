package com.example.bluepea.ringingflashlight;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InComingSMSActivity extends AppCompatActivity {
    private static FlashLight.ThreadBlink blink;
    @BindView(R.id.imgArrowBack)
    ImageView imgArrowBack;
    @BindView(R.id.imgBtnTestOn)
    ImageView imgBtnTestOn;
    @BindView(R.id.txtViewTimesSMS)
    TextView txtViewTimes;
    @BindView(R.id.txtViewmsSMS)
    TextView txtViewms;
    @BindView(R.id.mSeekBTimesSMS)
    MSeekBar mSeekBTimes;
    @BindView(R.id.mSeekBmsSMS)
    MSeekBar mSeekBms;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_coming_sms);
        ButterKnife.bind(this);
        blink = new FlashLight.ThreadBlink(mSeekBms.getCurValue(),5);
        loadUserConfig();
    }

    @OnClick({R.id.imgArrowBack, R.id.imgBtnTestOn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgArrowBack:
                arrowBackClick();
                break;
            case R.id.imgBtnTestOn:
                imgBtnTestOnClick();
                break;
        }
    }
    private void arrowBackClick(){
        onBackPressed();
    }
    private void imgBtnTestOnClick(){
        blink.interrupt();
        blink = new FlashLight.ThreadBlink(mSeekBms.getCurValue(),5);
        blink.start();
    }

    private void loadUserConfig(){
        SharedPreferences sharedPreferences = getSharedPreferences(MSharedPre.NAME_SHARED_PRE, Context.MODE_PRIVATE);
        mSeekBTimes.init(MSeekBar.MIN_STROBES,MSeekBar.MAX_STROBES,txtViewTimes,
                sharedPreferences,MSharedPre.NUMBER_STROBES_INCOMINGSMS,MSeekBar.MIN_STROBES);
        mSeekBTimes.setMethodUpdateConfig(new MSeekBar.UpdateConfig() {
            @Override
            public void updateConfig() {
                UserConfig.NUMBER_STROBES_SMS = mSeekBTimes.getCurValue();
            }
        });

        mSeekBms.init(MSeekBar.MIN_FLASH_SPEED,MSeekBar.MAX_FLASH_SPEED,txtViewms,
                sharedPreferences,MSharedPre.FLASH_SPEED_INCOMINGSMS,MSeekBar.MIN_FLASH_SPEED);
        mSeekBms.setMethodUpdateConfig(new MSeekBar.UpdateConfig() {
            @Override
            public void updateConfig() {
                UserConfig.FLASH_SPEED_SMS = mSeekBms.getCurValue();
            }
        });
    }
//    private void saveUserConfig(){
//        mSeekBTimes.saveUserConfig();
//        mSeekBms.saveUserConfig();
//    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        //saveUserConfig();
        super.onBackPressed();
    }
}
