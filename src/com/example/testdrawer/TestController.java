package com.example.testdrawer;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;

public class TestController {
    private static final String TAG = "TestController";
    
    private ArrayList<String> mCalendarList = new ArrayList<String>();
    private Context mContext;

    public TestController() {
        init();
    }
    
    public TestController(Context ctx) {
        mContext = ctx;
        init();
    }

    private void init() {
        Log.d(TAG, "TestController() constructor");
        
        mCalendarList = new ArrayList<String>();
        mCalendarList.add("기본 달력");
        mCalendarList.add("noeuli");
        mCalendarList.add("Work");
        mCalendarList.add("Health");
        mCalendarList.add("Wedding");
        mCalendarList.add("Work-schedule");
        mCalendarList.add("봄이 달력");

        mCalendarList.add("날씨");
        mCalendarList.add("달의 위상");
        mCalendarList.add("주소록 친구의 생일");
        mCalendarList.add("한국의 휴일");
    }
    
    public ArrayList<String> getCalendarList() {
        return mCalendarList;
    }
    
    public String[] getCalendarListArray() {
        return (String[]) mCalendarList.toArray(new String[0]);
    }

    public CharSequence getTitle(int index) {
        return (CharSequence) mCalendarList.get(index);
    }
}
