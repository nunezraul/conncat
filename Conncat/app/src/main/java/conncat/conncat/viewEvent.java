package conncat.conncat;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class viewEvent extends AppCompatActivity {
    private EventData eventData;
    private TextView title, eventDate, eventTime, eventCategories, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (TextView) findViewById(R.id.title);
        eventDate = (TextView) findViewById(R.id.eventDate);
        eventTime = (TextView) findViewById(R.id.eventTime);
        eventCategories = (TextView) findViewById(R.id.eventCategories);
        description = (TextView) findViewById(R.id.eventDescription);

        eventData = new EventData();
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            long rowid = extras.getLong("eventID");
            EventDBHelper db = new EventDBHelper(this);
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
            eventData = db.getEvent(rowid);
            db.close();

        }

        setTitle(eventData.getName());
        title.setText(eventData.getName());
        eventDate.setText(eventData.getStartDate());
        eventTime.setText(eventData.startTime);
        String cat = "";
        for(int i = 0; i < eventData.categories.size(); i++){
            cat += eventData.categories.get(i) + ", ";
        }
        eventCategories.setText(cat);
        description.setText(eventData.getDescription());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(this, editEvent.class);
                intent.putExtra("eventID", eventData.getRowid());
                startActivity(intent);
        }
        return true;
    }


}
