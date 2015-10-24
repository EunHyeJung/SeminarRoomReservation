package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextId = (EditText) findViewById(R.id.editText_loginId);
        editTextPassword = (EditText) findViewById(R.id.editText_loginPassword);

        sharedPreference = new SharedPreferenceClass(this);

        Button buttonLogin = (Button) findViewById(R.id.button_login);
        Button buttonJoin = (Button) findViewById(R.id.button_join);


        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), JoinActivity.class));
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String id = editTextId.getText().toString();
                final String password = EncryptionClass.testSHA256(editTextPassword.getText().toString());

                /* -------------------------  Retrofit 통신  -------------------------  */
                RestRequestHelper requestHelper = RestRequestHelper.newInstance();
                requestHelper.login(id, password, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject loginCallback, Response response) {
                        UserInfo.setUserInfo(id, password);
                        jsonParsing(loginCallback);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                        error.printStackTrace();
                    }
                });
                /*End of RequestHelper*/




            }
        });
         /*End of LoginButton*/
    }

    /* End Of onCreateView */

    public void jsonParsing(JsonObject jsonObject) {
        JsonObject responseData = jsonObject.getAsJsonObject("responseData"); //2 레벨 제이슨 객체를 얻음
        System.out.println("사용자 : "+responseData);
        int userId = responseData.getAsJsonPrimitive("id").getAsInt();
        int userMode = responseData.getAsJsonPrimitive("result").getAsInt();

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