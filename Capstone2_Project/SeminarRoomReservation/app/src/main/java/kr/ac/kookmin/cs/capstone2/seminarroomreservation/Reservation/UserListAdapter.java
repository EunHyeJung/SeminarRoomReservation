package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

/**
 * Created by hongjuhae on 2015. 10. 25..
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;

public class UserListAdapter extends BaseAdapter {

    ArrayList<String> UserIdArray;
    ArrayList<String> UserNameArray;

    RestRequestHelper restRequestHelper;

    //생성자
    UserListAdapter(){

        restRequestHelper = RestRequestHelper.newInstance();
        UserIdArray = new ArrayList<String>();
        UserNameArray = new ArrayList<String>();

    }

    @Override
    public int getCount() {
        return UserIdArray.size();
    }

    @Override
    public Object getItem(int position) {
        return UserIdArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();
        UserListViewHolder userlistViewHolder;

        if(convertView == null ){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.custom_listview_sample, parent, false);

            //초기 설정 부분
            init(convertView, position);

            //View holder 설정
            userlistViewHolder = new UserListViewHolder();
            userlistViewHolder.userId = (TextView)convertView.findViewById(R.id.list_userId);
            userlistViewHolder.userName = (TextView)convertView.findViewById(R.id.list_userName);

            convertView.setTag(userlistViewHolder);
        }
        else
        {
            userlistViewHolder = (UserListViewHolder) convertView.getTag();
        }

        userlistViewHolder.userId.setText(UserIdArray.get(position));
        userlistViewHolder.userName.setText(UserNameArray.get(position));


        //내용 설정
        return convertView;
    }

    //초기화 작업
    public void init(View convertView, int position){
        TextView userId = (TextView)convertView.findViewById(R.id.list_userId);
        TextView userName = (TextView)convertView.findViewById(R.id.list_userName);

        userId.setText(UserIdArray.get(position));
        userName.setText(UserNameArray.get(position));

    }

    //날짜 추가
    public void addUserId(String userid) {
        UserIdArray.add(userid); }


    //방 번호 추가
    public void addUserName(String username) { UserNameArray.add(username); }



    //초기화
    public void clear(){
        UserNameArray.clear();
        UserIdArray.clear();
    }

    //View holder Class
    public class UserListViewHolder{
        public TextView userId;
        public TextView userName;
    }
}
