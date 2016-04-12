package conncat.conncat;

import android.database.DatabaseUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nunez on 4/2/2016.
 */
public class xmlParser {
    static final String KEY_EVENT = "event";
    static final String KEY_START = "start";
    static final String KEY_END = "end";
    static final String KEY_YEAR = "fourdigityear";
    static final String KEY_MONTH = "twodigitmonth";
    static final String KEY_DAY = "twodigitday";
    static final String KEY_HOUR = "twodigithour";
    static final String KEY_MIN = "twodigitminute";
    static final String KEY_SUMMARY = "summary";
    static final String KEY_LINK = "link";
    static final String KEY_LOCATION = "location";
    static final String KEY_ADDRESS = "address";
    static final String KEY_CATEGORY = "category";
    static final String KEY_VALUE = "value";
    static final String KEY_CALENDAR = "calendar";
    static final String KEY_DESCRIPTION = "description";

    /**
     * Returns a list of type EventData which contains all the events.
     * It gets the events from the xml file by parsing it
     *
     * @param xml   The xml file with the data for the events
     * @return      the list containing the events
     */

    public static List<EventData> getEvents(InputStream xml){
        //List of events that will be returned
        List<EventData> events = new ArrayList<EventData>();

        //Temporary event
        EventData curEvent = null;
        //Temporary text holder
        String curText = "";

        boolean start = true;
        boolean calendar = false;
        String startyear = "", startmonth = "", startday = "", endyear = "", endmonth = "", endday = "";
        String startHour = "", startMin = "", endHour = "", endMin = "";

        try{
            //Setup xml parser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlp = factory.newPullParser();

            //Read the xml file
            xmlp.setInput(xml, "utf-8");

            //get first event type
            int eventType = xmlp.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT){

                String tagname = xmlp.getName();


                switch(eventType){

                    case XmlPullParser.START_TAG:
                        if(tagname.equalsIgnoreCase(KEY_EVENT)) {
                            curEvent = new EventData();
                        }
                        if(tagname.equalsIgnoreCase(KEY_CALENDAR)){
                            calendar = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        curText = xmlp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(tagname.equalsIgnoreCase(KEY_EVENT)){
                            //reached end of tag, add to list

                            events.add(curEvent);

                        }
                        else if(tagname.equalsIgnoreCase(KEY_START)){
                            curEvent.setStartDate(startyear, startmonth, startday);
                            curEvent.setStartTime(startHour, startMin);
                            start = !start;
                        }
                        else if(tagname.equalsIgnoreCase(KEY_END)){
                            curEvent.setEndDate(endyear, endmonth, endday);
                            curEvent.setEndTime(endHour, endMin);
                            start = !start;
                        }
                        else if(tagname.equalsIgnoreCase(KEY_YEAR)){
                            if(start){
                                startyear = curText;
                            }
                            else{
                                endyear = curText;
                            }
                        }
                        else if(tagname.equalsIgnoreCase(KEY_MONTH)){
                            if(start){
                                startmonth = curText;
                            }
                            else{
                                endmonth = curText;
                            }
                        }
                        else if(tagname.equalsIgnoreCase(KEY_DAY)){
                            if(start){
                                startday = curText;
                            }
                            else{
                                endday = curText;
                            }
                        }
                        else if(tagname.equalsIgnoreCase(KEY_HOUR)){
                            if(start){
                                startHour = curText;
                            }
                            else{
                                endHour = curText;
                            }
                        }
                        else if(tagname.equalsIgnoreCase(KEY_MIN)){
                            if(start){
                                startMin = curText;
                            }
                            else{
                                endMin = curText;
                            }
                        }
                        else if(tagname.equalsIgnoreCase(KEY_SUMMARY)){
                            if(!calendar)
                                curEvent.setName(curText);
                        }
                        else if(tagname.equalsIgnoreCase(KEY_LINK)){
                            curEvent.setHost(curText);
                            curEvent.setSource(curText);
                        }
                        else if(tagname.equalsIgnoreCase(KEY_ADDRESS)){
                            curEvent.setAddress(curText);
                        }
                        else if(tagname.equalsIgnoreCase(KEY_DESCRIPTION)){
                            curEvent.setDescription(curText);
                        }
                        else if(tagname.equalsIgnoreCase(KEY_CALENDAR)){
                            calendar = false;
                        }
                        break;

                    default: break;

                }

                eventType = xmlp.next();

            }



        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return events;


    }



}