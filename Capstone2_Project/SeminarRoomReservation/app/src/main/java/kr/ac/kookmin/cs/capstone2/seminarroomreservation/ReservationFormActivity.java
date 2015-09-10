package kr.ac.kookmin.cs.capstone2.seminarroomreservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

//150909
//jh
//예약신청 페이지
public class ReservationFormActivity extends AppCompatActivity {

    private EditText check_in_date = (EditText) findViewById(R.id.check_in_date);
    private EditText check_in_time = (EditText) findViewById(R.id.check_in_time);
    private EditText check_out_time = (EditText) findViewById(R.id.check_out_time);
    private EditText useTime = (EditText) findViewById(R.id.useTime);
    private EditText roomNumber = (EditText) findViewById(R.id.roomNumber);
    private EditText reason = (EditText) findViewById(R.id.reason);
    private EditText otherMembers = (EditText) findViewById(R.id.otherMembers);
    private Button button_makereservation = (Button) findViewById(R.id.button_makereservation);

    int year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationform);

        button_makereservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makereservation();
            }
        }); //예약하기 함수 호출


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




    public void onClickedMakeReservation(View v){
        Toast.makeText(getApplicationContext(), "예약하기버튼눌림", Toast.LENGTH_LONG).show();
        finish();
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

    //예약하기 함수
    public void makereservation(){ //예약하기 함수
        String id = check_in_date.getText().toString();
        String password = check_in_time.getText().toString();
        String name = check_out_time.getText().toString();
        //추가


    }


}