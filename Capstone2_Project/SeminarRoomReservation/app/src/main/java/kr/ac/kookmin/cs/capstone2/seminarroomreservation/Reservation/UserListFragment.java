package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;


import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends Fragment {

    private static final String TAG = "UserListFragment";

    ListView listViewUsers;
    UserListAdapter userListAdapter;
    HashMap<Integer, String> selectedUsers;

    private ArrayList<ItemUser> mUsers;
    private Button buttonAddMember;

    public UserListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        listViewUsers = (ListView) view.findViewById(R.id.listView_memberList);
        listViewUsers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        mUsers = new ArrayList<ItemUser>();
        selectedUsers = new HashMap<Integer, String>();
        init();


        buttonAddMember = (Button) view.findViewById(R.id.button_sample);
        buttonAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iterator<Integer> iterator = selectedUsers.keySet().iterator();
                while (iterator.hasNext()) {
                    int key = (Integer) iterator.next();
                    System.out.print("key="+key);
                    System.out.println(" value="+selectedUsers.get(key));
                }
            }
        });

        return view;
    }

    ///////////////////////////////////////////////////////////////////////
    private void init(){
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
      /*  listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("테스트 리스트뷰 위치 : " + i);
                ItemUser itemUser = userListAdapter.getItem(i);
                Toast.makeText(getActivity().getApplicationContext(), itemUser.getUserName()+" 추가", Toast.LENGTH_LONG).show();
            }
        });*/

    }


    private class UserListAdapter extends ArrayAdapter<ItemUser>{


        ArrayList<ItemUser> usersInfo;
        HashMap<Integer, Boolean> mCheck;

        public UserListAdapter(ArrayList<ItemUser> usersInfo) {
            super(getActivity(), 0, usersInfo);
            this.usersInfo = new ArrayList<ItemUser>();
            this.usersInfo = usersInfo;

            mCheck = new HashMap<Integer, Boolean>();

            for(int i=0 ; i<getCount() ; i++){
                mCheck.put(i, false);
            }
            System.out.println("테스트 몇번 불리는지");
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
                convertView = getActivity().getLayoutInflater()
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
