package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.UserListAdapter.AccidentListener;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.RoomInfo;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.SharedPreferenceClass;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.User.UserActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.UserInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static kr.ac.kookmin.cs.capstone2.seminarroomreservation.DefinedValues.*;

public class ReservationFormActivity extends AppCompatActivity implements UserListAdapter.AccidentListener{

    // 출력모드 / 1 - 예약 내역 출력  /  2 - 예약 신청
    private int mode;

    private TextView textViewFormTitle;
    private TextView textViewCheckInDate;
    private TextView textViewCheckInTime;
    private TextView textViewCheckOutTime;
    private TextView textViewParticipants;
    private EditText editTextContent;

    private Button buttonMakeReservation;

    int year, month, day, hour, minute;

    Spinner spinnerRoom;

    AutoCompleteTextView autoTextViewMember;
    UserListAdapter userListAdapter;
    private ArrayList<ItemUser> mUsers;
    public static HashMap<Integer, String> selectedUsers;
    public static int addedMember=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationform);

        findViewsById();

        Intent intent = getIntent();
        mode = intent.getExtras().getInt("viewMode", -1);
        initView(mode);

        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        // 사용자리스트 받아오기
        mUsers = new ArrayList<ItemUser>();
        ReservationFormActivity.selectedUsers.clear();
        setUserList();
    }

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

    public void setUsableTextView(TextView textView, boolean usable) {
        textView.setFocusableInTouchMode(usable);
        textView.setFocusable(usable);
        textView.setClickable(usable);
    }

    public void setUsableEditText(EditText editText, boolean usable) {
        editText.setFocusableInTouchMode(usable);
        editText.setFocusable(usable);
        editText.setClickable(usable);
    }

    public void setTextView(TextView textView, String text) {
        textView.setText(text);
    }


    /*  초기 화면 설정   */
    public void initView(int mode) {
        switch (mode) {
            case VIEW_MODE:     // 예약 조회 모드
                setTextView(textViewFormTitle, getString(R.string.reservation_inquiry));
                setUsableTextView(textViewCheckInDate, false);
                setUsableTextView(textViewCheckInTime, false);
                setUsableTextView(textViewCheckOutTime, false);
                setUsableEditText(editTextContent, false);
                //setUsableEditText(autoTextViewMember, false);
                autoTextViewMember.setHint(" ");
                buttonMakeReservation.setVisibility(View.INVISIBLE); // 예약하기 버튼 감추기기
                getReservationInfo();       // 초기 설정을 마친 뒤 서버로 예약 데이터 요청
                break;
            case REQUEST_MODE:  // 예약 신청 모드
                textViewCheckInDate.setOnClickListener(clickListener);
                textViewCheckInTime.setOnClickListener(clickListener);
                textViewCheckOutTime.setOnClickListener(clickListener);
                buttonMakeReservation.setOnClickListener(clickListener);
                setUsableEditText(editTextContent, true);
                setSpinnerRoom();
                break;
        }
    }


    /*  예약 내역 조회 모드  */
    // 서버로부터 예약 데이터를 받아옴
    public void getReservationInfo() {
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

    // 서버로부터 받은 Json형식의 데이터(예약 내역)를 파싱하는 함수
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

        setReservationInfo(userId, roomID, checkInDate, checkInTime, checkOutTime, content, participant);
    }

    /* 서버에서 받은 예약 내역 설정 */
    public void setReservationInfo(int userId, int roomId, String checkInDate, String checkInTime, String checkOutTime,
                                   String content, ArrayList<String> participant) {
        textViewCheckInDate.setText(checkInDate);
        textViewCheckInTime.setText(checkInTime);
        textViewCheckOutTime.setText(checkOutTime);
        editTextContent.setText(content);
        for (int i = 0; i < participant.size() && participant.get(i) != null; i++) {
            textViewParticipants.append(participant.get(i));
        }
    }

    /*  예약 신청 모드   */
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_makereservation:
                    requestReservation();
                    break;
                case R.id.textView_checkInDate:
                    new DatePickerDialog(ReservationFormActivity.this, dateSetListener, year, month, day).show();
                    break;
                case R.id.textView_checkInTime:
                    new CustomTimePickerDialog(ReservationFormActivity.this, timeSetListener1, hour, minute, false).show();
                    break;
                case R.id.textView_checkOutTime:
                    new CustomTimePickerDialog(ReservationFormActivity.this, timeSetListener2, hour, minute, false).show();
                    break;
            }
        }
    };


    public void setSpinnerRoom() {
        ArrayList<String> roomNames = new ArrayList<String>();
        roomNames = RoomInfo.getRoomNames();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roomNames);
        spinnerRoom = (Spinner) findViewById(R.id.spinner_roomList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoom.setAdapter(adapter);
        spinnerRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void requestReservation() {
        String date = textViewCheckInDate.getText().toString();
        String startTime = textViewCheckInTime.getText().toString();
        String endTime = textViewCheckOutTime.getText().toString();
        String context = editTextContent.getText().toString();
        int roomId = RoomInfo.getRoomId((String) spinnerRoom.getSelectedItem());
        ArrayList<Integer> participants = new ArrayList<Integer>();


        Iterator<Integer> iterator = selectedUsers.keySet().iterator();
        while (iterator.hasNext()) {
            int key = (Integer) iterator.next();
            participants.add(key);
        }
        int userId = SharedPreferenceClass.getValue("_id", 0);

        // 서버로 전송할 예약 내역 데이터
        final TransmissionResInfo transmissionResInfo =
                new TransmissionResInfo(roomId, userId, date, startTime, endTime, context, participants);

        // 서버로 예약 내역 데이터 전송
        RestRequestHelper requestHelper = RestRequestHelper.newInstance();
        requestHelper.makeReservation(transmissionResInfo, new Callback<Integer>() {
            @Override
            public void success(Integer integer, Response response) {
                Toast.makeText(getApplicationContext(), getString(R.string.reservation_request_completed), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

    }

    /* SQLite로부터 멤버리스트를 받아옴 */
    public void setUserList() {
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
           Log.d("ReservationFormActivity", "error : " + e.toString());
        }
        userListAdapter = new UserListAdapter(this, R.layout.activity_user_list, R.id.list_item_textView_userName, mUsers);
        autoTextViewMember.setAdapter(userListAdapter);
    }
    /*  End Of Reservation Request Mode  */


    /*  DatePickerDialog  */
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            String msg = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
            textViewCheckInDate.setText(msg); //선택 날짜 띄워주기
        }
    };


    /*  CustomTimePickerDialog  */
    private CustomTimePickerDialog.OnTimeSetListener timeSetListener1 = new CustomTimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            String msg = String.format("%02d:%02d:00", hourOfDay, minute);
            Toast.makeText(ReservationFormActivity.this, msg, Toast.LENGTH_SHORT).show();
            textViewCheckInTime.setText(msg);
        }
    };
    private CustomTimePickerDialog.OnTimeSetListener timeSetListener2 = new CustomTimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            String msg = String.format("%02d:%02d:00", hourOfDay, minute);
            Toast.makeText(ReservationFormActivity.this, msg, Toast.LENGTH_SHORT).show();
            textViewCheckOutTime.setText(msg);
        }
    };


    @Override
    public void upLoadTextView(final String addedMemeberId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewParticipants.append(" "+addedMemeberId);
            }
        });
    }
}