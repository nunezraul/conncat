package conncat.conncat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;



public class MainScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
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
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

      /*  viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);*/

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainScreen.this, addEvent.class);
                startActivity(i);
            }
        });



    }

    /*
    This function adds fragments to the tabLayout and labels them accordingly.

    @param  ViewPager   helps supply and manage lifecycle of each page
     */
   /* private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "All");
        adapter.addFragment(new TwoFragment(), "On Campus");
        adapter.addFragment(new ThreeFragment(), "Off Campus");
        viewPager.setAdapter(adapter);
    }
*/
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
            HomeScreen fragment = new HomeScreen();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_categories) {



        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_notif) {

        } else if (id == R.id.nav_sett) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

   /*class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }*/

        /*
        This function returns the item at a certain position

        @param  position    position you want more information about
        @return fragment
         */
     /*   @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }*/

        /*
        This function returns how many fragments are in the tabLayout

        @return size of fragment list
         */
     /*   @Override
        public int getCount() {
            return mFragmentList.size();
        }*/

        /*
        this function adds fragments to the tabLayout

        @param  fragment, title     specific fragment and name you want assigned to the tab
         */
        /*public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }*/

        /*
        This function returns the page title at a certain position of the tabLayout

        @param position you want more information about
        @return get character sequence at that position
         */
       /* @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }*/
}

