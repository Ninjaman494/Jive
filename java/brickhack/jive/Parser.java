package brickhack.jive;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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

            createExampleData(xmlSerializer);

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

    public boolean addEvent(){
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
