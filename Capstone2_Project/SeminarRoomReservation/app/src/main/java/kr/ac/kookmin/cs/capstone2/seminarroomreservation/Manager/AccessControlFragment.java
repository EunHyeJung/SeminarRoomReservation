package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
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
        listAdapter=new CustomControlLVAdapter();

        SeminarList.setAdapter(listAdapter);

        addSeminarList();

        //뷰를 돌려준다.
        return view;
    }

    public void addSeminarList(){
        requestHelper.roomList("admin",new Callback<String>() {

            @Override
            public void success(String s, Response response) {
                String tmp[]=s.split("&");
                for(int i=0;i<tmp.length;i++) {
                    Log.d("tmp" + i, tmp[i]);
                    listAdapter.add(tmp[i]);
                }
                listAdapter.notifyDataSetChanged();// 들어온 데이터들을 갱신
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Room Error", error.toString());
            }
        });
    }
}
