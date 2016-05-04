package conncat.conncat;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;

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


        String date = eventData.getStartDate();
        String[] data = date.split("-");

        setTitle("");
        title.setText(eventData.getName());
        eventDate.setText(getMonth(data[1]) + " " + data[2] + ", " + data[0]);
        eventTime.setText(get12hrtime(eventData.startTime) + " - " + get12hrtime(eventData.endTime));
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

    /*
    This function will return the month's name given the month number

    @param month    the month number
    @return monthString     the name of the month
     */
    public String getMonth(String month){
        String monthString = "";
        switch(month){
            case "1":
            case "01":
                monthString = "January";
                break;
            case "2":
            case "02":
                monthString = "February";
                break;
            case "3":
            case "03":
                monthString = "March";
                break;
            case "4":
            case "04":
                monthString = "April";
                break;
            case "5":
            case "05":
                monthString = "May";
                break;
            case "6":
            case "06":
                monthString = "June";
                break;
            case "7":
            case "07":
                monthString = "July";
                break;
            case "8":
            case "08":
                monthString = "August";
                break;
            case "9":
            case "09":
                monthString = "September";
                break;
            case "10":
                monthString = "October";
                break;
            case "11":
                monthString = "November";
                break;
            case "12":
                monthString = "December";
                break;

        }
        return monthString;
    }

    public String get12hrtime (String time ){
        String[] data = time.split(":");

        int hr = Integer.valueOf(data[0]);
        String min = data[1];

        String newtime;

        if (hr >= 12){
            if (hr > 12)
                hr = hr - 12;
            newtime = hr + ":" + min + " PM";
        }
        else newtime = hr + ":" + min + " AM";
        return newtime;
    }
}
