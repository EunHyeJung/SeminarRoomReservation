package kr.ac.kookmin.cs.capstone2.seminarroomreservation.User;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.SharedPreferenceClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {


    Button buttonLogout;


    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        buttonLogout = (Button) rootView.findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  SharedPreferenceClass.removeAllPreferences();
                SharedPreferenceClass.putValue("autoLogin", false);
                SharedPreferenceClass.putValue("storeId", false);
                getActivity().finish();
            }
        });

        return rootView;
    }


}
