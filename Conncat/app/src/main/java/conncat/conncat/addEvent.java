package conncat.conncat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.SQLException;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
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


public class addEvent extends AppCompatActivity {
    private Toolbar toolbar;

    private DatePicker datePicker;
    private Calendar calendar;
    private EditText title, address, host, description, categories;
    private TextView start_date, end_date, start_time, end_time;
    SimpleDateFormat dateFormat = new SimpleDateFormat("ccc, LLL d, yyyy", Locale.US);
    SimpleDateFormat dateFormatSQL = new SimpleDateFormat("yyyy-LL-dd", Locale.US);
    SimpleDateFormat timeFormat = new SimpleDateFormat("KK:mm a", Locale.US);
    SimpleDateFormat timeFormatSQL = new SimpleDateFormat("kk:mm", Locale.US);

    EventData eventData;

    /*
    This function sets up the toolbar

    @param  savedInstanceState  any saved information related to current activity state
    @see    toolbar
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Create Event");

        title = (EditText) findViewById(R.id.title);
        host = (EditText) findViewById(R.id.host);
        start_date = (TextView) findViewById(R.id.start_date);
        end_date = (TextView) findViewById(R.id.end_date);
        start_time = (TextView) findViewById(R.id.start_time);
        end_time = (TextView) findViewById(R.id.end_time);
        address = (EditText) findViewById(R.id.location);
        description = (EditText) findViewById(R.id.description);
        categories = (EditText) findViewById(R.id.categories);

        calendar = Calendar.getInstance();
        start_date.setText(dateFormat.format(calendar.getTime()));
        end_date.setText(dateFormat.format(calendar.getTime()));
        start_time.setText(timeFormat.format(calendar.getTime()));
        end_time.setText(timeFormat.format(calendar.getTime()));



        eventData = new EventData();

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

    public void createEvent() {
        if (!title.getText().toString().isEmpty() && !address.getText().toString().isEmpty() && !start_date.getText().toString().isEmpty() && !start_time.getText().toString().isEmpty() && !end_date.getText().toString().isEmpty() && !end_time.getText().toString().isEmpty() && !host.getText().toString().isEmpty() && !description.getText().toString().isEmpty()) {
            eventData.setName(title.getText().toString());
            eventData.setAddress(address.getText().toString());
            Geocoder geocoder = new Geocoder(this);
            //String merced = " merced, ca";
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
            eventData.setSource("User Created");
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
                db.add(eventData);
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
        getMenuInflater().inflate(R.menu.menu_add_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:
                createEvent();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return true;
    }


}

