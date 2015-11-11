package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.RoomInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//jh
//예약신청 페이지
public class ReservationFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText TxtCheckInDate;
    private EditText TxtCheckInTime;
    private EditText TxtCheckOutTime;
    private TextView Invite;
    private Button buttonAddMember;


    private EditText Edittxt_reason;

    private Button button_makereservation;

    int year, month, day, hour, minute;

    Spinner spinner_room;
    ArrayList<String> Roomlistarr;
    ArrayList<Integer> participantsId = new ArrayList<Integer>();
    ArrayList<Integer> participantsName = new ArrayList<Integer>();
    private TextView Show_userlist; //받아온 userlist 보여주기



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationform);

        findViewsById();

        //예약하기 함수 호출
        button_makereservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeReservation();
            }
        });


        //*********************  spinner ***********************
        SpinnerRoomList();

        //*********************  calendar ***********************
        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day= calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        //각 선택기로부터 값을 조사
        TxtCheckInDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 버튼 클릭시 DatePicker로부터 값을 읽어와서 Toast메시지로 보여준다
                new DatePickerDialog(ReservationFormActivity.this, dateSetListener, year, month, day).show();
            }


        });
        TxtCheckInTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(ReservationFormActivity.this, timeSetListener1, hour, minute, false).show();

            }
        });
        TxtCheckOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(ReservationFormActivity.this, timeSetListener2, hour, minute, false).show();
            }
        });


        // 회의 참여자 추가하기
        Invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                 Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
                startActivity(intent);
            }
        });


    }

    private void findViewsById() {

        TxtCheckInDate = (EditText) findViewById(R.id.editText_checkInDate);
        TxtCheckInTime = (EditText) findViewById(R.id.editText_checkInTime);
        TxtCheckOutTime = (EditText) findViewById(R.id.editText_checkOutTime);

        Invite = (TextView) findViewById(R.id.text_memberlist);
        //buttonAddMember = (Button) findViewById(R.id.button_addMemeber);
        Show_userlist = (TextView) findViewById(R.id.show_userlisttxt);

        Edittxt_reason = (EditText) findViewById(R.id.editText_context);
        button_makereservation = (Button) findViewById(R.id.button_makereservation);
    }

    //날짜, 시간 설정
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            String msg = String.format("%04d-%02d-%02d", year,monthOfYear+1, dayOfMonth);
            TxtCheckInDate.setText(msg); //선택 날짜 띄워주기!
        }
    };
    private TimePickerDialog.OnTimeSetListener timeSetListener1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            String msg = String.format("%02d:%02d:00", hourOfDay, minute);
            Toast.makeText(ReservationFormActivity.this, msg, Toast.LENGTH_SHORT).show();
            TxtCheckInTime.setText(msg);
        }
    };
    private TimePickerDialog.OnTimeSetListener timeSetListener2 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            String msg = String.format("%02d:%02d:00", hourOfDay, minute);
            Toast.makeText(ReservationFormActivity.this, msg, Toast.LENGTH_SHORT).show();
            TxtCheckOutTime.setText(msg);
        }
    };


/*
    protected void onNewIntent(Intent intent) {

        ArrayList<ItemUser> Participants;
        Participants = (ArrayList<ItemUser>) getIntent().getSerializableExtra("participants");

        // 텍스트뷰에 받아온 값을 띄워준다
        Iterator<Integer> iterator = UserListActivity.selectedUsers.keySet().iterator();
        while (iterator.hasNext()) {
            int key = (Integer) iterator.next();
            participantsId.add(key);
        }

        //msg = "참가자 : " + UserListActivity.selectedUsers.toString();
        String msg = participantsId.toString();
        Show_userlist.setText(msg);

        super.onNewIntent(intent);
    }

*/

    //스피너 : 세미나실 목록
    public void SpinnerRoomList() {

        String[] strRoomList = new String[RoomInfo.roomNamesSize()];
        for(int i=1 ; i<strRoomList.length ; i++){
            strRoomList[i] = RoomInfo.getRoomName(i);
        }

        Roomlistarr= new ArrayList<String>();
        for(int i=1 ; i < RoomInfo.roomNamesSize(); i ++) {
            Roomlistarr.add(strRoomList[i]);
        }

        ArrayAdapter <String> adapter;

        adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Roomlistarr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_room = (Spinner)findViewById(R.id.spinner_roomList);
        spinner_room.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //예약함수
    public void makeReservation(){

        String date = TxtCheckInDate.getText().toString();
        String startTime = TxtCheckInTime.getText().toString();
        String endTime = TxtCheckOutTime.getText().toString();
        String roomId = spinner_room.toString(); // room number 불러오기
        String context =  Edittxt_reason.getText().toString();
        ArrayList<Integer> participants = new ArrayList<Integer>();

        Intent intent = getIntent();
        // UserListActivity로부터 회의참가자들을 받앙옴
        HashMap<Integer, String> selectedUsers = (HashMap<Integer, String>) intent.getSerializableExtra("participants");

        Iterator<Integer> iterator = selectedUsers.keySet().iterator();
        while (iterator.hasNext()) {
            int key = (Integer) iterator.next();
            participants.add(key);      // 참가자 고유 ID값을 얻음
      /*      System.out.print("key=" + key);
            System.out.println(" value=" + selectedUsers.get(key));*/
        }

        final TransmissionResInfo transmissionResInfo = new TransmissionResInfo(6, 2, date, startTime, endTime, context, participants);

        RestRequestHelper requestHelper = RestRequestHelper.newInstance();
        requestHelper.makeReservation(transmissionResInfo, new Callback<Integer>() {
            @Override
            public void success(Integer integer, Response response) {
                    System.out.println("성공 ? : "+integer);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

    }

    //Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView myText = (TextView) view;
        Toast.makeText(ReservationFormActivity.this, "Room "+myText.getText()+" selected", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}