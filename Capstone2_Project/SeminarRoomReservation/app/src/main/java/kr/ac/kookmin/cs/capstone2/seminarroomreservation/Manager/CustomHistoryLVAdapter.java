package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;

/**
 * Created by song on 2015-10-01.
 */
public class CustomHistoryLVAdapter extends BaseAdapter {
    ArrayList<String> dateArray;
    ArrayList<String> roomIdArray;
    ArrayList<String> commandArray;
    ArrayList<String> userIdArray;

    RestRequestHelper restRequestHelper;

    //생성자
    CustomHistoryLVAdapter(){
        restRequestHelper = RestRequestHelper.newInstance();

        dateArray = new ArrayList<String>();
        roomIdArray = new ArrayList<String>();
        commandArray = new ArrayList<String>();
        userIdArray = new ArrayList<String>();
    }
    @Override
    public int getCount() {
        return commandArray.size();
    }

    @Override
    public Object getItem(int position) {
        return commandArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if(convertView == null ){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.custom_listview_history, parent, false);

            init(convertView, position);
        }
        return convertView;
    }

    //초기화 작업
    public void init(View convertView, int position){
        TextView roomId = (TextView)convertView.findViewById(R.id.list_history_roomId);
        TextView timeDate = (TextView)convertView.findViewById(R.id.list_history_time);
        TextView userId = (TextView)convertView.findViewById(R.id.list_history_userId);
        TextView order = (TextView)convertView.findViewById(R.id.list_history_command);

        roomId.setText(roomIdArray.get(position));
        timeDate.setText(dateArray.get(position));
        userId.setText(userIdArray.get(position));
        order.setText(commandArray.get(position));
    }

    //방 번호 추가
    public void addRoomId(String roomId) { roomIdArray.add(roomId); }

    //날짜 추가
    public void addDate(String date) { dateArray.add(date); }

    //사용자 추가
    public void addUser(String userId){ userIdArray.add(userId); }

    //문 명령 추가
    public void addCommand(String command){ commandArray.add(command); }

    //초기화
    public void clear(){
        roomIdArray.clear();
        dateArray.clear();
        userIdArray.clear();
        commandArray.clear();
    }
}
