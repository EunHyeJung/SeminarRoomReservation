package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;




import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.GridView;
import android.widget.Toast;


import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReservationStatusFragment extends Fragment {


    public ReservationStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_reservation_status, container, false);


        return rootView;
    }

    /*  end of onCreateView  */
/*
    @Override
    protected Dialog onCreateDialog(int id){
        if(id == DIALOG_ID) {
            return new DatePickerDialog(getActivity(), dpickerListenr,
                    yearX, monthX, dayX);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListenr
            = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
            yearX = year;
            monthX = monthOfYear + 1;
            dayX = dayOfMonth;
            Toast.makeText(getActivity().getApplicationContext(), yearX+"/"+monthX+"/"+dayX,
                    Toast.LENGTH_LONG).show();
        }
    };*/


}
