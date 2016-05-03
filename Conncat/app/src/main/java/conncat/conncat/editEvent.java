package conncat.conncat;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.SQLException;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class editEvent extends AppCompatActivity {

    private DatePicker datePicker;
    private Calendar calendar;
    private EditText title, address, host, description, categories;
    private TextView start_date, end_date, start_time, end_time;
    SimpleDateFormat dateFormat = new SimpleDateFormat("ccc, LLL d, yyyy", Locale.US);
    SimpleDateFormat dateFormatSQL = new SimpleDateFormat("yyyy-LL-dd", Locale.US);
    SimpleDateFormat timeFormat = new SimpleDateFormat("KK:mm a", Locale.US);
    SimpleDateFormat timeFormatSQL = new SimpleDateFormat("kk:mm", Locale.US);

    EventData eventData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (EditText) findViewById(R.id.title);
        host = (EditText) findViewById(R.id.host);
        start_date = (TextView) findViewById(R.id.start_date);
        end_date = (TextView) findViewById(R.id.end_date);
        start_time = (TextView) findViewById(R.id.start_time);
        end_time = (TextView) findViewById(R.id.end_time);
        address = (EditText) findViewById(R.id.location);
        description = (EditText) findViewById(R.id.description);
        categories = (EditText) findViewById(R.id.categories);

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


        try {
            start_date.setText( dateFormat.format( dateFormatSQL.parse( eventData.getStartDate() ) ) );
            end_date.setText( dateFormat.format( dateFormatSQL.parse( eventData.getEndDate() ) ) );
            start_time.setText( timeFormat.format( timeFormatSQL.parse(eventData.getStartTime()) ) );
            end_time.setText( timeFormat.format( timeFormatSQL.parse(eventData.getEndTime()) ) );
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        address.setText(eventData.getAddress());
        description.setText(eventData.getDescription());
        String cat = "";
        for(int i = 0; i < eventData.categories.size(); i++){
            cat += eventData.categories.get(i) + ", ";
        }
        categories.setText(cat);

    }

    @SuppressWarnings("deprecation")
    public void setStartDate(View view) {
        DialogFragment newFragment = new DatePickerFragment(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.DAY_OF_MONTH, day);
                start_date.setText(dateFormat.format(cal.getTime()));
            }
        };
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void setStartTime(View view){
        DialogFragment newFragment = new TimePickerFragment(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                Calendar cal = Calendar.getInstance();
                cal.set(0,0,0,hourOfDay,minute);
                start_time.setText(timeFormat.format(cal.getTime()));
            }
        };
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public  void setEndDate(View view){
        DialogFragment newFragment = new DatePickerFragment(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.DAY_OF_MONTH, day);
                end_date.setText(dateFormat.format(cal.getTime()));
            }
        };
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void setEndTime(View view){
        DialogFragment newFragment = new TimePickerFragment(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                Calendar cal = Calendar.getInstance();
                cal.set(0,0,0,hourOfDay,minute);
                end_time.setText(timeFormat.format(cal.getTime()));
            }
        };
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void saveEvent() {
        if (!title.getText().toString().isEmpty() && !address.getText().toString().isEmpty() && !start_date.getText().toString().isEmpty() && !start_time.getText().toString().isEmpty() && !end_date.getText().toString().isEmpty() && !end_time.getText().toString().isEmpty() && !host.getText().toString().isEmpty() && !description.getText().toString().isEmpty()) {
            eventData.setName(title.getText().toString());
            eventData.setAddress(address.getText().toString());
            Geocoder geocoder = new Geocoder(this);
            try{
                List<Address> e;
                if(eventData.getAddress() != null) {
                    AddressParser addressParser = new AddressParser();
                    e = geocoder.getFromLocationName(addressParser.getAddress(eventData.getAddress()), 5);
                    if (e.size() != 0) {
                        Address address = e.get(0);
                        eventData.setlongLat(address.getLongitude(), address.getLatitude());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                eventData.setStartDate( dateFormatSQL.format( dateFormat.parse( start_date.getText().toString() ) ) );
                eventData.setStartTime( timeFormatSQL.format( timeFormat.parse( start_time.getText().toString() ) ) );
                eventData.setEndDate( dateFormatSQL.format( dateFormat.parse( end_date.getText().toString() ) ) );
                eventData.setEndTime( timeFormatSQL.format( timeFormat.parse( end_time.getText().toString() ) ) );
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            eventData.setHost(host.getText().toString());
            eventData.setDescription(description.getText().toString());
            eventData.categories.clear();
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

            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }else{
            Toast.makeText(this, "Fields must not be blank", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveEvent();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return true;
    }

}
