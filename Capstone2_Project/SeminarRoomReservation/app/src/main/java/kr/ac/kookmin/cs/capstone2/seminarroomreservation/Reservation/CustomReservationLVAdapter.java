package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Vector;

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
    Vector<Reservation> reservList;
    Reservation reservation;

    Button btnOkay;
    Button btnDeny;
    Button btnCancel;
    RestRequestHelper restRequestHelper;

    static int RESERVATIONOKAY = 1;
    static int RESERVATIONREJECT = 0;

    public CustomReservationLVAdapter() {
        restRequestHelper = RestRequestHelper.newInstance();

        reservation = new Reservation();
        reservList = new Vector<Reservation>();
    }
    @Override
    public int getCount() {
        return reservList.size();
    }

    @Override
    public Object getItem(int position) {
        return reservList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context=parent.getContext();
        final ReservationViewHolder reservationViewHolder;
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
            reservationViewHolder.status = (TextView)convertView.findViewById(R.id.item_reservation_status);

            convertView.setTag(reservationViewHolder);
        }
        else
        {
            reservationViewHolder = (ReservationViewHolder)convertView.getTag();
        }

        //내용 설정
        reservationViewHolder.position = position;
        reservationViewHolder.userId.setText(reservList.get(position).reserveId);
        reservationViewHolder.room.setText(reservList.get(position).room);
        reservationViewHolder.stime.setText(reservList.get(position).stime);
        reservationViewHolder.etime.setText(reservList.get(position).etime);
        reservationViewHolder.date.setText(reservList.get(position).date);
        reservationViewHolder.status.setText(reservList.get(position).status);

        //리스트뷰 항목 클릭시
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReservationFormActivity.class);
                intent.putExtra("id", reservList.get(reservationViewHolder.position).reserveId); //사용자 고유 id 값을 보낸다.
                intent.putExtra("request", 1);
                context.startActivity(intent);
            }
        });

        //승인 버튼
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restRequestHelper.bookingFilter(reservList.get(reservationViewHolder.position).reserveId, RESERVATIONOKAY, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        int result = jsonObject.get("result").getAsInt();

                        switch (result) {
                            case 1 :
                                Toast.makeText(context, "승인되었습니다.", Toast.LENGTH_SHORT).show();
                                break;

                            default ://0
                                Toast.makeText(context, "승인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restRequestHelper.bookingFilter(reservList.get(reservationViewHolder.position).reserveId, RESERVATIONREJECT, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        int result = jsonObject.get("result").getAsInt();

                        switch (result) {
                            case 1:
                                Toast.makeText(context, "거절되었습니다.", Toast.LENGTH_SHORT).show();
                                break;

                            default ://0
                                Toast.makeText(context, "거절되지 않았습니다..", Toast.LENGTH_SHORT).show();
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

        //예약 취소 버튼
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //해당 부분의 값을 삭제한다.
                TransmissionReservation transmissionReservation = new TransmissionReservation(reservList.get(reservationViewHolder.position).reserveId);

                restRequestHelper.cancelBooking(transmissionReservation, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        JsonObject responseData = jsonObject.getAsJsonObject("responseData");
                        int result = responseData.get("result").getAsInt();

                        switch (result) {
                            case 1 :
                                Toast.makeText(context, "예약이 취소되었습니다.",Toast.LENGTH_SHORT).show();
                                break;

                            default ://-1
                                Toast.makeText(context, "예약이 취소되지 않았습니다.",Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("CRA", error.toString());
                    }
                });
            }
        });

        return convertView;
    }

    //초기화
    public void init(View convertView, final int position){

        //textview에 현재 position의 문자열 추가
        TextView userName = (TextView)convertView.findViewById(R.id.item_reservation_user);
        TextView roomName = (TextView)convertView.findViewById(R.id.item_reservation_roomname);
        TextView startTime = (TextView)convertView.findViewById(R.id.item_reservation_starttime);
        TextView endTime = (TextView)convertView.findViewById(R.id.item_reservation_endtime);
        TextView date = (TextView)convertView.findViewById(R.id.item_reservation_date);
        TextView status = (TextView)convertView.findViewById(R.id.item_reservation_status);

        userName.setText(reservList.get(position).user);
        roomName.setText(reservList.get(position).room);
        startTime.setText(reservList.get(position).stime);
        endTime.setText(reservList.get(position).etime);
        date.setText(reservList.get(position).date);
        status.setText(reservList.get(position).status);

        btnOkay = (Button)convertView.findViewById(R.id.item_reservokay_button);
        btnDeny = (Button)convertView.findViewById(R.id.item_reservation_deny_button);
        btnCancel = (Button)convertView.findViewById(R.id.item_reservation_cancel_button);

        if(UserInfo.getUserMode() == 1)//일반 사용자, 예약 확인과 예약 거절 버튼을 볼 수 없게 한다.
        {
            btnOkay.setVisibility(View.GONE);
            btnDeny.setVisibility(View.GONE);
        }
        else //관리자, 예약 취소 버튼은 볼 수 없게 한다.
        {
            btnCancel.setVisibility(View.GONE);
        }
    }

    //ReservationStatusFragment에서 추가
    public void add(int id, String user, String room, String stime, String etime, String date, int status){
        reservation.reserveId = id;
        reservation.user = user;
        reservation.room = room;
        reservation.stime = stime;
        reservation.etime = etime;
        reservation.date = date;
        reservation.status = status;

        reservList.add(reservation); //벡터에 추가
    }

    //객체 파일
    public class Reservation{
        public int reserveId; //
        public String user;
        public String room;
        public String stime;
        public String etime;
        public String date;
        public int status;
    }

    //뷰홀더
    public class ReservationViewHolder{
        public int position;
        public TextView userId;
        public TextView room;
        public TextView stime;
        public TextView etime;
        public TextView date;
        public TextView status;
    }

}
