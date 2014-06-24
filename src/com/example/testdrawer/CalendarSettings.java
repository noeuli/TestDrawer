package com.example.testdrawer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CalendarSettings extends Activity {
    private static final String TAG = "CalendarSettings";

    private Context mContext;
    PlaceholderFragment mFragmentBody;
    
    private static CalendarListAdapter mCalendarListAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_settings);
        
        mContext = getApplicationContext();

        if (savedInstanceState == null) {
            mFragmentBody = new PlaceholderFragment(); 
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mFragmentBody).commit();
        }
        
        mCalendarListAdapter = new CalendarListAdapter(mContext);
    }
    
    // query calendar list
    

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String TAG = "PlaceholderFragment";
        
        private View mRootView;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            mRootView = inflater.inflate(
                    R.layout.fragment_calendar_settings, container, false);
            Log.i(TAG, "onCreateView()");
            
            setupListView();
            
            return mRootView;
        }
        
        public View getViewById(int resId) {
            if (mRootView != null) {
                return mRootView.findViewById(resId);
            } else {
                return null;
            }
        }
        
        public View getRootView() {
            return mRootView;
        }

        public void setupListView() {
            if (mRootView == null) return;
            
            ListView calendarListView = (ListView) mRootView.findViewById(R.id.calendarListView);
            if (calendarListView != null) {
                calendarListView.setAdapter(mCalendarListAdapter);
            }
        }
    }
    
    
    public void onClick(View v) {
        int id = v.getId();
        Log.d(TAG, "onClick() id=" + id);
        
        if (id == R.id.ok) {
        } else if (id == R.id.cancel){
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult(" + requestCode + ", " + resultCode + ", " + data + ")");
        
        if (resultCode == RESULT_CANCELED) {
            Log.w(TAG, "onActivityResult(): CANCELED: req=" + requestCode + " data=" + data);
            if (data != null) {
                Bundle extras = data.getExtras();
                Log.w(TAG, "onActivityResult(): CANCELED: Extras=" + extras);
            }
        }
    }
}
