package conncat.conncat;

/**
 * Created by nunez on 4/2/2016.
 */
public class EventData {
    String name;
    String host;
    int longitude;
    int latitude;
    String startDate;
    String endDate;
    String startTime;
    String endTime;
    String address;
    String description;
    String source;

    /**
     * Sets the name field from the argument
     * @param name the name that will be set to the name string
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Sets the host data to the argument host
     * @param host the host of the event
     */
    public void setHost(String host){
        this.host = host;
    }

    /**
     * Sets the variable longitude and latitude to the value of the arguments respectively.
     * @param longitude the longitude of the event
     * @param latitude the latitude of the event
     */
    public void setlongLat(int longitude, int latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Sets the event start date from the three parameters year, month, and day
     * @param year the year the event will start
     * @param month the month the event will start
     * @param day the day the event will start
     */
    public void setStartDate(String year, String month, String day){
        this.startDate = year + "-" + month + "-" + day;
    }

    public void setStartDate(String date){
        this.startDate = date;
    }

    /**
     * Sets the event end date using the three arguments to form a date
     * @param year the year the event will end
     * @param month the month the event will end
     * @param day the day the event will end
     */
    public void setEndDate(String year, String month, String day){
        this.endDate = year + "-" + month + "-" + day;
    }

    public void setEndDate(String date){
        this.endDate = date;
    }

    /**
     * Sets the start time for event using the hour and min arguments
     * @param hour The hour the event will start
     * @param min the min the event will start
     */
    public void setStartTime(String hour, String min){
        this.startTime = hour + ":" + min;
    }

    public void setStartTime(String time){
        this.startTime = time;
    }

    /**
     * Sets the end time for the event using the hour and min parameters
     * @param hour the hour the event will end
     * @param min the minute the event will end
     */
    public void setEndTime(String hour, String min){
        this.endTime = hour + ":" + min;
    }

    public void setEndTime(String time){ this.endTime = time; }

    /**
     * Sets the address of the event
     * @param address the address where the event will be
     */
    public void setAddress(String address){
        this.address = address;
    }

    /**
     * Sets the description of the event
     * @param description the description of what the event will be
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * Sets the source of the event
     * @param source the source of where the event comes from
     */
    public void setSource(String source){
        this.source = source;
    }

    /**
     * Returns the name of the event
     * @return the name of the event
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the host of the event
     * @return the host of the event
     */
    public String getHost(){
        return host;
    }

    /**
     * Returns the longitude of the event
     * @return the longitude of the event
     */
    public int getLongitude(){
        return  longitude;
    }

    /**
     * returns the latitude of the event location
     * @return the latitude of the event
     */
    public int getLatitude(){
        return latitude;
    }

    /**
     * returns the start date of the events in utc format
     * @return the start date and time of the event
     */
    public String getStartDate(){
        return startDate + " " + startTime;
    }

    /**
     * returns the end date of the event in utc format
     * @return the end date and time of the event
     */
    public String getEndDate(){
        return endDate + " " + endTime;
    }

    /**
     * returns the address of the event
     * @return the address of the event
     */
    public String getAddress(){
        return address;
    }

    /**
     * returns the description of the event
     * @return the description of the event
     */
    public String getDescription(){
        return description;
    }

    /**
     * returns the source of the events
     * @return the source of the events
     */
    public String getSource(){
        return source;
    }

}
