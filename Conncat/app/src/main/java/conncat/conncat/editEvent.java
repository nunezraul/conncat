package conncat.conncat;

import android.app.DialogFragment;
import android.database.SQLException;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class editEvent extends AppCompatActivity {

    private DatePicker datePicker;
    private Calendar calendar;
    private EditText title, address, start_date, end_date, start_time, end_time, host, description, categories;
    private EditText rowid;
    private int year, month, day, hour, min;

    EventData eventData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = (EditText) findViewById(R.id.title);
        host = (EditText) findViewById(R.id.host);
        start_date = (EditText) findViewById(R.id.start_date);
        end_date = (EditText) findViewById(R.id.end_date);
        start_time = (EditText) findViewById(R.id.start_time);
        end_time = (EditText) findViewById(R.id.end_time);
        address = (EditText) findViewById(R.id.location);
        description = (EditText) findViewById(R.id.description);
        categories = (EditText) findViewById(R.id.categories);
        rowid = (EditText) findViewById(R.id.rowid);


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

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

        setTitle("Edit Event");

        title.setText(eventData.getName());
        host.setText(eventData.getHost());
        start_date.setText(eventData.getStartDate());
        end_date.setText(eventData.getEndDate());
        start_time.setText(eventData.startTime);
        end_time.setText(eventData.endTime);
        address.setText(eventData.getAddress());
        description.setText(eventData.getDescription());
        rowid.setText(Objects.toString(eventData.getRowid(), null));
        String cat = "";
        for(int i = 0; i < eventData.categories.size(); i++){
            cat += eventData.categories.get(i) + ", ";
        }
        categories.setText(cat);

    }

    public void setStartDate(View view) {
        DialogFragment newFragment = new DatePickerFragment(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                start_date.setText(year + "-" + month + "-" + day);
            }
        };
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void setStartTime(View view){
        DialogFragment newFragment = new TimePickerFragment(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                start_time.setText(hourOfDay + ":" + minute);
            }
        };
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public  void SetEndDate(View view){
        DialogFragment newFragment = new DatePickerFragment(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                end_date.setText(year + "-" + month + "-" + day);
            }
        };
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void setEndTime(View view){
        DialogFragment newFragment = new TimePickerFragment(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                end_time.setText(hourOfDay + ":" + minute);
            }
        };
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void saveEvent(View view) {
        if (!title.getText().toString().isEmpty() && !address.getText().toString().isEmpty() && !start_date.getText().toString().isEmpty() && !start_time.getText().toString().isEmpty() && !end_date.getText().toString().isEmpty() && !end_time.getText().toString().isEmpty() && !host.getText().toString().isEmpty() && !description.getText().toString().isEmpty()) {
            eventData.setName(title.getText().toString());
            eventData.setAddress(address.getText().toString());
            Geocoder geocoder = new Geocoder(this);
            try{
                List<Address> e = geocoder.getFromLocationName(address.getText().toString(), 5);
                if(e.size() != 0) {
                    Address address = e.get(0);
                    eventData.setlongLat(address.getLongitude() ,address.getLatitude());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            eventData.setStartDate(start_date.getText().toString());
            eventData.setStartTime(start_time.getText().toString());
            eventData.setEndDate(end_date.getText().toString());
            eventData.setEndTime(end_time.getText().toString());
            eventData.setHost(host.getText().toString());
            eventData.setDescription(description.getText().toString());
            eventData.setSource("USER");
            String cat = categories.getText().toString();
            String[] categories = cat.trim().split("\\s*,\\s*");
            for(String ss: categories){
                eventData.addCategory(ss);
            }

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
            try {
                db.updateEvent(eventData);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            db.close();
            finish();
        }
    }

}
