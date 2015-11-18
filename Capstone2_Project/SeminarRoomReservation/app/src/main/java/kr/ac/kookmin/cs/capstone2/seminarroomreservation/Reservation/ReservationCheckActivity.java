package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager.CustomHistoryLVAdapter;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hongjuhae on 2015. 10. 6..
 */
public class ReservationCheckActivity extends AppCompatActivity {

    private Button btn_yes;
    private Button btn_no;
    private TextView Username;
    private TextView selectedcheckInDate;
    private TextView selectedcheckInTime;
    private TextView selectedcheckOutTime;
    private TextView selectedtxt;
    private TextView selectedroom;
    private TextView selectedmember;


    CustomHistoryLVAdapter LogViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationcheck);

        findViewsById();
        init();





        // 예약승인
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });
        // 예약거절
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                rejectReservation();
            }
        });

    }

    private void findViewsById() {

        btn_yes = (Button) findViewById(R.id.button_yes); //예약 승인
        btn_no = (Button) findViewById(R.id.button_no); //예약 거절
        Username = (TextView) findViewById(R.id.txtUsername);
        selectedcheckInDate = (TextView) findViewById(R.id.selectedcheckInDate);
        selectedcheckInTime = (TextView) findViewById(R.id.selectedcheckInTime);
        selectedcheckOutTime = (TextView) findViewById(R.id.selectedcheckOutTime);
        selectedtxt = (TextView) findViewById(R.id.selectedtxt);
        selectedroom = (TextView) findViewById(R.id.selectedroom);
        selectedmember = (TextView) findViewById(R.id.selectedmember);
    }



    public void init(){
        // 서버로부터 사용자 리스트를 받아옴
        int userId=0;

        String userName=null;
        String date=null;
        String startTime=null;
        String endTime=null;
        String context=null;
        String participants=null;

        RestRequestHelper requestHelper = RestRequestHelper.newInstance();

        //서버로부터 받아올 정보
        requestHelper.checkReservation(userId, userName, date, startTime, endTime, context, participants, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                jsonParsing(jsonObject);
                System.out.println(jsonObject);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        Username.setText("juhae");
        selectedcheckInDate.setText("2015-11-11");
        selectedcheckInTime.setText("11:11:00");
        selectedcheckOutTime.setText("11:11:00");
        selectedtxt.setText("study");
        selectedroom.setText("602");
        selectedmember.setText("20113344");


    }


    public void jsonParsing(JsonObject jsonObject) {
        JsonObject responseData = jsonObject.getAsJsonObject("responseData"); //2 레벨 제이슨 객체를 얻음
        JsonArray checkReservation = responseData.getAsJsonArray("Reservationcheck");

        //존재하는 예약 목록 만큼 예약 리스트 받아오기

    }

    //거절
    public void rejectReservation(){

    }

    public void onClickYes(View v) { //버튼을 눌렀을때 어떤 작업을 할지 선언합니다
        //String inPutText = edittext.getText().toString(); //서버에서 문자열 받아오기
        Toast.makeText(ReservationCheckActivity.this, "예약이 승인되었습니다", Toast.LENGTH_SHORT).show();
    }

    public void onClickNo(View v) { //버튼을 눌렀을때 어떤 작업을 할지 선언합니다
        //String inPutText = edittext.getText().toString(); //서버에서 문자열 받아오기
        Toast.makeText(ReservationCheckActivity.this, "예약이 거절되었습니다", Toast.LENGTH_SHORT).show();
    }

}

