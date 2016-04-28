package conncat.conncat;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;


public class TwoFragment extends Fragment{
    View view;

    private EventAdapter eventAdapter;
    private ListView listView;

    public TwoFragment() {
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
        view = inflater.inflate(R.layout.fragment_two, container, false);

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
        List<EventData> ev = db.getOnCampusEvents();
        eventAdapter = new EventAdapter(getActivity(), -1, ev);
        listView.setAdapter(eventAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), viewEvent.class);
                intent.putExtra("eventID", eventAdapter.getItem(position).getRowid());
                startActivity(intent);
            }
        });

        return view;
    }

}