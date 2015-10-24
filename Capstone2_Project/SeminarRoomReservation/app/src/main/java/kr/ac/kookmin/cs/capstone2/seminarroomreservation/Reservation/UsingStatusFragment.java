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
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.ReservationsInfo;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.RoomInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsingStatusFragment extends Fragment {

    ListView listView;              // 스케줄표에서 시간나타내는 리스트뷰
    GridView gridView;              // 스케줄표에서 예약현황나타내는 그리드뷰

    TextView textViewRoom1;         // 방 데이터를 나타내어주는 텍스트뷰
    TextView textViewRoom2;
    TextView textViewRoom3;
    TextView textViewRoom4;
    TextView textViewRoom5;

    CustomGridAdapter customGridAdapter;    // 커스텀 그리드뷰 사용을 위함.

    TextView textViewDate;
    Button buttonDate;

    static String date;
    int year, month, day;

    Button buttonNext;
    Button buttonBack;

    AccidentListener mCallback;
    static int page = 0;

    HashMap<String, Integer> startTime = new HashMap<String, Integer>();
    HashMap<String, Integer> endTime = new HashMap<String, Integer>();


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

        buttonBack = (Button) rootView.findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.refershFragment(--page);
            }
        });


        //date="2015-10-10";
        try {
            init(date);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        buttonDate = (Button) rootView.findViewById(R.id.button_date);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCaldendarDialog();
            }
        });


        return rootView;
    }

    public void init(String date) throws JSONException {

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
        // date = "2015-10-10";        // 예약확인 위해 임의 날짜 설정, 나중에 삭제할 것.


        int[] roomIds = new int[5];
        for (int i = 0; i < roomIds.length; i++) {
            roomIds[i] = 5 * page + i + 1;
        }


        textViewRoom1.setText(getRoomName(roomIds[0]));
        textViewRoom2.setText(getRoomName(roomIds[1]));
        textViewRoom3.setText(getRoomName(roomIds[2]));
        textViewRoom4.setText(getRoomName(roomIds[3]));
        textViewRoom5.setText(getRoomName(roomIds[4]));
        textViewDate.setText(date);

        CustomListAdapter customListAdapter = new CustomListAdapter(getActivity(), inputTimeValues);
        listView.setAdapter(customListAdapter);

        // 서버로 예약 현황 받아 올 roomId 값들 전송
        final TransmissionData transmissionData = new TransmissionData(date, roomIds);

        // 서버로부터 데이터를 받아옴
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
    /*      End of Init         */


    /*      Start Of jsonParsing         */
    public void jsonParsing(JsonObject responseData) {
        JsonArray reservations = responseData.getAsJsonArray("reservation");

        // 예약 현황 데이터를 받아와서, gridView에 set
        ReservationsInfo[] reservationsInfos = new ReservationsInfo[reservations.size()];
        for (int i = 0; i < reservations.size(); i++) {
            reservationsInfos[i] = new ReservationsInfo();
        }

        for (int i = 0; i < reservations.size(); i++) {
            String tempStartTime = (reservations.get(i).getAsJsonObject().getAsJsonPrimitive("startTime").getAsString()).substring(0, 5);
            String tempEndTime = (reservations.get(i).getAsJsonObject().getAsJsonPrimitive("endTime").getAsString()).substring(0, 5);
            int reservationStatus = (reservations.get(i).getAsJsonObject().getAsJsonPrimitive("reservationStatus").getAsInt());
            int reservedRoomId = (reservations.get(i).getAsJsonObject().getAsJsonPrimitive("roomId").getAsInt());
            int temp1 = (5 * (startTime.get(tempStartTime) - 1)) + (reservedRoomId % 5) - 1; // 시작시간
            int temp2 = (5 * (endTime.get(tempEndTime) - 1)) + (reservedRoomId % 5) - 1; // 끝나는 시간
            reservationsInfos[i].setReservationsInfo(reservationStatus, temp1, temp2);
        }

        // 한번 검토, for문의 과정이 꼭 필요할까??
        ArrayList<Integer> inputValues = new ArrayList<Integer>();
        for (int i = 0; i < 120; i++) {
            inputValues.add(i);
        }
        customGridAdapter = new CustomGridAdapter(getActivity(), inputValues, reservationsInfos);
        gridView.setAdapter(customGridAdapter);
        gridView.setVerticalScrollBarEnabled(false);
    }


    public String getRoomName(int i) {
        String roomName="";
        if(RoomInfo.getRoomName(i) != null)
            roomName = RoomInfo.getRoomName(i).substring(1, 4);
        return roomName;
    }

    public void showCaldendarDialog() {
        CalendarDialog calendarDialog = new CalendarDialog(getActivity());
        calendarDialog.show();
        calendarDialog.setOnDismissListener((DialogInterface.OnDismissListener) getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            init(date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
