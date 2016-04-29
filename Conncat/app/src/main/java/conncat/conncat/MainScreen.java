package conncat.conncat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class MainScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener{
    NavigationView navigationView = null;
    private Toolbar toolbar = null;
   // private TabLayout tabLayout;
   // private ViewPager viewPager;
    private FloatingActionButton fab;


    /*
    This function initializes the toolbar, tabLayout, and fab. The tabLayout is used to view All, On Campus, and OffCampus events.
    The fab is used to create a new event. The setOnCLickListener waits for a click, once there is a click, a new intent is created
    to start a new activity with the addEvent class.

    @param  savedInstanceState  any saved information related to current activity state
    @see    toolbar, tabLayout, fab
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        //Initial HomeScreen Fragment
        HomeScreen fragment = new HomeScreen();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainScreen.this, addEvent.class);
                startActivity(i);
            }
        });
        updateDB();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;

        } else if (id == R.id.action_map){
            final Context context = this;
            Intent intent = new Intent(context, MapTabView.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if(!navigationView.getMenu().getItem(0).isChecked()) {
                setTitle("Conncat");
                HomeScreen fragment = new HomeScreen();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }

        } else if (id == R.id.nav_categories) {
            setTitle("Categories");
            Categories fragment = new Categories();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_notif) {

        } else if (id == R.id.nav_sett) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateDB(){
        String[] eventURLs = {"http://events.ucmerced.edu:7070/feeder/main/eventsFeed.do?f=y&sort=dtstart.utc:asc&fexpr=(categories.href!=%22/public/.bedework/categories/sys/Ongoing%22)%20and%20(entity_type=%22event%22%7Centity_type=%22todo%22)&skinName=list-xml&count=200", "https://drive.google.com/uc?export=download&id=0BwCHu0WyYCBkc21NblUtR1Z1MGM"};
        ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new Downloadlist(this).execute(eventURLs);
        } else {
            Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show();
        }
    }

    class Downloadlist extends AsyncTask<String, Void, String> {

        Activity mContex;
        String error = "Adding events to DB failed";
        public  Downloadlist(Activity contex)
        {
            this.mContex=contex;
        }

        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                downloadXML dl = new downloadXML();
                String result = dl.downloadXML(urls);

                List<EventData> events = xmlParser.getEvents(new ByteArrayInputStream(result.getBytes()));
                Geocoder geocoder = new Geocoder(mContex);

                String ucmerced = " merced, ca";
                for (int i = 0; i < events.size(); i++) {
                    try {
                        List<Address> e;
                        if(events.get(i).getAddress() != null) {
                            Log.v("Event: ", events.get(i).getAddress() + ucmerced);
                            if (events.get(i).getAddress().contains("SE2"))
                                e = geocoder.getFromLocationName("Science and Engineering Building 2" + ucmerced, 5);
                            else if (events.get(i).getAddress().contains("SE1"))
                                e = geocoder.getFromLocationName("Science and Engineering Building 1" + ucmerced, 5);
                            else if (events.get(i).getAddress().contains("SSM"))
                                e = geocoder.getFromLocationName("Social Science and Management building" + ucmerced, 5);
                            else if (events.get(i).getAddress().contains("SSB"))
                                e = geocoder.getFromLocationName("Student Services Building" + ucmerced, 5);
                            else if (events.get(i).getAddress().contains("KL ") || events.get(i).getAddress().equalsIgnoreCase("Library"))
                                e = geocoder.getFromLocationName("leo and dottie kolligian library" + ucmerced, 5);
                            else if (events.get(i).getAddress().contains("wallace dutra amphitheatre") || events.get(i).getAddress().contains("Wallace-Dutra Amphitheatre"))
                                e = geocoder.getFromLocationName("Kelley Grove" + ucmerced, 5);
                            else if (events.get(i).getAddress().contains("SAAC"))
                                e = geocoder.getFromLocationName("Student Activities and Athletics Center" + ucmerced, 5);
                            else if (events.get(i).getAddress().contains("Gallo Recreation"))
                                e = geocoder.getFromLocationName("Joseph E. Gallo Recreation and Wellness Center" + ucmerced, 5);
                            else if (events.get(i).getAddress().contains("Outdoor Center"))
                                e = geocoder.getFromLocationName("Student Activities and Athletics Center" + ucmerced, 5);
                            else if (events.get(i).getAddress().contains("Crescent Arch"))
                                e = geocoder.getFromLocationName("Half Dome" + ucmerced, 5);
                            else if (events.get(i).getAddress().contains("California Room"))
                                e = geocoder.getFromLocationName("Visitor Center" + ucmerced, 5);
                            else if (events.get(i).getAddress().contains("Bobcat Lair"))
                                e = geocoder.getFromLocationName("leo and dottie kolligian library" + ucmerced, 5);
                            else
                                e = geocoder.getFromLocationName(events.get(i).getAddress() + ucmerced, 5);
                            if (e.size() != 0) {
                                Address address = e.get(0);
                                events.get(i).setlongLat(address.getLongitude(), address.getLatitude());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                EventDBHelper db = new EventDBHelper(mContex);
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
                for (int i = 0; i < events.size(); i++) {
                    //Log.v("Happenings1", events.get(i).getName() + " Date: " + events.get(i).getStartDate());
                    //Log.v("Happenings2", events.get(i).getName() + " added to DB");
                    if (events.get(i).getStartDate() != null) {
                        if (events.get(i).getStartDate().contains("N/A"))
                            continue;
                    }
                    db.add(events.get(i));
                }
                /*List<String> c = db.getCategories();
                for(String s:c){
                    Log.v("Categories", s);
                }*/
                db.close();

                return "success";

            } catch (IOException e) {
                return error;
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(result.equals(error))
                Toast.makeText(mContex, error, Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        final Context context = this;
        Intent intent = new Intent(context, SearchActivity.class);
        startActivity(intent);
        // User pressed the search button
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return false;
    }

}

