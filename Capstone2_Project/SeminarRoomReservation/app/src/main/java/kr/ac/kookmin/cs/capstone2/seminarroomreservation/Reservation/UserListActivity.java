package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserListActivity extends AppCompatActivity {


    ListView listViewUsers;
    UserListAdapter userListAdapter;
    public static HashMap<Integer, String> selectedUsers;

    private ArrayList<ItemUser> mUsers;
    private Button buttonAddMember;
    ItemUser itemUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        listViewUsers = (ListView) findViewById(R.id.listView_memberList);
        listViewUsers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mUsers = new ArrayList<ItemUser>();
        selectedUsers = new HashMap<Integer, String>();

        init();

        buttonAddMember = (Button) findViewById(R.id.button_sample);
        buttonAddMember.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                //선택된 userlist 넘겨줌
                //Toast.makeText(UserListActivity.this, selectedUsers.toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), ReservationFormActivity.class);
                intent.putExtra("participants", selectedUsers);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                /*
                중요- 해시맵출력
                Iterator<Integer> iterator = selectedUsers.keySet().iterator();
                while (iterator.hasNext()) {
                    int key = (Integer) iterator.next();
                    System.out.print("key=" + key);
                    System.out.println(" value=" + selectedUsers.get(key));
                }*/
            }
        });

    }

    public void init(){
        // 서버로부터 사용자 리스트를 받아옴
        int id=0;
        String userName=null;
        String userId=null;
        RestRequestHelper requestHelper = RestRequestHelper.newInstance();
        requestHelper.getUserList(id, userName, userId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                jsonParsing(jsonObject);
                System.out.println(jsonObject);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        /*
        //임시 출력
        itemUser = new ItemUser(20113344, "홍주혜", "jh");
        mUsers.add(itemUser);

        itemUser = new ItemUser(20113314,"임은지", "ej");
        mUsers.add(itemUser);

        itemUser = new ItemUser(20123355,"정은혜","eh");
        mUsers.add(itemUser);

        itemUser = new ItemUser(20123336,"이송미","sm");
        mUsers.add(itemUser);

        userListAdapter = new UserListAdapter(mUsers);
        listViewUsers.setAdapter(userListAdapter);
        */



    }


    public void jsonParsing(JsonObject jsonObject) {
        JsonObject responseData = jsonObject.getAsJsonObject("responseData"); //2 레벨 제이슨 객체를 얻음
        JsonArray userList = responseData.getAsJsonArray("userList");

        for(int i=0 ; i<userList.size() ; i++){
            int id = userList.get(i).getAsJsonObject().getAsJsonPrimitive("id").getAsInt();
            String userName = userList.get(i).getAsJsonObject().getAsJsonPrimitive("name").getAsString();
            String userId = userList.get(i).getAsJsonObject().getAsJsonPrimitive("userId").getAsString();
            ItemUser itemUser = new ItemUser(id, userName, userId);
            mUsers.add(itemUser);
        }

        userListAdapter = new UserListAdapter(mUsers);
        listViewUsers.setAdapter(userListAdapter);
    }
    private class UserListAdapter extends ArrayAdapter<ItemUser> {


        ArrayList<ItemUser> usersInfo;
        HashMap<Integer, Boolean> mCheck;

        public UserListAdapter(ArrayList<ItemUser> usersInfo) {
            super(getApplicationContext(), 0, usersInfo);
            this.usersInfo = new ArrayList<ItemUser>();
            this.usersInfo = usersInfo;

            mCheck = new HashMap<Integer, Boolean>();

            for(int i=0 ; i<getCount() ; i++){
                mCheck.put(i, false);
            }

        }

        @Override
        public ItemUser getItem(int position){
            return usersInfo.get(position);
        }

        @Override
        public int getCount(){
            return usersInfo.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            final ViewHolder holder;
            final ItemUser content = this.getItem(position);

            if(convertView == null){
                convertView = getLayoutInflater()
                        .inflate(R.layout.list_item_user, null);
                holder = new ViewHolder();
                holder.userName = (TextView) convertView.findViewById(R.id.list_item_textView_userName);
                holder.isChecked = (CheckBox) convertView.findViewById(R.id.list_item_checkBox);
                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.userName.setText(content.getUserName());
            if(mCheck.get(position)){
                holder.isChecked.setChecked(true);
            } else{
                holder.isChecked.setChecked(false);
            }
            holder.isChecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mCheck.get(position)) {      // 현재체크박스의 상태가 false이면  true가 되도록 설정
                        mCheck.put(position, true);
                        selectedUsers.put(content.getId(), content.getUserName());
                        //selectedUsers.a
                    } else{                                 // 현재체크박스의 상태가 true이면 false가 되도록 설정
                        mCheck.put(position, false);
                        selectedUsers.remove(content.getId());
                    }

                }
            });

            return convertView;
        }


        class ViewHolder{
            TextView userName;
            CheckBox isChecked;
        }
    }

}
