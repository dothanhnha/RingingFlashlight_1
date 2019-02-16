package com.example.bluepea.ringingflashlight;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.print.PrinterId;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

public class MSeekBar extends android.support.v7.widget.AppCompatSeekBar implements SeekBar.OnSeekBarChangeListener {
    public static int MAX_FLASH_SPEED = 1000;
    public static int MIN_FLASH_SPEED = 50;
    public static int MAX_STROBES = 100;
    public static int MIN_STROBES = 10;
    public static int MAX_PERCENT = 100;
    public static int MIN_PERCENT = 1;
    private TextView txtViewShow;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String key;
    private UpdateConfig interf;
    private int maxValue;
    private int minValue;
    private int distance;



    private int curValue;
    public MSeekBar(Context context) {
        super(context);
    }

    public MSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);


    }

    public MSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }



    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.setOnSeekBarChangeListener(this);
        this.distance = this.maxValue - this.minValue;
    }

    public void init(int minValue, int maxValue, TextView txtViewShow, SharedPreferences sharedPre, String key, int def){
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.distance = maxValue - minValue;
        this.txtViewShow = txtViewShow;
        this.setCurValue(sharedPre.getInt(key,def));
        this.sharedPreferences = sharedPre;
        this.editor = sharedPreferences.edit();
        this.key = key;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }


    public TextView getTxtViewShow() {
        return txtViewShow;
    }

    public void setTxtViewShow(TextView txtViewShow) {
        this.txtViewShow = txtViewShow;
    }

    public int getCurValue() {
        return curValue;
    }

    public void setCurValue(int curValue) {
        this.curValue = curValue;
        this.setProgress((int)((curValue-minValue)*getMax()/distance));
        this.txtViewShow.setText(String.valueOf(curValue));
    }

    public void saveUserConfig(){
        editor.putInt(key, curValue);
        editor.apply();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.curValue = minValue + getProgress()*distance/getMax();
        this.txtViewShow.setText(String.valueOf(curValue));
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        saveUserConfig();
        this.interf.updateConfig();
    }

    public void setMethodUpdateConfig(UpdateConfig interf){
        this.interf = interf;
    }

    public static interface UpdateConfig{
        public void updateConfig();
    }
}
