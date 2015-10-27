package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Iterator;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.RoomInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccessControlFragment extends Fragment {
    ListView SeminarList;
    CustomControlLVAdapter listAdapter;
    RestRequestHelper requestHelper;

    public AccessControlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_aceess_control, container, false);
        requestHelper=RestRequestHelper.newInstance();
        SeminarList = (ListView)view.findViewById(R.id.SeminarList);
        listAdapter = new CustomControlLVAdapter();

        SeminarList.setAdapter(listAdapter);

        addSeminarList(); // 리스트에 방 내용 추가

        //뷰를 돌려준다.
        return view;
    }

    //리스트에 방 내용 추가
    public void addSeminarList(){
        HashMap room = RoomInfo.roomNames;
        Iterator<Integer> iterator = room.keySet().iterator();

        while(iterator.hasNext()){
            int key = iterator.next();
            listAdapter.addRoomId(key);
            listAdapter.addRoomName(room.get(key).toString().replace("\"",""));
        }
    }
}