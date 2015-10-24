package kr.ac.kookmin.cs.capstone2.seminarroomreservation.User;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
public class KeyFragment extends Fragment {
    public KeyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_key, container, false);
    }

}
