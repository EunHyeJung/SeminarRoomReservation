package kr.ac.kookmin.cs.capstone2.seminarroomreservation.User;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsingStatusFragment extends Fragment {


    public UsingStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_using_status, container, false);
        return rootView;
    }


}