package conncat.conncat;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;


public class ThreeFragment extends Fragment{
    View view;

    private EventAdapter eventAdapter;
    private ListView listView;

    private static int EVENT_MODIFED = 0;

    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_three, container, false);

        listView = (ListView) view.findViewById(R.id.eventList);
        EventDBHelper db = new EventDBHelper(getContext());
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            db.openDataBase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        List<EventData> ev = db.getOffCampusEvents();
        db.close();
        eventAdapter = new EventAdapter(getActivity(), -1, ev);
        listView.setAdapter(eventAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), viewEvent.class);
                intent.putExtra("eventID", eventAdapter.getItem(position).getRowid());
                startActivityForResult(intent, EVENT_MODIFED);
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == EVENT_MODIFED) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                Log.v("Event modified", "Event was RESULT_OK");
                EventDBHelper db = new EventDBHelper(getContext());
                try {
                    db.createDataBase();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try{
                    db.openDataBase();
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                List<EventData> ev = db.getOnCampusEvents();
                db.close();
                eventAdapter = new EventAdapter(getActivity(), -1, ev);
                listView.setAdapter(eventAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(view.getContext(), viewEvent.class);
                        intent.putExtra("eventID", eventAdapter.getItem(position).getRowid());
                        startActivityForResult(intent, EVENT_MODIFED);
                    }
                });

            }
        }
    }

}