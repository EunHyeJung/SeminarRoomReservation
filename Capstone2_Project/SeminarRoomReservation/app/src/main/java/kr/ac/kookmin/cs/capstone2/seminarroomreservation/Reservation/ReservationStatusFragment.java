package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;


import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager.AccessHistoryFragment;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReservationStatusFragment extends Fragment {
    ListView reservationListView;
    CustomReservationLVAdapter reservationLVAdapter;
    RestRequestHelper restRequestHelper;
    CalendarDialog calendarDialog;
    String date;

    Button btnDate;
    Button btnAll;

    public ReservationStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_reservation_status, container, false);
        init(view);//초기화 작업

        restRequestHelper = RestRequestHelper.newInstance(); // 네트워크 객체 생성

        getReservationData();

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReservationData();
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarDialog.show();
                calendarDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        calendarDialog.dismiss();
                        date = AccessHistoryFragment.date;
                        btnDate.setText(date);
                        getReservationData();
                    }
                });
            }
        });



        //뷰를 리턴한다.
        return view;
    }

    //초기화 작업
    public void init(View view)
    {
        calendarDialog = new CalendarDialog(getContext());

        //레이아웃 매핑
        reservationListView = (ListView)view.findViewById(R.id.listview_reservation);
        reservationLVAdapter = new CustomReservationLVAdapter();
        reservationListView.setAdapter(reservationLVAdapter);

        btnDate = (Button)view.findViewById(R.id.btn_reservation_date);
        btnAll = (Button)view.findViewById(R.id.btn_reservation_showall);

        date = "ALL";

    }

    public void getReservationData(){
        reservationLVAdapter.clear();
        reservationLVAdapter.notifyDataSetChanged();
        restRequestHelper.requestList(date, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                try {
                    Log.d("success", "retrofit");
                    JsonObject responseData = jsonObject.getAsJsonObject("responseData");
                    JsonArray requestList = responseData.getAsJsonArray("requestList");

                    Log.d("requestList", requestList.toString());
                    Log.d("request size", requestList.size()+"");
                    for (int i = 0; i < requestList.size(); i++) {
                        reservationLVAdapter.addNum(requestList.get(i).getAsJsonObject().getAsJsonPrimitive("reservationId").getAsInt());
                        reservationLVAdapter.addUser(requestList.get(i).getAsJsonObject().getAsJsonPrimitive("userId").getAsString());
                        reservationLVAdapter.addRoom(requestList.get(i).getAsJsonObject().getAsJsonPrimitive("roomId").getAsString());
                        reservationLVAdapter.addStartTime(requestList.get(i).getAsJsonObject().getAsJsonPrimitive("startTime").getAsString());
                        reservationLVAdapter.addEndTime(requestList.get(i).getAsJsonObject().getAsJsonPrimitive("endTime").getAsString());
                        reservationLVAdapter.addDate(requestList.get(i).getAsJsonObject().getAsJsonPrimitive("date").getAsString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                reservationLVAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Retrofit Error : ", error.toString());
            }
        });
    }
}
