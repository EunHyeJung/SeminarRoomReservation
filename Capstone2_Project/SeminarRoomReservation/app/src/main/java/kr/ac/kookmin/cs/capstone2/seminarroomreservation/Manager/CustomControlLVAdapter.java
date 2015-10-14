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

/**
 * Created by song on 2015-09-09.
 */
public class CustomControlLVAdapter extends BaseAdapter {
    //리스트뷰에 표시할 정보를 담는 배열
    ArrayList<String> seminarRoomNameList;
    ArrayList<Integer> seminarRoomIdList;
    Button BtnSeminarControl=null;//다이얼로그 띄우는 버튼

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
        //리스트가 길어져서 현재 화면에 보이지 않은 아이템은 convertView가 null 상태로 들어옴
        if(convertView==null){
            //view가 null 일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.custom_listview_item,parent,false);

            //초기 설정 부분
            init(convertView, position);
        }
        //제어 버튼 이벤트
        BtnSeminarControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //제어창 표시및 방이름 보내기
                Intent intent = new Intent(context, ControlDialogActivity.class);
                intent.putExtra("Room", seminarRoomNameList.get(position)); //방 이름을 보낸다.
                intent.putExtra("id", seminarRoomIdList.get(position)); //방  id를 보낸다.
                Log.i("Room : ", seminarRoomNameList.get(position));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public void init(View convertView, final int position){
        //textview에 현재 position의 문자열 추가
        TextView SeminarRoomName=(TextView)convertView.findViewById(R.id.SeminarListText);
        SeminarRoomName.setText(seminarRoomNameList.get(position));

        //버튼 매핑
        BtnSeminarControl=(Button)convertView.findViewById(R.id.btn_SeminarControl);
    }

    public void addRoomId(int id) { seminarRoomIdList.add(id); }
    public void addRoomName(String item){
        seminarRoomNameList.add(item);
    }
}
