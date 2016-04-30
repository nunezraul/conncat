package conncat.conncat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;


public class OneFragment extends Fragment{
       View view;

    private EventAdapter eventAdapter;
    private ListView listView;

    private static int EVENT_MODIFED = 0;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /*--- Called upon the creation of the class, saves the instance of the state   ---*/
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment, sets the listView declared in the layout file, calls the bindlistView()
        view = inflater.inflate(R.layout.fragment_one, container, false);
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
        List<EventData> ev = db.getAllEvents();
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
        //setRetainInstance(true);
        return view;

    }
    /*@Override
    public void onResume(){
        super.onResume();

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
        List<EventData> ev = db.getAllEvents();
        eventAdapter = new EventAdapter(getActivity(), -1, ev);
        listView.setAdapter(eventAdapter);

    }*/

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
                List<EventData> ev = db.getAllEvents();
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