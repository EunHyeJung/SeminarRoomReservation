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

import java.util.ArrayList;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;

/**
 * Created by song on 2015-09-24.
 */
public class CustomReservationLVAdapter extends BaseAdapter {
    ArrayList<String> arrayListReservation;
    Button btnReservation;
    RestRequestHelper restRequestHelper;

    public CustomReservationLVAdapter() { arrayListReservation = new ArrayList<String>(); }
    @Override
    public int getCount() {
        return arrayListReservation.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListReservation.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context=parent.getContext();
        if(convertView == null){
            //view가 null 일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.custom_listview_item,parent,false);

            //초기 설정 부분
            init(convertView, position);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ReservationFormActivity.class));
            }
        });

        btnReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    public void init(View convertView, final int position){
        restRequestHelper = RestRequestHelper.newInstance();

        //textview에 현재 position의 문자열 추가
        TextView reservationText=(TextView)convertView.findViewById(R.id.SeminarListText);
        reservationText.setText(arrayListReservation.get(position));

        btnReservation = (Button)convertView.findViewById(R.id.btn_SeminarControl);
        btnReservation.setText("승인");
    }

    //리스트뷰 내용 추가
    public void add(String str) { arrayListReservation.add(str); }

}
