package com.example.testdrawer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final String TAG = "MainActivity";
    private static final boolean LOGD = TestDrawer.LOGD;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    // noeuli [
    private Context mContext;
    private int mCalendarIndex;
    private CalendarData mCalendarData;
    // noeuli ]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.i(TAG, "onCreate()");
        
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // noeuli [
        mCalendarIndex = 0;
        TestDrawer app = (TestDrawer) mContext;
        mCalendarData = app.getCalendarData();
        // noeuli ]

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        Log.i(TAG, "onResume()");
        
        if (mNavigationDrawerFragment != null) {
            mNavigationDrawerFragment.updateList();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // noeuli [
        String title = (String) getTitle();
        if (mCalendarData != null) {
            title = (String) mCalendarData.getSelectedTitle(position);
        }
        if (LOGD) Log.d(TAG, "onNavigationDrawerItemSelected(" + position + ") title=" + title);

        if (title != null) updateFragment(position, title);
        // noeuli ]
    }

    public void updateFragment(int position, String title) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = PlaceholderFragment.newInstance(position + 1, title);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public void onSectionAttached(int number) {
        // noeuli [
        mCalendarIndex = number - 1;
        if (mCalendarData != null) {
            CharSequence title = mCalendarData.getSelectedTitle(mCalendarIndex);
            if (title != null) mTitle = title;
            if (LOGD) Log.d(TAG, "onSectionAttached(" + number + ") title=" + title + " mTitle=" + mTitle);
        }
        // noeuli ]
    }

    public void restoreActionBar() {
        if (LOGD) Log.d(TAG, "restoreActionBar() mTitle=" + mTitle);
        
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            // noeuli
            updateFragment(mCalendarIndex, (String)mTitle);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            //showToast("Show calendar setting activity!");
            Intent settings = new Intent(this, CalendarSettings.class);
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String TAG = "MainActivity.PlaceholderFragment";
        
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_TITLE = "section_title";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String title) {
            Log.i(TAG, "newInstance()");
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_SECTION_TITLE, title);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            Log.i(TAG, "onCreateView()");
            
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            String bodyText =  Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER))
                    + " : " + getArguments().getString(ARG_SECTION_TITLE);
            textView.setText(bodyText);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            Log.i(TAG, "onAttach()");
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
