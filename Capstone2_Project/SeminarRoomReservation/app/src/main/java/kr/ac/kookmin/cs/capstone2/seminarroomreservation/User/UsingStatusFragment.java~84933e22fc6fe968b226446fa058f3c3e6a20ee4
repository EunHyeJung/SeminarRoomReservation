package kr.ac.kookmin.cs.capstone2.seminarroomreservation.User;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;


import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Authentication.MainActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.CustomGridAdapter;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.CustomListAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsingStatusFragment extends Fragment  {

    GridView gridViewReservationSchedule;
    ListView listViewReservationTimeData;
    RestRequestHelper requestHelper = RestRequestHelper.newInstance();

    private Button buttonDateChoice;
    private TextView textViewDate;

    public static int year;
    private static int monthOfYear;
    private static int dayOfMonth;


    static final String[] initUsingStatus = new String[]{
            "available", "available", "available", "available", "available",
            "available", "available", "available", "available", "available",
     };

    static final String[] initUsingStatusTime= new String[]{
            "9:00 - 9:30",  "9:30 - 10:00", "10:00 - 10:30", "10:30 - 11:00",  "11:00 - 11:30", "11:30 - 12:00",
            "12:00 - 12:30",  "12:30 - 13:00",   "13:00 - 13:30",  "13:30 - 14:00",  "14:00 - 14:30", "14:30 - 15:00"
    };

    public UsingStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_using_status, container, false);



        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        monthOfYear = cal.get(Calendar.MONTH)+1;
        dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        textViewDate = (TextView) rootView.findViewById(R.id.textView_date);
        buttonDateChoice = (Button) rootView.findViewById(R.id.button_date);
        buttonDateChoice.setOnClickListener(new View.OnClickListener() {                                // 날짜 선택
            @Override
            public void onClick(View v) {
                MyDialogFragment frag = MyDialogFragment.newInstance();
                frag.show(getFragmentManager(), "TAG");
           }
        });


        textViewDate.setText(monthOfYear+"월 "+dayOfMonth+" 일 ("+getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK))+")");

        gridViewReservationSchedule = (GridView) rootView.findViewById(R.id.girdView_usingStatus);
        CustomGridAdapter gridViewAdapter = new CustomGridAdapter(getActivity().getApplicationContext(), initUsingStatus);
        gridViewReservationSchedule.setAdapter(gridViewAdapter);


        ListView listViewTime = (ListView) rootView.findViewById(R.id.listView_time);
        CustomListAdapter listViewAdapter = new CustomListAdapter(getActivity().getApplicationContext(), initUsingStatusTime);
        listViewTime.setAdapter(listViewAdapter);

        return rootView;
    }
    /* End of onCreateView */


    public static class MyDialogFragment extends DialogFragment {
        public static MyDialogFragment newInstance(){
            return new MyDialogFragment();
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), callBack, year, monthOfYear,dayOfMonth);
            return dialog;
        }

        DatePickerDialog.OnDateSetListener callBack =
                new DatePickerDialog.OnDateSetListener() {
              @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                  UsingStatusFragment.year = year;
                  UsingStatusFragment.monthOfYear = monthOfYear;
                  UsingStatusFragment.dayOfMonth = dayOfMonth;


                    }
                };
   }

    /*    End Of MyDialogFragment   */



    public String getDayOfWeek(int n){
        switch(n) {
            case 0:
                return "일";
            case 1:
                return "월";
            case 2:
                return "화";
            case 3:
                return "수";
            case 4:
                return "목";
            case 5:
                return "금";
            case 6:
                return "토";
        }
        return null;
    }


}