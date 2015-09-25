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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
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


                // 임시 선언 삭제할것.
         /*       Intent intent = new Intent(getApplicationContext(), ManagerActivity.class);
                startActivity(intent);

*/
                /*Json Test - 서버 통신 후에 삭제할 것
                {"responseData":{"id":1,"roomNames":[{"roomName":"601"},{"roomName":"602"},{"roomName":"603"}],"result":2}} */
                JSONObject tempJson;
                String tempnJsonData = "{ \"responseData\" : { \"id\" : 1, \"result\" : 2, " +
                        " \"roomNames\" : [ { \"roomName\" : \"601\" }, { \"roomName\" : \"602\" }, { \"roomName\" : \"603\" }] } }";
                try {
                    tempJson = new JSONObject(tempnJsonData);
                    jsonParsing(tempJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    /// Json Test - 서버 통신 후에 삭제할 것



                /* -------------------------  Retrofit 통신  -------------------------  */
                RestRequestHelper requestHelper = RestRequestHelper.newInstance();
                requestHelper.login(id, password, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject loginCallback, Response response) {
                        sharedPreference.put("id",id);          // 사용자 정보 저장
                        sharedPreference.put("password",password);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                        error.printStackTrace();
                    }
                });
                /*End of RequestHelper*/
            }
        });
         /*End of RequestHelper*/
    }

    /* End Of onCreateView */

    public void jsonParsing(JSONObject jsonObject) throws JSONException {
        JSONObject responseData = jsonObject.getJSONObject("responseData"); //2 레벨 제이슨 객체를 얻음
        int userId  = responseData.getInt("id");
        int result = responseData.getInt("result");

        sharedPreference.put("userId", userId);             // 사용자 고유 id 저장

        JSONArray roomNames = responseData.getJSONArray("roomNames");
        RoomInfoClass.init(roomNames.length());
        for(int i=0 ; i<roomNames.length() ; i++){
            RoomInfoClass.roomNames[i] = roomNames.getJSONObject(i).getString("roomName").toString();
        }

        loginProcess(result);
    }


    public void loginProcess(int result){
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
                intent = new Intent(getApplicationContext(), ManagerActivity.class);
                startActivity(intent);
                break;
        }
    }

}