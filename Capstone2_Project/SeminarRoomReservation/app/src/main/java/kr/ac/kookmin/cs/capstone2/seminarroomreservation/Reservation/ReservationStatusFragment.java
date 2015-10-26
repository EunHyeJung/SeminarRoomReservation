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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager.AccessHistoryFragment;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.UserInfo;
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

    JsonObject info;

    public ReservationStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_reservation_status, container, false);
        init(view);//초기화 작업

        restRequestHelper = RestRequestHelper.newInstance(); // 네트워크 객체 생성

        getReservationData(); // 예약 데이터 가져오기

        //전체 보기 버튼을 눌렀을 때
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = "ALL";
                Log.d("RSF date1", date);
                getReservationData();
            }
        });

        //날짜별 보기 버튼을 눌렀을 때
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

        btnDate = (Button)view.findViewById(R.id.btn_reservation_date);
        btnAll = (Button)view.findViewById(R.id.btn_reservation_showall);

        date = "ALL";

        info = new JsonObject();
        jsonInfo(info);

    }

    public void getReservationData(){
        reservationLVAdapter = new CustomReservationLVAdapter();//리스트뷰 초기화
        reservationListView.setAdapter(reservationLVAdapter);

        if(UserInfo.getUserMode() == 2) //관리자
        {
            restRequestHelper.requestList(date, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    addList(jsonObject);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("Retrofit Error : ", error.toString());
                }
            });
        }
        else // id ==1 , 일반
        {
            restRequestHelper.myBooking(info, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    addList(jsonObject);
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }

    public void addList(JsonObject jsonObject) {
        try {
            JsonObject responseData = jsonObject.getAsJsonObject("responseData");
            JsonArray requestList = responseData.getAsJsonArray("requestList");

            for (int i = 0; i < requestList.size(); i++) {
                //requestList.get(i).getAsJsonObject() 변수로 빼서 사용
                JsonObject tmpObject = requestList.get(i).getAsJsonObject();

                //add 작업
                reservationLVAdapter.addNum(tmpObject.getAsJsonPrimitive("reservationId").getAsInt());
                reservationLVAdapter.addUser(tmpObject.getAsJsonPrimitive("userId").getAsString());
                reservationLVAdapter.addRoom(tmpObject.getAsJsonPrimitive("roomId").getAsString());
                reservationLVAdapter.addStartTime(tmpObject.getAsJsonPrimitive("startTime").getAsString());
                reservationLVAdapter.addEndTime(tmpObject.getAsJsonPrimitive("endTime").getAsString());
                reservationLVAdapter.addDate(tmpObject.getAsJsonPrimitive("date").getAsString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        reservationLVAdapter.notifyDataSetChanged();// 데이터 변경
    }

    public void jsonInfo(JsonObject info){
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("Id", UserInfo.getUserId());
        jsonObject.addProperty("date", "ALL");

        info.add("info", jsonObject);

        Log.d("RSF", info.toString());
    }
}
