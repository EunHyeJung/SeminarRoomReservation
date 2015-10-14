package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager.AccessControlFragment;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager.AccessHistoryFragment;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.ReservationStatusFragment;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.UsingStatusFragment;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.User.SettingFragment;

/**
 * Created by ehye on 2015-09-10.
 */
public class CustomPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fragmentManager;
    private Map<Integer, String> fragmentTags;
    private int userId;

    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);

        userId = SharedPreferenceClass.getValue("userId",0);

        fragmentManager = fm;
        fragmentTags = new HashMap<Integer, String>();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(userId == 1) {
            switch (position) {         // 일반 사용자
                case 0:
                    return "이용현황";
                case 1:
                    return "나의예약";
                case 2:
                    return "스마트키";
                case 3:
                    return "환경설정";
            }
        } else {                        // 관리자
            switch (position) {
                case 0:
                    return "이용현황";
                case 1:
                    return "예약현황";
                case 2:
                    return "스마트키";
                case 3:
                    return "이용기록";
            }
        }
        return null;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment currentFragment = null;

        if(userId == 1) {           // 일반사용자
            switch (position) {
                case 0:
                    currentFragment = new UsingStatusFragment();
                    break;
                case 1:
                    currentFragment = new SettingFragment();
                    break;
                case 2:
                    currentFragment = new SettingFragment();
                    break;
                case 3:
                    currentFragment = new SettingFragment();

            }
        } else {                    // 관리자
            switch (position) {
                case 0:
                    currentFragment = new UsingStatusFragment();
                    break;
                case 1:
                    currentFragment = new ReservationStatusFragment();
                    break;
                case 2:
                    currentFragment = new AccessControlFragment();
                    break;
                case 3:
                    currentFragment = new AccessHistoryFragment();
            }
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
