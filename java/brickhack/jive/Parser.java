package brickhack.jive;

import android.content.Context;
import android.util.Xml;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

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
            //OutputStreamWriter out = new OutputStreamWriter(mContext.openFileOutput(fileName,0));
            JSONObject root = new JSONObject();
            JSONArray arr = new JSONArray();
            root.put("events",(Object)arr);
            System.out.println("What's being written: "+root.toString());
            fw.write(root.toString());
            fw.flush();
            fw.close();
            //out.write(root.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public JSONObject readUserJSON(){
        //String JSONraw = readFromFile();

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
            //OutputStreamWriter out = new OutputStreamWriter(mContext.openFileOutput(fileName,0));
            System.out.println("What's being written: "+ obj.toString());
            fw.write(obj.toString());
            fw.flush();
            fw.close();
            //out.write(root.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String readFromFile() {
        try {
            String fileName = this.fileName;
            FileInputStream fis = mContext.openFileInput(fileName);

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader streamReader = new BufferedReader(isr);

            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                System.out.println("APPENDING");
                responseStrBuilder.append(inputStr);
            }
            //JSONObject jsonobj = new JSONObject(responseStrBuilder.toString());
            streamReader.close();
            isr.close();
            fis.close();
            return responseStrBuilder.toString();
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void createUserFile(){
        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            xmlSerializer.setOutput(writer);
            //Start Document
            xmlSerializer.startDocument("UTF-8", true);
            //Start Root Tag
            xmlSerializer.startTag("","Records");

            //createExampleData(xmlSerializer);

            //Close Root Tag
            xmlSerializer.endTag("","Records");
            //End DOCUMENT
            xmlSerializer.endDocument();

            fileOutputStream.write(writer.toString().getBytes());
            fileOutputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readUserFile(){
        if(file!=null){
            try {
                XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
                XmlPullParser myparser = xmlFactoryObject.newPullParser();
                InputStream stream = new FileInputStream(file);
                myparser.setInput(stream, null);

                int event = myparser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT)  {
                    String name = myparser.getName();
                    switch (event){
                        case XmlPullParser.START_TAG:
                            break;

                        case XmlPullParser.END_TAG:
                            System.out.println(name+": "+myparser.getAttributeValue(null,"name")+" id:"+myparser.getAttributeValue(null,"id"));
                            break;
                    }
                    event = myparser.next();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean addEvent(String name, String key){
        JSONObject old = readUserJSON();
        JSONObject eventToAdd = new JSONObject();
        try {
            eventToAdd.put("name", name);
            eventToAdd.put("key", key);
            old.put("event",eventToAdd);
            System.out.println("inAdd: "+old.toString());
            writeJSON(old);

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }



    public boolean removeEvent(){
        return false;
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
