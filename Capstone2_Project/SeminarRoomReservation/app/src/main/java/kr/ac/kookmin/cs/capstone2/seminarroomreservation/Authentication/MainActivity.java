package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Set;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.EncryptionClass;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Join.JoinActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager.ManagerActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.RoomInfoClass;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.SharedPreferenceClass;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.User.UserActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    EditText editTextId;
    EditText editTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextId = (EditText) findViewById(R.id.editText_loginId);
        editTextPassword = (EditText) findViewById(R.id.editText_loginPassword);

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


            //    loginProcess();

                RestRequestHelper requestHelper = RestRequestHelper.newInstance();
                requestHelper.login(id, password, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject loginCallback, Response response) {

                        saveUserInfo(id, password);
                        loginProcess();

                        saveUserInfo(id, password);             // SharedPreference에 사용자 정보 저장
                        loginProcess(loginCallback);

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
            }
        });
    }

    /* End Of onCreateView */
    public void saveUserInfo(String id, String password){
        SharedPreferenceClass sharedPreference = new SharedPreferenceClass(this);
        sharedPreference.put("id",id);
        sharedPreference.put("password",password);
    }
    public void saveUserInfo(int userId){
        SharedPreferenceClass sharedPreference = new SharedPreferenceClass(this);
        sharedPreference.put("userId",userId);
    }

    public void loginProcess(JsonObject loginCallback){
        System.out.println("loginCallback : "+loginCallback);

        JsonElement jsonElement = loginCallback.get("id");
        int userId = jsonElement.getAsInt();
        saveUserInfo(userId);



    public void loginProcess(JsonObject loginCallback){
        Intent intent = new Intent(getApplicationContext(), ManagerActivity.class);
        startActivity(intent);



        jsonElement = loginCallback.get("result");
        int result = jsonElement.getAsInt();

        JsonArray jsonArray = loginCallback.getAsJsonArray("room");
        String roomNames[] = new String[jsonArray.size()];


        for(int i=0 ; i<jsonArray.size() ; i++){
            JsonObject temp = jsonArray.get(i).getAsJsonObject();
            String roomName = temp.get("roomName").toString();
            roomNames[i] = roomName;
        }
        RoomInfoClass roomInfo = new RoomInfoClass(jsonArray.size());
        roomInfo.setRoomNames(roomNames);



        switch (result) {
            case 0:         // 로그인 오류
                Toast.makeText(getApplicationContext(), "Invalid ID", Toast.LENGTH_LONG).show();
                break;
            case 1:             // 일반 사용자
                Toast.makeText(getApplicationContext(), "Login Success, User Mode", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
                break;
            case 2:             // 관리자 로그인
                Toast.makeText(getApplicationContext(), "Login Success, Admin Mode", Toast.LENGTH_LONG).show();
/*                saveUserInfo(id, password);
                System.out.println("login result "+response);*/
                intent = new Intent(getApplicationContext(), ManagerActivity.class);
                startActivity(intent);
                break;
        }
    }

}