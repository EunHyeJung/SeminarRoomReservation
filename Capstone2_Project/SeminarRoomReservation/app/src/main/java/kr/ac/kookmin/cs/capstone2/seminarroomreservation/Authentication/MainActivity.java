package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Encryption.EncryptionClass;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Join.JoinActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager.ManagerActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.RoomInfo;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.SharedPreferenceClass;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.User.UserActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.UserInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    EditText editTextId;
    EditText editTextPassword;
    SharedPreferenceClass sharedPreference;

    CheckBox checkBoxAutoLogin;
    CheckBox checkBoxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreference = new SharedPreferenceClass(this);

        editTextId = (EditText) findViewById(R.id.editText_loginId);
        editTextPassword = (EditText) findViewById(R.id.editText_loginPassword);

        checkBoxId = (CheckBox) findViewById(R.id.checkbox_id);
        checkBoxAutoLogin = (CheckBox) findViewById(R.id.checkbox_login);

        if (SharedPreferenceClass.getValue("autoLogin", false)) {       // 자동로그인 설정일 경우
            String userId = SharedPreferenceClass.getValue("id", "userId");
            String password = SharedPreferenceClass.getValue("password", "password");
            editTextId.setText(userId);
            editTextPassword.setText(password);
            login();
        }
        if (SharedPreferenceClass.getValue("storeId", false)) {        // 아이디 저장이 되어 있는 상태이면
            String userId = SharedPreferenceClass.getValue("id", "input your Id");
            editTextId.setText(userId);
            checkBoxId.setChecked(true);
        }



        Button buttonLogin = (Button) findViewById(R.id.button_login);
        Button buttonJoin = (Button) findViewById(R.id.button_join);


        checkBoxAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Toast.makeText(getApplicationContext(), "자동로그인 설정", Toast.LENGTH_SHORT).show();
                    SharedPreferenceClass.putValue("autoLogin",true);
                } else {
                    Toast.makeText(getApplicationContext(), "자동로그인 해제", Toast.LENGTH_SHORT).show();
                    SharedPreferenceClass.putValue("autoLogin",false);
                }
            }
        });
        checkBoxId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Toast.makeText(getApplicationContext(), "아이디저장", Toast.LENGTH_SHORT).show();
                    SharedPreferenceClass.putValue("storeId", true);
                } else {
                    Toast.makeText(getApplicationContext(), "아이디저장 해제", Toast.LENGTH_SHORT).show();
                    SharedPreferenceClass.putValue("storeId", false);
                }
            }
        });

        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), JoinActivity.class));
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
         /* End of LoginButton  */
    }

    /* End Of onCreateView */


    /*  Start Of Login  */
    public void login() {
        final String id = editTextId.getText().toString();
        final String password = EncryptionClass.testSHA256(editTextPassword.getText().toString());


         /* -------------------------  Retrofit 통신  -------------------------  */
        RestRequestHelper requestHelper = RestRequestHelper.newInstance();
        requestHelper.login(id, password, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject loginCallback, Response response) {
                UserInfo.setUserInfo(id, editTextPassword.getText().toString());
                jsonParsing(loginCallback);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "네트워크 접속 오류", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
                /*End of RequestHelper*/
    }

    /*  End Of Login   */


    public void jsonParsing(JsonObject jsonObject) {
        JsonObject responseData = jsonObject.getAsJsonObject("responseData"); //2 레벨 제이슨 객체를 얻음

        int userMode = responseData.getAsJsonPrimitive("result").getAsInt();
        if(userMode == -1) {
            Toast.makeText(getApplicationContext(), "잘못된 아이디입니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        int userId = responseData.getAsJsonPrimitive("id").getAsInt();

        UserInfo.setUserInfo(userId, userMode);           // 사용자 고유 ID 설정

        JsonArray roomNames = responseData.getAsJsonArray("room");

        for (int i = 0; i < roomNames.size(); i++) {
            int roomId = roomNames.get(i).getAsJsonObject().getAsJsonPrimitive("roomId").getAsInt();
            String roomName = roomNames.get(i).getAsJsonObject().getAsJsonPrimitive("roomName").toString();
            RoomInfo.setRoomInfo(roomId, roomName);
        }


        loginProcess(userMode);
    }


    public void loginProcess(int result) {
        switch (result) {
            case -1:         // 로그인 오류
                Toast.makeText(getApplicationContext(), "Invalid ID", Toast.LENGTH_LONG).show();
                break;
            case 1:             // 일반 사용자
                Toast.makeText(getApplicationContext(), "Login Success, User Mode", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
                break;
            case 2:             // 관리자 로그인
                Toast.makeText(getApplicationContext(), "Login Success, Admin Mode", Toast.LENGTH_LONG).show();
                intent = new Intent(getApplicationContext(), ManagerActivity.class);
                startActivity(intent);
                break;
        }
    }

}