package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class    ControlDialogActivity extends AppCompatActivity {
    Button OpenBtn;
    Button CloseBtn;
    TextView DoorStatusText;

    String RoomName="";
    int id = 0;

    RestRequestHelper requestHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_dialog);

        //기본적인 설정들을 처리
        init();

        //열기 버튼을 눌렀을 때
        OpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //문을 컨트롤하기 위한 함수를 부른다.
                requestHelper.controlDoor(id, RoomName, true, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        int result = jsonObject.get("result").getAsInt();

                        switch (result) {
                            //문 관리 실패
                            case 0:
                                Toast.makeText(getApplicationContext(),"네트워크 사정으로 문을 열 수 없습니다.",Toast.LENGTH_SHORT).show();
                                break;
                            //문 열기 가능
                            case 1:
                                DoorStatusText.setText("Door Status : Open");
                                break;
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Message Error : ",error.toString());
                    }
                });
            }
        });

        //닫기 버튼을 눌렀을 때
        CloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestHelper.controlDoor(id, RoomName, false, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        int result = jsonObject.get("result").getAsInt();
                        switch (result){
                            //문 닫기 실패
                            case 0 :
                                Toast.makeText(getApplicationContext(),"네트워크 사정으로 문을 닫을 수 없습니다.",Toast.LENGTH_SHORT).show();
                                break;
                            //문 닫기 성공
                            case 1:
                                DoorStatusText.setText("Door Status : Close");
                                break;
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Message Error : ", error.toString());
                    }
                });
            }
        });

    }

    //초기화 하기 위한 함수
    public void init(){
        requestHelper=RestRequestHelper.newInstance();

        //클릭한 세미나방 이름, id 가져오기
        Intent intent=getIntent();
        RoomName=intent.getExtras().getString("Room");
        id = intent.getExtras().getInt("id");

        //매핑하기
        OpenBtn=(Button)findViewById(R.id.btn_DoorOpen);
        CloseBtn=(Button)findViewById(R.id.btn_DoorClose);
        DoorStatusText=(TextView)findViewById(R.id.text_DoorStatus);

        //방 상태를 받아오는 함수를 실행
        requestHelper.roomStatus(id, RoomName, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                int status = jsonObject.get("status").getAsInt();
                switch (status){
                    //닫힘
                    case 0 :
                        DoorStatusText.setText("Door Status : Close");
                        break;
                    //열림
                    case 1:
                        DoorStatusText.setText("Door Status : Open");
                        break;
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Error : ",error.toString());
            }
        });
    }
}