package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Authentication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.EncryptionClass;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Join.JoinActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager.ManagerActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.User.UserActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity {

    EditText editTextId;
    EditText editTextPassword;


    public static EncryptionClass encrpytion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextId = (EditText) findViewById(R.id.editText_loginId);
        editTextPassword = (EditText) findViewById(R.id.editText_loginPassword);

        Button buttonLogin = (Button) findViewById(R.id.button_login);
        Button buttonJoin = (Button) findViewById(R.id.button_join);

        encrpytion = new EncryptionClass();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editTextId.getText().toString();
                String password = encrpytion.testSHA256(editTextPassword.getText().toString());

                Intent intent = new Intent(getApplicationContext(), ManagerActivity.class);
                startActivity(intent);

                RestRequestHelper requestHelper = RestRequestHelper.newInstance();
                requestHelper.login(id, password, new Callback<Integer>() {
                    @Override
                    public void success(Integer integer, Response response) {
                        switch (integer) {
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

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        Intent intent = new Intent(getApplicationContext(), ManagerActivity.class);
                        startActivity(intent);


                    }
                });

            }
        });
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

}
