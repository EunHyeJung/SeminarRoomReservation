package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model.ReservationsInfo;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model.RoomInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model.DefinedValues.*;
import static kr.ac.kookmin.cs.capstone2.seminarroomreservation.UpdateView.*;

/**
 *   UsingStatusFragment :
 *      Show serminar room using status for the selcted day.
 *      When clicked the gridview cell,
 *          1) user can make a reservation if selected room and time is available
 *          2) user can see the reservation information if selected room and time is reserved.
 */

public class UsingStatusFragment extends Fragment {

    ListView listView;              // listView to show available time list
    GridView gridView;              // gridView to show seminar room using status

    TextView textViewDate;          // textView to show date
    Button buttonDate;              // button to select date

    TextView textViewRoom1;         // textView to show roomName
    TextView textViewRoom2;
    TextView textViewRoom3;
    TextView textViewRoom4;
    TextView textViewRoom5;


    CustomGridAdapter customGridAdapter;


    static String date;
    static int year, month, day;

    Button buttonNext;
    Button buttonBack;

    AccidentListener mCallback;
    static int page = 0;

    HashMap<String, Integer> startTime = new HashMap<String, Integer>();
    HashMap<String, Integer> endTime = new HashMap<String, Integer>();


    public interface AccidentListener {
        public void refershFragment(int page);     // to update fragmentView

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_using_status, container, false);

        findViewByIds(rootView);

        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        date = year + "-" + (month + 1) + "-" + day;
        buttonDate.setOnClickListener(clickListener);

        // show seminar room using status for the selected day
        initView(date);

        // 통신없이 gridView선택시는 예약신청
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // ((TextView) view).getText().toString())
                Intent intent = new Intent(getActivity().getApplicationContext(), ReservationFormActivity.class);
                intent.putExtra("viewMode", REQUEST_MODE);
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }


    private void findViewByIds(View rootView) {
        textViewDate = (TextView) rootView.findViewById(R.id.textView_date);
        textViewRoom1 = (TextView) rootView.findViewById(R.id.testView_room1);
        textViewRoom2 = (TextView) rootView.findViewById(R.id.testView_room2);
        textViewRoom3 = (TextView) rootView.findViewById(R.id.testView_room3);
        textViewRoom4 = (TextView) rootView.findViewById(R.id.testView_room4);
        textViewRoom5 = (TextView) rootView.findViewById(R.id.testView_room5);
        listView = (ListView) rootView.findViewById(R.id.listView_time);
        gridView = (GridView) rootView.findViewById(R.id.girdView_usingStatus);
        buttonNext = (Button) rootView.findViewById(R.id.button_next);
        buttonBack = (Button) rootView.findViewById(R.id.button_back);
        buttonDate = (Button) rootView.findViewById(R.id.button_date);

    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_next:
                    mCallback.refershFragment(page++);
                    break;
                case R.id.button_back:
                    mCallback.refershFragment(--page);
                    break;
                case R.id.button_date:
                    showCaldendarDialog();
                    break;
            }
        }
    };


    public void initView(String date) {
        String[] inputTimeValues = {"09:00 - 09:30", "09:30 - 10:00", "10:00 - 10:30", "10:30 - 11:00",
                "11:00 - 11:30", "11:30 - 12:00", "12:00 - 12:30", "12:30 - 13:00",
                "13:00 - 13:30", "13:30 - 14:00", "14:00 - 14:30", "14:30 - 15:00",
                "15:00 - 15:30", "15:30 - 16:00", "16:00 - 16:30", "16:30 - 17:00",
                "17:00 - 17:30", "17:30 - 18:00", "18:00 - 18:30", "18:30 - 19:00",
                "19:00 - 19:30", "19:30 - 20:00"};

        for (int i = 1; i < inputTimeValues.length; i++) {
            startTime.put(inputTimeValues[i - 1].substring(0, 5), i);
            endTime.put(inputTimeValues[i - 1].substring(8, 13), i);
        }
        int[] roomIds = new int[5];
        for (int i = 0; i < roomIds.length; i++) {
            roomIds[i] = 5 * page + i + 1;
        }
        setTextView(textViewRoom1, RoomInfo.getRoomName(roomIds[0]));
        setTextView(textViewRoom2, RoomInfo.getRoomName(roomIds[1]));
        setTextView(textViewRoom3, RoomInfo.getRoomName(roomIds[2]));
        setTextView(textViewRoom4, RoomInfo.getRoomName(roomIds[3]));
        setTextView(textViewRoom5, RoomInfo.getRoomName(roomIds[4]));
        setTextView(textViewDate, date);



        CustomListAdapter customListAdapter = new CustomListAdapter(getActivity(), inputTimeValues);
        listView.setAdapter(customListAdapter);

        // date for transmitting to server
        final TransmissionData transmissionData = new TransmissionData(date, roomIds);

        // receive date from server
        RestRequestHelper requestHelper = RestRequestHelper.newInstance();
        requestHelper.receiveUsingStatue(transmissionData, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject usingStatusCallback, Response response) {
                JsonElement element = usingStatusCallback.get("responseData");
                if (!(element instanceof JsonNull)) {
                    JsonObject responseData = (JsonObject) element;
                    jsonParsing(responseData);
                } else {
                    ArrayList<Integer> inputValues = new ArrayList<Integer>();
                    for (int i = 0; i < 120; i++) {
                        inputValues.add(i);
                    }
                    customGridAdapter = new CustomGridAdapter(getActivity(), inputValues);
                    gridView.setAdapter(customGridAdapter);
                    gridView.setVerticalScrollBarEnabled(false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }



    public void jsonParsing(JsonObject responseData) {
        JsonArray reservations = responseData.getAsJsonArray("reservation");

        // set gridView cell with using statud date
        ReservationsInfo[] reservationsInfos = new ReservationsInfo[reservations.size()];
        for (int i = 0; i < reservations.size(); i++) {
            reservationsInfos[i] = new ReservationsInfo();
        }
        for (int i = 0; i < reservations.size(); i++) {
            String tempStartTime = (reservations.get(i).getAsJsonObject().getAsJsonPrimitive("startTime").getAsString()).substring(0, 5);
            String tempEndTime = (reservations.get(i).getAsJsonObject().getAsJsonPrimitive("endTime").getAsString()).substring(0, 5);
            int reservationStatus = (reservations.get(i).getAsJsonObject().getAsJsonPrimitive("reservationStatus").getAsInt());
            int reservedRoomId = (reservations.get(i).getAsJsonObject().getAsJsonPrimitive("roomId").getAsInt());
            int reservationId = (reservations.get(i).getAsJsonObject().getAsJsonPrimitive("reservationId").getAsInt());

            int cellStartPosition = (5 * (startTime.get(tempStartTime) - 1)) + (reservedRoomId % 5) - 1; // cell start position
            int cellEndPosition = (5 * (endTime.get(tempEndTime) - 1)) + (reservedRoomId % 5) - 1; // cell end position

            reservationsInfos[i].setReservationsInfo(reservationId, reservationStatus, cellStartPosition, cellEndPosition);
        }

        ArrayList<Integer> inputValues = new ArrayList<Integer>();
        for (int i = 0; i < 120; i++) {
            inputValues.add(i);
        }

        customGridAdapter = new CustomGridAdapter(getActivity(), inputValues, reservationsInfos);
        gridView.setAdapter(customGridAdapter);
        gridView.setVerticalScrollBarEnabled(false);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // ((TextView) view).getText().toString())
                Intent intent = new Intent(getActivity().getApplicationContext(), ReservationFormActivity.class);

                if (((TextView) view).getHint() == null) {  // 2 : 예약 신청 모드
                    intent.putExtra("viewMode", REQUEST_MODE);
                } else {     // getHint() == null
                    int reservationId = Integer.parseInt(((TextView) view).getHint().toString());
                    intent.putExtra("viewMode", VIEW_MODE);  // 1, 예약 조회 모드
                    intent.putExtra("reservationId", reservationId);    // 예약 조회를 위한 예약ID
                }
                getActivity().startActivity(intent);
            }
        });
    }





    public void showCaldendarDialog() {
        CalendarDialog calendarDialog = new CalendarDialog(getActivity());
        calendarDialog.show();
        calendarDialog.setOnDismissListener((DialogInterface.OnDismissListener) getActivity());
    }


    // to update fragment view when user click the next or back button.
    @Override
    public void onResume() {
        super.onResume();

        initView(date);

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
