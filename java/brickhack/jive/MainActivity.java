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

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
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
        /*parser.createUserFile();
        parser.readUserFile();*/
        createTabs(viewPager);

    }

    public String requestKey(String name){
        return server.getKey(name);
    }

    public double[] requestCoords(String key){
        return server.getCoords(key);
    }

    private void createTabs(final ViewPager viewPager) {
        ActionBar actionBar = getSupportActionBar();

        //All Events
        ArrayList<String> names = server.getNames();
        ArrayList<String> dates = server.getDates();
        ArrayList<String> hours = server.getHours();
        ArrayList<String> desps = server.getDesps();
        ArrayList<ArrayList<String>> allEvents = new ArrayList<>();
        System.out.println("size beofre:"+names.size());
        allEvents.add(names);allEvents.add(dates);allEvents.add(hours);allEvents.add(desps);
        System.out.println("size after:"+names.size());

        Skeys = parser.getKeys();
        System.out.println("Skeys:"+Skeys.toString());
        //Attending
        ArrayList<String> names2 = server.getNamesSubset(Skeys);
        ArrayList<String> dates2 = server.getNamesSubset(Skeys);
        ArrayList<String> hours2 = server.getNamesSubset(Skeys);
        ArrayList<String> desps2 = server.getNamesSubset(Skeys);
        System.out.println("subset:"+names2.toString());
        ArrayList<ArrayList<String>> attending = new ArrayList<>();
        attending.add(names2);attending.add(dates2);attending.add(hours2);attending.add(desps2);

        HashMap<String,ArrayList<ArrayList<String>>> map = new HashMap<>();


        map.put("Attending",allEvents);
        map.put("All Events",allEvents);
        map.put("Your Events",allEvents);


        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(),map));

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

    public ArrayList<String> getNamesSubset(ArrayList<String> keys,ArrayList<String> names){
        ArrayList<String> subset = new ArrayList<>();
        System.out.println("Name size:"+names.size());
        if(names.size()!=0) {
            for (String s : keys) {
                int index = server.keys.indexOf(s);
                String dateToAdd = names.get(index);
                subset.add(dateToAdd);
            }
        }
        return subset;
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
            ArrayList<String> names = data.get(tabTitles[position]).get(0);//data.get(0);
            ArrayList<String> dates = data.get(tabTitles[position]).get(1);
            ArrayList<String> hours = data.get(tabTitles[position]).get(2);
            ArrayList<String> desps = data.get(tabTitles[position]).get(3);

            return AllEventsFragment.newInstance(names,dates,hours,desps);
           /* System.out.println("pos: "+position);
            if(position==0){

                return AttendingEventsFragment.newInstance(names,dates,hours,desps);
            }
            else if(position==1) {
                return AllEventsFragment.newInstance(names,dates,hours,desps);
            }
            else if(position==2){
                return AllEventsFragment.newInstance(names,dates,hours,desps);
            }
            return null;*/
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
    }
}



