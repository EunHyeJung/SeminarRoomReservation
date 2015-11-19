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

    Button btnOkay;
    Button btnDeny;
    Button btnCancel;
    RestRequestHelper restRequestHelper;

    static final int RESERVATIONOKAY = 1;
    static final int RESERVATIONREJECT = 0;

    public CustomReservationLVAdapter() {
        restRequestHelper = RestRequestHelper.newInstance();

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
            reservationViewHolder.user = (TextView)convertView.findViewById(R.id.item_reservation_user);
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
        reservationViewHolder.setHolder(position, reservList);

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
                restRequestHelper.bookingFilter(reservList.get(reservationViewHolder.getPosition()).reserveId, RESERVATIONOKAY, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        int result = jsonObject.get("result").getAsInt();

                        switch (result) {
                            case RESERVATIONOKAY :
                                Toast.makeText(context, R.string.reservation_approval_okay, Toast.LENGTH_SHORT).show();
                                break;

                            default ://0
                                Toast.makeText(context, R.string.reservation_approval_fail, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("CRL failure", error.toString());
                    }
                });
            }
        });

        //거절 버튼
        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restRequestHelper.bookingFilter(reservList.get(reservationViewHolder.getPosition()).reserveId, RESERVATIONREJECT, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        int result = jsonObject.get("result").getAsInt();

                        switch (result) {
                            case RESERVATIONOKAY:
                                Toast.makeText(context, R.string.reservation_deny_okay, Toast.LENGTH_SHORT).show();
                                btnOkay.setVisibility(View.GONE);
                                btnDeny.setVisibility(View.GONE);
                                break;

                            default ://0
                                Toast.makeText(context, R.string.reservation_deny_fail, Toast.LENGTH_SHORT).show();
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
                TransmissionReservation transmissionReservation = new TransmissionReservation(reservList.get(reservationViewHolder.getPosition()).reserveId);

                restRequestHelper.cancelBooking(transmissionReservation, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        JsonObject responseData = jsonObject.getAsJsonObject("responseData");
                        int result = responseData.get("result").getAsInt();

                        switch (result) {
                            case RESERVATIONOKAY :
                                Toast.makeText(context, R.string.reservation_cancel_okay,Toast.LENGTH_SHORT).show();
                                btnCancel.setVisibility(View.GONE);
                                break;

                            default ://-1
                                Toast.makeText(context, R.string.reservation_cancel_fail,Toast.LENGTH_SHORT).show();
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

        String tmp = setStatus(reservList.get(position).status);
        status.setText(tmp);

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
        Reservation reservation = new Reservation();
        reservation.add(id, user, room, stime, etime, date, status);//객체에 내용물 추가
        reservList.add(reservation); //벡터에 추가

    }

    //벡터내 데이터 구성물
    class Reservation{
        int reserveId;
        String user;
        String room;
        String stime;
        String etime;
        String date;
        int status;

        //객체에 값 설정
        public void add(int id, String user, String room, String stime, String etime, String date, int status){
            reserveId = id;
            this.user = user;
            this.room = room;
            this.stime = stime;
            this.etime = etime;
            this.date = date;
            this.status = status;
        }

    }

    //뷰홀더
    class ReservationViewHolder{
        int position;
        int reservationId;
        TextView user;
        TextView room;
        TextView stime;
        TextView etime;
        TextView date;
        TextView status;

        //홀더 데이터 설정
        public void setHolder(int pos, Vector<Reservation> reserv){
            position = pos;

            reservationId = reserv.get(pos).reserveId;
            user.setText(reserv.get(pos).user);
            room.setText(reserv.get(pos).room);
            stime.setText(reserv.get(pos).stime);
            etime.setText(reserv.get(pos).etime);
            date.setText(reserv.get(pos).date);

            String tmp = setStatus(reserv.get(pos).status);
            status.setText(tmp);
        }

        //position 값 리턴
        public int getPosition(){ return position; }
    }

    public String setStatus(int status){
        String tmp="";
        if(status == 0)
            tmp = "거절";
        else if(status == 1)
            tmp = "승인";
        else
            tmp = "대기";

        return tmp;
    }

}