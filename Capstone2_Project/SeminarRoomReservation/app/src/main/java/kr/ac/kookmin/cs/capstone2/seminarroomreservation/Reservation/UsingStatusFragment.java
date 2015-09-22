package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsingStatusFragment extends Fragment {

    ListView listView;
    GridView gridView;

    TextView textViewDate;
    TextView textViewEmpty;
    Button buttonDate;

    static String date;
    int year, month, day;

    public UsingStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_using_status, container, false);

        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        date = year + " / " + (month + 1) + " / " + day;
        textViewDate = (TextView) rootView.findViewById(R.id.textView_date);

        listView = (ListView) rootView.findViewById(R.id.listView_time);

        gridView = (GridView) rootView.findViewById(R.id.girdView_usingStatus);


        init(date);

        buttonDate = (Button) rootView.findViewById(R.id.button_date);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCaldendarDialog();
            }
        });


        return rootView;
    }


    public void init(String date) {

        String[] inputTimeValues = {"09:00 - 09:30", "09:30 - 10:00", "10:00 - 10:30", "10:30 - 11:00", "11:00 - 11:30", "11:30 - 12:00",
                "12:00 - 12:30", "12:30 - 13:00", "13:00 - 14:30", "14:30 - 15:00", "15:00 - 16:30", "16:30 - 17:00",
                "17:00 - 17:30", "17:30 - 18:00", "18:00 - 18:30", "18:30 - 19:00", "19:00 - 19:30", "19:30 - 20:00",
                "20:00 - 20:30", "20:30 - 21:00"
        };
        ArrayList<String > inputValues = new ArrayList<String>();
        for(int i=0 ; i<120 ; i++){
            inputValues.add("x");
        }
        CustomGridAdapter customGridAdapter = new CustomGridAdapter(getActivity(), inputValues);
        gridView.setAdapter(customGridAdapter);
        gridView.setVerticalScrollBarEnabled(false);


        textViewDate.setText(date);
        CustomListAdapter customListAdapter = new CustomListAdapter(getActivity(), inputTimeValues);
        listView.setAdapter(customListAdapter);

        RestRequestHelper requestHelper = RestRequestHelper.newInstance();
        requestHelper.receiveUsingStatue(date, new Callback<JSONObject>() {
            @Override
            public void success(JSONObject signUpCallback, Response response) {
                System.out.println("receivewUsingStatus"+response);
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void showCaldendarDialog() {
        CalendarDialog calendarDialog = new CalendarDialog(getActivity());
        calendarDialog.show();
        calendarDialog.setOnDismissListener((DialogInterface.OnDismissListener) getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        init(date);
    }
}
