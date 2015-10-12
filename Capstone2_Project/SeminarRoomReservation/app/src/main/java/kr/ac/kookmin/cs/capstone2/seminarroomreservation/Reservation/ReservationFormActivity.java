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

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager.ManagerActivity;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Network.RestRequestHelper;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//jh
//예약신청 페이지
public class ReservationFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText TxtCheckInDate;
    private EditText TxtCheckInTime;
    private EditText TxtCheckOutTime;
    //private EditText Edittxt_reason;

    private Button button_makereservation;

    int year, month, day, hour, minute;

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationform);

        spinner = (Spinner) findViewById(R.id.spinner_roomList);
        adapter = ArrayAdapter.createFromResource(this, R.array.roomList, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
/*
        spinner = (Spinner) findViewById(R.id.spinner_memberList);
        adapter = ArrayAdapter.createFromResource(this, R.array.memberlist, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);*/

        TxtCheckInDate = (EditText) findViewById(R.id.txtCheckInDate);
        TxtCheckInTime = (EditText) findViewById(R.id.txtCheckInTime);
        TxtCheckOutTime = (EditText) findViewById(R.id.txtCheckOutTime);
        //Edittxt_reason = (EditText) findViewById(R.id.reason);
        button_makereservation = (Button) findViewById(R.id.button_makereservation);

        //예약버튼 누르면 예약하기 함수 호출
        button_makereservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makereservation();
            }
        });

        //calendar
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


        //임시 DB
        // Create DataHelper object and insert some sample data
        DBHelper datahelper=new DBHelper(this);
        datahelper.insertProvince("20113344");
        datahelper.insertProvince("20113324");
        datahelper.insertProvince("20120815");
        datahelper.insertProvince("12947251");

        // Get sample data from the database and display them in the spinner
        Spinner spinner=(Spinner)findViewById(R.id.spinner_memberList);
        ArrayList<String> list=datahelper.getAllProvinces();
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text, list);
        spinner.setAdapter(adapter);


    }
//날짜. 시간 설정
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

    //예약하기 함수
    //서버로 예약 신청 정보 전송
    public void makereservation(){

        //
        Toast.makeText(ReservationFormActivity.this, "예약신청 완료", Toast.LENGTH_SHORT).show(); //확인
        Intent intent = new Intent(getApplicationContext(), ManagerActivity.class);
        startActivity(intent);

        //
        String date = TxtCheckInDate.getText().toString();
        String start_time = TxtCheckInTime.getText().toString();
        String end_time = TxtCheckOutTime.getText().toString();
        String room_id = "1";
        //String reason =  Edittxt_reason.getText().toString();

        RestRequestHelper requestHelper = RestRequestHelper.newInstance();
        requestHelper.makeReservation(date, start_time, end_time, room_id,  new Callback<Integer>() {

            @Override
            public void success(Integer makeReservationCallback, Response response) {
                System.out.println("reservation success" + makeReservationCallback);
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