package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.DatabaseHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager.ManagerActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.RoomInfo;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.SharedPreferenceClass;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.User.UserActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.UserInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static kr.ac.kookmin.cs.capstone2.seminarroomreservation.DefinedValues.*;

public class ReservationFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // 출력모드 / 1 - 예약 내역 출력  /  2 - 예약 신청
    private int mode = 1;

    private TextView textViewFormTitle;
    private TextView textViewCheckInDate;
    private TextView textViewCheckInTime;
    private TextView textViewCheckOutTime;
    private TextView textViewParticipants;
    private Button buttonAddMember;


    private EditText editTextContent;

    private Button buttonMakeReservation;

    int year, month, day, hour, minute;

    Spinner spinnerRoom;
    ArrayList<String> Roomlistarr;


    // 세미나실 이용 참가자 부분
    AutoCompleteTextView autoTextViewMember;
    UserListAdapter userListAdapter;
    private ArrayList<ItemUser> mUsers;
    public static HashMap<Integer, String> selectedUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationform);

        findViewsById();



        Intent  intent = getIntent();
        mode = intent.getExtras().getInt("viewMode", -1);
        init(mode);     // 예약 신청, 조회 모드에 따른 초기화


        //*********************  spinner ***********************
        SpinnerRoomList();

        //*********************  calendar ***********************
        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);


        mUsers = new ArrayList<ItemUser>();
        ReservationFormActivity.selectedUsers.clear();
        tempInit();


        //예약하기 함수 호출
        buttonMakeReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeReservation();
            }
        });

    }

    /*  End of OnCreateView()   */

    /////////////////////////////////////////////////// 임시, 삭제할 것
    void tempInit() {
        // Sqlite DataBase로부터 사용자 리스트를 받아 올 것
        SQLiteDatabase database;
        DatabaseHelper databaseHelper = new DatabaseHelper(ReservationFormActivity.this);
        database = databaseHelper.getReadableDatabase();
        Cursor cursor;
        String sql = "SELECT * from member";
        try {
            cursor = database.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String userId = cursor.getString(2);
                ItemUser itemUser = new ItemUser(id, userId, name);
                mUsers.add(itemUser);
            }
        } catch (Exception e) {
            System.out.println("error in FromActivity : " + e);
            //Log.d("StartActivityyyy", "error in init : " + e.toString());
        }
                userListAdapter = new UserListAdapter(this, R.layout.activity_user_list, R.id.list_item_textView_userName, mUsers);
        autoTextViewMember.setAdapter(userListAdapter);
    }

    /////////////////////////////////////////////////////
    private void findViewsById() {
        textViewFormTitle = (TextView) findViewById(R.id.textView_form_title);
        textViewCheckInDate = (TextView) findViewById(R.id.textView_checkInDate);
        textViewCheckInTime = (TextView) findViewById(R.id.textView_checkInTime);
        textViewCheckOutTime = (TextView) findViewById(R.id.textView_checkOutTime);
        editTextContent = (EditText) findViewById(R.id.editText_content);
        buttonMakeReservation = (Button) findViewById(R.id.button_makereservation);
        autoTextViewMember = (AutoCompleteTextView) findViewById(R.id.textView_userName);
        autoTextViewMember.setThreshold(1);

        textViewParticipants = (TextView) findViewById(R.id.textView_participants);
        selectedUsers = new HashMap<Integer, String>();
    }
/*

    public final static int VIEW_MODE = 1;
    public final static int REQUEST_MODE = 2;
*/

    private void init(int mode) {
        switch (mode) {
            case VIEW_MODE:        // 예약 내역 조회 모드
                showReservationInfo();
                break;
            case REQUEST_MODE:       // 예약 신청 모드
                reservationRequest();
                break;
        }
    }


    // 예약 신청(예약 생성) 일 때
    private void reservationRequest() {
        System.out.println("mode : 예약 신청 모드");

        //각 선택기로부터 값을 조사
        textViewCheckInDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 버튼 클릭시 DatePicker로부터 값을 읽어와서 Toast메시지로 보여준다
                new DatePickerDialog(ReservationFormActivity.this, dateSetListener, year, month, day).show();
            }


        });
        textViewCheckInTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(ReservationFormActivity.this, timeSetListener1, hour, minute, false).show();

            }
        });
        textViewCheckOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(ReservationFormActivity.this, timeSetListener2, hour, minute, false).show();
            }
        });

    }


    // 예약 내역 조회 모드
    private void showReservationInfo() {
        System.out.println("mode : 예약 조회 모드");
        textViewFormTitle.setText("예약 조회");
        textViewCheckInDate.setFocusableInTouchMode(false);
        textViewCheckInDate.setFocusable(false);
        textViewCheckInDate.setClickable(false);
        textViewCheckInTime.setClickable(false);
        textViewCheckInTime.setFocusable(false);
        textViewCheckOutTime.setClickable(false);
        textViewCheckOutTime.setFocusable(false);
        editTextContent.setClickable(false);
        editTextContent.setFocusable(false);

//        spinnerRoom.setVisibility(View.GONE);
        buttonMakeReservation.setVisibility(View.GONE); // 예약하기 버튼 감추기기


        // 예약 ID를 서버로 전송하고, 해당 예약 데이터를 받아옴
        Intent intent = getIntent();
        int reservationId = intent.getExtras().getInt("reservationId");

        RestRequestHelper requestHelper = RestRequestHelper.newInstance();
        requestHelper.getReservationInfo(reservationId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                jsonParsing(jsonObject);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void jsonParsing(JsonObject jsonObject) {
        JsonObject responseData = jsonObject.getAsJsonObject("responseData");
        int userId = responseData.getAsJsonPrimitive("user").getAsInt();
        int roomID = responseData.getAsJsonPrimitive("room").getAsInt();
        String checkInDate = responseData.getAsJsonPrimitive("date").getAsString();
        String checkInTime = responseData.getAsJsonPrimitive("startTime").getAsString();
        String checkOutTime = responseData.getAsJsonPrimitive("endTime").getAsString();
        String content = responseData.getAsJsonPrimitive("context").getAsString();
        JsonArray memberList = responseData.getAsJsonArray("memberList");

        ArrayList<String> participant = new ArrayList<String>();
        for (int i = 0; i < memberList.size(); i++) {
            participant.add(memberList.get(i).getAsJsonObject().getAsJsonPrimitive("member").getAsString());
        }

        setContent(userId, roomID, checkInDate, checkInTime, checkOutTime, content, participant);
    }


    public void setContent(int userId, int roomId, String checkInDate, String checkInTime, String checkOutTime,
                            String content, ArrayList<String> participant) {
        textViewCheckInDate.setText(checkInDate);
        textViewCheckInTime.setText(checkInTime);
        textViewCheckOutTime.setText(checkOutTime);
        editTextContent.setText(content);
        for (int i = 0; i < participant.size() && participant.get(i) != null; i++) {
            textViewParticipants.append(participant.get(i));
        }

    }



    //날짜, 시간 설정
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            String msg = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
            textViewCheckInDate.setText(msg); //선택 날짜 띄워주기!
        }
    };
    private TimePickerDialog.OnTimeSetListener timeSetListener1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            String msg = String.format("%02d:%02d:00", hourOfDay, minute);
            Toast.makeText(ReservationFormActivity.this, msg, Toast.LENGTH_SHORT).show();
            textViewCheckInTime.setText(msg);
        }
    };
    private TimePickerDialog.OnTimeSetListener timeSetListener2 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            String msg = String.format("%02d:%02d:00", hourOfDay, minute);
            Toast.makeText(ReservationFormActivity.this, msg, Toast.LENGTH_SHORT).show();
            textViewCheckOutTime.setText(msg);
        }
    };


    //스피너 : 세미나실 목록
    public void SpinnerRoomList() {

        String[] strRoomList = new String[RoomInfo.roomNamesSize()];
        for (int i = 1; i < strRoomList.length; i++) {
            strRoomList[i] = RoomInfo.getRoomName(i);
        }

        Roomlistarr = new ArrayList<String>();
        for (int i = 1; i < RoomInfo.roomNamesSize(); i++) {
            Roomlistarr.add(strRoomList[i]);
        }

        ArrayAdapter<String> adapter;

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Roomlistarr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoom = (Spinner) findViewById(R.id.spinner_roomList);
        spinnerRoom.setAdapter(adapter);
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
    public void makeReservation() {

        String date = textViewCheckInDate.getText().toString();
        String startTime = textViewCheckInTime.getText().toString();
        String endTime = textViewCheckOutTime.getText().toString();
        String roomId = spinnerRoom.toString(); // room number 불러오기
        String context = editTextContent.getText().toString();
        ArrayList<Integer> participants = new ArrayList<Integer>();

        Iterator<Integer> iterator = selectedUsers.keySet().iterator();
        while (iterator.hasNext()) {
            int key = (Integer) iterator.next();
            participants.add(key);      // 참가자 고유 ID값을 얻음
        /*   System.out.print("key=" + key);
            System.out.println(" value=" + selectedUsers.get(key));  */
        }
        int userId = SharedPreferenceClass.getValue("id", 0);
        System.out.println("sharedPreference.getValue(id) : " + userId);

        final TransmissionResInfo transmissionResInfo =
                new TransmissionResInfo(601, userId, date, startTime, endTime, context, participants);

        RestRequestHelper requestHelper = RestRequestHelper.newInstance();
        requestHelper.makeReservation(transmissionResInfo, new Callback<Integer>() {
            @Override
            public void success(Integer integer, Response response) {
                Toast.makeText(getApplicationContext(), "예약 신청 완료", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
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
        Toast.makeText(ReservationFormActivity.this, "Room " + myText.getText() + " selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}