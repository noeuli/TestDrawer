package com.example.testdrawer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class TestDrawer extends Application {
    private static final String TAG = "TestDrawer";
    public static final boolean LOGD = true;

    private static final String PREFERENCES = "CalendarSettings";

    private Context mContext;
    private CalendarData mCalendarData;
    private Toast mToast;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Log.i(TAG, "onCreate()");
        
        mContext = getApplicationContext();
        mCalendarData = new CalendarData(mContext);
        loadSettings();
    }
    
    public Context getContext() {
        return mContext;
    }
    
    public CalendarData getCalendarData() {
        return mCalendarData;
    }

    public void loadSettings() {
        if (LOGD) Log.i(TAG, "loadSettings()");
        
        CalendarData data = mCalendarData;
        
        try {
            SharedPreferences pref = mContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            if (pref != null) {
                for (int i=0; i<data.size(); i++) {
                    CalendarRecord item = data.getItem(i);
                    String key = String.valueOf(item.getId());
                    boolean value = pref.getBoolean(key,  false);
                    if (LOGD) Log.d(TAG, "loadSettings(" + i + ") key=" + key + " value=" + value);
                    item.setCalendarChecked(value);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "loadSettings() Error.", e);
        }
    }
    
    public void saveSettings() {
        if (LOGD) Log.i(TAG, "saveSettigns()");
        
        CalendarData data = mCalendarData;
        
        try {
            SharedPreferences pref = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            if (pref != null) {
                SharedPreferences.Editor ed = pref.edit();
                if (ed != null) {
                    for (int i=0; i<data.size(); i++) {
                        CalendarRecord item = data.getItem(i);
                        String key = String.valueOf(item.getId());
                        boolean value = item.getCalendarChecked();
                        if (LOGD) Log.d(TAG, "saveSettings(" + i + ") key=" + key + " value=" + value);
                        ed.putBoolean(key, value);
                    }
                    ed.commit();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "saveSettings() Error.", e);
        }
    }

    public void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        if (mToast != null) {
            mToast.show();
        }
    }

    
}
