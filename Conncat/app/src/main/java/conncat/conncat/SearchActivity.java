package conncat.conncat;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private EventAdapter eventAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.eventList);
        EventDBHelper db = new EventDBHelper(this);
        Bundle extras = getIntent().getExtras();
        String searchQuery = extras.getString("searchQuery");
        setTitle("Results for '" + searchQuery + "'");

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
        List<EventData> ev = db.search(searchQuery);
        eventAdapter = new EventAdapter(this, -1, ev);
        listView.setAdapter(eventAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), viewEvent.class);
                intent.putExtra("eventID", eventAdapter.getItem(position).getRowid());
                startActivity(intent);
            }
        });
        //setRetainInstance(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return true;
    }


}
