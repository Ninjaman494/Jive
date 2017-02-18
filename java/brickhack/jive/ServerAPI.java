package brickhack.jive;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by akash on 2/11/2017.
 */

public class ServerAPI {
    ServerListener mContext;
    ArrayList<JSONObject> events;
    static ArrayList<String> names = new ArrayList<>();
    static ArrayList<String> dates = new ArrayList<>();
    static ArrayList<String> hours = new ArrayList<>();
    static ArrayList<String> desps = new ArrayList<>();
    static ArrayList<double[]> coords = new ArrayList<>();
     ArrayList<String> keys = new ArrayList<>();

    private final String eventUrl = "https://jive-backend.herokuapp.com/events";
    private final String locUrl = "https://jive-backend.herokuapp.com/colleges/";

    public ServerAPI(ServerListener context){
        mContext = context;
    }

    public void refreshEvents(){
        //Clearing lists;
        names.clear();
        dates.clear();
        hours.clear();
        desps.clear();
        coords.clear();
        keys.clear();
        //Sending request
        RequestQueue queue = Volley.newRequestQueue((Context)mContext);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, eventUrl, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for(int i =0;i<response.length();i++){
                                JSONObject obj = (JSONObject)response.get(i);
                                //System.out.println("obj: "+events.get(i).toString());
                                names.add(obj.get("name").toString());
                                dates.add(parseDate(obj.get("start_date").toString()));
                                hours.add(parseHours(obj.get("start_date").toString(),obj.get("end_date").toString()));
                                desps.add(obj.get("description").toString());
                                keys.add(obj.get("id").toString());

                                double d[] = new double[2];
                                d[0] = (Double)obj.get("geo_lat");
                                d[1] = (Double)obj.get("geo_lon");
                                coords.add(d);
                            }
                            mContext.onResult(true);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mContext.onResult(false);
                    }
                });
        queue.add(jsObjRequest);
    }

    public double[] getCoords(String key){
       int index = keys.indexOf(key);
        return coords.get(index);
    }

    public ArrayList<String> getNamesSubset(ArrayList<String> keys){
        ArrayList<String> subset = new ArrayList<>();
        if(names.size()!=0) {
            for (String s : keys) {
                int index = this.keys.indexOf(s);
                String dateToAdd = names.get(index);
                subset.add(dateToAdd);
            }
        }
        return subset;
    }

    public ArrayList<String> getDatesSubset(ArrayList<String> keys){
        ArrayList<String> subset = new ArrayList<>();
        if(dates.size()!=0) {
            for (String s : keys) {
                int index = this.keys.indexOf(s);
                String dateToAdd = dates.get(index);
                subset.add(dateToAdd);
            }
        }
        return subset;
    }

    public ArrayList<String> getHoursSubset(ArrayList<String> keys){
        ArrayList<String> subset = new ArrayList<>();
        if(hours.size()!=0) {
            for (String s : keys) {
                int index = this.keys.indexOf(s);
                String dateToAdd = hours.get(index);
                subset.add(dateToAdd);
            }
        }
        return subset;
    }

    public ArrayList<String> getDespsSubset(ArrayList<String> keys){
        ArrayList<String> subset = new ArrayList<>();
        if(desps.size()!=0) {
            for (String s : keys) {
                int index = this.keys.indexOf(s);
                String dateToAdd = desps.get(index);
                subset.add(dateToAdd);
            }
        }
        return subset;
    }

    public ArrayList<String> getNames(){
        return names;
    }

    public ArrayList<String> getDates(){
        return dates;
    }

    public ArrayList<String> getHours(){
        return hours;
    }

    public ArrayList<String> getDesps(){
        return desps;
    }

    public String getKey(String name){
        int index = names.indexOf(name);
        return keys.get(index);
    }

    public String parseDate(String ISOdate){
        DateTime dt = ISODateTimeFormat.dateTime().parseDateTime(ISOdate);
        String date = dt.getMonthOfYear() + "/" + dt.getDayOfMonth();

        return date;
    }

    public String parseHours(String startISOdate,String endISOdate){
        DateTime start = ISODateTimeFormat.dateTime().parseDateTime(startISOdate);
        DateTime end = ISODateTimeFormat.dateTime().parseDateTime(endISOdate);
        int startTime = start.getHourOfDay();
        int endTime = end.getHourOfDay();

        String startTimeString,endTimeString;
        if(startTime > 12){
            startTime -= 12;
            startTimeString = startTime + " PM";
        }else{ startTimeString = startTime + " AM";}
        if(endTime > 12){
            endTime -= 12;
            endTimeString = endTime +"PM";
        }else{ endTimeString = endTime + " AM";}

        String hours = startTimeString + " to "+ endTimeString;

        return hours;
    }
}

interface ServerListener{
    void onResult(boolean success);
}
