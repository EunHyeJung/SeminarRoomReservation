package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Join;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Authentication.MainActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Encryption.EncryptionClass;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model.SharedPreferenceClass;
import retrofit.Callback;
import retrofit.RetrofitError;

import retrofit.client.Response;

public class JoinActivity extends Activity {

    private EditText editTextId;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextPhone;
    private Button buttonJoin;

    SharedPreferenceClass sharedPreference = new SharedPreferenceClass(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        editTextId = (EditText) findViewById(R.id.editText_joinId);
        editTextPassword = (EditText) findViewById(R.id.editText_joinPassword);
        editTextName = (EditText) findViewById(R.id.editText_joinName);
        editTextPhone = (EditText) findViewById(R.id.editText_joinPhone);
        buttonJoin = (Button) findViewById(R.id.button_joinRequest);

        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    public void signup() {

        final String id = editTextId.getText().toString();
        final String password = EncryptionClass.encryptSHA256(editTextPassword.getText().toString());
        final String name = editTextName.getText().toString();
        final String phone = editTextPhone.getText().toString();

        TransmissionJoinInfo transmissionJoinInfo = new TransmissionJoinInfo(id, password, name, phone);

        RestRequestHelper requestHelper = RestRequestHelper.newInstance();
        requestHelper.signUp(transmissionJoinInfo, new Callback<Integer>() {
            @Override
            public void success(Integer signUpCallback, Response response) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}

