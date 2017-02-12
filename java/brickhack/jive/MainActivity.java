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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    Parser parser;
    ServerAPI server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        parser = new Parser(this);
        server = new ServerAPI(this);
        /*parser.createUserFile();
        parser.readUserFile();*/
        //parser.addEvent("BrickHack","1");
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


        ArrayList<String> names = server.getNames();
        ArrayList<String> dates = server.getDates();
        ArrayList<String> hours = server.getHours();
        ArrayList<String> desps = server.getDesps();
        System.out.println("lenghts: "+names.size()+" "+dates.size());
        HashMap<String,ArrayList<ArrayList<String>>> map = new HashMap<>();

        ArrayList<ArrayList<String>> lists = new ArrayList<>();
        lists.add(names);lists.add(dates);lists.add(hours);lists.add(desps);
        map.put("Attending",lists);
        map.put("All Events",lists);
        map.put("Your Events",lists);


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

}

class TabPagerAdapter extends FragmentStatePagerAdapter {
    private String tabTitles[] = new String[] { "Attending", "All Events", "Your Events" };
    private HashMap<String,ArrayList<ArrayList<String>>> data;
    public TabPagerAdapter(FragmentManager fm, HashMap<String,ArrayList<ArrayList<String>>> data ) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        ArrayList<String> names = data.get(tabTitles[position]).get(0);
        ArrayList<String> dates = data.get(tabTitles[position]).get(1);
        ArrayList<String> hours = data.get(tabTitles[position]).get(2);
        ArrayList<String> desps = data.get(tabTitles[position]).get(3);
        return AllEventsFragment.newInstance(names,dates,hours,desps);
       /* if(position==0){
            return lessonNotesFragment;
        }
        if(position==1) {
            return grammarListFragment;
        }
        else{
            return vocabListFragment;
        }*/
    }
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}

