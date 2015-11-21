package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model.RoomInfo;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model.UserInfo;

/**
 * Created by song on 2015-09-09.
 */
public class CustomControlLVAdapter extends BaseAdapter {
    //리스트뷰에 표시할 정보를 담는 배열
    ArrayList<String> seminarRoomNameList;
    ArrayList<Integer> seminarRoomIdList;
    Button BtnSeminarControl=null;//다이얼로그 띄우는 버튼
    TextView SeminarRoomName;

    public CustomControlLVAdapter(){
        seminarRoomNameList = new ArrayList<String>();
        seminarRoomIdList = new ArrayList<Integer>();
    }
    @Override
    public int getCount() {
        return seminarRoomNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return seminarRoomNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context=parent.getContext();
        final int pos = position;
        final ControlViewHolder holder;
        //리스트가 길어져서 현재 화면에 보이지 않은 아이템은 convertView가 null 상태로 들어옴
        if(convertView==null){
            //view가 null 일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.custom_listview_item,parent,false);

            //초기 설정 부분
            init(convertView, position);

            holder = new ControlViewHolder();
            holder.id = seminarRoomIdList.get(pos);
            holder.controlTxt = (TextView)convertView.findViewById(R.id.SeminarListText);
            holder.controlBtn = (Button)convertView.findViewById(R.id.btn_SeminarControl);

            convertView.setTag(holder); //안쓰면 null pointer Exception 발생

        }
        //캐시된 뷰가 있을 경우 저장된 뷰 홀더 사용
        else {
            holder = (ControlViewHolder) convertView.getTag();
        }

        holder.id = seminarRoomIdList.get(pos);
        holder.controlBtn.setText("제어");
        holder.controlTxt.setText(seminarRoomNameList.get(pos));


        //제어 버튼 이벤트
        BtnSeminarControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //제어창 표시및 방이름 보내기
                //holder에 가진 값으로 보내주지 않으면
                //새로 캐시된 값이 보내지기 때문에 값이 오류가 난다.
                Intent intent = new Intent(context, ControlDialogActivity.class);
                intent.putExtra("roomId", RoomInfo.getRoomId(holder.controlTxt.getText().toString())); //방 ID을 보낸다.
                intent.putExtra("userId", UserInfo.getUserId()); //사용자  id를 보낸다.

                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public void init(View convertView, final int position){
        //textview에 현재 position의 문자열 추가
        SeminarRoomName=(TextView)convertView.findViewById(R.id.SeminarListText);
        SeminarRoomName.setText(seminarRoomNameList.get(position));

        //버튼 매핑
        BtnSeminarControl=(Button)convertView.findViewById(R.id.btn_SeminarControl);
    }

    public void addRoomId(int id) { seminarRoomIdList.add(id); }
    public void addRoomName(String item){
        seminarRoomNameList.add(item);
    }

    public class ControlViewHolder{
        public int id;
        public TextView controlTxt;
        public Button controlBtn;
    }
}