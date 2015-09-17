package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import java.util.Calendar;
import java.util.GregorianCalendar;

//jh
//예약신청 페이지
public class ReservationFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText check_in_date;
    private EditText check_in_time;
    private EditText check_out_time;
    private EditText useTime;
    private EditText roomNumber;
    private EditText reason;
    private EditText otherMembers;
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

        spinner = (Spinner) findViewById(R.id.spinner_memberList);
        adapter = ArrayAdapter.createFromResource(this, R.array.memberlist, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        /*

        //RoomList
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setPrompt("이용 세미나실 선택");

        adspin = ArrayAdapter.createFromResource(this, R.array.romList, android.R.layout.simple_spinner_item);

        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adspin);
        spinner.setOnItemSelectedListener(new SelectingItem() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ReservationFormActivity.this,
                        adspin.getItem(position) + "을 선택 했습니다.", 1).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        */

        check_in_date = (EditText) findViewById(R.id.check_in_date);
        check_in_time = (EditText) findViewById(R.id.check_in_time);
        check_out_time = (EditText) findViewById(R.id.check_out_time);
        useTime = (EditText) findViewById(R.id.useTime);
        //roomNumber = (EditText) findViewById(R.id.roomNumber);
        reason = (EditText) findViewById(R.id.reason);
       // otherMembers = (EditText) findViewById(R.id.otherMembers);
        button_makereservation = (Button) findViewById(R.id.button_makereservation);

        //예약버튼 누르면 예약하기 함수 호출
        button_makereservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makereservation();
            }
        });




        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day= calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        //각 선택기로부터 값을 조사
        //findViewById(R.id.check_in_date).setOnClickListener(new View.OnClickListener() {
        check_in_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 버튼 클릭시 DatePicker로부터 값을 읽어와서 Toast메시지로 보여준다
                new DatePickerDialog(ReservationFormActivity.this, dateSetListener, year, month, day).show();
            }


        });
        findViewById(R.id.check_in_time).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(ReservationFormActivity.this, timeSetListener, hour, minute, false).show();
            }
        });
        findViewById(R.id.check_out_time).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(ReservationFormActivity.this, timeSetListener, hour, minute, false).show();
            }
        });
    }


    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            String msg = String.format("%d / %d / %d", year,monthOfYear+1, dayOfMonth);
            Toast.makeText(ReservationFormActivity.this, msg, Toast.LENGTH_SHORT).show(); //확인용
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            String msg = String.format("%d : %d", hourOfDay, minute);
            Toast.makeText(ReservationFormActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

/*
    public void onClickedMakeReservation(View v){
        Toast.makeText(getApplicationContext(), "예약버튼 눌림", Toast.LENGTH_LONG).show();
        finish();
    }
*/





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
    //서버로 예약 신청 정보 전송 : 짜야함
    public void makereservation(){ //예약하기 함수
        String id = check_in_date.getText().toString();
        String password = check_in_time.getText().toString();
        String name = check_out_time.getText().toString();
        //추가


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