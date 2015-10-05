package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.RoomInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsingStatusFragment extends Fragment {

    ListView listView;
    GridView gridView;

    TextView textViewRoom1;
    TextView textViewRoom2;
    TextView textViewRoom3;
    TextView textViewRoom4;
    TextView textViewRoom5;

    CustomGridAdapter customGridAdapter;

    TextView textViewDate;
    Button buttonDate;

    static String date;
    int year, month, day;

    Button buttonNext;

    AccidentListener mCallback;
    static int page = 0;

    public interface AccidentListener {
        void refershFragment(int page);
    }

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
        date = year + "-" + (month + 1) + "-" + day;
        textViewDate = (TextView) rootView.findViewById(R.id.textView_date);


        textViewRoom1 = (TextView) rootView.findViewById(R.id.testView_room1);
        textViewRoom2 = (TextView) rootView.findViewById(R.id.testView_room2);
        textViewRoom3 = (TextView) rootView.findViewById(R.id.testView_room3);
        textViewRoom4 = (TextView) rootView.findViewById(R.id.testView_room4);
        textViewRoom5 = (TextView) rootView.findViewById(R.id.testView_room5);


        listView = (ListView) rootView.findViewById(R.id.listView_time);
        gridView = (GridView) rootView.findViewById(R.id.girdView_usingStatus);

        buttonNext = (Button) rootView.findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.refershFragment(page++);
            }
        });

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

        String[] inputTimeValues = {"09:00 - 09:30", "09:30 - 10:00", "10:00 - 10:30", "10:30 - 11:00",
                "11:00 - 11:30", "11:30 - 12:00", "12:00 - 12:30", "12:30 - 13:00",
                "13:00 - 13:30", "13:30 - 14:00", "14:00 - 14:30", "14:30 - 15:00",
                "15:00 - 15:30", "15:30 - 16:00", "16:00 - 16:30", "16:30 - 17:00",
                "17:00 - 17:30", "17:30 - 18:00", "18:00 - 18:30", "18:30 - 19:00"};

        String[] inputRoomNames = RoomInfo.roomNames;          // 방이름 데이터를 가져옴.


        textViewRoom1.setText(inputRoomNames[5 * page + 0]);
        textViewRoom2.setText(inputRoomNames[5 + page + 1]);
        textViewRoom3.setText(inputRoomNames[5 * page + 2]);
        textViewRoom4.setText(inputRoomNames[5 * page + 3]);
        textViewRoom5.setText(inputRoomNames[5 * page + 4]);


        textViewDate.setText(date);
        CustomListAdapter customListAdapter = new CustomListAdapter(getActivity(), inputTimeValues);
        listView.setAdapter(customListAdapter);


        // 서버로부터 데이터를 받아옴
        RestRequestHelper requestHelper = RestRequestHelper.newInstance();
        requestHelper.receiveUsingStatue(date, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject usingStatusCallback, Response response) {
                try {
                    jsonParsing(usingStatusCallback);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });


    }
    /*      End of Init         */


    public void jsonParsing(JsonObject jsonObject) throws JSONException {
        JsonObject responseData = jsonObject.getAsJsonObject("responseData");
        JsonArray reservations = responseData.getAsJsonArray("reservation");

        // 예약 현황 데이터를 받아와서, gridView에 set.
        for (int i = 0; i < reservations.size(); i++) {

            //reservations.get(i).getAsJsonObject().getAsJsonPrimitive("roomId"));
        }

        // 추후삭제
        ArrayList<Integer> inputValues = new ArrayList<Integer>();

        for (int i = 0; i < 120; i++) {
            inputValues.add(i);
        }
        // 추후삭제

        customGridAdapter = new CustomGridAdapter(getActivity(), inputValues);
        gridView.setAdapter(customGridAdapter);
        gridView.setVerticalScrollBarEnabled(false);


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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (AccidentListener) activity;
        } catch (ClassCastException e) {

        }

    }
}
