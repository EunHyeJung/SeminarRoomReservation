package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Manager.CustomHistoryLVAdapter;
import kr.ac.kookmin.cs.capstone2.seminarroomreservation.R;

/**
 * Created by hongjuhae on 2015. 10. 6..
 */
public class ReservationCheckActivity extends AppCompatActivity {

    private Button btn_yes;
    private Button btn_no;
    private TextView txt1;

    CustomHistoryLVAdapter LogViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationcheck);

        btn_yes = (Button) findViewById(R.id.button_yes); //예약 승인
        btn_no = (Button) findViewById(R.id.button_no); //예약 거절

        txt1 = (TextView) findViewById(R.id.txtUsername);
        txt1.setText("예시 2015-10-01");

        //서버에서 받아온 Json 형식의 데이터 Parsing

    }


    public void onClickYes(View v) { //버튼을 눌렀을때 어떤 작업을 할지 선언합니다
        //String inPutText = edittext.getText().toString(); //서버에서 문자열 받아오기
        Toast.makeText(ReservationCheckActivity.this, "예약이 승인되었습니다", Toast.LENGTH_SHORT).show();
    }

    public void onClickNo(View v) { //버튼을 눌렀을때 어떤 작업을 할지 선언합니다
        //String inPutText = edittext.getText().toString(); //서버에서 문자열 받아오기
        Toast.makeText(ReservationCheckActivity.this, "예약이 거절되었습니다", Toast.LENGTH_SHORT).show();
    }

}

