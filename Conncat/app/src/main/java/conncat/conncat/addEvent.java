package conncat.conncat;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.io.IOException;
import java.util.Calendar;


public class addEvent extends AppCompatActivity{
    private Toolbar toolbar;

    private DatePicker datePicker;
    private Calendar calendar;
    private EditText title, address, start_date, end_date, start_time, end_time, host, description;
    private int year, month, day, hour, min;

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

        title = (EditText) findViewById(R.id.title);
        host = (EditText) findViewById(R.id.host);
        start_date = (EditText) findViewById(R.id.start_date);
        end_date = (EditText) findViewById(R.id.end_date);
        start_time = (EditText) findViewById(R.id.start_time);
        end_time = (EditText) findViewById(R.id.end_time);
        address = (EditText) findViewById(R.id.location);
        description = (EditText) findViewById(R.id.description);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        eventData = new EventData();

    }


    @SuppressWarnings("deprecation")
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

    public void createEvent(View view) {
        if (!title.getText().toString().isEmpty() && !address.getText().toString().isEmpty() && !start_date.getText().toString().isEmpty() && !start_time.getText().toString().isEmpty() && !end_date.getText().toString().isEmpty() && !end_time.getText().toString().isEmpty() && !host.getText().toString().isEmpty() && !description.getText().toString().isEmpty()) {
            eventData.setName(title.getText().toString());
            eventData.setAddress(address.getText().toString());
            eventData.setStartDate(start_date.getText().toString());
            eventData.setStartTime(start_time.getText().toString());
            eventData.setEndDate(end_date.getText().toString());
            eventData.setEndTime(end_time.getText().toString());
            eventData.setHost(host.getText().toString());
            eventData.setDescription(description.getText().toString());
            eventData.setSource("USER");

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
            finish();
        }
    }


}

