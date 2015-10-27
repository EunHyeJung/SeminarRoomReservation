package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.UserInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by song on 2015-09-24.
 */
public class CustomReservationLVAdapter extends BaseAdapter {
    ArrayList<String> arrayUserList;
    ArrayList<String> arrayRoomList;
    ArrayList<String> arrayStartList;
    ArrayList<String> arrayEndList;
    ArrayList<Integer> arrayNumList;
    ArrayList<String> arrayDate;

    Button btnOkay;
    Button btnCancel;
    RestRequestHelper restRequestHelper;

    static int RESERVATIONOKAY = 1;
    static int RESERVATIONREJECT = 0;

    public CustomReservationLVAdapter() {
        restRequestHelper = RestRequestHelper.newInstance();
        arrayUserList = new ArrayList<String>();
        arrayRoomList = new ArrayList<String>();
        arrayStartList = new ArrayList<String>();
        arrayEndList = new ArrayList<String>();
        arrayNumList = new ArrayList<Integer>();
        arrayDate = new ArrayList<String>();
    }
    @Override
    public int getCount() {
        return arrayDate.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context=parent.getContext();
        ReservationViewHolder reservationViewHolder;
        if(convertView == null){
            //view가 null 일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.custom_listview_reservation_item, parent, false);


            //초기 설정 부분
            init(convertView, position);

            //view holder 설정
            reservationViewHolder = new ReservationViewHolder();
            reservationViewHolder.userId = (TextView)convertView.findViewById(R.id.item_reservation_user);
            reservationViewHolder.room = (TextView)convertView.findViewById(R.id.item_reservation_roomname);
            reservationViewHolder.stime = (TextView)convertView.findViewById(R.id.item_reservation_starttime);
            reservationViewHolder.etime = (TextView)convertView.findViewById(R.id.item_reservation_endtime);
            reservationViewHolder.date = (TextView)convertView.findViewById(R.id.item_reservation_date);

            convertView.setTag(reservationViewHolder);
        }
        else
        {
            reservationViewHolder = (ReservationViewHolder)convertView.getTag();
        }

        //내용 설정
        reservationViewHolder.userId.setText(arrayUserList.get(position));
        reservationViewHolder.room.setText(arrayRoomList.get(position));
        reservationViewHolder.stime.setText(arrayStartList.get(position));
        reservationViewHolder.etime.setText(arrayEndList.get(position));
        reservationViewHolder.date.setText(arrayDate.get(position));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ReservationFormActivity.class));
            }
        });

        //승인 버튼
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restRequestHelper.bookingFilter(arrayNumList.get(position), RESERVATIONOKAY, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        int result = jsonObject.get("result").getAsInt();

                        switch (result) {
                            case 0:
                                Toast.makeText(context, "승인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(context, "승인되었습니다.", Toast.LENGTH_SHORT).show();
                                btnOkay.setText("승인 완료");
                                btnOkay.setClickable(false);
                                btnCancel.setVisibility(View.INVISIBLE);

                                break;
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("failure", error.toString());
                    }
                });
            }
        });

        //거절 버튼
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restRequestHelper.bookingFilter(arrayNumList.get(position), RESERVATIONREJECT, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        int result = jsonObject.get("result").getAsInt();

                        switch (result) {
                            case 0:
                                Toast.makeText(context, "거절되지 않았습니다..", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(context, "거절되었습니다.", Toast.LENGTH_SHORT).show();
                                btnCancel.setText("거절 완료");
                                btnCancel.setClickable(false);
                                btnOkay.setVisibility(View.INVISIBLE);
                                break;
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("failure", error.toString());
                    }
                });
            }
        });

        return convertView;
    }

    public void init(View convertView, final int position){

        //textview에 현재 position의 문자열 추가
        TextView userName = (TextView)convertView.findViewById(R.id.item_reservation_user);
        TextView roomName = (TextView)convertView.findViewById(R.id.item_reservation_roomname);
        TextView startTime = (TextView)convertView.findViewById(R.id.item_reservation_starttime);
        TextView endTime = (TextView)convertView.findViewById(R.id.item_reservation_endtime);
        TextView date = (TextView)convertView.findViewById(R.id.item_reservation_date);

        userName.setText(arrayUserList.get(position));
        roomName.setText(arrayRoomList.get(position));
        startTime.setText(arrayStartList.get(position));
        endTime.setText(arrayEndList.get(position));
        date.setText(arrayDate.get(position));

        btnOkay = (Button)convertView.findViewById(R.id.item_reservokay_button);
        btnCancel = (Button)convertView.findViewById(R.id.item_reservcancel_button);

        if(UserInfo.getUserMode() == 1)
        {
            btnOkay.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        }
    }

    //고유 번호 추가
    public void addNum(int num) { arrayNumList.add(num); }

    //리스트뷰 사용자 추가
    public void addUser(String str) { arrayUserList.add(str); }

    //방 추가
    public void addRoom(String room) { arrayRoomList.add(room); }

    //시작시간 추가
    public void addStartTime(String stime) { arrayStartList.add(stime); }

    //끝 시간 추가
    public void addEndTime(String etime) { arrayEndList.add(etime); }

    //날짜 추가
    public void addDate(String date) { arrayDate.add(date); }
    
    public void clear()
    {
        arrayNumList.clear();
        arrayStartList.clear();
        arrayEndList.clear();
        arrayStartList.clear();
        arrayRoomList.clear();
        arrayDate.clear();
    }

    public class ReservationViewHolder{
        public TextView userId;
        public TextView room;
        public TextView stime;
        public TextView etime;
        public TextView date;
    }

}
