package brickhack.jive;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomePageActivity extends AppCompatActivity implements ServerListener {
    ViewPager viewPager;
    Parser parser;
    ServerAPI server;
    ArrayList<String> Skeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getSupportActionBar().setSelectedNavigationItem(position);
                    }
                });

        viewPager.setOffscreenPageLimit(5);
        parser = new Parser(this);
        server = new ServerAPI(this);
        server.refreshEvents();
    }

    public void onResult(boolean success){
        if(onRestart){
            System.out.println("onRestartResult");
            adapter.refreshData(buildMap());
            adapter.notifyDataSetChanged();
        }
        else {
            //createTabs(viewPager);
            setupTabs(buildMap());
            onRestart = false;
        }
    }

    boolean onRestart = false;
    @Override
    public void onRestart(){
        System.out.println("onRestart");
        onRestart = true;
        //Refresh Events might be adding the duplicates to ServerAPI, so I commented it out, I could also clear the lists in Resfresh Events
        server.refreshEvents();
        //adapter.refreshData(buildMap());
        //adapter.notifyDataSetChanged();
        super.onRestart();
    }
    public HashMap<String, ArrayList< ArrayList<String> >> buildMap(){
        //All Events
        ArrayList<ArrayList<String>> allEvents  = new ArrayList<>();
        ArrayList<String> names = server.getNames();
        ArrayList<String> dates = server.getDates();
        ArrayList<String> hours = server.getHours();
        ArrayList<String> desps = server.getDesps();
        allEvents.add(names);allEvents.add(dates);allEvents.add(hours);allEvents.add(desps);

        //Getting Keys
        ArrayList<String> keys = parser.getKeys();

        //Attending
        ArrayList<ArrayList<String>> attendingEvents  = new ArrayList<>();
        names = server.getNamesSubset(keys);
        dates = server.getDatesSubset(keys);
        hours = server.getHoursSubset(keys);
        desps = server.getDespsSubset(keys);
        attendingEvents.add(names);attendingEvents.add(dates);attendingEvents.add(hours);attendingEvents.add(desps);

        //Building Map
        HashMap<String, ArrayList< ArrayList<String> >> map = new HashMap<>();
        map.put("Attending",attendingEvents);
        map.put("All Events",allEvents);
        map.put("Your Events",allEvents);

        return map;
    }

    TabPagerAdapter adapter;
    public void setupTabs(HashMap<String, ArrayList< ArrayList<String> >> map){
        adapter = new TabPagerAdapter(getSupportFragmentManager(),map);
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

    public String requestKey(String name){
        return server.getKey(name);
    }

    public double[] requestCoords(String key){
        return server.getCoords(key);
    }

    class TabPagerAdapter extends FragmentStatePagerAdapter {
        private String tabTitles[] = new String[] { "Attending", "All Events", "Your Events" };
        private  HashMap<String,ArrayList<ArrayList<String>>> data;
        public TabPagerAdapter(FragmentManager fm,  HashMap<String,ArrayList<ArrayList<String>>> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {
            ArrayList<String> names = data.get(tabTitles[position]).get(0);
            ArrayList<String> dates = data.get(tabTitles[position]).get(1);
            ArrayList<String> hours = data.get(tabTitles[position]).get(2);
            ArrayList<String> desps = data.get(tabTitles[position]).get(3);
            System.out.println("getItem: "+names.size());
            return AllEventsFragment.newInstance(names,dates,hours,desps);
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

        public void refreshData(HashMap<String,ArrayList<ArrayList<String>>> data){
            this.data = data;
            System.out.println(data.get("Attending").get(0).get(0));
        }
    }
}



