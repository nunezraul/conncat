package conncat.conncat;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

public class CategorizedEvents extends AppCompatActivity {

    View view;
    private EventAdapter eventAdapter;
    private ListView listView;
    ViewGroup parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorized_events);
        view = getLayoutInflater().inflate(R.layout.activity_categorized_events, parent, false);
        listView = (ListView) view.findViewById(R.id.categorizedEvents);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String category = extras.getString("category");
            EventDBHelper db = new EventDBHelper(getApplicationContext());
            try {
                db.createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                db.openDataBase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            List<EventData> ev = db.getEventsByCategory(category);
            eventAdapter = new EventAdapter(this, R.layout.row_event, ev);
            listView.setAdapter(eventAdapter);
            db.close();

        }


        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), viewEvent.class);
                intent.putExtra("eventID", eventAdapter.getItem(position).getRowid());
                startActivity(intent);
            }
        });*/
        //setRetainInstance(true);


    }
    @Override
    public void onResume(){
        super.onResume();

        EventDBHelper db = new EventDBHelper(getApplicationContext());
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
        eventAdapter = new EventAdapter(this, -1, ev);
        listView.setAdapter(eventAdapter);

    }

}
