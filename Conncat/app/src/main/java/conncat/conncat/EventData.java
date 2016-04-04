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

    public void setName(String name){
        this.name = name;
    }

    public void setHost(String host){
        this.host = host;
    }

    public void setlongLat(int longitude, int latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void setStartDate(String year, String month, String day){
        this.startDate = year + "-" + month + "-" + day;
    }

    public void setEndDate(String year, String month, String day){
        this.endDate = year + "-" + month + "-" + day;
    }

    public void setStartTime(String hour, String min){
        this.startTime = hour + ":" + min;
    }

    public void setEndTime(String hour, String min){
        this.endTime = hour + ":" + min;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setSource(String source){
        this.source = source;
    }

    public String getName(){
        return name;
    }

    public String getHost(){
        return host;
    }

    public int getLongitude(){
        return  longitude;
    }

    public int getLatitude(){
        return latitude;
    }

    public String getStartDate(){
        return startDate + " " + startTime;
    }

    public String getEndDate(){
        return endDate + " " + endTime;
    }

    public String getAddress(){
        return address;
    }

    public String getDescription(){
        return description;
    }

    public String getSource(){
        return source;
    }

}
