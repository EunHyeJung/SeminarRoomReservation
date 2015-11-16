package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Authentication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import kr.ac.kookmin.cs.capstone2.seminarroomreservation.DefinedValues;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Encryption.EncryptionClass;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Gcm.QuickstartPreferences;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Gcm.RegistrationIntentService;
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

import static kr.ac.kookmin.cs.capstone2.seminarroomreservation.DefinedValues.*;

public class MainActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver registrationBroadcastReceiver;

    EditText editTextId;
    EditText editTextPassword;
    SharedPreferenceClass sharedPreference;

    CheckBox checkBoxAutoLogin;
    CheckBox checkBoxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // GCM
        registBroadcastReceiver();
        getInstanceIdToken();
        //
        sharedPreference = new SharedPreferenceClass(this);

        editTextId = (EditText) findViewById(R.id.editText_loginId);
        editTextPassword = (EditText) findViewById(R.id.editText_loginPassword);

        checkBoxId = (CheckBox) findViewById(R.id.checkBox_id);
        checkBoxAutoLogin = (CheckBox) findViewById(R.id.checkBox_login);

        init();

        Button buttonLogin = (Button) findViewById(R.id.button_login);
        Button buttonJoin = (Button) findViewById(R.id.button_join);

        // 자동 로그인 설정 체크박스
        checkBoxAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferenceClass.putValue("autoLogin", b);
            }
        });

        // 아이디 저장 체크 박스
        checkBoxId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferenceClass.putValue("storeId", b);
            }
        });


        // 회원 가입 버튼 클릭시
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), JoinActivity.class));
            }
        });


        // 로그인 버튼 클릭시
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(false);
            }
        });
    }

    /* End Of onCreateView */

    public void init() {
        if (SharedPreferenceClass.getValue("autoLogin", false)) {       // 자동로그인 설정일 경우, 아이디 저장도 같이 체크
            autoLogin();
            checkBoxId.setChecked(true);
        }
        if (SharedPreferenceClass.getValue("storeId", false)) {        // 아이디 저장이 되어 있는 상태이면
            String userId = SharedPreferenceClass.getValue("id", "input your Id");
            editTextId.setText(userId);
        }
    }


    public void autoLogin() {
        String userId = SharedPreferenceClass.getValue("id", "input your Id");
        editTextId.setText(userId);
        editTextPassword.setText(DEFAULT_PASSWORD);
        login(true);
    }

    /*  Start Of Login  */

    public void login(final boolean isAutoLogin) {
        final String id = editTextId.getText().toString();
        final String password;
        final String instanceId = SharedPreferenceClass.getValue("instanceID","empty");

        if (isAutoLogin) {
            password = SharedPreferenceClass.getValue("password", "default");
        } else {
            password = EncryptionClass.encryptSHA256(editTextPassword.getText().toString());
        }

         /* -------------------------  Retrofit 통신  -------------------------  */
        RestRequestHelper requestHelper = RestRequestHelper.newInstance();
        requestHelper.login(id, password, instanceId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject loginCallback, Response response) {
                UserInfo.setUserInfo(id, password);
                jsonParsing(loginCallback);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.network_connection_error), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    /*  End Of Login   */


    /*  Start Of jsonParsing */
    public void jsonParsing(JsonObject jsonObject) {
        JsonObject responseData = jsonObject.getAsJsonObject("responseData"); //2 레벨 제이슨 객체를 얻음
        int userMode = responseData.getAsJsonPrimitive("result").getAsInt();
        if(userMode == WRONG_USER_INFO){
            loginProcess(userMode);
            return;
        }
        int userId = responseData.getAsJsonPrimitive("id").getAsInt();

        UserInfo.setUserInfo(userId, userMode);

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
            case WRONG_USER_INFO :         // 로그인 오류
                Toast.makeText(getApplicationContext(), getString(R.string.wrong_user_info), Toast.LENGTH_LONG).show();
                break;
            case USER_MODE :             // 일반 사용자
                Toast.makeText(getApplicationContext(), getString(R.string.login_user_mode), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
                break;
            case ADMIN_MODE :             // 관리자 로그인
                Toast.makeText(getApplicationContext(), getString(R.string.login_admin_mode), Toast.LENGTH_LONG).show();
                intent = new Intent(getApplicationContext(), ManagerActivity.class);
                startActivity(intent);
                break;
        }
    }

    // InstanceId를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    /*
        LocaBroadcast 리시버를 정의한다.
        토큰을 획득하기 위한 READY, GENERATING, COMPLETE 액션에 따라 UI에 변화를 준다.
    */
    public void registBroadcastReceiver() {
        registrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        };
    }

    // Googe Play Service를 사용할 수 있는 환경인지를 체크한다.
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(registrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(registrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(registrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    /**
     * 앱이 화면에서 사라지면 등록된 LocalBoardcast를 모두 삭제한다.
     */
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(registrationBroadcastReceiver);
        super.onPause();
    }
}