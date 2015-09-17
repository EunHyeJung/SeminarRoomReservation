package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReservationStatusFragment extends Fragment {

    GridView gridViewReservationSchedule;
    private String[] itemList;

    public ReservationStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_using_status, container, false);

        CustomGridAdapter adapter = new CustomGridAdapter(getActivity().getApplicationContext(), itemList);
       gridViewReservationSchedule = (GridView) rootView.findViewById(R.id.girdView_usingStatus);


        return rootView;
    }

    public void initUsingStatus(){
        for(int i=0 ; i<=50 ; i++){

        }
    }


}
