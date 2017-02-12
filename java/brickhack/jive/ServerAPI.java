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
    Context mContext;
    ArrayList<JSONObject> events;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<String> hours = new ArrayList<>();
    ArrayList<double[]> coords = new ArrayList<>();
    private ArrayList<String> keys = new ArrayList<>();

    private final String eventUrl = "https://jive2.herokuapp.com/events";
    private final String locUrl = "https://jive2.herokuapp.com/colleges/";

    public ServerAPI(Context context){
        mContext = context;
        refreshEvents();
    }

    public ServerAPI(Context context,boolean b){
        mContext = context;
    }

    public void refreshEvents(){
        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, eventUrl, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for(int i =0;i<response.length();i++){
                                JSONObject obj = (JSONObject)response.get(i);
                                //System.out.println("obj: "+events.get(i).toString());
                                names.add(obj.get("name").toString());
                                dates.add(parseDate(obj.get("start_time").toString()));
                                hours.add(parseHours(obj.get("start_time").toString(),obj.get("end_time").toString()));
                                keys.add(obj.get("id").toString());

                                System.out.println("coords: "+obj.get("gps_lat")+" , "+obj.get("gps_lon"));
                                double d[] = new double[2];
                                d[0] = (Double)obj.get("gps_lat");
                                d[1] = (Double)obj.get("gps_lon");
                                coords.add(d);

                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Someone loathes you bitch");
                    }
                });
        queue.add(jsObjRequest);
    }

    //ArrayList<Double> coords = new ArrayList<>();
   /* public void getLocationData(String key){
        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, locUrl+key, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("onResponse");
                        try {
                            for(int i =0;i<response.length();i++){
                                JSONObject obj = (JSONObject)response.get(i);
                                Double  lat = (Double)obj.get("gps_lat");
                                Double  lon = (Double)obj.get("gps_lon");
                                coords.add(lat);
                                coords.add(lon);
                                System.out.println("loc:"+obj.toString());
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(locUrl+1);
                        System.out.println("Someone loathes you bitch");
                    }
                });
        queue.add(jsObjRequest);
    }*/

    public double[] getCoords(String key){
       int index = keys.indexOf(key);
        return coords.get(index);
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
