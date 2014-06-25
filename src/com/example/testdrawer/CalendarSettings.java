package com.example.testdrawer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class CalendarSettings extends Activity implements OnClickListener {
    private static final String TAG = "CalendarSettings";
    private static final boolean LOGD = TestDrawer.LOGD;
    
    private static Context mContext;
    private static CalendarListAdapter mCalendarListAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_settings);
        
        mContext = getApplicationContext();
        mCalendarListAdapter = new CalendarListAdapter(mContext);

        ListView calendarListView = (ListView) findViewById(R.id.calendarListView);
        if (calendarListView != null) {
            calendarListView.setAdapter(mCalendarListAdapter);
        }
        mCalendarListAdapter.notifyDataSetChanged();
    }
    
    public void onClick(View v) {
        int id = v.getId();
        if (LOGD) Log.i(TAG, "onClick() id=" + id);
        
        if (id == R.id.ok) {
            TestDrawer app = (TestDrawer) mContext;
            app.saveSettings();
            finish();
        } else if (id == R.id.cancel){
            finish();
        }
    }
}
