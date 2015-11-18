package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserListActivity extends AppCompatActivity {


    AutoCompleteTextView autoTextViewMember;

    UserListAdapter userListAdapter;
    private ArrayList<ItemUser> mUsers;

    private Button buttonAddMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);



        init();

        buttonAddMember = (Button) findViewById(R.id.button_sample);
        buttonAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                /*
                Iterator<Integer> iterator = selectedUsers.keySet().iterator();
                while (iterator.hasNext()) {
                    int key = (Integer) iterator.next();
                    System.out.print("key=" + key);
                    System.out.println(" value=" + selectedUsers.get(key));
                }*/
            }
        });

    }

    /*  End Of onCreate */

    public void init() {
        // 서버로부터 사용자 리스트를 받아옴
        String sendingMessage = "userList";
        RestRequestHelper requestHelper = RestRequestHelper.newInstance();
        requestHelper.getUserList(sendingMessage, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
          //      jsonParsing(jsonObject);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    /*  End Of Init */



    /*  End Of jsonParsing  */


    /////////////////////////////////////////////////////////////////////////////


}
