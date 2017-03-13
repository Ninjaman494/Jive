package brickhack.jive;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Created by akash on 2/11/2017.
 */

public class Parser {
    Context mContext;
    private final String fileName = "USER_RECORDS";
    File file;

    public Parser(Context context){
        mContext = context;
        file = new File(context.getFilesDir(),fileName);
        if(!file.exists()){
            createUserJSON();
        }
    }

    private void createUserJSON(){
        try {
            FileWriter fw = new FileWriter(file);
            JSONObject root = new JSONObject();
            JSONArray arr = new JSONArray();
            root.put("events",arr);
            fw.write(root.toString());
            fw.flush();
            fw.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public JSONObject readUserJSON(){
        try {
            FileInputStream is = new FileInputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String mResponse = new String(buffer);
            System.out.println(mResponse);

            JSONObject jsonobj = new JSONObject(mResponse);
            System.out.println("JSON: "+jsonobj.toString());
            return jsonobj;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void writeJSON(JSONObject obj){
        try {
            FileWriter fw = new FileWriter(file);
            System.out.println("What's being written: "+ obj.toString());
            fw.write(obj.toString());
            fw.flush();
            fw.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addEvent(String name, String key){
        JSONObject old = readUserJSON();
        JSONObject eventToAdd = new JSONObject();
        try {
            eventToAdd.put("name", name);
            eventToAdd.put("key", key);
            ((JSONArray) old.get("events")).put(eventToAdd);
            System.out.println("inAdd: "+old.toString());
            writeJSON(old);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isInFile(String key){
        JSONObject obj = readUserJSON();
        try {
            JSONArray root = obj.getJSONArray("events");
            for(int i=0;i<root.length();i++){
                JSONObject event = (JSONObject)root.get(i);
                if(event.get("key").toString().equals(key)){
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void removeEvent(String key){
        JSONObject old = readUserJSON();
        try {
            JSONArray root = old.getJSONArray("events");
            for(int i=0;i<root.length();i++){
                JSONObject event = (JSONObject)root.get(i);
                if(event.get("key").toString().equals(key)){
                    root.remove(i);
                    break;
                }
            }
            writeJSON(old);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<String> getKeys(){
        ArrayList<String> keys = new ArrayList<>();
        JSONObject obj = readUserJSON();
        try {
            JSONArray root = (JSONArray) obj.get("events");
            for(int i=0;i<root.length();i++){
                JSONObject event = (JSONObject)root.get(i);
                keys.add(event.get("key").toString());
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return keys;
    }

    private void createExampleData(XmlSerializer xmlSerializer){
        try {
            //Start Event Tag
            xmlSerializer.startTag("", "event");
            xmlSerializer.attribute("","name","BricHack 3");
            xmlSerializer.attribute("","id","12345");
            //End Event Tag
            xmlSerializer.endTag("","event");

            //Start Event Tag
            xmlSerializer.startTag("", "event");
            xmlSerializer.attribute("","name","SSE Meeting");
            xmlSerializer.attribute("","id","67890");
            //End Event Tag
            xmlSerializer.endTag("","event");

            //Start Event Tag
            xmlSerializer.startTag("", "event");
            xmlSerializer.attribute("","name","Men's Hockey Game");
            xmlSerializer.attribute("","id","abcde");
            //End Event Tag
            xmlSerializer.endTag("","event");
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
