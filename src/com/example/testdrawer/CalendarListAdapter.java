package com.example.testdrawer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class CalendarListAdapter extends BaseAdapter {

    private static final String TAG = "CalendarListAdapter";
    
    private static final int INVALID_ID = -1;
    
    private Context mContext;
    private LayoutInflater mInflater;
    private CalendarData mCalendarData;
    
    public CalendarListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mCalendarData = new CalendarData(mContext);
    }
    
    @Override
    public int getCount() {
        int count = 0;
        if (mCalendarData != null) {
            count = mCalendarData.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        CalendarRecord item = null;
        
        if (mCalendarData != null && mCalendarData.size() > position) {
            item = mCalendarData.getItem(position);
        }
        
        return (Object) item;
    }

    @Override
    public long getItemId(int position) {
        long id = INVALID_ID;
        
        if (mCalendarData != null && mCalendarData.size() > position) {
            CalendarRecord item = mCalendarData.getItem(position);
            if (item != null) {
                id = item.getId();
            }
        }
        return id;
    }

    private class ViewHolder {
        public TextView title;
        public CheckBox checkbox;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CalendarRecord item = null;
        ViewHolder holder = null;
        
        if (parent == null) {
            Log.w(TAG, "getView(" + position + ") Error. parent view is NULL!");
            return convertView;
        }
        
        if (mCalendarData != null && mCalendarData.size() > position) {
            item = mCalendarData.getItem(position);
        }
        if (item == null) {
            Log.w(TAG, "getView(" + position + ") Error. item is NULL!");
            return null;
        }
        
        if (convertView == null) {
            holder = new ViewHolder();
            
            convertView = mInflater.inflate(R.layout.calendar_list_item, parent, false);
            if (convertView == null) {
                Log.w(TAG, "getView(" + position + ") item view inflate error!");
                return null;
            }
            
            holder.title = (TextView) convertView.findViewById(R.id.calendar_title);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.calendar_checkbox);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        String title = item.getCalendarTitle();
        boolean checked = item.getCalendarChecked();
        
        if (holder.title != null) {
            holder.title.setText(title);
        }
        
        if (holder.checkbox != null) {
            holder.checkbox.setChecked(checked);
        }
        
        return convertView;
    }

}
