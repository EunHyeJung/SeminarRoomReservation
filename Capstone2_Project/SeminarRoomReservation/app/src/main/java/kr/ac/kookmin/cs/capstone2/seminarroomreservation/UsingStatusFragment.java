package kr.ac.kookmin.cs.capstone2.seminarroomreservation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsingStatusFragment extends Fragment {

    GridView gridViewReservationSchedule;
    ListView listViewReservationTimeData;

    static final String[] initUsingStatus = new String[]{
            "9:00 - 9:30","available", "available", "available", "available", "available",
            "9:30 - 10:00","available", "available", "available", "available", "available",
            "10:00 - 10:30","available", "available", "available", "available", "available",
            "10:30 - 11:00","available", "available", "available", "available", "available",
            "11:00 - 11:30","available", "available", "available", "available", "available",
            "11:30 - 12:00","available", "available", "available", "available", "available",
            "12:00 - 12:30","available", "available", "available", "available", "available",
            "12:30 - 13:00","available", "available", "available", "available", "available",
            "13:00 - 13:30","available", "available", "available", "available", "available",
            "13:30 - 14:00","available", "available", "available", "available", "available",
            "14:00 - 14:30","available", "available", "available", "available", "available",
            "14:30 - 15:00","available", "available", "available", "available", "available",
            "15:00 - 15:30","available", "available", "available", "available", "available",
            "15:30 - 16:00","available", "available", "available", "available", "available",
            "16:00 - 16:30","available", "available", "available", "available", "available",
            "16:30 - 17:00","available", "available", "available", "available", "available",
            "17:00 - 17:30","available", "available", "available", "available", "available",
            "17:30 - 18:00","available", "available", "available", "available", "available",
            "18:00 - 18:30","available", "available", "available", "available", "available",
            "18:30 - 19:00","available", "available", "available", "available", "available",
            "19:00 - 19:30","available", "available", "available", "available", "available",
            "19:30 - 20:00","available", "available", "available", "available", "available",
            "20:00 - 20:30","available", "available", "available", "available", "available",
            "20:30 - 21:00","available", "available", "available", "available", "available"
    };


    public UsingStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_using_status, container, false);

        gridViewReservationSchedule = (GridView) rootView.findViewById(R.id.girdView_usingStatus);
        CustomGridAdapter gridViewAdapter = new CustomGridAdapter(getActivity().getApplicationContext(), initUsingStatus);
        gridViewReservationSchedule.setAdapter(gridViewAdapter);

        return rootView;
    }

}
