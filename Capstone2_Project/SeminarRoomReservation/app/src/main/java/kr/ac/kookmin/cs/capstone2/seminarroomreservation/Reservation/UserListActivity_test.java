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

import java.util.ArrayList;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;

/**
 * Created by hongjuhae on 2015. 10. 25..
 */
public class UserListActivity_test extends AppCompatActivity implements View.OnClickListener{

    //test
    MyCustomAdapter myAdapter = null;
    ListView listView_userlist;

    ArrayList<UserList> UserListArrayList = new ArrayList<UserList>();
    UserList userlist;


    Button button_invite;

    ArrayAdapter<String> myArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_listview_userlist_test);

        findViewsById();

        //Generate list View from ArrayList
        displayListView();
        inviteButtonClick();

    }

    private void displayListView() {

        userlist = new UserList("20103424","박민욱",false);
        UserListArrayList.add(userlist);

        userlist = new UserList("20113344","홍주혜",false);
        UserListArrayList.add(userlist);

        userlist = new UserList("20123314","임은지",false);
        UserListArrayList.add(userlist);

        userlist = new UserList("20123335","정은혜",false);
        UserListArrayList.add(userlist);

        userlist = new UserList("20123336","이송미",false);
        UserListArrayList.add(userlist);


        myAdapter = new MyCustomAdapter(this, R.layout.userlist_info, UserListArrayList);
        ListView listView = (ListView) findViewById(R.id.userlistview);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 줄 클릭 : 클릭시, 메세지 보여주기
                UserList userlist = (UserList) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + userlist.getName() +". Check Box 를 클릭하세요", Toast.LENGTH_LONG).show();
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
                holder.id = (TextView) convertView.findViewById(R.id.code);
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

                for(int i=0;i<UserListArrayList.size();i++){
                    UserList userlist = UserListArrayList.get(i);
                    if(userlist.isSelected()){
                        responseText.append("\n" + userlist.getName());
                    }
                }
                responseText.append("입니다.\n");

                Toast.makeText(getApplicationContext(),responseText, Toast.LENGTH_LONG).show();
                //?? String product = listView_userlist.toString();

            }
        });

    }

    private void findViewsById() {



    }

    @Override
    public void onClick(View v) {

    }
}

