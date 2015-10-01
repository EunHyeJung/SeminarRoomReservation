package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReservationStatusFragment extends Fragment {
    ListView reservationListView;
    CustomReservationLVAdapter reservationLVAdapter;
    RestRequestHelper restRequestHelper;

    public ReservationStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_reservation_status, container, false);
        init(view);//초기화 작업

        reservationLVAdapter.add("test");
        reservationLVAdapter.notifyDataSetChanged();

        //뷰를 리턴한다.
        return view;
    }

    //초기화 작업
    public void init(View view)
    {
        restRequestHelper = RestRequestHelper.newInstance(); // 네트워크 객체 생성

        //레이아웃 매핑
        reservationListView = (ListView)view.findViewById(R.id.listview_reservation);
        reservationLVAdapter = new CustomReservationLVAdapter();
        reservationListView.setAdapter(reservationLVAdapter);
    }

}
