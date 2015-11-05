package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hongjuhae on 2015. 10. 25..
 */
public class UserListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView sampleuserlistview;

    UserListAdapter userlistviewAdapter;

    RestRequestHelper restRequest;//네트워크 변수
    public static String date="";

    String roomName;//기본은 ALL
    Button button_sample;//친구 추가 버튼


    ArrayList<String> entry;
    CalendarDialog calendarDialog;

    public UserListActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_userlistview);

        restRequest= RestRequestHelper.newInstance();
        //초기 설정

        //레이아웃과 매핑하기
        button_sample=(Button) findViewById(R.id.button_sample);
        sampleuserlistview=(ListView) findViewById(R.id.sampleuserlistview);

        getUserList();

    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_userlistview, container, false);

        //네트워크와 연결
        restRequest= RestRequestHelper.newInstance();

        //초기 설정
        init(view);

        getUserList();

        //뷰화면 리턴하기
        return view;
    }
*/
    //기록 가져오기
    public void getUserList(){
        // userlistviewAdapter.clear();//내용을 비운다.

        userlistviewAdapter = new UserListAdapter();

        sampleuserlistview.setAdapter(userlistviewAdapter);

        restRequest.getUserList(date, new Callback<JsonObject>() {

            @Override
            public void success(JsonObject jsonObject, Response response) {
                Log.d("JSON Object : ", jsonObject.toString());
                try {
                    JsonObject responseData = jsonObject.getAsJsonObject("responseData");// 1레벨 추출
                    JsonArray history = responseData.getAsJsonArray("userList");//2레벨 추출

                    //Array 내용을 추출해서 담는다.
                    for (int i = 0; i < history.size(); i++) {
                        JsonObject tmpObject = history.get(i).getAsJsonObject();

                        userlistviewAdapter.addUserId(tmpObject.getAsJsonPrimitive("userId").getAsString());
                        userlistviewAdapter.addUserName(tmpObject.getAsJsonPrimitive("name").getAsString());

                    }

                    userlistviewAdapter.notifyDataSetChanged();

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        getUserList();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}