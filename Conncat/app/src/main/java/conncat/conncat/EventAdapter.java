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

    @Override
    public View getView(int pos, View convertView, ViewGroup event){
        RelativeLayout row = (RelativeLayout)convertView;
        Log.i("Events", "getView pos = " + pos);
        if(null == row){
            //No recycled View, we have to inflate one.
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (RelativeLayout)inflater.inflate(R.layout.row_event, null);
        }

        TextView title = (TextView)row.findViewById(R.id.title);
        TextView date = (TextView)row.findViewById(R.id.date);
        TextView time = (TextView)row.findViewById(R.id.time);
        TextView description = (TextView)row.findViewById(R.id.description);


        title.setText(getItem(pos).getName());
        date.setText(getItem(pos).getStartDate());
        time.setText(getItem(pos).getEndDate());
        description.setText(getItem(pos).getDescription());

        return row;

    }

}

