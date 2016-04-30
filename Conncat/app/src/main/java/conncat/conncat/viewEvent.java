package conncat.conncat;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Objects;

public class viewEvent extends AppCompatActivity {
    private EventData eventData;
    private TextView title, eventDate, eventTime, eventCategories, location, description;

    static final int EDIT_EVENT = 1;  // The request code
    static int EVENT_EDITED = 0;

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
        location = (TextView) findViewById(R.id.eventLocation);
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
        location.setText(eventData.getAddress());
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
                startActivityForResult(intent, EDIT_EVENT);
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == EDIT_EVENT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.v("Edit Event", "Event was RESULT_OK");
                EVENT_EDITED = 1;
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
                location.setText(eventData.getAddress());
                description.setText(eventData.getDescription());
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(EVENT_EDITED == 1) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
        }
        else{
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
        }
        super.onBackPressed();
    }


}
