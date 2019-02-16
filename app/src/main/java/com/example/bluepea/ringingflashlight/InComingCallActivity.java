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

public class InComingCallActivity extends AppCompatActivity {
    private static FlashLight.ThreadBlink blink;
    @BindView(R.id.imgArrowBack)
    ImageView imgArrowBack;
    @BindView(R.id.imgBtnTestOn)
    ImageView imgBtnTestOn;
    @BindView(R.id.txtViewFlaSpeed)
    TextView txtViewFlaSpeed;
    @BindView(R.id.mSeekBar)
    MSeekBar mSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_coming_call);
        ButterKnife.bind(this);
        blink = new FlashLight.ThreadBlink(mSeekBar.getCurValue(),5);
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
        blink = new FlashLight.ThreadBlink(mSeekBar.getCurValue(),5);
        blink.start();
    }

    private void loadUserConfig(){
        SharedPreferences sharedPreferences = getSharedPreferences(MSharedPre.NAME_SHARED_PRE, Context.MODE_PRIVATE);

        mSeekBar.init(MSeekBar.MIN_FLASH_SPEED,MSeekBar.MAX_FLASH_SPEED,txtViewFlaSpeed,
                sharedPreferences,MSharedPre.FLASH_SPEED_INCOMINGCALL,MSeekBar.MIN_FLASH_SPEED);
        mSeekBar.setMethodUpdateConfig(new MSeekBar.UpdateConfig() {
            @Override
            public void updateConfig() {
                UserConfig.FLASH_SPEED_CALL = mSeekBar.getCurValue();
            }
        });
    }
//    private void saveUserConfig(){
//        mSeekBar.saveUserConfig();
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
