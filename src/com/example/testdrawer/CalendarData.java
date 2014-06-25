package com.example.testdrawer;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.CalendarEntity;
import android.util.Log;

public class CalendarData {
    private static final String TAG = "CalendarData";
    private static final boolean LOGD = TestDrawer.LOGD;

    private static final String CALENDAR_URI = "content://com.android.calendar";
    private static final int INVALID_ID = -1;
    
    private ContentResolver mCR;
    private ArrayList<CalendarRecord> mCalendarList;
    private int mCalendarId;
    
    public CalendarData(Context ctx) {
        mCR = ctx.getContentResolver();
        mCalendarList = new ArrayList<CalendarRecord>();
        mSelectedCalendarList = new ArrayList<String>();
        mCalendarId = INVALID_ID;
        initCalendarId();
    }

    // Find out calendar id and save into mCalendarId
    public void initCalendarId() {
        if (LOGD) Log.i(TAG, "initCalendarId()");
        
        Uri calendarUri = Uri.parse(CALENDAR_URI + "/calendars");
        String[] selection = new String[] {
                CalendarEntity._ID,
                CalendarEntity.ACCOUNT_NAME,
                CalendarEntity.ACCOUNT_TYPE,
                CalendarEntity._SYNC_ID,
                CalendarEntity.CALENDAR_DISPLAY_NAME,
                CalendarEntity.CALENDAR_ACCESS_LEVEL,
                CalendarEntity.OWNER_ACCOUNT,
        };
        String[] condition = new String[] {
                "600",
                CalendarContract.ACCOUNT_TYPE_LOCAL,
        };
        Cursor c = mCR.query(
                calendarUri, selection,
                //null, null, null);  // no condition
                "calendar_access_level>=? and account_type<>?", condition, null);

        if (c==null || !c.moveToFirst()) {
            // System does not have any calendars.
            Log.e(TAG, "No Calenders found.");
            return;
        }
        
        if (mCalendarList == null) {
            mCalendarList = new ArrayList<CalendarRecord>();
        }
        if (mCalendarList != null) {
            mCalendarList.clear();
        }

        try {
            int i=0;
            int rows = c.getCount();
            int cols = c.getColumnCount();

            if (LOGD) Log.w(TAG, "initCalendarId() count=" + rows + " cols=" + cols);

            do {
                // Temporary set the first one.
                if (mCalendarId==INVALID_ID) mCalendarId = c.getInt(0);
                
                CalendarRecord r = new CalendarRecord(c.getInt(0), c.getString(1), c.getString(2),
                        c.getString(3), c.getString(4), c.getInt(5), c.getString(6));
                mCalendarList.add(r);
                
                if (LOGD) Log.d(TAG, "initCalendarId() [" + (i++) + "] record: " + r);
            } while (c.moveToNext());

        } catch (Exception e) {
            Log.e(TAG, "Error : Exception occurred on initCalendarId()." , e);
        } finally {
            c.close();
        }
    }
    
    public int size() {
        int size = 0;
        
        if (mCalendarList != null)
            size = mCalendarList.size();
        
        return size;
    }
    
    public CalendarRecord getItem(int position) {
        CalendarRecord item = null;
        
        if (mCalendarList != null && mCalendarList.size() > position) {
            item = mCalendarList.get(position);
        }
        
        return item;
    }

    public CharSequence getTitle(int index) {
        CharSequence title = null;
        
        if (mCalendarList != null && mCalendarList.size() > index) {
            CalendarRecord item = mCalendarList.get(index);
            if (item != null) {
                title = item.getCalendarTitle();
            }
        }
        return title;
    }
    
    ArrayList<String> mSelectedCalendarList = null;
    
    public CharSequence getSelectedTitle(int index) {
        CharSequence title = null;
        if (mSelectedCalendarList == null || 
                mSelectedCalendarList.size()==0) {
            getCalendarListArray();
        }

        if (mSelectedCalendarList.size() > index) {
            title = mSelectedCalendarList.get(index);
        }
        
        if (LOGD) Log.d(TAG, "getSelectedTitle(" + index + ") returns " + title);
        
        return title;
    }

    public ArrayList<String> getCalendarListArray() {
        return getCalendarListArray(false);
    }
    
    public ArrayList<String> getCalendarListArray(boolean ignoreChecked) {
        if (mSelectedCalendarList == null) {
            mSelectedCalendarList = new ArrayList<String>();
        }
        mSelectedCalendarList.clear();
        
        if (mCalendarList == null) return mSelectedCalendarList;
        
        for (int i=0; i<mCalendarList.size(); i++) {
            CalendarRecord item = mCalendarList.get(i);
            if (item == null) continue;
            boolean checked = item.getCalendarChecked();

            if (checked || ignoreChecked) {
                mSelectedCalendarList.add(item.getCalendarTitle()); 
            }
        }

        if (LOGD) {
            Log.d(TAG, "getCalendarListArray(" + ignoreChecked + ") size=" 
                    + mSelectedCalendarList.size() + "/" + mCalendarList.size());
        }

        return mSelectedCalendarList;
    }
}
