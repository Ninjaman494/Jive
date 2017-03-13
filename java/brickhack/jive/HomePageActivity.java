package brickhack.jive;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class HomePageActivity extends AppCompatActivity implements ServerListener {
    ViewPager viewPager;
    TabPagerAdapter adapter;
    Parser parser;
    ServerAPI server;
    HashMap<String, ArrayList<ArrayList<String>>> dataMap;
    boolean onRestart = false;

    //Constants
    private static final String ON_ORIENTATION_CHANGE = "on orientation change";
    private static final String DATA_MAP = "DATA_MAP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parser = new Parser(this);
        server = new ServerAPI(this);

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the corresponding tab.
                        getSupportActionBar().setSelectedNavigationItem(position);
                    }
                });
        viewPager.setOffscreenPageLimit(5);

        if(savedInstanceState == null) {
            dataMap = (HashMap<String, ArrayList<ArrayList<String>>>) getIntent().getSerializableExtra(DATA_MAP);
        }else {
            dataMap = (HashMap) savedInstanceState.getSerializable(DATA_MAP);
        }
        setupTabs(dataMap);
    }

    /** Called when server request has been completed */
    public void onResult(boolean success) {
        dataMap = buildMap(server, parser);
        if (onRestart) {
            System.out.println("onRestartResult");
            adapter.refreshData(dataMap);
            adapter.notifyDataSetChanged();
            onRestart = false;
        } else {
            setupTabs(dataMap);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle saveState) {
        System.out.println("onSaveInstanceState");
        saveState.putBoolean(ON_ORIENTATION_CHANGE, true);
        saveState.putSerializable(DATA_MAP,dataMap);
    }

    @Override
    public void onRestart() {
        System.out.println("onRestart");
        onRestart = true;
        server.refreshEvents();
        super.onRestart();
    }

    public static HashMap<String, ArrayList<ArrayList<String>>> buildMap(ServerAPI server, Parser parser) {
        //All Events
        ArrayList<ArrayList<String>> allEvents = new ArrayList<>();
        ArrayList<String> names = server.getNames();
        ArrayList<String> dates = server.getDates();
        ArrayList<String> hours = server.getHours();
        ArrayList<String> desps = server.getDesps();
        ArrayList<String> keys = server.getKeys();
        allEvents.add(names);
        allEvents.add(dates);
        allEvents.add(hours);
        allEvents.add(desps);
        allEvents.add(keys);

        //Getting Keys
        ArrayList<String> localKeys = parser.getKeys();

        //Attending
        ArrayList<ArrayList<String>> attendingEvents = new ArrayList<>();
        names = server.getNamesSubset(localKeys);
        dates = server.getDatesSubset(localKeys);
        hours = server.getHoursSubset(localKeys);
        desps = server.getDespsSubset(localKeys);
        attendingEvents.add(names);
        attendingEvents.add(dates);
        attendingEvents.add(hours);
        attendingEvents.add(desps);
        attendingEvents.add(localKeys);

        //Building Map
        HashMap<String, ArrayList<ArrayList<String>>> map = new HashMap<>();
        map.put("Attending", attendingEvents);
        map.put("All Events", allEvents);
        map.put("Your Events", allEvents);

        return map;
    }

    public void setupTabs(HashMap<String, ArrayList<ArrayList<String>>> map) {
        adapter = new TabPagerAdapter(getSupportFragmentManager(), map);
        viewPager.setAdapter(adapter);
        ActionBar actionBar = getSupportActionBar();

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };
        // Add 3 tabs, specifying the tab's text and TabListener
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Attending")
                        .setTabListener(tabListener));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("All Events")
                        .setTabListener(tabListener));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Your Events")
                        .setTabListener(tabListener));

    }

    class TabPagerAdapter extends FragmentStatePagerAdapter {
        private String tabTitles[] = new String[]{"Attending", "All Events", "Your Events"};
        private HashMap<String, ArrayList<ArrayList<String>>> data;

        public TabPagerAdapter(FragmentManager fm, HashMap<String, ArrayList<ArrayList<String>>> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {
            ArrayList<String> names = data.get(tabTitles[position]).get(0);
            ArrayList<String> dates = data.get(tabTitles[position]).get(1);
            ArrayList<String> hours = data.get(tabTitles[position]).get(2);
            ArrayList<String> desps = data.get(tabTitles[position]).get(3);
            ArrayList<String> keys = data.get(tabTitles[position]).get(4);
            return AllEventsFragment.newInstance(names, dates, hours, desps,keys);
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void refreshData(HashMap<String, ArrayList<ArrayList<String>>> data) {
            this.data = data;
        }
    }
}



