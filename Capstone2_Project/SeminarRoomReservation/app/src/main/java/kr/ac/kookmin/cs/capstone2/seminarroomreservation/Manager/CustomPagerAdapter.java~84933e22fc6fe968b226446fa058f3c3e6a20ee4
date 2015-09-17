package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.ReservationStatusFragment;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.User.UsingStatusFragment;

/**
 * Created by ehye on 2015-09-10.
 */
public class CustomPagerAdapter extends FragmentPagerAdapter {

    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;



    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);

        mFragmentManager = fm;
        mFragmentTags = new HashMap<Integer, String>();

    }


    @Override
    public CharSequence getPageTitle(int position) {
       switch (position){
            case 0:
                return "이용현황";
            case 1:
                return "예약현황";
            case 2:
                 return "스마트키";
            case 3:
                 return "출입기록";
        }
       return null;
    }

    @Override
    public Fragment getItem(int position) {
       Fragment currentFragment = null;
        switch (position){
            case 0:
                currentFragment =  new UsingStatusFragment();
                break;
            case 1:
                currentFragment =  new ReservationStatusFragment();
                break;
            case 2:
                currentFragment = new AccessControlFragment();
                break;
            case 3:
                currentFragment = new AccessHistoryFragment();
        }
        return currentFragment;
    }

    @Override
    public int getCount() {
        return 4;
    }



    public Fragment getFragment(int position){
        String tag = mFragmentTags.get(position);

        if(tag == null)
            return null;

        return mFragmentManager.findFragmentByTag(tag);
    }

}
