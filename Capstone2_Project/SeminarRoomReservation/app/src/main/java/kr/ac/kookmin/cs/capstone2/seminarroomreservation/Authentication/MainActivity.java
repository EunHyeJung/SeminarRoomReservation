package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Authentication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model.DatabaseHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Encryption.EncryptionClass;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Gcm.QuickstartPreferences;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Gcm.RegistrationIntentService;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Join.JoinActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager.ManagerActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.ItemUser;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model.RoomInfo;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model.SharedPreferenceClass;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.User.UserActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model.UserInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model.DefinedValues.*;
import static kr.ac.kookmin.cs.capstone2.seminarroomreservation.UpdateView.*;

public class MainActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver registrationBroadcastReceiver;

    EditText editTextId;
    EditText editTextPassword;
    SharedPreferenceClass sharedPreference;

    CheckBox checkBoxAutoLogin;
    CheckBox checkBoxId;

    //

    DatabaseHelper databaseHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // GCM
        registBroadcastReceiver();
        getInstanceIdToken();

        databaseHelper = new DatabaseHelper(MainActivity.this);
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


    public void init() {
        if (SharedPreferenceClass.getValue("autoLogin", false)) {       // 자동로그인 설정일 경우, 아이디 저장도 같이 체크
            autoLogin();
            checkBoxId.setChecked(true);
        } else if (SharedPreferenceClass.getValue("storeId", false)) {        // 아이디 저장이 되어 있는 상태이면
            String userId = SharedPreferenceClass.getValue("id", "false");
            editTextId.setText(userId);
        }
    }


    public void autoLogin() {
        String userId = SharedPreferenceClass.getValue("id", "input your Id");
        editTextId.setText(userId);
        editTextPassword.setText(DEFAULT_PASSWORD);
        login(true);
    }


    public void login(final boolean isAutoLogin) {
        final String id = editTextId.getText().toString();
        final String password;
        final String instanceId = SharedPreferenceClass.getValue("instanceID", "empty");

        if (isAutoLogin) {
            password = SharedPreferenceClass.getValue("password", "default");
        } else {
            password = EncryptionClass.encryptSHA256(editTextPassword.getText().toString());
        }

        RestRequestHelper requestHelper = RestRequestHelper.newInstance();
        requestHelper.login(id, password, instanceId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject loginCallback, Response response) {
                UserInfo.setUserInfo(id, password);
                jsonParsing(loginCallback);
            }

            @Override
            public void failure(RetrofitError error) {
                makeToast(getApplicationContext(), getString(R.string.network_connection_error));
                error.printStackTrace();
            }
        });
    }

    public void jsonParsing(JsonObject jsonObject) {
        JsonObject responseData = jsonObject.getAsJsonObject("responseData");
        int userMode = responseData.getAsJsonPrimitive("result").getAsInt();
        if (userMode == WRONG_VALUE) {
            loginProcess(userMode);
            return;
        }
        int userId = responseData.getAsJsonPrimitive("id").getAsInt();

        UserInfo.setUserInfo(userId, userMode);

        JsonArray roomNames = responseData.getAsJsonArray("room");

        int roomId;
        String roomName;
        for (int i = 0; i < roomNames.size(); i++) {
            roomId = roomNames.get(i).getAsJsonObject().getAsJsonPrimitive("roomId").getAsInt();
            roomName = roomNames.get(i).getAsJsonObject().getAsJsonPrimitive("roomName").toString();
            roomName = roomName.substring(1, 4);
            RoomInfo.setRoomInfo(roomId, roomName);
            RoomInfo.setRoomIfo(roomName, roomId);
        }


        // store users info into SQLite
        JsonArray userList = responseData.getAsJsonArray("user");
        storeUsersInfo(userList);
        loginProcess(userMode);
    }

    public void storeUsersInfo(JsonArray userList){
        int id;
        String memberId;
        String memberName;
        for (int i = 0; i < userList.size(); i++) {
            id = userList.get(i).getAsJsonObject().getAsJsonPrimitive("id").getAsInt();
            memberId = userList.get(i).getAsJsonObject().getAsJsonPrimitive("userId").getAsString();
            memberName = userList.get(i).getAsJsonObject().getAsJsonPrimitive("name").getAsString();
            ItemUser itemUser = new ItemUser(id, memberId, memberName);
            databaseHelper.insertMember(itemUser);
        }
    }

    public void loginProcess(int result) {
        switch (result) {
            case WRONG_VALUE:         // login error
                makeToast(getApplicationContext(), getString(R.string.wrong_user_info));
                break;
            case USER_MODE:             // general user
                makeToast(getApplicationContext(), getString(R.string.login_user_mode));
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
                break;
            case ADMIN_MODE:             // administrator
                makeToast(getApplicationContext(), getString(R.string.login_admin_mode));
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

    // Define Local Broad Cast Receiver
    // According to READY, GENEREATING, COMPLETE action, change UI for acquiring token.
    public void registBroadcastReceiver() {
        registrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        };
    }

    // Check whether the environment, which can be used for the Google Play Service.
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
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(registrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(registrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(registrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }


    // If app disappears from screen, delete all registered LocalBroadcast.
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(registrationBroadcastReceiver);
        super.onPause();
    }
}