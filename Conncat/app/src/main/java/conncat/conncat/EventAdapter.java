package conncat.conncat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nunez on 4/3/2016.
 */
public class EventAdapter extends ArrayAdapter<EventData> {

    public EventAdapter(Context context, int textViewResourceId, List<EventData> events){
        super(context, textViewResourceId, events);
    }

    /**
     * An adapter for the list view that adapts a list
     * of type EventData into the listview
     *
     * @param pos the index for the arraylist
     * @param convertView the new view that will be used to display the events.
     * @param event the list of events
     * @return returns a view with the appropriate fields set with the data
     */

    @Override
    public View getView(int pos, View convertView, ViewGroup event){
        RelativeLayout row = (RelativeLayout)convertView;
        Log.i("Events", "getView pos = " + pos);
        if(null == row){
            //No recycled View, we have to inflate one.
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (RelativeLayout)inflater.inflate(R.layout.row_event, null);
        }
        //getting each event and setting them so that it shows up on the xml
        TextView title = (TextView)row.findViewById(R.id.title);
        TextView month = (TextView)row.findViewById(R.id.month);
        TextView day = (TextView)row.findViewById(R.id.day);
        TextView year = (TextView)row.findViewById(R.id.year);
        TextView loc = (TextView)row.findViewById(R.id.loc);
        TextView time = (TextView)row.findViewById(R.id.time);
        //TextView description = (TextView)row.findViewById(R.id.description);

        String date = getItem(pos).getStartDate();
        String[] data = date.split("-");

        title.setText(getItem(pos).getName());
        month.setText(getMonth(data[1]));
        year.setText(data[0]);
        day.setText(data[2]);
        loc.setText(getItem(pos).getAddress());
        time.setText(getItem(pos).startTime);
      //  description.setText(getItem(pos).getDescription());

        return row;
    }

    /*
    This function will return the month's name given the month number

    @param month    the month number
    @return monthString     the name of the month
     */
    public String getMonth(String month){
        String monthString = "";
        switch(month){
            case "01":
                monthString = "JAN";
                break;
            case "02":
                monthString = "FEB";
                break;
            case "03":
                monthString = "MAR";
                break;
            case "04":
                monthString = "APR";
                break;
            case "05":
                monthString = "MAY";
                break;
            case "06":
                monthString = "JUN";
                break;
            case "07":
                monthString = "JUL";
                break;
            case "08":
                monthString = "AUG";
                break;
            case "09":
                monthString = "SEP";
                break;
            case "10":
                monthString = "OCT";
                break;
            case "11":
                monthString = "NOV";
                break;
            case "12":
                monthString = "DEC";
                break;

        }
        return monthString;
    }


}

