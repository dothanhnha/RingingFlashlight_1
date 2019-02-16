package com.example.bluepea.ringingflashlight;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

public class TextViewTimer extends android.support.v7.widget.AppCompatTextView implements View.OnClickListener {
    public final static int DEF_HOUR_OF_DAY = 0;
    public final static int DEF_MINUTE = 0;
    public final static String CHARACTER_SPLIT = ":";
    public final static String DEF_TIME = DEF_HOUR_OF_DAY + CHARACTER_SPLIT + DEF_MINUTE;
    private int hourOfDay = DEF_HOUR_OF_DAY;
    private int minute = DEF_MINUTE;
    private UpdateConfig interf;
    private String key;
    private SharedPreferences sharedPreferences;

    public void init(SharedPreferences sharedPre, String key){
        this.sharedPreferences = sharedPre;
        this.key =key;
        String time = sharedPre.getString(key,DEF_TIME);
        String[] split = time.split(CHARACTER_SPLIT);
        setTime(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    public String getTextTime24h(){
        return hourOfDay+CHARACTER_SPLIT+minute;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

    public void setTime(int hourOfDay, int minute){
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.setText(getTime12H(hourOfDay,minute));
    }

    private String getTime12H(int hourOfDay, int minute){
        String detail = "AM";
        if(hourOfDay >= 12){
            if(hourOfDay > 12)
                hourOfDay -= 12;
            detail = "PM";
        }
        return String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) +" "+ detail;
    }

    public void saveUserConfig(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, getTextTime24h());
        editor.apply();
    }

    public TextViewTimer(Context context) {
        super(context);
    }

    public TextViewTimer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewTimer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onClick(View v) {
        TimePickerDialog timeDialog = new TimePickerDialog(this.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.d(""+hourOfDay,"myservice");
                setTime(hourOfDay, minute);
                saveUserConfig();
                interf.updateConfig();
            }
        },hourOfDay,minute,false);

        timeDialog.show();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.setOnClickListener(this);
    }

    public void setMethodUpdateConfig(TextViewTimer.UpdateConfig interf){
        this.interf = interf;
    }

    public static interface UpdateConfig{
        public void updateConfig();
    }

    public static boolean after(String x, String y){
        String[] a = x.split(TextViewTimer.CHARACTER_SPLIT);
        String[] b = y.split(TextViewTimer.CHARACTER_SPLIT);
        if(Integer.parseInt(a[0])>Integer.parseInt(b[0]))
            return true;
        else if(Integer.parseInt(a[0]) == Integer.parseInt(b[0])
                && Integer.parseInt(a[1])>=Integer.parseInt(b[1]))
            return true;
        return false;
    }

    public static boolean before(String x, String y){
        String[] a = x.split(TextViewTimer.CHARACTER_SPLIT);
        String[] b = y.split(TextViewTimer.CHARACTER_SPLIT);
        if(Integer.parseInt(a[0])<Integer.parseInt(b[0]))
            return true;
        else if(Integer.parseInt(a[0]) == Integer.parseInt(b[0])
                && Integer.parseInt(a[1])<=Integer.parseInt(b[1]))
            return true;
        return false;
    }


//    public static int getHoursOfDay(String textTime){
//        String[] split = textTime.split(CHARACTER_SPLIT);
//        return Integer.parseInt(split[0]);
//    }
//
//    public static int getMuniutes(String textTime){
//        String[] split = textTime.split(CHARACTER_SPLIT);
//        return Integer.parseInt(split[1]);
//    }
}
