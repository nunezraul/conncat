package conncat.conncat;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeScreen extends Fragment {
    View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public HomeScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
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
