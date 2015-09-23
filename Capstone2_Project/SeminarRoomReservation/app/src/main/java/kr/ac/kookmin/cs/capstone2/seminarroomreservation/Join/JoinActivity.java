package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Join;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Authentication.MainActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.EncryptionClass;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.SharedPreferenceClass;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_join, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void signup() {

        final String id = editTextId.getText().toString();
        final String password = EncryptionClass.testSHA256(editTextPassword.getText().toString());
        final String name = editTextName.getText().toString();
        final String phone = editTextPhone.getText().toString();


        RestRequestHelper requestHelper = RestRequestHelper.newInstance();
        requestHelper.signUp(id, password, name, phone, new Callback<Integer>() {

            @Override
            public void success(Integer signUpCallback, Response response) {
                System.out.println("signup success" + signUpCallback);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

    }

}

