
package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

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

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.ControlDialogActivity;

/**
 * Created by song on 2015-09-09.
 */
public class ListViewAdapter extends BaseAdapter {
    //리스트뷰에 표시할 정보를 담는 배열
    ArrayList<String> SeminarArrayList;
    Button BtnSeminarControl=null;//다이얼로그 띄우는 버튼

    public ListViewAdapter(){
        SeminarArrayList=new ArrayList<String>();
    }
    @Override
    public int getCount() {
        return SeminarArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return SeminarArrayList.get(position);
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
            convertView=inflater.inflate(R.layout.custom_item,parent,false);

            //textview에 현재 position의 문자열 추가
            TextView SeminarRoomName=(TextView)convertView.findViewById(R.id.SeminarListText);
            SeminarRoomName.setText(SeminarArrayList.get(position));

            //버튼 매핑
            BtnSeminarControl=(Button)convertView.findViewById(R.id.btn_SeminarControl);
        }
        //제어 버튼 이벤트
        BtnSeminarControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //제어창 표시및 방이름 보내기
                Intent intent = new Intent(context, ControlDialogActivity.class);
                intent.putExtra("Room", SeminarArrayList.get(position));
                Log.i("Room : ", SeminarArrayList.get(position));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public void add(String item){
        SeminarArrayList.add(item);
    }
}