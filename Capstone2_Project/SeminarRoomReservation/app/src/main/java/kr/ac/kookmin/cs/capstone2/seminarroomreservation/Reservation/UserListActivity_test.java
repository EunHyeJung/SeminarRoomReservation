package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hongjuhae on 2015. 10. 25..
 */
public class UserListActivity_test extends AppCompatActivity implements View.OnClickListener{

    //test
    MyCustomAdapter myAdapter = null;
    ListView listView_userlist;

    ArrayList<UserList> UserListArrayList = new ArrayList<UserList>();
    UserList userlist;

    public static String date="";
    RestRequestHelper restRequest_userlist;//네트워크 변수
    Button button_invite;
    ArrayAdapter<String> myArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_listview_userlist);

        restRequest_userlist= RestRequestHelper.newInstance();
        findViewsById();

        getUserList();
        //Generate list View from ArrayList
        //displayListView();
        inviteButtonClick();

    }

    public void getUserList(){

        myAdapter = new MyCustomAdapter(this, R.layout.userlist_info, UserListArrayList);
        ListView userlistview = (ListView) findViewById(R.id.userlistview);
        userlistview.setAdapter(myAdapter);

        restRequest_userlist.getUserList(date, new Callback<JsonObject>() {

            @Override
            public void success(JsonObject jsonObject, Response response) {
                Log.d("JSON Object : ", jsonObject.toString());
                try {
                    JsonObject responseData = jsonObject.getAsJsonObject("responseData");// 1레벨 추출
                    JsonArray history = responseData.getAsJsonArray("userList");//2레벨 추출

                    //Array 내용을 추출해서 담는다.
                    for (int i = 0; i < history.size(); i++) {
                        JsonObject tmpObject = history.get(i).getAsJsonObject();

                        userlist = new UserList(tmpObject.getAsJsonPrimitive("userId").getAsString(),
                                tmpObject.getAsJsonPrimitive("name").getAsString(), false);
                        UserListArrayList.add(userlist);

                    }

                    myAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Retrofit Error : ", error.toString());
            }
        });

        userlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 줄 클릭 : 클릭시, 메세지 보여주기
                UserList userlist = (UserList) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + userlist.getName() + ". Check Box 를 클릭하세요", Toast.LENGTH_LONG).show();
            }
        });
    }

    private class MyCustomAdapter extends ArrayAdapter<UserList> {

        private ArrayList<UserList> UserListArrayList;

        public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<UserList> UserListArrayList) {
            super(context, textViewResourceId, UserListArrayList);

            this.UserListArrayList = new ArrayList<UserList>();
            this.UserListArrayList.addAll(UserListArrayList);
        }

        private class ViewHolder {
            TextView id;
            CheckBox name;
        }

        @Override
        //데이터 설정
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.userlist_info, null);

                holder = new ViewHolder();
                holder.id = (TextView) convertView.findViewById(R.id.id);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox);
                convertView.setTag(holder);

                //checkbox click event
                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        UserList userlist = (UserList) cb.getTag();
                        Toast.makeText(getApplicationContext(), "Clicked on Checkbox: " + cb.getText() +  " is " + cb.isChecked(), Toast.LENGTH_LONG).show();
                        userlist.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            UserList userlist = UserListArrayList.get(position);

            holder.id.setText(" (" +  userlist.getId() + ")");
            holder.name.setText(userlist.getName());
            holder.name.setChecked(userlist.isSelected());
            holder.name.setTag(userlist);

            return convertView;

        }

    }

    private void inviteButtonClick() {


        Button myButton = (Button) findViewById(R.id.button_invite);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("추가된 참가자는\n");

                ArrayList<UserList> UserListArrayList = myAdapter.UserListArrayList;

                for (int i = 0; i < UserListArrayList.size(); i++) {
                    UserList userlist = UserListArrayList.get(i);
                    if (userlist.isSelected()) {
                        responseText.append("\n" + userlist.getName());
                    }
                }
                responseText.append("입니다.\n");

                Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
                //String product = listView_userlist.toString();

            }
        });

    }

    private void findViewsById() {
        button_invite=(Button) findViewById(R.id.button_invite);
        listView_userlist=(ListView) findViewById(R.id.userlistview);
    }

    @Override
    public void onClick(View v) {

    }
}

