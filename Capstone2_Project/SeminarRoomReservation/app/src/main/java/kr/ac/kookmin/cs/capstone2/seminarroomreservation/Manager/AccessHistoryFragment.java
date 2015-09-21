package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccessHistoryFragment extends Fragment {

    ListView SeminarLogView;
    ArrayAdapter<String> LogViewAdapter;
    RestRequestHelper restRequest;//네트워크 변수

    Button dayBtn;//날짜별 보기
    Spinner roomSpinner;//방 별 보기
    ArrayAdapter<String> spinnerAdapter;
    ArrayList<String> entry;

    public AccessHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_access_history, container, false);

        //초기 설정
        init(view);

        //기본으로는 날짜별로 보여준다.
        getDayHistory();

        dayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDayHistory();
            }
        });

        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("item select", parent.getAdapter().getItem(position).toString());
                restRequest.roomWatch(parent.getAdapter().getItem(position).toString(), new Callback<JSONObject>() {
                    @Override
                    public void success(JSONObject jsonObject, Response response) {
                        Log.d("JSON Object : ", jsonObject.toString());
                        LogViewAdapter.add("로그 찍힘");
                        LogViewAdapter.notifyDataSetChanged();//화면 갱신
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LogViewAdapter.add("네트워크 상황이 안좋아 보여드릴 수 없습니다. (방 별 보기)");
                        Log.e("Retrofit Error : ", error.toString());
                        LogViewAdapter.notifyDataSetChanged();//화면 갱신
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //뷰화면 리턴하기
        return view;
    }

    //초기 설정 부분
    public void init(View view){
        //네트워크와 연결
        restRequest=RestRequestHelper.newInstance();

        //레이아웃과 매핑하기
        dayBtn=(Button)view.findViewById(R.id.btn_dayWatch);
        LogViewAdapter=new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1);
        SeminarLogView=(ListView)view.findViewById(R.id.SeminarLog);
        SeminarLogView.setAdapter(LogViewAdapter);

        roomSpinner=(Spinner)view.findViewById(R.id.spinner_room_watch);
        entry=new ArrayList<String>();
        spinnerAdapter=new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1,entry);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        roomSpinner.setAdapter(spinnerAdapter);


        restRequest.roomList("admin", new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                String tmp[]=s.split("&");
                for(int i=0;i<tmp.length;i++){
                    spinnerAdapter.add(tmp[i]);
                }
                spinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                spinnerAdapter.add("0");
                spinnerAdapter.add("1");
                spinnerAdapter.notifyDataSetChanged();
            }
        });
    }

    //날짜별 로그 가져오기
    public void getDayHistory(){
        Date date=new Date();
        restRequest.dayWatch(date, new Callback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject, Response response) {
                Log.d("JSON Object : ", jsonObject.toString());
                LogViewAdapter.add("로그 찍힘");
                LogViewAdapter.notifyDataSetChanged();//화면 갱신
            }

            @Override
            public void failure(RetrofitError error) {
                LogViewAdapter.add("네트워크 상황이 안좋아 보여드릴 수 없습니다. ㅜ");
                Log.e("Retrofit Error : ", error.toString());
                LogViewAdapter.notifyDataSetChanged();//화면 갱신
            }
        });
    }
}
