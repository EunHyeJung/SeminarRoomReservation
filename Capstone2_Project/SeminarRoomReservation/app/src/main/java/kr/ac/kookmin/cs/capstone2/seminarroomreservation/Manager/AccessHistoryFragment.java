package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager;


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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.CalendarDialog;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.RoomInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccessHistoryFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    ListView SeminarLogView;
   CustomHistoryLVAdapter LogViewAdapter;
    RestRequestHelper restRequest;//네트워크 변수
    public static String date;
    String roomName;//기본은 ALL

    Button dayBtn;//날짜별 보기
    Spinner roomSpinner;//방 별 보기
    ArrayAdapter<String> spinnerAdapter;

    ArrayList<String> entry;
    CalendarDialog calendarDialog;

    public AccessHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_access_history, container, false);

        //네트워크와 연결
        restRequest=RestRequestHelper.newInstance();

        //초기 설정
        init(view);

        calendarDialog = new CalendarDialog(getContext());

        //날짜별 보기를 클릭했을 때
        dayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarDialog.show(); //날짜 다이얼로그를 보여준다.
                calendarDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        calendarDialog.dismiss(); // 날짜 다이얼로그를 닫음.
                        dayBtn.setText(date); // 버튼 텍스트를 갱신
                        getHistory(); // 날짜에 맞는 화면을 보여준다.
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
        roomName = "ALL";

        //날짜 설정
        Calendar calendar = Calendar.getInstance();
        date = calendar.get(Calendar.YEAR)+"-"+String.format("%02d", (calendar.get(Calendar.MONTH) + 1))
                +"-"+String.format("%02d", calendar.get(Calendar.DATE));

        //레이아웃과 매핑하기
        dayBtn=(Button)view.findViewById(R.id.btn_dayWatch);
        dayBtn.setText(date);

        SeminarLogView=(ListView)view.findViewById(R.id.SeminarLog);

        initSpinnerList(view);
    }

    //기록 가져오기
    public void getHistory(){
       // LogViewAdapter.clear();//내용을 비운다.
        LogViewAdapter = new CustomHistoryLVAdapter();
        SeminarLogView.setAdapter(LogViewAdapter);

        restRequest.getHistory(date, roomName, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                Log.d("JSON Object : ", jsonObject.toString());
                try {
                    JsonObject responseData = jsonObject.getAsJsonObject("responseData");// 1레벨 추출
                    JsonArray history = responseData.getAsJsonArray("history");//2레벨 추출

                    //Array 내용을 추출해서 담는다.
                    for(int i = 0; i < history.size(); i++){
                        JsonObject tmpObject = history.get(i).getAsJsonObject();

                        LogViewAdapter.addRoomId(tmpObject.getAsJsonPrimitive("roomId").getAsString());
                        LogViewAdapter.addUser(tmpObject.getAsJsonPrimitive("textId").getAsString());
                        LogViewAdapter.addDate(tmpObject.getAsJsonPrimitive("timeStamp").getAsString());
                        LogViewAdapter.addCommand(tmpObject.getAsJsonPrimitive("command").getAsString());
                    }

                    LogViewAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Retrofit Error : ", error.toString());
            }
        });
    }

    //새로운 방을 눌렀을 때 방 이름을 갱신하고 화면을 다시 보여준다.
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        roomName = parent.getAdapter().getItem(position).toString();//방 이름 갱신
        getHistory();//화면을 다시 뿌려준다.
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {   }

    //스피너 데이터 초기화
    public void initSpinnerList(View view){
        //스피너 매핑 부분
        roomSpinner=(Spinner)view.findViewById(R.id.spinner_room_watch);
        entry=new ArrayList<String>();
        entry.add("ALL");

        spinnerAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1,entry);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        roomSpinner.setAdapter(spinnerAdapter);
        String[] roomList = RoomInfo.getRoomNames();

        if(roomList.length >0 ){
            for(int i=0 ; i < roomList.length ; i++){
                spinnerAdapter.add(roomList[i]);
            }

            spinnerAdapter.notifyDataSetChanged();// 스피너 갱신
        }
    }

}