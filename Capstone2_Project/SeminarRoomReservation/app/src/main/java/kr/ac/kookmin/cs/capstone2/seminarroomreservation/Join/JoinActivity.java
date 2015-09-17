package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Join;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import retrofit.Callback;
import retrofit.RetrofitError;

import retrofit.client.Response;


public class JoinActivity extends Activity {

    private EditText editTextId;
    private EditText editTextPassword;
    private EditText editTextName ;
    private EditText editTextPhone;
    private Button buttonJoin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        editTextId = (EditText) findViewById(R.id.editText_id);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
        editTextName = (EditText) findViewById(R.id.editText_name);
         editTextPhone = (EditText) findViewById(R.id.editText_phone);
        buttonJoin = (Button) findViewById(R.id.button_join);

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


    public void signup(){
        String id = editTextId.getText().toString();
        String password = editTextPassword.getText().toString();
        String name = editTextName.getText().toString();
        String phone  = editTextPhone.getText().toString();

        RestRequestHelper requestHelper = RestRequestHelper.newInstance();
        requestHelper.signUp(id, password, name, phone, new Callback<Integer>(){

            @Override
            public void success(Integer signUpCallback, Response response) {
                System.out.println("signup success"+signUpCallback);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

    }




}
