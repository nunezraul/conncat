package conncat.conncat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;



public class MainScreen extends AppCompatActivity{
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;


    /*
    This function initializes the toolbar, tabLayout, and fab. The tabLayout is used to view All, On Campus, and OffCampus events.
    The fab is used to create a new event. The setOnCLickListener waits for a click, once there is a click, a new intent is created
    to start a new activity with the addEvent class.

    @param  savedInstanceState  any saved information related to current activity state
    @see    toolbar, tabLayout, fab
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

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
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "All");
        adapter.addFragment(new TwoFragment(), "On Campus");
        adapter.addFragment(new ThreeFragment(), "Off Campus");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        /*
        This function returns the item at a certain position

        @param  position    position you want more information about
        @return fragment
         */
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        /*
        This function returns how many fragments are in the tabLayout

        @return size of fragment list
         */
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        /*
        this function adds fragments to the tabLayout

        @param  fragment, title     specific fragment and name you want assigned to the tab
         */
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        /*
        This function returns the page title at a certain position of the tabLayout

        @param position you want more information about
        @return get character sequence at that position
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
