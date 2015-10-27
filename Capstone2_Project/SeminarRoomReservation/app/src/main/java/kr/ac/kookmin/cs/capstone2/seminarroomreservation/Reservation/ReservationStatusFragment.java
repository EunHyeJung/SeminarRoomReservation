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
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.RoomInfo;
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

    TransmissionUserInfo info;

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

       if(UserInfo.getUserMode() == 1){
            btnDate.setVisibility(View.GONE);
            btnAll.setVisibility(View.GONE);
        }

        date = "ALL";

        info = new TransmissionUserInfo(UserInfo.getId());
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
                    Log.e("Retrofit Error : ", error.toString());
                }
            });
        }
    }

    /**
     * ListView에 예약 현황을 추가하는 함수
     * */
    public void addList(JsonObject jsonObject) {
        try {
            JsonObject responseData = jsonObject.getAsJsonObject("responseData");
            JsonArray requestList = responseData.getAsJsonArray("requestList");

            for (int i = 0; i < requestList.size(); i++) {
                JsonObject tmpObject = requestList.get(i).getAsJsonObject();
                int mode = UserInfo.getUserMode();

                //add 작업
                if(mode == 1)//일반 사용자의 경우
                {
                    String roomId = RoomInfo.getRoomName(tmpObject.getAsJsonPrimitive("roomId").getAsInt());
                    reservationLVAdapter.addUser(UserInfo.getUserId());
                    reservationLVAdapter.addRoom(roomId.replace("\"",""));
                }else{//관리자의 경우
                    reservationLVAdapter.addUser(tmpObject.getAsJsonPrimitive("userId").getAsString());
                    reservationLVAdapter.addRoom(tmpObject.getAsJsonPrimitive("roomId").getAsString());
                }

                //공통 항목
                reservationLVAdapter.addNum(tmpObject.getAsJsonPrimitive("reservationId").getAsInt());
                reservationLVAdapter.addStartTime(tmpObject.getAsJsonPrimitive("startTime").getAsString());
                reservationLVAdapter.addEndTime(tmpObject.getAsJsonPrimitive("endTime").getAsString());
                reservationLVAdapter.addDate(tmpObject.getAsJsonPrimitive("date").getAsString());
            }

            reservationLVAdapter.notifyDataSetChanged();// 데이터 변경

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
