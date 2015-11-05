package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.ReservationStatusFragment;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.UsingStatusFragment;

/**
 * Created by ehye on 2015-09-10.
 */
public class CustomPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fragmentManager;
    private Map<Integer, String> fragmentTags;

    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);

        fragmentManager = fm;
        fragmentTags = new HashMap<Integer, String>();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "이용현황";
            case 1:
                return "예약현황";
            case 2:
                return "출입키";
            case 3:
                return "출입기록";
        }
        return null;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment currentFragment = null;
        System.out.println("position : "+position);
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


    @Override
    public Object instantiateItem(ViewGroup container, int position){
        Object obj = super.instantiateItem(container, position);
        // fragment tag瑜??ш린 湲곕줉
        Fragment f = (Fragment) obj;
        String tag = f.getTag();
        fragmentTags.put(position, tag);
        return obj;
    }

    public Fragment getFragment(int position){
        String tag = fragmentTags.get(position);

        if(tag == null)
            return null;
        return fragmentManager.findFragmentByTag(tag);
    }
}