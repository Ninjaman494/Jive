package brickhack.jive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class SplashActivity extends AppCompatActivity implements ServerListener{
    ServerAPI server;
    Parser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        server = new ServerAPI(this);
        parser = new Parser(this);
        server.refreshEvents();
    }

    @Override
    public void onResult(boolean success) {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.putExtra("DATA_MAP",buildMap());
        startActivity(intent);
        finish();
    }

    public HashMap<String, ArrayList<ArrayList<String>>> buildMap() {
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
}
