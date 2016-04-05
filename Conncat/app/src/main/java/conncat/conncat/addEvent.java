package conncat.conncat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class addEvent extends AppCompatActivity{
    private Toolbar toolbar;

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


    }

}

