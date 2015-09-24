package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager;


import android.app.AlertDialog;
import android.content.DialogInterface;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.CalendarDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccessHistoryFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    ListView SeminarLogView;
    ArrayAdapter<String> LogViewAdapter;
    RestRequestHelper restRequest;//네트워크 변수
    public static String date;

    Button dayBtn;//날짜별 보기
    Spinner roomSpinner;//방 별 보기
    ArrayAdapter<String> spinnerAdapter;

    ArrayList<String> entry;
    AlertDialog.Builder alert;

    CalendarDialog calendarDialog;

    public AccessHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_access_history, container, false);

        //초기 설정
        init(view);

        alert = new AlertDialog.Builder(getContext());
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        calendarDialog = new CalendarDialog(getContext());

        dayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarDialog.show();
                calendarDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        calendarDialog.dismiss();
                        getDayHistory();
                    }
                });
            }
        });

        roomSpinner.setOnItemSelectedListener(this);

        //뷰화면 리턴하기
        return view;
    }

    //초기 설정 부분
    public void init(View view){
        //날짜 설정
        Calendar calendar = Calendar.getInstance();
        int month=calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);

        //네트워크와 연결
        restRequest=RestRequestHelper.newInstance();

        //레이아웃과 매핑하기
        dayBtn=(Button)view.findViewById(R.id.btn_dayWatch);
        LogViewAdapter=new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1);
        SeminarLogView=(ListView)view.findViewById(R.id.SeminarLog);
        SeminarLogView.setAdapter(LogViewAdapter);

        //스피너 매핑 부분
        roomSpinner=(Spinner)view.findViewById(R.id.spinner_room_watch);
        entry=new ArrayList<String>();
        entry.add("ALL");
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
                alert.setMessage("서버 문제로 내용을 가져올 수 없습니다.");
                alert.show();
            }
        });
    }

    //날짜별 기록 가져오기
    public void getDayHistory(){
        LogViewAdapter.clear();//내용을 비운다.
        restRequest.dayHistory(date, new Callback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject, Response response) {
                Log.d("JSON Object : ", jsonObject.toString());
                try {
                    JSONArray dayHistory = jsonObject.getJSONArray("result");

                    for(int i=0; i< dayHistory.length(); i++)
                    {
                     LogViewAdapter.add(dayHistory.getJSONObject(i).getString("time")+" "+dayHistory.getJSONObject(i).getString("roomName")+" "
                                        +dayHistory.getJSONObject(i).getString("id")+" "+dayHistory.getJSONObject(i).getString("order"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    //방 별 기록 가져오기
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        LogViewAdapter.clear();
        if (!parent.getAdapter().getItem(position).toString().equals("ALL")) {
            restRequest.roomHistory(date, parent.getAdapter().getItem(position).toString(), new Callback<JSONObject>() {
                @Override
                public void success(JSONObject jsonObject, Response response) {
                    Log.d("JSON Object : ", jsonObject.toString());
                    try {
                        JSONArray dayHistory = jsonObject.getJSONArray("result");

                        for(int i=0; i< dayHistory.length(); i++)
                        {
                            LogViewAdapter.add(dayHistory.getJSONObject(i).getString("time")+" "
                                    +dayHistory.getJSONObject(i).getString("id")+" "+dayHistory.getJSONObject(i).getString("order"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    LogViewAdapter.notifyDataSetChanged();//화면 갱신
                }

                @Override
                public void failure(RetrofitError error) {
                    LogViewAdapter.add("네트워크 상황이 안좋아 보여드릴 수 없습니다. (방 별 보기)");
                    Log.e("Retrofit Error : ", error.toString());
                    LogViewAdapter.notifyDataSetChanged();//화면 갱신
                }
            });
        } else {
            getDayHistory();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
